package commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {

    static void registerEvent(String command) {
        File log = new File("log.txt");
        FileWriter fileWriter = null;
        if (log.length() > 100000) {
            log.delete();
        }
        try {
            fileWriter = new FileWriter(log, true);
            Date date = new Date();
            fileWriter.append(date.toString())
                    .append(" ")
                    .append(command)
                    .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
