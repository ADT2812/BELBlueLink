import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private static ConcurrentHashMap<String, ClientHandler> users =
            new ConcurrentHashMap<>();

    public static void addUser(String username, ClientHandler clientHandler) {

        users.put(username, clientHandler);

        System.out.println(username + " connected.");
    }

    public static ClientHandler getUser(String username) {

        return users.get(username);
    }

    public static void removeUser(String username) {

        users.remove(username);

        System.out.println(username + " disconnected.");
    }
}