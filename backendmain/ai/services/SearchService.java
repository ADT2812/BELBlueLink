package ai.services;

import ai.core.AIManager;
import ai.prompts.PromptBuilder;

public class SearchService {

    private AIManager aiManager;

    public SearchService() {
        aiManager = new AIManager();
    }

    public String searchConversation(String conversation, String question) {

        String prompt = PromptBuilder.buildSearchPrompt(conversation, question);

        String answer = aiManager.sendPrompt(prompt);

        return answer;
    }
}