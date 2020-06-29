/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ws.klijenti;

import java.util.List;
import org.foi.nwtis.lukkristi.ws.serveri.Aerodrom;


/**
 *
 * @author lukkristi
 */
public class Zadaca2_1WS {
    
    /**
     * Dohavaca aerodrome korisnika koje prati
     * @param korisnik korisnicko ime
     * @param lozinka
     * @return vraca listu korisnokovih aerodroma
     */
    public List<Aerodrom> dajAerodomeKorisnika(String korisnik, String lozinka){
        List<Aerodrom> aerodromi = null;
        
        try { 
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();                     
            aerodromi = port.dajAerodomeKorisnika(korisnik, lozinka);           
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.2 dajAerodromeKorisnika");
        }

        return aerodromi;
    }
    
    /**
     * Dodaje odabrani aerodrom u bazu prema obranom icao 
     * @param korisnik
     * @param lozinka
     * @param icao 
     * @return vraca true ako je dodao u suprotnom false
     */
    public boolean dodajMojAerodrom(String korisnik, String lozinka, String icao){
        boolean dodao= false;
        
        try {
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.lukkristi.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.lukkristi.ws.serveri.Zadaca2 port = service.getZadaca2Port();          
            dodao= port.dodajMojAerodrom(korisnik, lozinka, icao);           
        } catch (Exception ex) {
            System.out.println("GRESKA kod 2.2 dodajMojAerodrom "+ex.getMessage());
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
            System.out.println("GRESKA kod 2.2 dajAerodromNaziv "+ex.getMessage());
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
            System.out.println("GRESKA kod 2.2 dajAerodromDrzava "+ex.getMessage());
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
            System.out.println("GRESKA kod 2.2 dajAerodrom "+ex.getMessage());
        }

        return aerodrom;
    }
}
