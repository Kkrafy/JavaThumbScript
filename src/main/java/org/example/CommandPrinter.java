package org.example;

import java.io.*;

public class CommandPrinter {
    public static void main(String[] args) {
        try {
            if(args.length != 1){
                throw new RuntimeException("Invalid arguments, please provide only the program's folder");
            }
            String programFolder = args[0];
            InputStreamReader isReader = new FileReader(programFolder + "command.txt");
            String command = "";
            char next = (char) isReader.read();
            while(next != (char)-1) {
                command += next;
                next = (char) isReader.read();
            }
            if(command.isEmpty()){
                System.out.println("echo Empty command, was CommandGenerator already executed?");
            }else if(command.equals("AllThumbsDetected")){
                System.out.println("echo All files detected already have a thumbnail.");
            }else {
                System.out.print(command); //This output shall be executed by the shell script
                Writer fileWriter = new FileWriter(programFolder + "command.txt");
                fileWriter.write("");
                fileWriter.flush();
            }
        }catch (IOException e){
            System.out.println("echo Command not found, CommandGenerator already executed?");
            throw new RuntimeException("Error parsing/finding resource command.txt",e);
        }
    }
}
