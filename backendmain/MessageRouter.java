public class MessageRouter {

   
    public static void sendFile(
            String sender,
            String receiver,
            String fileName,
            long fileSize,
            boolean isImage,
            String base64Data) {

        ChatHistoryManager.saveFileEvent(
                sender,
                receiver,
                fileName,
                fileSize);

        ClientHandler receiverHandler =
                UserManager.getUser(receiver);

        if (receiverHandler != null) {

            receiverHandler.sendMessage(
                    "FILE:" +
                    sender + ":" +
                    fileName + ":" +
                    fileSize + ":" +
                    isImage + ":" +
                    base64Data);

        } else {

            System.out.println(
                    "User not found: " + receiver);
        }
    }

   
    public static void sendGroupFile(
            String sender,
            String groupName,
            String fileName,
            long fileSize,
            boolean isImage,
            String base64Data) {

        ChatHistoryManager.saveGroupFileEvent(
                groupName,
                sender,
                fileName,
                fileSize);

        var members =
                GroupManager.getMembers(groupName);

        if (members == null) {
            return;
        }

        for (String member : members) {

            if (!member.equals(sender)) {

                ClientHandler handler =
                        UserManager.getUser(member);

                if (handler != null) {

                    handler.sendMessage(
                            "GROUPFILE:" +
                            groupName + ":" +
                            sender + ":" +
                            fileName + ":" +
                            fileSize + ":" +
                            isImage + ":" +
                            base64Data);
                }
            }
        }
    }

    public static void sendMessage(
            String sender,
            String receiver,
            String message) {

        ChatHistoryManager.saveMessage(
                sender,
                receiver,
                message);

        ClientHandler receiverHandler =
                UserManager.getUser(receiver);

        if (receiverHandler != null) {

            receiverHandler.sendMessage(
                    sender + ": " + message);

        } else {

            System.out.println(
                    "User not found: " + receiver);
        }
    }
    public static void sendGroupMessage(
        String sender,
        String groupName,
        String message) {

            ChatHistoryManager.saveGroupMessage(
            groupName,
            sender,
            message);

    var members =
            GroupManager.getMembers(
                    groupName);

    if (members == null) {
        return;
    }

    for (String member : members) {

        if (!member.equals(sender)) {

            ClientHandler handler =
                    UserManager.getUser(member);

            if (handler != null) {

                handler.sendMessage(
                        "[GROUP:" +
                        groupName +
                        "] " +
                        sender +
                        ": " +
                        message);
            }
        }
    }
}
}