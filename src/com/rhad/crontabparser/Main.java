/*
 * Copyright 2014 Roberto Fabrizi.
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to:
 * Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 */
package com.rhad.crontabparser;

import com.rhad.crontabparser.config.ConfigProperties;
import com.rhad.crontabparser.model.CrontabParser;
import com.rhad.crontabparser.persistence.EntityManagerFactoryUtil;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.persistence.*;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 * The main class / starting point of the application.
 * @author Roberto Fabrizi
 */
public class Main {
    
    private static final Logger LOGGER = Logger.getLogger(Main.class);    
    private static final ConfigProperties CONFIG_PROPERTIES = LoadConfigurationParameters();
            
    /**
     * Starts the scan of the paths of interest.
     * @param args the command line arguments
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        try {
            LOGGER.info("ConfigProperties loaded");
            LOGGER.info(CONFIG_PROPERTIES);
            EntityManagerFactoryUtil.CreateEntityManagerFactoryUtil();
            LOGGER.info("EntityManagerFactoryUtil loaded");
            String hostname=InetAddress.getLocalHost().getHostName();
            String[] usersArray=CONFIG_PROPERTIES.getUsersArray();
            if(usersArray!=null && usersArray.length>0){
                for(int i=0; i<usersArray.length; i++){
                    String username = usersArray[i];
                    String crontabPath = "/var/spool/cron/crontabs/"+username;
                    try {
                        List<String[]> crontabEntries=GetCrontabContent(crontabPath);
                        for(String[] crontabEntry : crontabEntries){
                            CrontabParser crontabParser = new CrontabParser(hostname, username, crontabEntry[0], crontabEntry[1], crontabEntry[2],
                                crontabEntry[3], crontabEntry[4], crontabEntry[5]);
                            LOGGER.trace(crontabParser);
                            Merge(crontabParser);
                        }
                    } catch (IOException ioe) {
                        LOGGER.error("IOException while trying to parse the crontab at "+crontabPath, ioe);
                    }                    
                }
            } else {
                LOGGER.error("No path to scan");
            }
        } catch (Exception e){
            LOGGER.fatal("A fatal exception has occurred", e);
        } finally {
            // to avoid issues with c3p0 connection pooling wait a bit before shutting down
            Thread.sleep(10000L);            
            EntityManagerFactoryUtil.Close();
            try {
            Thread.sleep(2000L);
            } catch(InterruptedException ie) {}
            LOGGER.info("EntityManagerFactory shut down");        
        }       
    }
    
    /**
     * Returns a List of String arrays containing the tokens for each crontab entry.
     * @param crontabPath the absolute crontab path to parse
     * @returns a List of String arrays containing the tokens for each crontab entry
     * @throws IOException if the passed crontab couldn't be parsed
     */
    private static List<String[]> GetCrontabContent(String crontabPath) throws IOException {
        FileInputStream fileInputStream = null;
        Reader fileReader = null;
        BufferedReader bufferedReader = null;
        String line;
        List<String[]> results = new ArrayList<String[]>();
        try {
            // input stream created on the log of interest
            fileInputStream = new FileInputStream(crontabPath);
            // Reader and BufferedReader to optimize IO
            fileReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                // if the line isnt empty
                if(!line.isEmpty()){
                    // if CONFIG_PROPERTIES.includeCommentedEntries is true, parse any lines
                    if(CONFIG_PROPERTIES.includeCommentedEntries()){
                        String[] crontabEntryArray = line.split(" ", 6);
                        results.add(crontabEntryArray);                    
                    } else {
                        //parse the line only if it doesnt start with a #
                        if(!line.startsWith("#")){
                            String[] crontabEntryArray = line.split(" ", 6);
                            results.add(crontabEntryArray);
                        } 
                    }
                } 
            }
        } finally {
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if(fileReader!=null){
                    fileReader.close();
                }
                if(fileInputStream!=null){
                    fileInputStream.close();
                }                
            } catch(IOException ioe){}
        }
        return results;
    }
    
    /**
     * Merges the passed {@link com.rhad.crontabparser.model.CrontabParser}.
     * @param crontabParser the <code>CrontabParser</code> to merge
     */
    private static void Merge(CrontabParser crontabParser) {
        LOGGER.trace("EntityManager.merge() called");
        EntityManager entityManager = null;
        // a transaction object
        EntityTransaction tx = null;
        try {
            // get an EntityManager from the EntityManagerFactory
            entityManager = EntityManagerFactoryUtil.GetEntityManagerFactory().createEntityManager();
            // get a transaction from the EntityManager
            tx = entityManager.getTransaction();
            LOGGER.trace("EntityManager.getTransaction() ended");
            // open a transaction
            tx.begin();
            LOGGER.trace("EntityTransaction opened");
            entityManager.merge(crontabParser);
            // commit
            tx.commit();
            LOGGER.trace("EntityTransaction.commit() ended successfully");
        } catch(HibernateException he) {
            // grab this superclass runtime exception, print the object that caused it
            LOGGER.error("The CrontabParser that caused the exception is:");
            LOGGER.error(crontabParser);
        } finally {
            if (tx != null && tx.isActive()) {
                LOGGER.trace("Tx is not null and it is active");
                try {
                    // Second try catch as the rollback could fail as well
                    tx.rollback();
                    LOGGER.trace("Tx rolled back");
                } catch (Exception e1) {
                    LOGGER.error("Could not rollback the current transaction",e1);
                }
            }
            // close the entity manager to not go out of memory
            if(entityManager!=null && entityManager.isOpen()){
                entityManager.close();
                LOGGER.trace("EntityManager.close() called");
            }
        }
        LOGGER.trace("EntityManager.merge() ended");
    }
    
    /**
     * Loads the content of the classpath-exported <code>config.properties</code> file in a {@link com.rhad.crontabparser.ConfigProperties} object and returns it. 
     * If the process fails, the application terminates.
     * @return a configProperties object that maps the <code>config.properties</code> classpath-exported file
     */
    private static ConfigProperties LoadConfigurationParameters() {
        LOGGER.trace("LoadConfigurationParameters called");
        File propertiesConfigFile;
        ConfigProperties configurationProperties = null;
        try {
            propertiesConfigFile = new File(Main.class.getClassLoader().getResource("config.properties").toURI());
            if(propertiesConfigFile.exists()){
                // if config.properties exists, parse it and load its content in a ConfigurationProperties object
                configurationProperties = new ConfigProperties(propertiesConfigFile);
            } else {
                throw new FileNotFoundException();
            }            
        } catch (URISyntaxException ex) {
            LOGGER.fatal("Fatal URISyntaxException: the URI of the file 'config.properties' is incorrect", ex);
            System.exit(0);
        } catch (IOException ex) {
            LOGGER.fatal("Fatal URISyntaxException: the URI of the file 'config.properties' is incorrect", ex);
            System.exit(0);
        }
        LOGGER.trace("LoadConfigurationParameters ended");
        return configurationProperties;
    }
}