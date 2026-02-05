package org.example;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandGenerator {
    public static void main(String[] args) {
        String files = args[0];
        Pattern fileTypePattern = Pattern.compile(".avi|.mkv|.mp4");
        Matcher fileTypeMatcher = fileTypePattern.matcher(files);
        int startPos = 0;

        ArrayList<String> fileArray;
        for(fileArray = new ArrayList(); fileTypeMatcher.find(); startPos = fileTypeMatcher.end() + 1) {
            String file = files.substring(startPos, fileTypeMatcher.end());
            fileArray.add("\"" + file + "\"");
        }

        String command = "";

        for(String file : fileArray) {
            command = command + "ffmpegthumbnailer -i " + file + " -o " + file.substring(0, file.length() - 5) + ".jpg\";";
        }

        System.out.print(command);
    }
}

