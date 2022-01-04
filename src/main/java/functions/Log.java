package functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {

    static void registerEvent(String command) {
        File logFile = new File("log.txt");
        boolean isOverLimit = logFile.length() > 100000;
        if (isOverLimit) {
            eraseFile(logFile);
        }
        writeToFile(null, command, logFile);
    }

    static void writeToFile(FileWriter fileWriter, String command, File logFile) {
        try {
            fileWriter = new FileWriter(logFile, true);
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

    private static void eraseFile(File logFile) {
        logFile.delete();
    }
}
