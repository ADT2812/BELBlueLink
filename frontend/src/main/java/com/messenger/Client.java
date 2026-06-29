package com.messenger;
import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public boolean connect(String username) {

        try {

            socket = new Socket("localhost", 5000);

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            writer = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            // Read "Enter Username:"
            reader.readLine();

            // Send username
            writer.println(username);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public void sendMessage(String receiver, String message) {
    if (writer != null) {
        writer.println(receiver + ":" + message);
    }
}

    public String receiveMessage() {

        try {

            return reader.readLine();

        } catch (Exception e) {

            return null;

        }

    }

    public void disconnect() {

        try {

            if (socket != null) {

                socket.close();

            }

        } catch (Exception ignored) {

        }

    }

    public void sendFile(String target,
                         File file,
                         boolean isGroup) {

        try {

            String base64 =
                    FileManager.encodeFileToBase64(file);

            boolean image =
                    FileManager.isImage(file.getName());

            String command =
                    (isGroup ? "/groupfile " : "/sendfile ")
                            + target + " "
                            + file.length() + " "
                            + image + " "
                            + file.getName();

            writer.println(command);
            writer.println(base64);

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ---------------- AI COMMANDS ----------------

    public void requestSummary(String user) {

        writer.println("/summary " + user);

    }

    public void requestPriority(String user) {

        writer.println("/priority " + user);

    }

    public void requestClassification(String user) {

        writer.println("/classify " + user);

    }

    public void requestReplySuggestions(String user) {

        writer.println("/reply " + user);

    }

    public void requestSmartSearch(String user,
                                   String keyword) {

        writer.println("/search " + user + " " + keyword);

    }


    public void askAI(String prompt) {
    writer.println("/ai " + prompt);
}

public String receiveAIResponse() {
    return receiveMessage();
}
}