/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ws.serveri;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.lukkristi.web.podaci.AirportDAO;
import org.foi.nwtis.lukkristi.web.podaci.KorisnikDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.AvionLetiPozicije;
import org.foi.nwtis.rest.podaci.LetPozicija;

/**
 *
 * @author lukkristi
 */
@WebService(serviceName = "Zadaca2")
public class Zadaca2 {

    @Inject
    ServletContext context;

    /**
     * This is a sample web service operation
     *
     * @param noviKorisnik
     * @return
     */
    @WebMethod(operationName = "dodajKorisnika")
    public Boolean dodajKorisnika(@WebParam(name = "noviKorisnik") Korisnik noviKorisnik
            ) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO= KorisnikDAO.getInstanca(bpk);
        return korisnikDAO.dodajKorisnika(noviKorisnik, bpk);
        //TODO dodaj korisnika u BP, korisnikDAO kao primjer u priajsnjem primjeru 
        
    }
    
    
    /**
     * This is a sample web service operation
     *
     * @param korisnik
     * @param lozinka
     * @param icao24
     * @param zaVrijeme
     * @return
     */
    @WebMethod(operationName = "najvecaVisinaLetaAviona")
    public LetPozicija najvecaVisinaLetaAviona(@WebParam(name = "korIme") String korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao24") String icao24,
            @WebParam(name = "zaVrijeme") long zaVrijeme) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String osnKorisnik= bpk.getKonfig().dajPostavku("OpenSkyNetwork.korisnik");
        String osnLozinka= bpk.getKonfig().dajPostavku("OpenSkyNetwork.lozinka");
        OSKlijent oSKlijent= new OSKlijent(osnKorisnik, osnLozinka);
        AvionLetiPozicije pozicijeLetaAviona=oSKlijent.getTracks(icao24, zaVrijeme);
//        SimpleDateFormat dfad = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//            
//            Date lukas2=dfad.parse(lukas);               
//            System.out.println("LUKAS sa datumima "+ lukas2.getTime());
        if(pozicijeLetaAviona!=null){
            LetPozicija maxValue = pozicijeLetaAviona.getPath().stream().max(Comparator.comparing(v -> v.getBaro_altitude())).get();
            return maxValue;
        }
        
        //pronadji najvecu viisnu leta
        return null;                
    }
    
    /**
     * This is a sample web service operation
     *
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "provjeriKorisnika")
    public Boolean provjeriKorisnika(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO= KorisnikDAO.getInstanca(bpk);
        return korisnikDAO.provjeriKorisnika(korIme, lozinka);
        //TODO dodaj korisnika u BP, korisnikDAO kao primjer u priajsnjem primjeru 
        
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param korisnik
     * @return 
     */
    @WebMethod(operationName = "azurirajKorisnika")
    public boolean azurirajKorisnika(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "kor") Korisnik korisnik) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO= KorisnikDAO.getInstanca(bpk);
        return korisnikDAO.azurirajKorisnika(korisnik, lozinka, korIme);
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "dajKorisnike")
    public List<Korisnik> dajKorisnike(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO= KorisnikDAO.getInstanca(bpk);
        List<Korisnik> korisnici = new ArrayList<>();
        korisnici= korisnikDAO.dohvatiKorisnike(korIme, lozinka);
        return korisnici;
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "korisniciAerodroma")
    public List<Korisnik> korisniciAerodroma(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO= KorisnikDAO.getInstanca(bpk);
        List<Korisnik> korisnici = new ArrayList<>();
        korisnici= korisnikDAO.dohvatiKorisnikeAerodroma(korIme, lozinka);
        return korisnici;
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param naziv
     * @return 
     */
    @WebMethod(operationName = "dajAerodomeNaziv")
    public java.util.List<Aerodrom> dajAerodomeNaziv(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "naziv") String naziv) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);       
        return airportDAO.dohvatikAerodromeNaziv(korIme, lozinka, naziv);
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param iso_drzave
     * @return 
     */
    @WebMethod(operationName = "dajAerodomeDrzava")
    public java.util.List<Aerodrom> dajAerodomeDrzava(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "iso_drzave") String iso_drzave) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dohvatikAerodromeDrzava(korIme, lozinka, iso_drzave);
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "dajAerodomeKorisnika")
    public java.util.List<Aerodrom> dajAerodomeKorisnika(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dohvatikAerodromeKorisnika(korIme, lozinka);
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return 
     */
    @WebMethod(operationName = "imamAerodrom")
    public boolean imamAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.imamAerodrom(korIme, lozinka, ICAO);
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return 
     */
    @WebMethod(operationName = "dajAerodrom")
    public Aerodrom dajAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dohvatiAerodrom(korIme, lozinka, ICAO);
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return 
     */
    @WebMethod(operationName = "dodajMojAerodrom")
    public boolean dodajMojAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dodajMojAerodrom(korIme, lozinka, ICAO);
        
    }

    /**
     * Web service operation
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @param od
     * @param doDrugi
     * @return 
     */
    @WebMethod(operationName = "dajSveAvoneAerodromaOdDo")
    public java.util.List<AvionLeti> dajSveAvoneAerodromaOdDo(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") 
            String lozinka, @WebParam(name = "ICAO") String ICAO, @WebParam(name = "od") long od, 
            @WebParam(name = "doDrugi") long doDrugi) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        String osnKorisnik= bpk.getKonfig().dajPostavku("OpenSkyNetwork.korisnik");
        String osnLozinka= bpk.getKonfig().dajPostavku("OpenSkyNetwork.lozinka");
        OSKlijent oSKlijent= new OSKlijent(osnKorisnik, osnLozinka);             
//        SimpleDateFormat dfad = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
//        Date lukas2=dfad.parse(lukas);               
//        System.out.println("LUKAS sa datumima "+ lukas2.getTime());
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dajSveAvioneSaAerodromaOdDo(korIme, lozinka, ICAO, od, doDrugi, oSKlijent);
    }

    
    
    

    
}
