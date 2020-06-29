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
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.podaci.OdgovorAerodrom;

/**
 *
 * @author lukkristi
 */
@Named(value = "dodavanjeAerodroma")
@javax.enterprise.context.SessionScoped
public class DodavanjeAerodroma implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;
    String korisnik = "";
    String lozinka = "";

    @Getter
    @Setter
    String drzava;
    
    @Getter
    @Setter
    String naziv;

    @Getter
    List<Aerodrom> aerodromi;
    @Getter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    @Getter
    @Setter
    String icaoOdabrani;

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public DodavanjeAerodroma() {
    }

    /**
     * metoda koja preuzima podatke injekcijom od prijave
     */
    public void preuzmiPodatkeKorisnika() {
        korisnik = prijavaKorisnika.getKorisnik();
        lozinka = prijavaKorisnika.getLozinka();

    }

    /**
     * Metoda koja pziva REST servis  i dohvaca aerodrome prema nazivu ili drzavi
     * @return
     */
    public String dajAerodromeDrzavaNaziv() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        System.err.println(drzava+" drzava");
        System.err.println(naziv+" naziv");
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeDrzavaNaziv(OdgovorAerodrom.class, drzava, naziv);
        aerodromi =new  LinkedList<> (Arrays.asList(odgovor.getOdgovor()));
        OdgovorAerodrom odgovor1 = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika =new  LinkedList<> (Arrays.asList(odgovor1.getOdgovor()));
        izbaciKorisnikoveAerodrome();
        return "";
    }

    /**
     * metoda dodaje aerodrom u korisnikovu kolekciju 
     * @param icao
     * @return
     */
    public String dodajAerodromKorisniku(String icao) {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka, icao);
        Object odg=null;
        Response res=zadaca2_2RS.dodajMojAerodom(odg);
        return "";
    }

    /**
     * Metoda brise aerodrome koje je korisnik vec prati
     */
    public void izbaciKorisnikoveAerodrome() {
        List<Aerodrom> duplikati= new ArrayList<>();
        if (aerodromi != null && !aerodromi.isEmpty() && aerodromiKorisnika != null && !aerodromiKorisnika.isEmpty()) {
             for(Aerodrom ak: aerodromiKorisnika){
                 for(Aerodrom a: aerodromi){
                     if(ak.getIcao().equals(a.getIcao())){                         
                            duplikati.add(a);                        
                     }
                     
                         
                 }
             }
                 
//             System.err.println(aerodromi.size()+" prije izbacivanja");
             aerodromi.removeAll(duplikati);
//             System.err.println(aerodromi.size()+" nakon");
        }
    }

}
