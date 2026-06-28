package ai.services;

import ai.core.AIManager;
import ai.prompts.PromptBuilder;

public class PriorityService {

    private AIManager aiManager;

    public PriorityService() {
        aiManager = new AIManager();
    }

    public String detectPriority(String message) {

        // Create the priority prompt
        String prompt = PromptBuilder.buildPriorityPrompt(message);

        // Send prompt to AI
        String priority = aiManager.sendPrompt(prompt);

        // Return AI response
        return priority;
    }
}