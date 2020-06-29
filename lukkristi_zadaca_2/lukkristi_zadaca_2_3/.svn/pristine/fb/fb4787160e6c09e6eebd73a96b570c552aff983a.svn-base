/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.lukkristi.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.OdgovorAerodrom;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author lukkristi
 */
@Named(value = "pregledAerodroma")
@javax.enterprise.context.SessionScoped
public class PregledAerodroma implements Serializable {

    @Inject
    ServletContext context;
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
    Aerodrom aerodrom;
    
    @Getter
    List<Aerodrom> aerodromi;
    @Getter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    @Getter
    @Setter
    String icaoOdabrani;
    
    @Getter
    @Setter
    String temperatura;
    
    @Getter
    @Setter
    String vlaga;
    
    @Getter
    @Setter
    String adresa;
    @Getter
    @Setter
    Lokacija lokacija;

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public PregledAerodroma() {
    }

    /**
     * metoda koja preuzima podatke injekcijom od prijave
     */
    public void preuzmiPodatkeKorisnika() {
        korisnik = prijavaKorisnika.getKorisnik();
        lozinka = prijavaKorisnika.getLozinka();

    }

    /**
     *  dohvaca korisnikove aerodrome pozivanje servisa REST
     * @return listu korisnikovih aerodroma
     */
    public List<Aerodrom> dajAerodromeKorisnika() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);             
        OdgovorAerodrom odgovor1 = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika =new  LinkedList<> (Arrays.asList(odgovor1.getOdgovor()));       
        return aerodromiKorisnika;
    }
   
    /**
     * Metoda koja nas prosljedjuje na odredenu stranicu nakon akcije 
     * @param icao
     * @return pregledAvion stranica
     */
    public String pokreniPregledAerodroma(String icao) {
        icaoOdabrani=icao;
        return "pregledAviona.xhtml?faces-redirect=true";
        
    }
    
    /**
     * Dohacanje meteo podataka i lokacije pomocu LIQ i QWM klijenata ispis informacija za aerodrom 
     * @param icao
     * @return 
     */
    public String dohvatiMeteoiGeoPodatke(String icao){
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String LiqKlijentToken= bpk.getKonfig().dajPostavku("LocationIQ.token");
        String OWMKlijentApi= bpk.getKonfig().dajPostavku("OpenWeatherMap.apikey");
        LIQKlijent lIQKlijent=new LIQKlijent(LiqKlijentToken);
        OWMKlijent oWMKlijent= new OWMKlijent(OWMKlijentApi);
        MeteoPodaci meteoPodaci;       
        for(Aerodrom a: aerodromiKorisnika){
            if(a.getIcao().equals(icao))
                aerodrom=a;
        }
        
        if(aerodrom!=null){
            
            lokacija=lIQKlijent.getGeoLocation(aerodrom.getIcao());
            meteoPodaci=oWMKlijent.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());
            vlaga=meteoPodaci.getHumidityValue().toString();
            temperatura=meteoPodaci.getTemperatureValue().toString();      
        }
            
        return "pregledAerodroma.xhtml?faces-redirect=true";
    }
    
   

}
