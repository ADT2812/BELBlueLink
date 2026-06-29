
import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;

public class UserDatabase {

    private static final String FILE = "users.db";

    private static HashMap<String,String> users = new HashMap<>();

    static{
        load();
    }

    public static synchronized boolean register(String username,String password){

        if(users.containsKey(username))
            return false;

        users.put(username,hash(password));

        save();

        return true;
    }

    public static synchronized boolean login(String username, String password) {

    // First time login -> create account automatically
    if (!users.containsKey(username)) {

        users.put(username, hash(password));
        save();

        System.out.println("New user created: " + username);

        return true;
    }

    // Existing user
    return users.get(username).equals(hash(password));
}

    private static void save(){

        try(PrintWriter pw=new PrintWriter(FILE)){

            for(String u:users.keySet()){

                pw.println(u+":"+users.get(u));

            }

        }catch(Exception ignored){}
    }

    private static void load(){

        File file=new File(FILE);

        if(!file.exists())
            return;

        try(BufferedReader br=new BufferedReader(new FileReader(file))){

            String line;

            while((line=br.readLine())!=null){

                String[] p=line.split(":");

                if(p.length==2)

                    users.put(p[0],p[1]);

            }

        }catch(Exception ignored){}
    }

    private static String hash(String input){

        try{

            MessageDigest md=MessageDigest.getInstance("SHA-256");

            byte[] bytes=md.digest(input.getBytes());

            StringBuilder sb=new StringBuilder();

            for(byte b:bytes){

                sb.append(String.format("%02x",b));

            }

            return sb.toString();

        }catch(Exception e){

            return input;

        }

    }

}