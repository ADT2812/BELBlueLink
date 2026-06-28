package ai.prompts;

public class PromptBuilder {

    // Conversation Summary Prompt
    public static String buildSummaryPrompt(String conversation) {
        return """
You are an intelligent assistant.

Summarize the following conversation in less than 100 words.

Conversation:

""" + conversation + """

Summary:
""";
    }

    // Smart Search Prompt
    public static String buildSearchPrompt(String conversation, String question) {
        return """
You are an AI search assistant.

Read the conversation carefully.

Answer the user's question using only the conversation.

Conversation:

""" + conversation + """

Question:

""" + question + """

Answer:
""";
    }

    // Reply Suggestion Prompt
    public static String buildReplyPrompt(String message) {
        return """
Suggest three short professional replies.

Message:

""" + message + """

Replies:
""";
    }

    // Message Classification Prompt
    public static String buildClassificationPrompt(String message) {
        return """
Classify the following message.

Categories:
- General
- Technical
- Announcement
- Urgent

Message:

""" + message + """

Category:
""";
    }

    // Priority Detection Prompt
    public static String buildPriorityPrompt(String message) {
        return """
Determine the priority of the following message.

Return only one word from:
LOW
MEDIUM
HIGH

Message:

""" + message + """

Priority:
""";
    }
}