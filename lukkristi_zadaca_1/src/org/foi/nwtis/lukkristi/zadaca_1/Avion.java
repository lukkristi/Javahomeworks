
package org.foi.nwtis.lukkristi.zadaca_1;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author lukkristi
 */
public class Avion implements Serializable{
    
    private String naziv;
    private Aerodrom polazniAerodrom;
    private Aerodrom odredisniAerodrom;
    private Date vrijemePolijetanja;
    private Date vrijemeSlijetanja;

    public Avion(String naziv, Aerodrom polazniAerodrom, Aerodrom odredisniAerodrom, Date vrijemePolijetanja, Date vrijemeSlijetanja) {
        this.naziv = naziv;
        this.polazniAerodrom = polazniAerodrom;
        this.odredisniAerodrom = odredisniAerodrom;
        this.vrijemePolijetanja = vrijemePolijetanja;
        this.vrijemeSlijetanja = vrijemeSlijetanja;
    }

    

    public Avion() {
    }

    

    /**
     * vraća naziv aviona
     * @return
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Postavlja naziv aviona
     * @param naziv
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
    /**
     * vraća naziv aerodroma gdje se nalazi avion
     * @return
     */
    public Aerodrom getPolazniAerodrom() {
        return polazniAerodrom;
    }

    /**
     * Postavlja naziv aerodroma aviona
     * @param polazniAerodrom
     * 
     */
    public void setPolazniAerodrom(Aerodrom polazniAerodrom) {
        this.polazniAerodrom = polazniAerodrom;
    }
    
    /**
     * vraća naziv odredisnog aerodroma gdje se nalazi avion
     * @return
     */
    public Aerodrom getOdredisniAerodrom() {
        return odredisniAerodrom;
    }

    /**
     * Postavlja naziv odredisnog aerodroma aviona
     * @param odredisniAerodrom
     * 
     */
    public void setOdredisniAerodrom(Aerodrom odredisniAerodrom) {
        this.odredisniAerodrom = odredisniAerodrom;
    }
    

    /**
     * Vraća vrijeme polijetanja aviona
     * @return
     */
    public Date getVrijemePolijetanja() {
        return vrijemePolijetanja;
    }

    /**
     * Vraća vrijeme slijetanja aviona
     * @return
     */
    public Date getVrijemeSlijetanja() {
        return vrijemeSlijetanja;
    }

    /**
     * Postavlja vrijeme polijetanja aviona
     * @param vrijemePolijetanja
     */
    public void setVrijemePolijetanja(Date vrijemePolijetanja) {
        this.vrijemePolijetanja = vrijemePolijetanja;
    }

    /**
     * Postavlja vrijeme slijetanja aviona
     * @param vrijemeSlijetanja
     */
    public void setVrijemeSlijetanja(Date vrijemeSlijetanja) {
        this.vrijemeSlijetanja = vrijemeSlijetanja;
    }
    
}
