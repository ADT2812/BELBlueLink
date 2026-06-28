package ai.services;

import ai.core.AIManager;
import ai.prompts.PromptBuilder;

public class SummaryService {

    private AIManager aiManager;

    public SummaryService() {
        aiManager = new AIManager();
    }

    public String summarizeConversation(String conversation) {

        // Build the prompt
        String prompt =
                PromptBuilder.buildSummaryPrompt(conversation);

        // Send to AI
        String summary =
                aiManager.sendPrompt(prompt);

        // Return AI response
        return summary;
    }

}