import java.io.*;
import java.time.LocalDateTime;

public class ChatHistoryManager {

    public static synchronized void saveMessage(
            String sender,
            String receiver,
            String message) {

        try {

            File directory =
                    new File("chat_history");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName;

            if (sender.compareTo(receiver) < 0) {

                fileName =
                        "chat_history/" +
                        sender +
                        "_" +
                        receiver +
                        ".txt";

            } else {

                fileName =
                        "chat_history/" +
                        receiver +
                        "_" +
                        sender +
                        ".txt";
            }

            FileWriter writer =
                    new FileWriter(fileName, true);

            String timestamp =
                    LocalDateTime.now().toString();

            writer.write(
                    "[" + timestamp + "] " +
                    sender +
                    ": " +
                    message +
                    "\n");

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static synchronized void saveGroupMessage(
            String groupName,
            String sender,
            String message) {

        try {

            File directory =
                    new File("group_history");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileWriter writer =
                    new FileWriter(
                            "group_history/" +
                            groupName +
                            ".txt",
                            true);

            String timestamp =
                    LocalDateTime.now().toString();

            writer.write(
                    "[" + timestamp + "] " +
                    sender +
                    ": " +
                    message +
                    "\n");

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static synchronized void saveFileEvent(
            String sender,
            String receiver,
            String fileName,
            long fileSize) {

        try {

            File directory =
                    new File("chat_history");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileNameForHistory;

            if (sender.compareTo(receiver) < 0) {

                fileNameForHistory =
                        "chat_history/" +
                        sender +
                        "_" +
                        receiver +
                        ".txt";

            } else {

                fileNameForHistory =
                        "chat_history/" +
                        receiver +
                        "_" +
                        sender +
                        ".txt";
            }

            FileWriter writer =
                    new FileWriter(fileNameForHistory, true);

            String timestamp =
                    LocalDateTime.now().toString();

            writer.write(
                    "[" + timestamp + "] " +
                    sender +
                    " sent a file: " +
                    fileName +
                    " (" + fileSize + " bytes)" +
                    "\n");

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static synchronized void saveGroupFileEvent(
            String groupName,
            String sender,
            String fileName,
            long fileSize) {

        try {

            File directory =
                    new File("group_history");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileWriter writer =
                    new FileWriter(
                            "group_history/" +
                            groupName +
                            ".txt",
                            true);

            String timestamp =
                    LocalDateTime.now().toString();

            writer.write(
                    "[" + timestamp + "] " +
                    sender +
                    " sent a file: " +
                    fileName +
                    " (" + fileSize + " bytes)" +
                    "\n");

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static String getConversation(
            String user1,
            String user2) {

        try {

            String fileName;

            if (user1.compareTo(user2) < 0) {

                fileName =
                        "chat_history/" +
                        user1 +
                        "_" +
                        user2 +
                        ".txt";

            } else {

                fileName =
                        "chat_history/" +
                        user2 +
                        "_" +
                        user1 +
                        ".txt";
            }

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(fileName));

            StringBuilder history =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                history.append(line)
                       .append("\n");
            }

            reader.close();

            return history.toString();

        } catch (Exception e) {

            return "No conversation found.";
        }
    }
}