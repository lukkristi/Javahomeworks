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
     * Metoda dodaje noovog korisnika kod registracije u bazu podataka
     *
     * @param noviKorisnik
     * @return vraca true ako je dodala korisnika inace false
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
     * metoda koja vraca najvecu objekt LetPozicija iz kojeg mozemo dobiti najvecu visinu koju je avion dosegno tokom putovanja
     * 
     * @param korisnik
     * @param lozinka
     * @param icao24
     * @param zaVrijeme
     * @return vraca objekt LetPozicija
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
     * Provjerava korisnika u bazi pdoataka , metoda sluzi za prijavu korisnika
     *
     * @param korIme
     * @param lozinka
     * @return vraca true ako su ispravni podaci u suprotnom false
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
     * Metoda koja azurira korisnika u bazi
     * @param korIme
     * @param lozinka
     * @param korisnik
     * @return vraca true ako je korisnik azuriran u suprotnom false
     */
    @WebMethod(operationName = "azurirajKorisnika")
    public boolean azurirajKorisnika(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "kor") Korisnik korisnik) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO= KorisnikDAO.getInstanca(bpk);
        return korisnikDAO.azurirajKorisnika(korisnik, lozinka, korIme);
    }

    /**
     * Metoda koja vraca listu svih korisnika
     * @param korIme
     * @param lozinka
     * @return lista korisnika
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
     * vraca listu korisnikovih aerodroma poziva soap seervis iz prve aplikacije
     * @param korIme
     * @param lozinka
     * @return lista korisnikovih aerodroma
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
     * Vraca listu aerodroma prema nazivu koji je korisnik unjeo, u slucaju da je ostavio prazno vracaju se svi aerodromi 
     * @param korIme
     * @param lozinka
     * @param naziv
     * @return vraca listu aerodroma prema nazivu ili svih aerodroma
     */
    @WebMethod(operationName = "dajAerodomeNaziv")
    public java.util.List<Aerodrom> dajAerodomeNaziv(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "naziv") String naziv) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);       
        return airportDAO.dohvatikAerodromeNaziv(korIme, lozinka, naziv);
    }

    /**
     * Vraca listu aerodroma prema drzavi koju je korisnik unjeo
     * @param korIme
     * @param lozinka
     * @param iso_drzave
     * @return lista aerodroma prema unesenoj drzavi
     */
    @WebMethod(operationName = "dajAerodomeDrzava")
    public java.util.List<Aerodrom> dajAerodomeDrzava(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "iso_drzave") String iso_drzave) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dohvatikAerodromeDrzava(korIme, lozinka, iso_drzave);
    }

    /**
     * Dohavaca sve aerodrome od svih korisnika koje  prate 
     * @param korIme
     * @param lozinka
     * @return listu aerodroma
     */
    @WebMethod(operationName = "dajAerodomeKorisnika")
    public java.util.List<Aerodrom> dajAerodomeKorisnika(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dohvatikAerodromeKorisnika(korIme, lozinka);
    }

    /**
     * Provjerava u korisnikovoj kolekciji dali postoji aerodrom 
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return true ako postoji inace false
     */
    @WebMethod(operationName = "imamAerodrom")
    public boolean imamAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.imamAerodrom(korIme, lozinka, ICAO);
    }

    /**
     * Vraca objekt Aerodrom prema unesenom icao, vraca korisnikov aerodroma prema icao
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return podataka tipa Aerodrom
     */
    @WebMethod(operationName = "dajAerodrom")
    public Aerodrom dajAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ICAO") String ICAO) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dohvatiAerodrom(korIme, lozinka, ICAO);
    }

    /**
     * operacija koja poziva metodu za dodavanje aerodroma u korisnikovu kolekciju 
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return  true ako je pohranio uspjesno incae false
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
     * Dohvaca sve avione koji su poletjeli sa odredjenog aerodroma u periodu od do
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @param od
     * @param doDrugi
     * @return vraca listu aviona
     */
    @WebMethod(operationName = "dajSveAvoneAerodromaOdDo")
    public java.util.List<AvionLeti> dajSveAvoneAerodromaOdDo(@WebParam(name = "korIme") String korIme, @WebParam(name = "lozinka") 
            String lozinka, @WebParam(name = "ICAO") String ICAO, @WebParam(name = "od") long od, 
            @WebParam(name = "doDrugi") long doDrugi) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");                
//        SimpleDateFormat dfad = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
//        Date lukas2=dfad.parse(lukas);               
//        System.out.println("LUKAS sa datumima "+ lukas2.getTime());
        AirportDAO airportDAO= AirportDAO.getInstanca(bpk);
        return airportDAO.dajSveAvioneSaAerodromaOdDo(korIme, lozinka, ICAO, od, doDrugi);
    }

    
    
    

    
}
