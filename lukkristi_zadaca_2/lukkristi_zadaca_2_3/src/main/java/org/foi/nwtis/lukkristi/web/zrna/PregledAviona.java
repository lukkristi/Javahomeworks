/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.lukkristi.ws.serveri.AvionLeti;

/**
 *
 * @author lukkristi
 */
@Named(value = "pregledAviona")
@javax.enterprise.context.SessionScoped
public class PregledAviona implements Serializable {

    @Inject
    PregledAerodroma pregledAerodroma;
    @Inject
    PrijavaKorisnika prijavaKorisnika;
    String korisnik = "";
    String lozinka = "";

    @Getter
    @Setter
    String odDatum;
    
    @Getter
    @Setter
    String doDatum;
    
    @Getter
    @Setter
    String icaoOdabrani;
    
    @Getter
    List<AvionLeti> avioni = new ArrayList<>();

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public PregledAviona() {
    }

    /**
     * Koonstruktor koji preuzima podatke koji su potrebni za rad na stranici 
     */
    public void preuzmiPodatkeKorisnika() {
        korisnik = prijavaKorisnika.getKorisnik();
        lozinka = prijavaKorisnika.getLozinka();
        icaoOdabrani=pregledAerodroma.getIcaoOdabrani();
        odDatum=pregledAerodroma.getOdDatum();
        doDatum=pregledAerodroma.getDoDatum();

    }

    /**
     * metoda poziva soap servis i dohvaca avione 
     * @return avioni vraca listu aviona odabranog icao
     */
    public List<AvionLeti> dajAvioneKorisnika() {
        preuzmiPodatkeKorisnika();           
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        SimpleDateFormat dfad = new SimpleDateFormat("dd.mm.yyyy HH:mm");            
        try {
            Date datum1=dfad.parse(odDatum);
            Date datum2=dfad.parse(doDatum);
            long datumOd= datum1.getTime()/1000;
            long datumDo= datum2.getTime()/1000;
            avioni =zadaca2_1WS.dajAvioneAerodomaOdDo(korisnik, lozinka, icaoOdabrani, datumOd, datumDo);
        } catch (ParseException ex) {
            Logger.getLogger(PregledAviona.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Pogreska kog klase PregledAviona metoda dajAvione " + ex.getMessage());
        }        
        
        return avioni;
    }
    
    public String vratiPregledAerodroma() {       
        return "pregledAerodroma.xhtml?faces-redirect=true";
        
    }
           
}
