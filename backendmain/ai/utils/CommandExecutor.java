package ai.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandExecutor {

    public static String execute(ProcessBuilder builder) {

        StringBuilder output = new StringBuilder();

        try {

            Process process = builder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {

                output.append(line);

                output.append("\n");
            }

            process.waitFor();

        }

        catch (Exception e) {

            return "Error : " + e.getMessage();

        }

        return output.toString();

    }

}