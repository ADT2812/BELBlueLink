package ai.services;

import ai.core.AIManager;
import ai.prompts.PromptBuilder;

public class ClassificationService {

    private AIManager aiManager;

    public ClassificationService() {
        aiManager = new AIManager();
    }

    public String classifyMessage(String message) {

        // Build the classification prompt
        String prompt = PromptBuilder.buildClassificationPrompt(message);

        // Send prompt to AI
        String category = aiManager.sendPrompt(prompt);

        // Return AI response
        return category;
    }
}