
package org.foi.nwtis.lukkristi.zadaca_1;

/**
 *
 * @author lukkristi
 */
public class Korisnik {
    private String korime;
    private String lozinka;

    /**
     * Konstruktor korisnika
     * @param korime
     * @param lozinka 
     */
    public Korisnik(String korime, String lozinka) {
        this.korime = korime;
        this.lozinka = lozinka;
    }

    /**
     * Dohvaćanje korisničkog imena korisnika
     * @return 
     */
    public String getKorime() {
        return korime;
    }

    /**
     * Zadavanje korisničkog imena korisnika
     * @param korime 
     */
    public void setKorime(String korime) {
        this.korime = korime;
    }

    /**
     * Dohvaćanje lozinke korisnika
     * @return 
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     * Postavljanje lozinke korisnika
     * @param lozinka 
     */
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    
}
