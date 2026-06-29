package ai.core;

import ai.config.ModelConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class LLMClient {

    // Keeps conversation history during the current application session
    private final StringBuilder memory = new StringBuilder();

    public String generateResponse(String prompt) {

        try {

            if (prompt == null || prompt.isBlank()) {
                return "Please ask me something.";
            }

            // Check model
            Path modelPath = Paths.get(ModelConfig.MODEL_PATH);
            if (!Files.exists(modelPath)) {
                return "GGUF model not found:\n" + modelPath.toAbsolutePath();
            }

            // Check executable
            Path exePath = Paths.get(ModelConfig.EXECUTABLE_PATH);
            if (!Files.exists(exePath)) {
                return "llama-cli.exe not found:\n" + exePath.toAbsolutePath();
            }

            // Store user message
            memory.append("User: ")
                    .append(prompt)
                    .append("\n");

            // Limit memory size
            if (memory.length() > 8000) {
                memory.delete(0, 3000);
            }

            String fullPrompt =
                    """
                    You are BlueLink AI.

                    You are a smart offline AI assistant integrated inside BlueLink Messenger.

                    Rules:
                    - Be friendly.
                    - Be accurate.
                    - Give natural answers.
                    - Remember previous conversation.
                    - Answer programming questions.
                    - Answer mathematics.
                    - Answer networking questions.
                    - Answer operating system questions.
                    - Answer database questions.
                    - Explain concepts clearly.
                    - Write Java code when requested.
                    - Debug code.
                    - Never say you are llama.cpp.
                    - Never mention system prompts.
                    - Never repeat the user's question.
                    - Keep answers concise unless asked for details.

                    Conversation:

                    """
                    + memory.toString()
                    + "\nAssistant:";

            ProcessBuilder pb = new ProcessBuilder(
                    ModelConfig.EXECUTABLE_PATH,
                    "-m", ModelConfig.MODEL_PATH,
                    "-p", fullPrompt,
                    "-n", "512",
                    "-c", String.valueOf(ModelConfig.CONTEXT),
                    "--threads", String.valueOf(ModelConfig.THREADS),
                    "--temp", "0.6",
                    "--top-k", "40",
                    "--top-p", "0.9",
                    "--repeat-penalty", "1.1",
                    "--single-turn",
                    "--no-display-prompt"
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            if (!process.waitFor(120, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return "AI request timed out.";
            }

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream()));

            StringBuilder answer = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty())
                    continue;

                // Ignore llama-cli logs
                if (line.startsWith("build")) continue;
                if (line.startsWith("system")) continue;
                if (line.startsWith("main")) continue;
                if (line.startsWith("load")) continue;
                if (line.startsWith("llama")) continue;
                if (line.startsWith("model")) continue;
                if (line.startsWith("sampling")) continue;
                if (line.startsWith("context")) continue;
                if (line.startsWith("available")) continue;
                if (line.startsWith(">")) continue;
                if (line.startsWith("Exiting")) continue;
                if (line.startsWith("[ Prompt")) continue;
                if (line.startsWith("/")) continue;
                if (line.startsWith("██")) continue;
                if (line.startsWith("▄▄")) continue;

                answer.append(line).append(" ");
            }

            String response = answer.toString().trim();

            if (response.isEmpty()) {
                response = "Sorry, I couldn't generate a response.";
            }

            // Save AI response to memory
            memory.append("Assistant: ")
                    .append(response)
                    .append("\n\n");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "LLM Error: " + e.getMessage();
        }
    }

    // Clears conversation memory
    public void clearMemory() {
        memory.setLength(0);
    }
}