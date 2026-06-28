package ai.services;

import ai.core.AIManager;
import ai.prompts.PromptBuilder;

public class ReplySuggestionService {

    private AIManager aiManager;

    public ReplySuggestionService() {
        aiManager = new AIManager();
    }

    public String generateReplySuggestions(String message) {

        // Create prompt
        String prompt = PromptBuilder.buildReplyPrompt(message);

        // Send to AI
        String replies = aiManager.sendPrompt(prompt);

        // Return AI response
        return replies;
    }
}