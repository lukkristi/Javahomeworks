/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Korisnik;

/**
 *
 * @author lukkristi
 */
public class KorisnikDAO {

    private String server;
    private String korisnik;
    private String lozinkaBP;
    private String baza;
    private BP_Konfiguracija bpk;
    
    private static KorisnikDAO instanca = null;

    private KorisnikDAO(BP_Konfiguracija bpk) {
        this.bpk=bpk;
        this.baza = bpk.getUserDatabase();
        this.server = bpk.getServerDatabase();
        this.korisnik = bpk.getUserUsername();
        this.lozinkaBP = bpk.getUserPassword();
    }
    
    public static KorisnikDAO getInstanca(BP_Konfiguracija bpk) {
        if (instanca == null) {
            instanca = new KorisnikDAO(bpk);
        }
        return instanca;
    }

    public List<Korisnik> dohvatiKorisnike(String korIme, String lozinka) {
        if(!provjeriKorisnika(korIme, lozinka))
            return null;
        String upit = "SELECT * FROM korisnici";

        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                
                List<Korisnik> korisnici= new ArrayList<Korisnik>(); 
                while (rs.next()) {
                    String korisnik1 = rs.getString("kor_ime");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("email_adresa");                   
                    Date kreian = rs.getDate("datum_kreiranja");
                    Date promjena = rs.getTimestamp("datum_promjene");
                    Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreian, promjena);
                    korisnici.add(k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex+" SQL exception kod dohvati korisnike");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex+" ClassNotFoundException exception kod dohvati korisnike");
        }
        return null;
    }

    public boolean azurirajKorisnika(Korisnik k, String lozinka, String korIme) {       
        String upit = "";
        if(!provjeriKorisnika(korIme, lozinka))
            return false;
        if (provjeraPodataka(k)) {
            return false;
        }
        if (k.getLozinka() != null && !k.getLozinka().isEmpty()) {
            upit = "UPDATE korisnici SET IME = '" + k.getIme() + "', PREZIME = '" + k.getPrezime()
                    + "', EMAIL_ADRESA = '" + k.getEmailAdresa() + "', LOZINKA = '" + k.getLozinka() + "', DATUM_PROMJENE = CURRENT_TIMESTAMP WHERE "
                    + "KOR_IME = '" + k.getKorIme() + "'";
        } else {
            upit = "UPDATE korisnici SET IME = '" + k.getIme() + "', PREZIME = '" + k.getPrezime()
                    + "', EMAIL_ADRESA = '" + k.getEmailAdresa() + "', LOZINKA = '" + lozinka + "', DATUM_PROMJENE = CURRENT_TIMESTAMP WHERE "
                    + "KOR_IME = '" + k.getKorIme() + "'";
        }

        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement()) {

                int brojAzuriranja = s.executeUpdate(upit);

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean provjeraPodataka(Korisnik k) {
        try{
            if( !k.getIme().isEmpty() && k.getIme() != null && !k.getKorIme().isEmpty() && k.getKorIme() != null && !k.getEmailAdresa().isEmpty()
                && k.getEmailAdresa() != null && !k.getPrezime().isEmpty() && k.getPrezime() != null)
            return false;
        }
        catch (NullPointerException exp){
            return true;
        }
        return true;
    }

    public boolean dodajKorisnika(Korisnik k, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        if (provjeraPodataka(k)) {
            return false;
        }
        String upit = "INSERT INTO korisnici (IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE) VALUES ("
                + "'" + k.getIme() + "', '" + k.getPrezime()
                + "', '" + k.getEmailAdresa() + "', '" + k.getKorIme() + "', '" + k.getLozinka() + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {

                int brojAzuriranja = s.executeUpdate(upit);

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean provjeriKorisnika(String korIme, String lozinka) {
        String upit = "SELECT IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA FROM korisnici WHERE "
                + "KOR_IME = '" + korIme + "' AND LOZINKA = '" + lozinka + "'";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));
            try {
                Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery(upit);
                if (rs.next()) {
                    return true;
                }

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<Korisnik> dohvatiKorisnikeAerodroma(String korIme, String lozinka) {
        if(!provjeriKorisnika(korIme, lozinka))
            return null;
        String upit = "SELECT DISTINCT k.* FROM korisnici k, myairports ma WHERE k.KOR_IME=ma.USERNAME";

        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {
                
                List<Korisnik> korisnici= new ArrayList<Korisnik>(); 
                while (rs.next()) {
                    String korisnik1 = rs.getString("kor_ime");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("email_adresa");                   
                    Date kreian = rs.getDate("datum_kreiranja");
                    Date promjena = rs.getTimestamp("datum_promjene");
                    Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreian, promjena);
                    korisnici.add(k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex+" SQL exception kod dohvati korisnike");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex+" ClassNotFoundException exception kod dohvati korisnike");
        }
        return null;
    }
    
}
