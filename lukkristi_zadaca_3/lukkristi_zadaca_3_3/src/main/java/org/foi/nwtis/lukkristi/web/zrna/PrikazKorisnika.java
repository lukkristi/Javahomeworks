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
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.ejb.eb.Myairports;
import org.foi.nwtis.lukkristi.ejb.eb.Myairportslog;
import org.foi.nwtis.lukkristi.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.lukkristi.ejb.sb.MyairportslogFacadeLocal;
import org.foi.nwtis.podaci.Korisnik;

/**
 *
 * @author lukkristi
 */
@Named(value = "prikazKorisnika")
@ViewScoped
public class PrikazKorisnika implements Serializable {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    @EJB
    MyairportslogFacadeLocal myairportslogFacadeLocal;
    @Getter
    List<Korisnici> korisnici = new ArrayList<>();
    @Getter
    List<Myairports> aerodromiKorisnika = new ArrayList<>();
    @Getter
    List<Myairportslog> aerodromLogovi = new ArrayList<>();
    @Getter
    @Setter
    String odabraniKorisnik = null;
    @Getter
    @Setter
    boolean odabraniKorisnik2 = false;
    @Getter
    @Setter
    boolean odabraniAerodom = false;
    private boolean prikaziAerodrome = false;
    @Getter
    @Setter
    private Airports aerodromOdabrani;

    /**
     * Creates a new instance of PrikazKorisnika
     */
    public PrikazKorisnika() {
    }

    @PostConstruct
    private void dohvatiKorisnike() {
        korisnici = korisniciFacadeLocal.findAll();
    }

    /**
     * metoda poziva facade korisnika i dohvaca korisnikove aerodrome ali prethodno ako je popunjena ocisti 
     * ju te poziva metodu za otkrivanje tablice
     * @return
     */
    public String dohvatiAerodromeKorisnika() {
        if (!aerodromiKorisnika.isEmpty()) {
            aerodromiKorisnika.clear();
        }
        Korisnici k = korisniciFacadeLocal.find(odabraniKorisnik);
        aerodromiKorisnika.addAll(k.getMyairportsList());
        prikaziAerodrome();      
        return "";
    }
    
    /**
     * metoda koja postavlja vrijednost atributa rendered u tablici true , tj. postaje vidljiva
     * @return
     */
    public String prikaziAerodrome(){        
        odabraniKorisnik2 = true;
        return "";
    }
    
    /**
     * metoda pomocu facade myairporslog dohvaca od odabranog aerodroma logove
     * prethodno ocisti listu ako je popunjena 
     * @param aerodrom
     * @return
     */
    public String dohvatiAerodromLog(Airports aerodrom) {
        if (!aerodromLogovi.isEmpty()) {
            aerodromLogovi.clear();
        }
        aerodromOdabrani=aerodrom;        
        aerodromLogovi.addAll(aerodrom.getMyairportslogList());
        prikaziAerodromLogove();        
        return "";
    }
    
    /**
     * metoda koja tkriva tablicu za P3
     * @return
     */
    public String prikaziAerodromLogove(){        
        odabraniAerodom = true;
        return "";
    }
    
    /**
     * metoda koja brise log iz baze i lista
     * @param log
     * @return
     */
    public String izbrisiAerodromLog(Myairportslog log){
        aerodromOdabrani.getMyairportslogList().remove(log);
        myairportslogFacadeLocal.remove(log);
        aerodromLogovi.remove(log);
        return "";
    }
    
    /**
     * metoda koja skriva obje tablice P2 i P3
     * @return
     */
    public String vratiNaPocetak(){        
        odabraniAerodom = false;
        odabraniKorisnik2 = false;
        return "";
    }
    
    

}
