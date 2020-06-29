/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author lukkristi
 */
@Stateless
public class Meteorolog implements MeteorologLocal {

    @Inject
    ServletContext context;
    
    @EJB
    MyairportsFacadeLocal myairportsFacadeLocal;
    
   
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    /**
     * Dohacanje meteo podataka i lokacije za aerodrom pomocu LIQ i QWM klijenata
     *
     * @param ident je icao aerodroma
     * @return vraca podatak MeteoPodaci za odredjeni aerodrom
     */
    @Override
    public MeteoPodaci dohvatiGeoIMeteoPodatke(String ident,BP_Konfiguracija bpk) {       
        String LiqKlijentToken = bpk.getKonfig().dajPostavku("LocationIQ.token");
        String OWMKlijentApi = bpk.getKonfig().dajPostavku("OpenWeatherMap.apikey");
        LIQKlijent lIQKlijent = new LIQKlijent(LiqKlijentToken);
        OWMKlijent oWMKlijent = new OWMKlijent(OWMKlijentApi);
        MeteoPodaci meteoPodaci;
//ovo dole ide u zrno haha 

        Lokacija lokacija;
        try{
        lokacija = lIQKlijent.getGeoLocation(ident);
        meteoPodaci = oWMKlijent.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());
        if(lokacija==null)
            System.err.println("PORUKA OD vlage: ");
         if(meteoPodaci==null)
            System.err.println("PORUKA OD vlage: ");
        return meteoPodaci;}
        catch(Exception ex){
            System.err.println("PORUKA OD vlage: " + ex.getMessage());
        }               
        return null;
    }
   
    /**
     * metoda provjera jeli postoji vec aerodrom i korisnik u listi te ako nema onda dodaje inace ne dodaje 
     * @param korime
     * @param aerodrom
     * @return vraca true ako je aerodrom dodan korisniku inace false ako nije jer ga vec ima 
     */
    @Override
    public boolean dodajAerodromKorisniku(Korisnici korime, Airports aerodrom) {
        List<Myairports> da=myairportsFacadeLocal.findAll();
        for(Myairports ada: da){
            if(ada.getIdent().getIdent().equals(aerodrom.getIdent()) && ada.getUsername().getKorIme().equals(korime.getKorIme()))
                return false;
        }
        
            Myairports a = new Myairports();
            a.setIdent(aerodrom);
            a.setUsername(korime);
            Date d = new Date();
            a.setStored(d);
            myairportsFacadeLocal.create(a);
            return true;
        
            
        
    }
    
    
}
