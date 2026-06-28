package ai.core;

public class AIManager {

    private final LLMClient client;

    public AIManager() {
        client = new LLMClient();
    }

    public String sendPrompt(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            return "Prompt cannot be empty.";
        }

        return client.generateResponse(prompt);
    }
}