import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;


public class FileManager {

    private static final String RECEIVED_DIR = "received_files";

    private static final String GROUP_RECEIVED_DIR = "group_received_files";


    public static final long MAX_FILE_SIZE_BYTES = 25L * 1024 * 1024;

   
    public static String encodeFileToBase64(File file) throws IOException {

        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found: " + file.getPath());
        }

        if (file.length() > MAX_FILE_SIZE_BYTES) {
            throw new IOException(
                    "File too large (" + file.length() +
                    " bytes). Max allowed is " +
                    MAX_FILE_SIZE_BYTES + " bytes.");
        }

        byte[] bytes = Files.readAllBytes(file.toPath());

        return Base64.getEncoder().encodeToString(bytes);
    }


    public static String saveIncomingFile(
            String username,
            String fileName,
            String base64Data) throws IOException {

        return saveToDirectory(
                RECEIVED_DIR + File.separator + sanitize(username),
                fileName,
                base64Data);
    }

   
    public static String saveIncomingGroupFile(
            String groupName,
            String fileName,
            String base64Data) throws IOException {

        return saveToDirectory(
                GROUP_RECEIVED_DIR + File.separator + sanitize(groupName),
                fileName,
                base64Data);
    }

    private static String saveToDirectory(
            String directoryPath,
            String fileName,
            String base64Data) throws IOException {

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String safeName = new File(sanitize(fileName)).getName();

        File destination = new File(directory, safeName);

        if (destination.exists()) {
            destination = new File(
                    directory,
                    System.currentTimeMillis() + "_" + safeName);
        }

        byte[] bytes = Base64.getDecoder().decode(base64Data);

        if (bytes.length > MAX_FILE_SIZE_BYTES) {
            throw new IOException(
                    "Incoming file exceeds max allowed size.");
        }

        Files.write(destination.toPath(), bytes);

        return destination.getPath();
    }

  
    private static String sanitize(String name) {

        if (name == null) {
            return "unknown";
        }

        return name.replace("..", "")
                   .replace("/", "")
                   .replace("\\", "")
                   .trim();
    }

   
    public static boolean isImage(String fileName) {

        if (fileName == null) {
            return false;
        }

        String lower = fileName.toLowerCase();

        return lower.endsWith(".png")
                || lower.endsWith(".jpg")
                || lower.endsWith(".jpeg")
                || lower.endsWith(".gif")
                || lower.endsWith(".bmp")
                || lower.endsWith(".webp");
    }
}
