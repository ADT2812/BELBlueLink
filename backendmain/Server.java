import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for clients...");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler =
                        new ClientHandler(clientSocket);

                Thread thread = new Thread(clientHandler);

                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}