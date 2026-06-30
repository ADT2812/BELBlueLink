package ai.core;

import ai.config.ModelConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LLMClient {

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

            String fullPrompt =
                    "<|begin_of_text|>"
                    + "<|start_header_id|>system<|end_header_id|>\n"
                    + "You are BlueLink AI. Answer only with the final answer. Do not repeat the prompt.\n"
                    + "<|eot_id|>"
                    + "<|start_header_id|>user<|end_header_id|>\n"
                    + prompt
                    + "<|eot_id|>"
                    + "<|start_header_id|>assistant<|end_header_id|>\n";

            ProcessBuilder pb = new ProcessBuilder(
                    ModelConfig.EXECUTABLE_PATH,
                    "-m", ModelConfig.MODEL_PATH,
                    "-p", fullPrompt,
                    "-n", "256",
                    "-c", String.valueOf(ModelConfig.CONTEXT),
                    "--threads", String.valueOf(ModelConfig.THREADS),
                    "--temp", "0.6",
                    "--top-k", "40",
                    "--top-p", "0.9",
                    "--repeat-penalty", "1.1",
                    "--single-turn"
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();

            String response = output.toString();

// Find where the assistant starts
String marker = "<|start_header_id|>assistant<|end_header_id|>";
int start = response.indexOf(marker);

if (start != -1) {
    response = response.substring(start + marker.length());
}

// Remove everything after generation statistics
int end = response.indexOf("[ Prompt:");
if (end != -1) {
    response = response.substring(0, end);
}

// Remove "Exiting..."
response = response.replace("Exiting...", "");

// Trim whitespace
response = response.trim();

memory.append("User: ").append(prompt).append("\n");
memory.append("Assistant: ").append(response).append("\n");

return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "LLM Error: " + e.getMessage();
        }
    }

    public void clearMemory() {
        memory.setLength(0);
    }
}