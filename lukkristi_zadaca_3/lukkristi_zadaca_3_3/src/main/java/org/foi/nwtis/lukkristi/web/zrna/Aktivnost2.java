/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.ejb.sb.AirportsFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.MeteorologLocal;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.lukkristi.web.PrijenosPodataka;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author lukkristi
 */
@Named(value = "aktivnost2")
@ViewScoped
public class Aktivnost2 implements Serializable{

    @Inject
    ServletContext context;
    @EJB
    MeteorologLocal meteorologLocal;
    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    @EJB
    AirportsFacadeLocal aerodromiFacadeLocal;    
    @Getter
    List<Korisnici> korisnici = new ArrayList<>();
    @Getter
    List<Airports> aerodromi = new ArrayList<>();    
    @Getter
    @Setter
    String odabraniKorisnik = null;
    @Getter
    @Setter
    String naziv = null;
    @Getter
    @Setter
    boolean prikazAerodom = false;
    private boolean prikaziAerodrome = false;
    @Getter
    @Setter
    private String aerodromOdabrani;
    @Getter
    @Setter
    String temperatura;
    
    @Getter
    @Setter
    String vlaga;
    /**
     * Creates a new instance of Aktivnost2
     */
    public Aktivnost2() {
    }
    
    @PostConstruct
    private void dohvatiKorisnike() {
        korisnici = korisniciFacadeLocal.findAll();
    }
    
    /**
     * metoda koja poziva JPQL upit iz prve apliakcije te dohvaca listu aerodroma trazenih
     * @return
     */
    public String izvrsiJPQLUpit(){        
        if (!aerodromi.isEmpty()) {
            aerodromi.clear();
        }
        aerodromi.addAll(aerodromiFacadeLocal.pretraziPoNazivuJPQL(naziv));
        prikaziAerodrome();      
        return "";               
    }
     
    /**
     * metoda koja poziva CApi upit iz prve apliakcije te dohvaca listu aerodroma trazenih
     * @return
     */
    public String izvrsiCApipit(){        
        if (!aerodromi.isEmpty()) {
            aerodromi.clear();
        }
        aerodromi.addAll(aerodromiFacadeLocal.pretraziPoNazivuJPQL(naziv));
        prikaziAerodrome();        
        return "";               
    }
     
    /**
     * metoda koja renderira tablicu za prikaz aerodroma 
     * @return
     */
    public String prikaziAerodrome(){        
        prikazAerodom=true;
        return "";               
    }

    /**
     * Metoda koja je trebala ispisati vlagu i temperaturu za svaki aerodroma ali zbog gresaka BAD request i TO MANY REQUEST nije u JSF pogledu ostavljena
     */
    public void DohvatiVlagu(){
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String LiqKlijentToken = bpk.getKonfig().dajPostavku("LocationIQ.token");
        String OWMKlijentApi = bpk.getKonfig().dajPostavku("OpenWeatherMap.apikey");
        LIQKlijent lIQKlijent = new LIQKlijent(LiqKlijentToken);
        OWMKlijent oWMKlijent = new OWMKlijent(OWMKlijentApi);       
        MeteoPodaci meteoPodaci;
        Lokacija lokacija;
        for(Airports a:aerodromi ){
            lokacija = lIQKlijent.getGeoLocation(a.getIdent());
            meteoPodaci = oWMKlijent.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());        
            vlaga=meteoPodaci.getHumidityValue().toString();
            temperatura=meteoPodaci.getTemperatureValue().toString();
            
        }       
        if(vlaga==null)
             System.out.println("org.foi.nwtis.lukkristi.web.zrna.Aktivnost2.DohvatiVlagu()");                      
    }
     
    /**
     * Jos jedan pokusaj dohvacanja vlage ali zbog isti gresaka  kao prethodna metoda nije ostavljena u pogledu
     * @param ident
     * @return
     */
    public String DohvatiTemp(String ident){
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        MeteoPodaci meteoPodaci=meteorologLocal.dohvatiGeoIMeteoPodatke(ident,bpk);       
        temperatura=meteoPodaci.getTemperatureValue().toString();
        if(temperatura==null)
             System.out.println("org.foi.nwtis.lukkristi.web.zrna.Aktivnost2.DohvatiVlagu()");
        return temperatura;               
    }
      
    /**
     * Metoda koja salje websocketom posluzitelju poruku sa korsinikom i identom te dodaje u mojaerodrom
     * @param ident
     * @return
     */
    public String dodajAerodrom(String ident){
        if(odabraniKorisnik == null){
            PrijenosPodataka.saljiPoruku("Niste odabrali korisnika!");
            return "";
        }
        Korisnici k = korisniciFacadeLocal.find(odabraniKorisnik);
        Airports a= aerodromiFacadeLocal.find(ident);
        boolean Dodan=meteorologLocal.dodajAerodromKorisniku(k, a);
         if (Dodan) {
            PrijenosPodataka.saljiPoruku("Dodan je aerodrom korisniku: " + k.getKorIme() + " " + a.getIdent());
        } else {
            PrijenosPodataka.saljiPoruku("aerodrom nije dodan korisniku: " + k.getKorIme() + " " + a.getIdent() + " jer ga vec ima");
        }
        return "";
    }
}
