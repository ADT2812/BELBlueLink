package ai.core;

import ai.config.ModelConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class LLMClient {

    public String generateResponse(String prompt) {

        try {

            if (prompt == null || prompt.isBlank()) {
                return "Error: Prompt cannot be empty.";
            }

            Path modelPath = Paths.get(ModelConfig.MODEL_PATH);
            if (!Files.exists(modelPath)) {
                return "Error: GGUF model not found:\n"
                        + modelPath.toAbsolutePath();
            }

            Path exePath = Paths.get(ModelConfig.EXECUTABLE_PATH);
            if (!Files.exists(exePath)) {
                return "Error: llama-cli.exe not found:\n"
                        + exePath.toAbsolutePath();
            }

            ProcessBuilder builder = new ProcessBuilder(
                    ModelConfig.EXECUTABLE_PATH,
                    "-m", ModelConfig.MODEL_PATH,
                    "-p", prompt,
                    "-n", "256",
                    "-c", String.valueOf(ModelConfig.CONTEXT),
                    "--threads", String.valueOf(ModelConfig.THREADS),
                    "--temp", "0.2",
                    "--single-turn",
                    "--no-display-prompt"
            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            if (!process.waitFor(120, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return "Error: AI timed out.";
            }

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) continue;
                if (line.startsWith("Loading model")) continue;
                if (line.startsWith("build")) continue;
                if (line.startsWith("model")) continue;
                if (line.startsWith("modalities")) continue;
                if (line.startsWith("available commands")) continue;
                if (line.startsWith("/exit")) continue;
                if (line.startsWith("/regen")) continue;
                if (line.startsWith("/clear")) continue;
                if (line.startsWith("/read")) continue;
                if (line.startsWith("/glob")) continue;
                if (line.startsWith("[ Prompt:")) continue;
                if (line.startsWith("Exiting")) continue;
                if (line.startsWith(">")) continue;
                if (line.startsWith("▄▄")) continue;
                if (line.startsWith("██")) continue;

                response.append(line).append("\n");
            }

            int exitCode = process.exitValue();

            if (exitCode != 0) {
                return "Error: llama-cli exited with code "
                        + exitCode;
            }

            return response.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "LLM Error: " + e.getMessage();
        }
    }
}