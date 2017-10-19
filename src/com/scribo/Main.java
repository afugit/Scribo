package com.scribo;

import java.io.IOException;
import java.util.List;
import java.util.logging.*;
import java.time.LocalDateTime;

import com.scribo.common.*;

public class Main {


    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        // get CLI arguments
        Arguments arg = new Arguments(args);
        if (!arg.getValid()) {
            System.exit(0);
        }

        // build our thesaurus
        Thesaurus dbc = new Thesaurus(arg.getPass());
        List thesaurus = dbc.BuildThesaurus();x`x`

        // create copy and open
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        String inputFilePath = filePath + arg.getFile();
        String outputFilePath = filePath + arg.getFile() + "_" + LocalDateTime.now();
        FileManager fileMan = new FileManager(inputFilePath, outputFilePath);
        fileMan.copy();


        // search and replace in file, write
        fileMan.buildNewFile(fileMan.getOutputFile().getPath(), thesaurus);

        // todo: store successful replacements in analytics table

    }
}
