/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ws.klijenti;

import java.util.List;
import org.foi.nwtis.lukkristi.ws.serveri.Aerodrom;
import org.foi.nwtis.lukkristi.ws.serveri.AvionLeti;
import org.foi.nwtis.lukkristi.ws.serveri.Korisnik;


/**
 *
 * @author lukkristi
 */
public class Zadaca2_1WS {
    
    /**
     * vraca listu korisnikovih aerodroma poziva soap seervis iz prve aplikacije
     * @param korisnik
     * @param lozinka
     * @return lista korisnikovih aerodroma
     */
    public List<Aerodrom> dajAerodomeKorisnika(String korisnik, String lozinka){
        List<Aerodrom> aerodromi = null;
        
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();                     
            aerodromi = port.dajAerodomeKorisnika(korisnik, lozinka);           
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 dajAerodromeKorisnika");
        }

        return aerodromi;
    }
    
    /**
     * Dodaje aerodrom u korisnikovu kolekciju 
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return vraca istinu ako je dodoa u suprotnom false
     */
    public boolean dodajMojAerodrom(String korisnik, String lozinka, String icao){
        boolean dodao= false;
        
        try {
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();          
            dodao= port.dodajMojAerodrom(korisnik, lozinka, icao);           
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 dodajMojAerodrom "+ex.getMessage());
        }

        return dodao;
    }
    
    /**
     * Vraca listu aerodroma prema nazivu koji je korisnik unjeo, u slucaju da je ostavio prazno vracaju se svi aerodromi 
     * @param korisnik
     * @param lozinka
     * @param naziv
     * @return vraca listu aerodroma prema nazivu ili svih aerodroma
     */
    public List<Aerodrom> dajAerodomeNaziv(String korisnik, String lozinka, String naziv){
        List<Aerodrom> aerodromi = null;
                
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            if(naziv==null)
                naziv="";
            aerodromi = port.dajAerodomeNaziv(korisnik, lozinka, naziv);          
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 dajAerodromNaziv "+ex.getMessage());
        }
        return aerodromi;
    }
    
    /**
     * Vraca listu aerodroma prema drzavi koju je korisnik unjeo
     * @param korisnik
     * @param lozinka
     * @param drzava
     * @return lista aerodroma prema unesenoj drzavi
     */
    public List<Aerodrom> dajAerodomeDrzava(String korisnik, String lozinka, String drzava){
        List<Aerodrom> aerodromi = null;
                
        
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();           
            aerodromi= port.dajAerodomeDrzava(korisnik, lozinka, drzava);                      
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 dajAerodromDrzava "+ex.getMessage());
        }
        return aerodromi;
    }
    
    /**
     * Vraca objekt Aerodrom prema unesenom icao
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return podataka tipa Aerodrom
     */
    public Aerodrom dajAerodomKorisnika(String korisnik, String lozinka, String icao){
        Aerodrom aerodrom = null;                   
        
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();           
            aerodrom= port.dajAerodrom(korisnik, lozinka, icao);          
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 dajAerodrom "+ex.getMessage());
        }

        return aerodrom;
    }
    
    /**
     * Provjerava korisnika u bazi pdoataka , metoda sluzi za prijavu korisnika
     * @param korisnik
     * @param lozinka
     * @return vraca true ako su ispravni podaci u suprotnom false
     */
    public boolean provjeriKorisnika(String korisnik, String lozinka){               
        boolean ispravno=false;
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();           
            ispravno= port.provjeriKorisnika(korisnik, lozinka);          
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 provjeri korisnika "+ex.getMessage());
        }

        return ispravno;
    }
     
    /**
     * Metoda dodaje noovog korisnika kod registracije u bazu podataka
     * @param novi
     * @return vraca true ako je dodala korisnika inace false
     */
    public boolean dodajKorisnika(Korisnik novi){               
        boolean ispravno=false;
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();           
            ispravno= port.dodajKorisnika(novi);          
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 provjeri korisnika "+ex.getMessage());
        }

        return ispravno;
    }
     
    /**
     * Dohvaca sve avione koji su poletjeli sa odredjenog aerodroma u periodu od do
     * @param korisnik
     * @param lozinka
     * @param icao
     * @param odDat
     * @param doDat
     * @return vraca listu aviona
     */
    public List<AvionLeti> dajAvioneAerodomaOdDo(String korisnik, String lozinka, String icao,long odDat, long doDat){
        List<AvionLeti> avioni = null;
                
        
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();           
            avioni= port.dajSveAvoneAerodromaOdDo(korisnik, lozinka, icao, odDat, doDat);                      
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.3 dajAerodromAvioneOdDo "+ex.getMessage());
        }
        return avioni;
    }
}
