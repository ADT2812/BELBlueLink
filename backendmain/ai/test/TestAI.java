package ai.test;

import ai.core.AIService;
import java.util.Scanner;

public class TestAI {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AIService ai = new AIService();

        while (true) {

            System.out.println("\n==============================");
            System.out.println("      AI TEST MENU");
            System.out.println("==============================");

            System.out.println("1. Conversation Summary");
            System.out.println("2. Smart Search");
            System.out.println("3. Reply Suggestions");
            System.out.println("4. Message Classification");
            System.out.println("5. Priority Detection");
            System.out.println("6. Exit");

            System.out.print("\nChoose an option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

    System.out.println("\nEnter Conversation.");
    System.out.println("Type END on a new line when finished.\n");

    StringBuilder conversationBuilder = new StringBuilder();

    while (true) {

        String line = scanner.nextLine();

        if (line.equalsIgnoreCase("END")) {
            break;
        }

        conversationBuilder.append(line).append("\n");
    }

    String summary =
            ai.summarize(
                    conversationBuilder.toString());

    System.out.println("\nSummary:");

    System.out.println(summary);

    break;

                case 2:

                    System.out.println("\nEnter Conversation:");

                    String chat = scanner.nextLine();

                    System.out.print("Ask Question : ");

                    String question = scanner.nextLine();

                    String answer =
                            ai.search(chat, question);

                    System.out.println("\nAnswer:");

                    System.out.println(answer);

                    break;

                case 3:

                    System.out.print("\nEnter Message : ");

                    String message = scanner.nextLine();

                    String replies =
                            ai.suggestReplies(message);

                    System.out.println("\nSuggested Replies:");

                    System.out.println(replies);

                    break;

                case 4:

                    System.out.print("\nEnter Message : ");

                    String classify = scanner.nextLine();

                    String category =
                            ai.classify(classify);

                    System.out.println("\nCategory:");

                    System.out.println(category);

                    break;

                case 5:

                    System.out.print("\nEnter Message : ");

                    String priorityMessage = scanner.nextLine();

                    String priority =
                            ai.detectPriority(priorityMessage);

                    System.out.println("\nPriority:");

                    System.out.println(priority);

                    break;

                case 6:

                    System.out.println("Exiting AI Test.");

                    scanner.close();

                    return;

                default:

                    System.out.println("Invalid Choice.");

            }

        }

    }

}