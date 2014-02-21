/*
 * Copyright 2014 Roberto Fabrizi.
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to:
 * Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 */
package com.rhad.crontabparser.config;

import java.io.*;
import java.util.*;

/**
 * This class is a mapping of the <code>config.properties</code> file.
 * @author Roberto Fabrizi
 */
public final class ConfigProperties {

    private final Boolean includeCommentedEntries;
    private String[] usersArray;

    /**
     * Creates a <code>ConfigProperties</code>, which is in charge of parsing the config.properties file and extract the
     * required informations from it.
     * @param configFile a File that points at the config.properties
     * @throws IOException if a {@link java.io.FileInputStream} from the config.properties file cannot be created or loaded
     * @throws IllegalArgumentException if the <code>ConfigProperties</code> couldn't be created because some mandatory fields are null or invalid
     */
    public ConfigProperties(File configFile) throws IOException {
        // create the Properties object, load the data into it, and close the stream
        FileInputStream propertiesFile = null;
        try {
            Properties defaultProps = new Properties();
            propertiesFile = new FileInputStream(configFile);
            defaultProps.load(propertiesFile);
            this.includeCommentedEntries = Boolean.valueOf(defaultProps.getProperty("include_commented_entries"));
            String users=defaultProps.getProperty("users");
            if(users==null || users.equalsIgnoreCase("")){
                throw new IllegalArgumentException("The users property cannot be null.");
            }
            // if it's not null and it contains at least a , split it over the ,
            if(users.contains(",")){
                this.usersArray = users.split(",");
            } else if(!users.contains(",")){
                // if it isnt null but doesnt contain any , use it as is
                this.usersArray = new String[1];
                this.usersArray[0] = users;
            }
        } finally {
            if(propertiesFile!=null){
                propertiesFile.close();
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if(this.usersArray != null && this.usersArray.length > 0){
            for(int i = 0; i<this.usersArray.length; i++){
                sb.append(this.usersArray[i]).append(",");
            }
        }
        return "ConfigProperties{" + "includeCommentedEntries=" + this.includeCommentedEntries + ", usersArray=" + sb.toString() + '}';
    }
    
    /**
     * Returns whether the application will parse commented entries or not. If this parameter isn't specified in the config.properties file, it returns false.
     * @return whether the application will parse commented entries or not. If this parameter isn't specified in the config.properties file, it returns false
     */
    public Boolean includeCommentedEntries() {
        return this.includeCommentedEntries;
    }
    
    /**
     * Returns the array of user(s) whose crontabs should be parsed.
     * @return the array of user(s) whose crontabs should be parsed
     */
    public String[] getUsersArray() {
        return this.usersArray;
    }    
}