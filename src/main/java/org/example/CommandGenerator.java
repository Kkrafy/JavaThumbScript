package org.example;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class CommandGenerator {

    ArrayList<File> fileArray;
    JSONObject topObject;
    String programFolder;

    /**
     * Reads the argument supplied to the class and transforms it into an array containing all the videos in the server(fileArray)
     * obs:you dont need to send only unthumbnailed videos to the class, it verifies if every one of them has a thumb before generating a new one;
     * @param args arguments of the main method
     */
    public CommandGenerator(String[] args){
        String files = args[0];
        Pattern notSpacePattern = Pattern.compile("[^ ]");
        Matcher notSpaceMatcher = notSpacePattern.matcher(files);
        if(notSpaceMatcher.find()){ // Continues only if the argument is not just a bunch of spaces
            Pattern fileTypePattern = Pattern.compile(".avi|.mkv|.mp4|.wmv");
            Matcher fileTypeMatcher = fileTypePattern.matcher(files);
            int startPos = notSpaceMatcher.start();
            for (fileArray = new ArrayList<>(); fileTypeMatcher.find(); startPos = fileTypeMatcher.end() + 1) {
                String notAddedFiles = files.substring(startPos);
                notSpaceMatcher = notSpacePattern.matcher(notAddedFiles);
                notSpaceMatcher.find();
                startPos = notSpaceMatcher.start() + files.length() - notAddedFiles.length(); //needed to avoid extra spaces on the beginning of file names caused by thumbnailscript
                String file = files.substring(startPos, fileTypeMatcher.end());
                fileArray.add(new File(file));
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("\nThumbnail Generator Script by Kkrafy\n\n");
        CommandGenerator commandGenerator = new CommandGenerator(args);
        if(args.length > 2) {
            if (args[1].equals("scan")) {
                System.out.println("Scan mode activated, this will take longer to run but the performace gain will be permanent");
                commandGenerator.programFolder = args[2];
                commandGenerator.topObject = commandGenerator.readThumbJson();
                commandGenerator.writeThumbnailedFiles();
            }
        }else {
            commandGenerator.programFolder = args[1];
            commandGenerator.topObject = commandGenerator.readThumbJson();
            System.out.println("Running the standard cycle");
        }

        String command = "";

        for(File file : commandGenerator.fileArray) {
            String fileString = "\"" + file.toString() + "\"";
            String newCommandTemplate = "ffmpegthumbnailer -i " + fileString + " -o " + fileString.substring(0, fileString.length() - 5) + ".jpg\";";
            if(commandGenerator.topObject.has(file.getName())) {
                //System.out.println(file.getName() + " has");
                if (!commandGenerator.topObject.getBoolean(file.getName())) {
                    //System.out.println("wasfalse");
                    command = command + newCommandTemplate;
                    commandGenerator.writeVideoToJson(file,true);
                }
            }else{
                boolean hasThumb = commandGenerator.checkThumb(file);
                if(!hasThumb) {
                    //System.out.println("hadnot");
                    command = command + newCommandTemplate;
                    commandGenerator.writeVideoToJson(file, true);
                }else{
                    commandGenerator.writeVideoToJson(file,true);
                }
            }
        }
        try {
            FileWriter writer = new FileWriter(commandGenerator.programFolder + "command.txt");
            if(command.isEmpty()){
                command = "AllThumbsDetected";
            }
            writer.write(command);
            writer.flush();
            System.out.println("Comando:" + command);
            if(args.length == 1) {
                System.out.println("If any thumbnail have not been generated, try running the program with --scan");
            }
        }
        catch(IOException e){
            throw new RuntimeException("Fail writing file command.txt",e);
        }
    }

    public void writeVideoToJson(File video, boolean hasThumb){
        topObject.put(video.getName(),hasThumb);
        try {
            Writer writer = new FileWriter(programFolder + "json/thumbnailedFiles.json");
            topObject.write(writer);
            writer.flush();
            //System.out.println(topObject.toString());
        }catch (IOException e){
            System.out.println("Unable to write a video to json, fixing it is recommended for performace improvements");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

     public JSONObject readThumbJson(){
        try {
            InputStream inputStream = new FileInputStream(programFolder + "json/thumbnailedFiles.json");
            byte[] readBytes = inputStream.readAllBytes();
            String jsonString = new String(readBytes);
            return new JSONObject(jsonString);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }

    /**
     * Check which of the videos already have a thumbnail and uses that data to update thumbnailedFiles.json
     */
    public void writeThumbnailedFiles(){
        for(File video:fileArray){
            topObject.put(video.getName(),checkThumb(video));
        }
        //System.out.println(ClassLoader.getSystemResource("json/thumbnailedFiles.json"));
        try {
            Writer writer = new FileWriter(programFolder + "json/thumbnailedFiles.json");
            topObject.write(writer);
            writer.flush();
            //System.out.println(topObject.toString());
        }catch (IOException e){
            throw new RuntimeException("Fail writing thumbnailedFiles.json ",e);
        }catch (JSONException e){
            e.printStackTrace();
        }
     }

    /**
     *Check if a file has a thumbnail
     * @param video file to check
     */
    public boolean checkThumb(File video){
        //System.out.println("Checkthumb");
        String fileParent = video.getParent();
        String name = video.getName();
        String nameWithoutExtension = name.substring(0,name.length() - 4);
        try {
            if(fileParent != null) {
                //System.out.println(fileParent + "/" + nameWithoutExtension + ".jpg");
                new FileInputStream(fileParent + "/" + nameWithoutExtension + ".jpg");
            }else{
                //System.out.println(nameWithoutExtension + ".jpg");
                new FileInputStream(nameWithoutExtension + ".jpg");
            }
            return true;
        }catch (IOException e){
            return false;
        }
     }
}

