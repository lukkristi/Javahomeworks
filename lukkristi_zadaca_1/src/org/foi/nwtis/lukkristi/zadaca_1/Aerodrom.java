
package org.foi.nwtis.lukkristi.zadaca_1;

import java.io.Serializable;

/**
 *
 * @author lukkristi
 */
public class Aerodrom implements Serializable{
    private String icao;
    private String naziv;
    private String drzava;
    private String gSirina;
    private String gDuzina;

    /**
     * Konstruktor aerodroma
     * @param icao
     * @param naziv
     * @param drzava
     * @param gSirina
     * @param gDuzina
     */
    public Aerodrom(String icao, String naziv, String drzava, String gSirina, String gDuzina) {
        this.icao = icao;
        this.naziv = naziv;
        this.drzava = drzava;
        this.gSirina = gSirina;
        this.gDuzina = gDuzina;
    }
    public Aerodrom(){
    
    }

    /**
     * ICAO getter, vraća ICAO aerodroma
     * @return 
     */
    public String getIcao() {
        return icao;
    }

    /**
     * Dohvaćanje naziva aerodroma
     * @return 
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Dohvaćanje države u kojoj se nalazi aerodrom
     * @return 
     */
    public String getDrzava() {
        return drzava;
    }

    /**
     * Dohvaćanje geografske širine aerodroma
     * @return 
     */
    public String getgSirina() {
        return gSirina;
    }

    /**
     * Dohvaćanje geografske dužine aerodroma
     * @return 
     */
    public String getgDuzina() {
        return gDuzina;
    }

    /**
     * ICAO setter, postavlja ICAO aerodroma
     * @param icao 
     */
    public void setIcao(String icao) {
        this.icao = icao;
    }

    /**
     * Zadavanje naziva aerodroma
     * @param naziv 
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     * Zadavanje drzave u kojoj se nalazi aerodrom
     * @param drzava
     */
    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    /**
     * Zadavanje geografske širine aerodroma
     * @param gSirina
     */
    public void setgSirina(String gSirina) {
        this.gSirina = gSirina;
    }

    /**
     * Zadavanje geografske dužine aerodroma
     * @param gDuzina
     */
    public void setgDuzina(String gDuzina) {
        this.gDuzina = gDuzina;
    }
    
    
}
