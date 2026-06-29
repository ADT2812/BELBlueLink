package ai.core;

public class AIService {

    private final LLMClient client = new LLMClient();

    public String generateResponse(String prompt) {
        return client.generateResponse(prompt);
    }

    public String summarize(String text) {
        return client.generateResponse(
            "Summarize this conversation:\n\n" + text);
    }

    public String detectPriority(String text) {
        return client.generateResponse(
            "Classify this message as HIGH, MEDIUM or LOW priority and explain briefly:\n\n" + text);
    }

    public String classify(String text) {
        return client.generateResponse(
            "Classify this message into one category (Work, Personal, Finance, Study, Other):\n\n" + text);
    }

    public String suggestReplies(String text) {
        return client.generateResponse(
            "Suggest three short replies for:\n\n" + text);
    }

    public String search(String history, String query) {
        return client.generateResponse(
            "Answer the question using only this chat history.\n\nHistory:\n"
            + history +
            "\n\nQuestion: " + query);
    }
}