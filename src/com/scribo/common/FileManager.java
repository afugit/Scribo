package com.scribo.common;

import com.scribo.objects.Term;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    private File inputFile = null;
    private File outputFile = null;

    public File getOutputFile() {
        return outputFile;
    }

    public FileManager(String input, String output) {
        LOGGER.log(Level.INFO, "Creating copy of file.");
        this.inputFile = new File(input);
        this.outputFile = new File(output);
    }

    private static final Logger LOGGER = Logger.getLogger(FileManager.class.getName());

    public void copy() {
        File source = this.inputFile;
        File destination = this.outputFile;

        if (source == null) {
            LOGGER.log(Level.WARNING, "Null source");
        }
        else if (destination == null) {
            LOGGER.log(Level.WARNING, "Null destination");
        }
        else if (source.isDirectory()) {
            copyDirectory(source, destination);
        }
        else {
            copyFile(source, destination);
        }
    }

    private static void copyDirectory(File source, File destination) {
        try {
            copyDirectory(source, destination, null);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not copy directory.");
        }
    }

    private static void copyDirectory(File source, File destination, FileFilter filter) {
        File nextDirectory = new File(destination, source.getName());
        // create the directory if necessary...
        if (!nextDirectory.exists() && !nextDirectory.mkdirs()) {
            LOGGER.log(Level.SEVERE, "Failed to create new directory", nextDirectory);
        }
        File[] copyFiles = source.listFiles();
        
        // and then all the items below the directory...
        for (int n = 0; n < copyFiles.length; ++n) {
            if (filter == null || filter.accept(copyFiles[n])) {
                if (copyFiles[n].isDirectory()) {
                    copyDirectory(copyFiles[n], nextDirectory, filter);
                } else {
                    copyFile(copyFiles[n], nextDirectory);
                }
            }
        }
    }

    private static void copyFile(File source, File destination) {
        File cDestionation = destination;
        // what we really want to do is create a file with the same name in that dir
        if (cDestionation.isDirectory())
            cDestionation = new File(cDestionation, source.getName());

        try (FileInputStream input = new FileInputStream(source)) {
            copyFile(input, cDestionation);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private static void copyFile(InputStream input, File destination){
        InputStream cInput = input;
        File cDestination = destination;
        try (OutputStream output = new FileOutputStream(cDestination)) {
            byte[] buffer = new byte[1024];
            int bytesRead = cInput.read(buffer);
            while (bytesRead >= 0) {
                output.write(buffer, 0, bytesRead);
                bytesRead = cInput.read(buffer);
            }
            cInput.close();
            output.close();
        } 
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private static List<String> open(String filePath) throws IOException {
        List<String> list = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
            String str;
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
        }
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot open file.");
        }

        LOGGER.log(Level.WARNING, list.size() + " found in list");

        return list;
    }

    public void buildNewFile(String filepath, List thesaurus) throws IOException {
        List<String> fileContents = open(filepath);
        List<String> newContents = new ArrayList<>();
        int fileContentsLen = fileContents.size();
        int thesaurusLen = thesaurus.size();

        for(int i = 0; i < fileContentsLen; i++) { //go through each line in markdown file
            String line = fileContents.get(i);
            for(int j = 0; j < thesaurusLen; j++) { // go through each word in thesaurus
                Term t = (Term) thesaurus.get(j);
                line = line.replaceAll("\\b" + t.getSearchTerm() + "\\b", t.getReplaceTerm());
                //LOGGER.log(Level.WARNING, "search=" + t.getSearchTerm() + ", replace=" + t.getReplaceTerm());
            }
            LOGGER.log(Level.WARNING, "line = " + line);
            newContents.add(line + "\r\n");
        }

        // write new contents to outputFilePath
        FileWriter fileWriter = new FileWriter(filepath);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        int newContentsLen = newContents.size();
        for(int i = 0; i < newContentsLen; i++) {
            printWriter.print(newContents.get(i));
        }
        printWriter.close();
    }
}