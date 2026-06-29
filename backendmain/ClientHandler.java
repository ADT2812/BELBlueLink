import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import ai.core.AIService;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String username;
    private PrintWriter writer;
    private final AIService aiService = new AIService();

    public ClientHandler(Socket socket) {

        this.clientSocket = socket;
    }

    public void sendMessage(String message) {

        
        if (message.startsWith("FILE:")) {

            handleIncomingFile(message);
            return;
        }

        if (message.startsWith("GROUPFILE:")) {

            handleIncomingGroupFile(message);
            return;
        }

        writer.println(message);
    }

    private void handleIncomingFile(String message) {

        String[] parts = message.split(":", 6);

        if (parts.length != 6) {
            writer.println(message);
            return;
        }

        String sender = parts[1];
        String fileName = parts[2];
        String fileSize = parts[3];
        String isImage = parts[4];
        String base64Data = parts[5];

        try {

            String savedPath =
                    FileManager.saveIncomingFile(
                            username,
                            fileName,
                            base64Data);

            writer.println(
                    (Boolean.parseBoolean(isImage) ? "[IMAGE]" : "[FILE]") +
                    " " + sender + " sent: " + fileName +
                    " (" + fileSize + " bytes) -> saved to " + savedPath);

        } catch (Exception e) {

            writer.println(
                    "Failed to receive file from " + sender +
                    ": " + fileName + " (" + e.getMessage() + ")");
        }
    }

    private void handleIncomingGroupFile(String message) {


        String[] parts = message.split(":", 7);

        if (parts.length != 7) {
            writer.println(message);
            return;
        }

        String groupName = parts[1];
        String sender = parts[2];
        String fileName = parts[3];
        String fileSize = parts[4];
        String isImage = parts[5];
        String base64Data = parts[6];

        try {

            String savedPath =
                    FileManager.saveIncomingGroupFile(
                            groupName,
                            fileName,
                            base64Data);

            writer.println(
                    (Boolean.parseBoolean(isImage) ? "[IMAGE]" : "[FILE]") +
                    " [GROUP:" + groupName + "] " + sender + " sent: " +
                    fileName + " (" + fileSize + " bytes) -> saved to " +
                    savedPath);

        } catch (Exception e) {

            writer.println(
                    "Failed to receive group file from " + sender +
                    ": " + fileName + " (" + e.getMessage() + ")");
        }
    }

    @Override
    public void run() {

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    clientSocket.getInputStream()));

            writer =
                    new PrintWriter(
                            clientSocket.getOutputStream(),
                            true);

            writer.println("Enter Username:");

            username = reader.readLine();

            UserManager.addUser(username, this);

            writer.println(
                "Connected! Use format: username:message");

            writer.println(
                "Commands: /history <user> | /create <group> | /join <group> | /group <name> <message>");

            writer.println(
                "Files: /sendfile <user> <fileSize> <isImage> <fileName> (then send the Base64 data line)");

            writer.println(
                "Group files: /groupfile <group> <fileSize> <isImage> <fileName> (then send the Base64 data line)");

            String input;

            while ((input = reader.readLine()) != null) {
if (input.startsWith("/summary ")) {

    System.out.println("SUMMARY COMMAND RECEIVED");

    String otherUser = input.substring(9);

    System.out.println("Other user = " + otherUser);

    String conversation =
            ChatHistoryManager.getConversation(
                    username,
                    otherUser);

    System.out.println("Conversation:");
    System.out.println(conversation);

    String summary =
            aiService.summarize(conversation);

    System.out.println("Summary:");
    System.out.println(summary);

    sendMessage(summary);

    continue;
}

if (input.startsWith("/priority ")) {

    String message = input.substring(10);

    String priority =
            aiService.detectPriority(message);

    sendMessage(priority);

    continue;
}

if (input.startsWith("/classify ")) {

    String message = input.substring(10);

    String category =
            aiService.classify(message);

    sendMessage(category);

    continue;
}

if (input.startsWith("/reply ")) {

    String message = input.substring(7);

    String reply =
            aiService.suggestReplies(message);

    sendMessage(reply);

    continue;
}

if (input.startsWith("/search ")) {

    String query = input.substring(8);

    // Searches within the user's own chat history.
    String conversation =
            ChatHistoryManager.getConversation(username, username);

    String result =
            aiService.search(conversation, query);

    sendMessage(result);

    continue;
}


    if (input.startsWith("/history ")) {

        String otherUser =
                input.substring(9);

        sendMessage(
                ChatHistoryManager.getConversation(
                        username,
                        otherUser));

        continue;
    }

    if (input.startsWith("/create ")) {

        String groupName =
                input.substring(8);

        GroupManager.createGroup(
                groupName);

        sendMessage(
                "Group created: " +
                groupName);

        continue;
    }

    if (input.startsWith("/join ")) {

        String groupName =
                input.substring(6);

        GroupManager.joinGroup(
                groupName,
                username);

        sendMessage(
                "Joined group: " +
                groupName);

        continue;
    }

    if (input.startsWith("/group ")) {

        String[] parts =
                input.split(" ", 3);

        if (parts.length == 3) {

            MessageRouter.sendGroupMessage(
                    username,
                    parts[1],
                    parts[2]);
        }

        continue;
    }

    if(input.startsWith("/ai ")){

    String prompt=input.substring(4);

    String answer=
            aiService.generateResponse(prompt);

    sendMessage(answer);

    continue;
}
    if (input.startsWith("/sendfile ")) {

        
        String[] parts =
                input.substring(10).split(" ", 4);

        if (parts.length != 4) {

            sendMessage(
                    "Usage: /sendfile <username> <fileSize> <isImage> <fileName>");

            continue;
        }

        String receiver = parts[0];
        long fileSize;
        boolean isImage;
        String fileName = parts[3];

        try {

            fileSize = Long.parseLong(parts[1]);
            isImage = Boolean.parseBoolean(parts[2]);

        } catch (NumberFormatException e) {

            sendMessage("Invalid file size in /sendfile command.");
            continue;
        }

        if (fileSize > FileManager.MAX_FILE_SIZE_BYTES) {

            sendMessage(
                    "File too large. Max allowed is " +
                    FileManager.MAX_FILE_SIZE_BYTES +
                    " bytes.");

            
            reader.readLine();

            continue;
        }

        String base64Data = reader.readLine();

        if (base64Data == null) {
            break;
        }

        MessageRouter.sendFile(
                username,
                receiver,
                fileName,
                fileSize,
                isImage,
                base64Data);

        sendMessage("File sent to " + receiver + ": " + fileName);

        continue;
    }

    if (input.startsWith("/groupfile ")) {

        String[] parts =
                input.substring(11).split(" ", 4);

        if (parts.length != 4) {

            sendMessage(
                    "Usage: /groupfile <groupName> <fileSize> <isImage> <fileName>");

            continue;
        }

        String groupName = parts[0];
        long fileSize;
        boolean isImage;
        String fileName = parts[3];

        try {

            fileSize = Long.parseLong(parts[1]);
            isImage = Boolean.parseBoolean(parts[2]);

        } catch (NumberFormatException e) {

            sendMessage("Invalid file size in /groupfile command.");
            continue;
        }

        if (fileSize > FileManager.MAX_FILE_SIZE_BYTES) {

            sendMessage(
                    "File too large. Max allowed is " +
                    FileManager.MAX_FILE_SIZE_BYTES +
                    " bytes.");

            reader.readLine();

            continue;
        }

        String base64Data = reader.readLine();

        if (base64Data == null) {
            break;
        }

        MessageRouter.sendGroupFile(
                username,
                groupName,
                fileName,
                fileSize,
                isImage,
                base64Data);

        sendMessage("File sent to group " + groupName + ": " + fileName);

        continue;
    }

    if (input.contains(":")) {

        String[] parts =
                input.split(":", 2);

        String receiver =
                parts[0];

        String message =
                parts[1];

        MessageRouter.sendMessage(
                username,
                receiver,
                message);
    }
}

        } catch (Exception e) {

    System.out.println(
            username + " disconnected.");

} finally {

    try {

        UserManager.removeUser(username);

        clientSocket.close();

    } catch (Exception ignored) {
    }
}
    }
}
