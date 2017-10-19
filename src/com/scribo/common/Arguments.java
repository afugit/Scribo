package com.scribo.common;

import java.util.Scanner;
import java.util.logging.*;

public class Arguments {
    private static final Logger LOGGER = Logger.getLogger(Arguments.class.getName());

    // accepted arguments
    private String file = null;
    private String pass = null;
    private boolean valid = false;

    // accessors
    public String getFile() {
        return this.file;
    }

    public String getPass() {
        return this.pass;
    }

    public boolean getValid() {
        return this.valid;
    }
    
    public Arguments(String[] args) {
        // get values of accepted arguments and set
        int argsLen = args.length;
        for(int i = 0; i < argsLen; i++) {
            // determine which argument
            String[] argument = args[i].split("=");
            switch(argument[0]) {
                case "--file":      setFile(argument[1]);
                                    break;
                case "--password":  setPass(argument[1]);
                                    break;
                default:            LOGGER.log(Level.WARNING, "Unknown argument provided", argument[0]);
                                    break;
            }
        }
        
        // if all required arguments are found, set to valid
        if(this.file == null || this.file.isEmpty()) {
            Scanner reader = new Scanner(System.in);
            LOGGER.log(Level.OFF, "Enter filename");
            this.file = reader.next();
        }
        
        if(this.pass == null || this.pass.isEmpty()) {
            Scanner reader = new Scanner(System.in);
            LOGGER.log(Level.OFF, "Enter password");
            this.pass = reader.next();        
        }
            
        setValid(true);
    }

    private void setFile(String file) {
        this.file = file;
    }

    private void setPass(String pass) {
        this.pass = pass;
    }

    private void setValid(boolean valid) {
        this.valid = valid;
    }

}
