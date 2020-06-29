/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.dretve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.lukkristi.web.podaci.AirportDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author lukkristi
 */
public class PreuzimanjeLetovaAvionaAerodroma extends Thread {

    private BP_Konfiguracija konf;
    int intervalCiklusa = 100 * 1000;
    Boolean preuzimanjeStatus;
    String username;
    String password;
    OSKlijent oSKlijent;
    int inicijalniPocetakIntervala;
    String pocetniDatum;
    String krajDatum;
    Long pocetniInterval;
    Long sljedeceInterval;
    int pauzaDretve;
    int trajanjeCiklusaDretve;
    AirportDAO airportDAO;

    public PreuzimanjeLetovaAvionaAerodroma(BP_Konfiguracija konf) {
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        preuzimanjeStatus = false;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        int brojac = 0;
        while (preuzimanjeStatus) {
            System.out.println("BROJAC: " + brojac++);
            try {
                List<Airport> aerodromi = new ArrayList<>();
                aerodromi = airportDAO.dajSveKorisnikAerodrome();
                java.util.Date myDate = new java.util.Date(pocetniInterval);
                java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
                for (Airport aerodrom : aerodromi) {
                    if (!airportDAO.provjeriAerodromULog(aerodrom.getIdent(), sqlDate)) {
                        List<AvionLeti> avioni = oSKlijent.getDepartures(aerodrom.getIdent(), pocetniInterval/1000, sljedeceInterval/1000);
//                        List<AvionLeti> avioni = oSKlijent.getDepartures("EDDF", 1588521744, 1588694544);
                        airportDAO.spremiAvione(avioni);
                        airportDAO.spremiAerodromULog(aerodrom.getIdent(), sqlDate,avioni.size());
                        Thread.sleep(pauzaDretve);
                    }
                }

                pocetniInterval = sljedeceInterval;
                sljedeceInterval = pocetniInterval + (24 * 60 * trajanjeCiklusaDretve * 1000);
                if (provjeriTrenutniDatum(pocetniInterval)) {
                    Thread.sleep(24 * 60 * 60 * 1000);
                }

                //TODO üreuzimanje aerodroma i avione od aerodroma za pojedini dan 
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        username = konf.getOpenSkyNetworkKorisnik();
        password = konf.getOpenSkyNetworkLozinka();
        oSKlijent = new OSKlijent(username, password);
        //TODO preuzimanje podataka iz konfiguracije  i pridruzi ih atributima
        pocetniDatum = konf.getPreuzimanjePocetak();
        krajDatum = konf.getPreuzimanjeKraj();
        pauzaDretve = konf.getPreuzimanjePauza();
        trajanjeCiklusaDretve = konf.getPreuzimanjeCiklusa();
        airportDAO = AirportDAO.getInstanca(konf);
        preuzimanjeStatus= konf.getPreuzimanjeStatus();
        pretvoriDatumUMilisekunde();

        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * metoda koja pretvara datum u miliskeunde 
     */
    public void pretvoriDatumUMilisekunde() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date datum;
        try {
            datum = df.parse(pocetniDatum);
            pocetniInterval = datum.getTime();
            sljedeceInterval = pocetniInterval + (24 * 60 * trajanjeCiklusaDretve * 1000);
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * provjerava trenutni datum odnosno danasnji
     * @param datPocetni
     * @return
     */
    public boolean provjeriTrenutniDatum(long datPocetni) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        Date trenutniDatum = new Date();
        Date datumOdDretva = new Date(datPocetni);
        return fmt.format(trenutniDatum).equals(fmt.format(datumOdDretva));
    }
    
    

}
