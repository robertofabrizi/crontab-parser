/*
 * Copyright 2014 Roberto Fabrizi.
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to:
 * Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 */
package com.rhad.crontabparser.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class maps the CRONTAB_PARSER table on the database. If the table doesn't exist it is automatically generated.
 * @author Roberto Fabrizi
 */
@Entity
@Table(name="CRONTAB_PARSER", schema="ORAP8")
public class CrontabParser implements java.io.Serializable {

    @Id
    @GenericGenerator(name="kaugen" , strategy="increment")
    @GeneratedValue(generator="kaugen")
    @Column(name="ID", precision=22, scale=0)
    private BigDecimal id;
    @Column(name="NOME_SERVER", nullable=false, length=30 )
    private String nomeServer;
    @Column(name="UTENTE", nullable=false, length=30)
    private String utente;
    @Temporal(TemporalType.DATE)
    @Column(name="DATA_PARSER", nullable=false, length=7)
    private Date dataParser = new java.util.Date();
    @Column(name="MINUTI", nullable=false, length=128)
    private String minuti;
    @Column(name="ORE", nullable=false, length=64)
    private String ore;
    @Column(name="GIORNO_MESE", nullable=false, length=64)
    private String giornoMese;
    @Column(name="MESE", nullable=false, length=64)
    private String mese;
    @Column(name="GIORNO_SETTIMANA", nullable=false, length=64)
    private String giornoSettimana;
    @Column(name="COMANDO", nullable=false, length=512)
    private String comando;

    /**
     * Creates an empty <code>CrontabParser</code>.
     */
    public CrontabParser() {
    }

    /**
     * Creates a <code>CrontabParser</code> using the passed parameters.
     * @param id the id of this instance
     * @param nomeServer the hostname where this CrontabParser was taken from
     * @param utente the user that has this CrontabParser as an entry in his crontab
     * @param minuti the first crontab entry field (minutes)
     * @param ore the second crontab entry field (hours)
     * @param giornoMese the third crontab entry field (day of the month)
     * @param mese the fourth crontab entry field (month)
     * @param giornoSettimana the fifth crontab entry field (day of the week)
     * @param comando the sixth crontab entry field (the command to launch)
     */
    public CrontabParser(BigDecimal id, String nomeServer, String utente, String minuti, String ore, String giornoMese, String mese, String giornoSettimana, String comando) {
       this(nomeServer, utente, minuti, ore, giornoMese, mese, giornoSettimana, comando);
       this.id = id;
    }
    
    /**
     * Creates a <code>CrontabParser</code> using the passed parameters.
     * @param nomeServer the hostname where this CrontabParser was taken from
     * @param utente the user that has this CrontabParser as an entry in his crontab
     * @param minuti the first crontab entry field (minutes)
     * @param ore the second crontab entry field (hours)
     * @param giornoMese the third crontab entry field (day of the month)
     * @param mese the fourth crontab entry field (month)
     * @param giornoSettimana the fifth crontab entry field (day of the week)
     * @param comando the sixth crontab entry field (the command to launch)
     */
    public CrontabParser(String nomeServer, String utente, String minuti, String ore, String giornoMese, String mese, String giornoSettimana, String comando) {
       this.nomeServer = nomeServer;
       this.utente = utente;
       this.minuti = minuti;
       this.ore = ore;
       this.giornoMese = giornoMese;
       this.mese = mese;
       this.giornoSettimana = giornoSettimana;
       this.comando = comando;
    }

    @Override
    public String toString() {
        return "CrontabParser{" + "id=" + this.id + ", nomeServer=" + this.nomeServer + ", utente=" + this.utente 
            + ", dataParser=" + this.dataParser + ", minuti=" + this.minuti + ", ore=" + this.ore 
            + ", giornoMese=" + this.giornoMese + ", mese=" + this.mese + ", giornoSettimana=" + this.giornoSettimana 
            + ", comando=" + this.comando + '}';
    }
   
    /**
     * Returns the id of this instance.
     * @return the id of this instance
     */
    public BigDecimal getId() {
        return this.id;
    }
    
    /**
     * Sets the id of this instance.
     * @param id the id of this instance
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }
    
    /**
     * Returns the hostname where this CrontabParser was taken from.
     * @return the hostname where this CrontabParser was taken from
     */
    public String getNomeServer() {
        return this.nomeServer;
    }
    
    /**
     * Sets the hostname where this CrontabParser was taken from.
     * @param nomeServer the hostname where this CrontabParser was taken from
     */
    public void setNomeServer(String nomeServer) {
        this.nomeServer = nomeServer;
    }
    
    /**
     * Returns the user that has this CrontabParser as an entry in his crontab.
     * @return the user that has this CrontabParser as an entry in his crontab
     */
    public String getUtente() {
        return this.utente;
    }
    
    /**
     * Sets the user that has this CrontabParser as an entry in his crontab.
     * @param utente the user that has this CrontabParser as an entry in his crontab
     */
    public void setUtente(String utente) {
        this.utente = utente;
    }
    /**
     * Returns the current date.
     * @return the current date
     */
    public Date getDataParser() {
        return this.dataParser;
    }
    
    /**
     * Sets the current date.
     * @param dataParser the current date
     */
    public void setDataParser(Date dataParser) {
        this.dataParser = dataParser;
    }
    
    /**
     * Returns the first crontab entry field (minutes).
     * @return the first crontab entry field (minutes)
     */
    public String getMinuti() {
        return this.minuti;
    }
    
    /**
     * Sets the first crontab entry field (minutes).
     * @param minuti the first crontab entry field (minutes)
     */
    public void setMinuti(String minuti) {
        this.minuti = minuti;
    }
         
    /**
     * Return the second crontab entry field (hours).
     * @return the second crontab entry field (hours)
     */
    public String getOre() {
        return this.ore;
    }
    
    /**
     * Sets the second crontab entry field (hours).
     * @param ore the second crontab entry field (hours)
     */
    public void setOre(String ore) {
        this.ore = ore;
    }
    
    /**
     * Returns the third crontab entry field (day of the month).
     * @return the third crontab entry field (day of the month)
     */
    public String getGiornoMese() {
        return this.giornoMese;
    }
    
    /**
     * Sets the third crontab entry field (day of the month).
     * @param giornoMese the third crontab entry field (day of the month)
     */
    public void setGiornoMese(String giornoMese) {
        this.giornoMese = giornoMese;
    }
    
    /**
     * Returns the fourth crontab entry field (month).
     * @return the fourth crontab entry field (month)
     */
    public String getMese() {
        return this.mese;
    }
    
    /**
     * Sets the fourth crontab entry field (month).
     * @param mese the fourth crontab entry field (month)
     */
    public void setMese(String mese) {
        this.mese = mese;
    }
    
    /**
     * Returns the fifth crontab entry field (day of the week).
     * @return the fifth crontab entry field (day of the week)
     */
    public String getGiornoSettimana() {
        return this.giornoSettimana;
    }
    
    /**
     * Sets the fifth crontab entry field (day of the week).
     * @param giornoSettimana the fifth crontab entry field (day of the week)
     */
    public void setGiornoSettimana(String giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }
    
    /**
     * Returns the sixth crontab entry field (the command to launch).
     * @return the sixth crontab entry field (the command to launch)
     */
    public String getComando() {
        return this.comando;
    }
    
    /**
     * Sets the sixth crontab entry field (the command to launch).
     * @param comando the sixth crontab entry field (the command to launch)
     */
    public void setComando(String comando) {
        this.comando = comando;
    }
}