import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) {

        try {

            Socket socket =
                    new Socket("localhost", 5000);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            PrintWriter writer =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            Scanner scanner =
                    new Scanner(System.in);

            System.out.println(reader.readLine());

            String username =
                    scanner.nextLine();

            writer.println(username);

            Thread receiveThread =
                    new Thread(() -> {

                        try {

                            String message;

                            while ((message =
                                    reader.readLine()) != null) {

                                System.out.println(message);
                            }

                        } catch (Exception ignored) {
                        }
                    });

            receiveThread.start();

            System.out.println(
                    "Tip: /sendfile <user> <path> and " +
                    "/groupfile <group> <path> read a real file " +
                    "from disk and send it for you.");

            while (true) {

                String message =
                        scanner.nextLine();

                if (message.startsWith("/sendfile ")) {

                    sendFileCommand(
                            writer,
                            message.substring(10),
                            false);

                    continue;
                }

                if (message.startsWith("/groupfile ")) {

                    sendFileCommand(
                            writer,
                            message.substring(11),
                            true);

                    continue;
                }

                writer.println(message);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

   
    private static void sendFileCommand(
            PrintWriter writer,
            String args,
            boolean isGroup) {

        String[] parts = args.split(" ", 2);

        if (parts.length != 2) {

            System.out.println(
                    "Usage: " +
                    (isGroup ? "/groupfile <group> <path>"
                             : "/sendfile <user> <path>"));

            return;
        }

        String target = parts[0];
        String path = parts[1];

        File file = new File(path);

        if (!file.exists() || !file.isFile()) {

            System.out.println("File not found: " + path);
            return;
        }

        try {

            String base64Data =
                    FileManager.encodeFileToBase64(file);

            boolean isImage =
                    FileManager.isImage(file.getName());

            String command =
                    (isGroup ? "/groupfile " : "/sendfile ") +
                    target + " " +
                    file.length() + " " +
                    isImage + " " +
                    file.getName();

            writer.println(command);
            writer.println(base64Data);

            System.out.println(
                    "Sending " + file.getName() +
                    " (" + file.length() + " bytes) to " + target);

        } catch (Exception e) {

            System.out.println(
                    "Failed to send file: " + e.getMessage());
        }
    }
}
