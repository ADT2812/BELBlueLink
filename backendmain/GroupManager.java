import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GroupManager {

    private static ConcurrentHashMap<
            String,
            Set<String>> groups =
            new ConcurrentHashMap<>();

    public static void createGroup(
            String groupName) {

        groups.putIfAbsent(
                groupName,
                ConcurrentHashMap.newKeySet());
    }

    public static void joinGroup(
            String groupName,
            String username) {

        if (groups.containsKey(groupName)) {

            groups.get(groupName)
                  .add(username);
        }
    }

    public static Set<String> getMembers(
            String groupName) {

        return groups.get(groupName);
    }
}