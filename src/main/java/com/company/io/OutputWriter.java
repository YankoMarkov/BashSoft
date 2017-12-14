package com.company.io;


public class OutputWriter {
    public static void writeMessage(String message){
        System.out.print(message);
    }
    public static void writeMessageOnNewLine(String message){
        System.out.println(message);
    }
    public static void writeEmptyLine(){
        System.out.println();
    }
    public static void displayException(String message){
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }
    public static void displayStudent(String studentName, Double mark){
        String output = String.format("%s - %.2f", studentName, mark);
        OutputWriter.writeMessageOnNewLine(output);
    }
}
