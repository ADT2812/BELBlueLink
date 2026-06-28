package ai.core;

public class AIService {

    private final LLMClient client = new LLMClient();

    // 1. Conversation Summary
    public String summarize(String conversation) {

        String prompt =
                """
                You are an intelligent assistant.

                Summarize the following conversation in less than 100 words.

                Conversation:
                """ + conversation;

        return client.generateResponse(prompt);
    }

    // 2. Priority Detection
    public String detectPriority(String message) {

        String prompt =
                """
                Classify the message as:

                High
                Medium
                Low

                Return ONLY one word.

                Message:
                """ + message;

        return client.generateResponse(prompt);
    }

    // 3. Message Classification
    public String classify(String message) {

        String prompt =
                """
                Categorize this message into ONE category:

                Work
                Personal
                Meeting
                Urgent
                Casual

                Return ONLY the category.

                Message:
                """ + message;

        return client.generateResponse(prompt);
    }

    // 4. Reply Suggestions
    public String suggestReplies(String message) {

        String prompt =
                """
                Suggest three short replies.

                Return one reply per line.

                Message:
                """ + message;

        return client.generateResponse(prompt);
    }

    // 5. Smart Search
    public String search(String conversation, String query) {

        String prompt =
                """
                Search the conversation below and answer the user's query.

                Query:
                """ + query +

                """

                Conversation:
                """ + conversation;

        return client.generateResponse(prompt);
    }
}