package ai.config;

public class ModelConfig {

    public static final String EXECUTABLE_PATH =
            "C:\\BlueLink\\backendmain\\ai\\runtime\\bin\\llama-cli.exe";

    public static final String MODEL_PATH =
            "C:\\BlueLink\\backendmain\\ai\\runtime\\model\\Llama-3.2-3B-Instruct-Q4_K_M.gguf";

    public static final int THREADS =
            Runtime.getRuntime().availableProcessors();

    public static final int CONTEXT = 4096;
}