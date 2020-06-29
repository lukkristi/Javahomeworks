/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import javax.ejb.Local;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Korisnici;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author lukkristi
 */
@Local
public interface MeteorologLocal {

    MeteoPodaci dohvatiGeoIMeteoPodatke(String ident,BP_Konfiguracija bpk);

    boolean dodajAerodromKorisniku(Korisnici korime, Airports aerodrom);
    
}
