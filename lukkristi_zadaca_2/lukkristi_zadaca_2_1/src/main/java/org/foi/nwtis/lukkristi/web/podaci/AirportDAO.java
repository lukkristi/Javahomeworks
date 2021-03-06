/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.lukkristi.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 *
 * @author lukkristi
 */
public class AirportDAO {

    private String server;
    private String korisnik;
    private String lozinkaBP;
    private String baza;
    private BP_Konfiguracija bpk;
    private KorisnikDAO korisnikDAO;

    private static AirportDAO instanca = null;

    private AirportDAO(BP_Konfiguracija bpk) {
        this.bpk = bpk;
        this.baza = bpk.getUserDatabase();
        this.server = bpk.getServerDatabase();
        this.korisnik = bpk.getUserUsername();
        this.lozinkaBP = bpk.getUserPassword();
        this.korisnikDAO = KorisnikDAO.getInstanca(bpk);

    }

    /**
     * Singleton varijanta konstruktor 
     * @param bpk
     * @return
     */
    public static AirportDAO getInstanca(BP_Konfiguracija bpk) {
        if (instanca == null) {
            instanca = new AirportDAO(bpk);
        }
        return instanca;
    }

    /**
     * Metoda dodaje noovog korisnika u bazu podataka
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return vraca true ako je dodala korisnika inace false
     */
    public boolean dodajMojAerodrom(String korIme, String lozinka, String ICAO) {
        if (!korisnikDAO.provjeriKorisnika(korIme, lozinka)) {
            return false;
        }
        String upit = "SELECT ident FROM AIRPORTS WHERE ident= '" + ICAO + "'";
        String upit1 = "INSERT INTO MYAIRPORTS (ident, username, stored) VALUES ('" + ICAO + "', '" + korIme + "', CURRENT_TIMESTAMP)";              
                                       
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Airport> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    s.executeUpdate(upit1);
                    return true;                                                                                
                }               

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @return
     */
    public List<Airport> dajSveKorisnikAerodrome() {
        String upit = "SELECT a.* FROM MYAIRPORTS ma,AIRPORTS a WHERE ma.IDENT=a.IDENT";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Airport> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    String elevation_ft = rs.getString("elevation_ft");
                    String continent = rs.getString("continent");
                    String iso_country = rs.getString("iso_country");
                    String iso_region = rs.getString("iso_region");
                    String municipality = rs.getString("municipality");
                    String gps_code = rs.getString("gps_code");
                    String iata_code = rs.getString("iata_code");
                    String local_code = rs.getString("local_code");
                    String coordinates = rs.getString("coordinates");
                    Airport a = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);

                    aerodromi.add(a);
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param ident
     * @param datum
     * @return
     */
    public boolean provjeriAerodromULog(String ident, java.sql.Date datum) {
        String upit = "SELECT * FROM MYAIRPORTSLOG WHERE "
                + "IDENT = '" + ident + "' AND FLIGHTDATE = '" + datum + "'";
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
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL provjera aerodromaUlog " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @param ident
     * @param datum
     * @param brojAviona
     */
    public void spremiAerodromULog(String ident, java.sql.Date datum, int brojAviona) {
        String upit = "INSERT INTO  MYAIRPORTSLOG (IDENT, FLIGHTDATE, STORED, NUMBEROFAIRPLANES) VALUES ("
                + "'" + ident + "', '" + datum + "', CURRENT_TIMESTAMP, " + brojAviona + ")";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));
            try {
                Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                Statement s = con.createStatement();
                s.executeUpdate(upit);

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL spremanje aerodromaUlog " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ClassNotFoundException spremanje aerodromaUlog " + ex);
        }
    }

    /**
     *
     * @param avioni
     */
    public void spremiAvione(List<AvionLeti> avioni) {
        for (AvionLeti avion : avioni) {
            if (avion.getEstArrivalAirport() != null) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String upit = "INSERT INTO AIRPLANES (icao24, firstseen, estdepartureairport, lastseen, estarrivalairport, "
                        + "callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, "
                        + "estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount, stored)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try (Connection con = DriverManager.
                        getConnection(server + baza, korisnik, lozinkaBP);) {
                    PreparedStatement s = con.prepareCall(upit);
                    s.setString(1, avion.getIcao24());
                    s.setInt(2, avion.getFirstSeen());
                    s.setString(3, avion.getEstDepartureAirport());
                    s.setInt(4, avion.getLastSeen());
                    s.setString(5, avion.getEstArrivalAirport());
                    s.setString(6, avion.getCallsign());
                    s.setInt(7, avion.getEstDepartureAirportHorizDistance());
                    s.setInt(8, avion.getEstDepartureAirportVertDistance());
                    s.setInt(9, avion.getEstArrivalAirportHorizDistance());
                    s.setInt(10, avion.getEstArrivalAirportVertDistance());
                    s.setInt(11, avion.getDepartureAirportCandidatesCount());
                    s.setInt(12, avion.getArrivalAirportCandidatesCount());
                    s.setTimestamp(13, timestamp);
                    s.executeUpdate();
                    //                rs.close();
                    //                s.close();
                    //                con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("SQL spremiAvione " + ex);
                }
            }
        }
    }

    /**
     * Vraca listu aerodroma prema nazivu koji je korisnik unjeo, u slucaju da je ostavio prazno vracaju se svi aerodromi 
     * @param korIme
     * @param lozinka
     * @param naziv
     * @return vraca listu aerodroma prema nazivu ili svih aerodroma
     */
    public List<Aerodrom> dohvatikAerodromeNaziv(String korIme, String lozinka, String naziv) {
        if (!korisnikDAO.provjeriKorisnika(korIme, lozinka)) {
            return null;
        }
        String upit = "SELECT ident, name, iso_country, coordinates  FROM AIRPORTS WHERE NAME LIKE '%" + naziv + "%'";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Aerodrom> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String name = rs.getString("name");
                    String iso_country = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] lonILat = coordinates.split(",");
                    Lokacija l = new Lokacija();
                    l.postavi(lonILat[0].trim(), lonILat[1].trim());
                    Aerodrom a = new Aerodrom(ident, name, iso_country, l);

                    aerodromi.add(a);
                }
                if (aerodromi.isEmpty()) {
                    return null;
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL dohvatikAerodromeNaziv " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Vraca listu aerodroma prema drzavi koju je korisnik unjeo
     * @param korIme
     * @param lozinka
     * @param drzava
     * @return lista aerodroma prema unesenoj drzavi
     */
    public List<Aerodrom> dohvatikAerodromeDrzava(String korIme, String lozinka, String drzava) {
        if (!korisnikDAO.provjeriKorisnika(korIme, lozinka)) {
            return null;
        }
        String upit = "SELECT * FROM AIRPORTS WHERE iso_country = '" + drzava + "'";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Aerodrom> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String name = rs.getString("name");
                    String iso_country = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] lonILat = coordinates.split(",");
                    Lokacija l = new Lokacija();
                    l.postavi(lonILat[0].trim(), lonILat[1].trim());
                    Aerodrom a = new Aerodrom(ident, name, iso_country, l);

                    aerodromi.add(a);
                }
                if (aerodromi.isEmpty()) {
                    return null;
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL dohvatikAerodromeDrzava " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dohavaca sve aerodrome od svih korisnika koje  prate 
     * @param korIme
     * @param lozinka
     * @return listu aerodroma
     */
    public List<Aerodrom> dohvatikAerodromeKorisnika(String korIme, String lozinka) {
        if (!korisnikDAO.provjeriKorisnika(korIme, lozinka)) {
            return null;
        }
         String upit = "SELECT ma.ident, name, iso_country, coordinates  FROM AIRPORTS a, MYAIRPORTS ma WHERE ma.USERNAME='" + korIme + "' "
                 + "AND a.ident=ma.IDENT";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Aerodrom> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String name = rs.getString("name");
                    String iso_country = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] lonILat = coordinates.split(",");
                    Lokacija l = new Lokacija();
                    l.postavi(lonILat[0].trim(), lonILat[1].trim());
                    Aerodrom a = new Aerodrom(ident, name, iso_country, l);

                    aerodromi.add(a);
                }
                if (aerodromi.isEmpty()) {
                    return null;
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL dohvatikAerodromeKorisnika " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
//        List<Airport> airportovi = dajSveKorisnikAerodrome();
//        List<Aerodrom> aerodromi = new ArrayList<>();
//        List<Korisnik> korisnici= korisnikDAO.dohvatiKorisnikeAerodroma(korIme, lozinka);
        
//        for (Airport a : airportovi) {
//            if(a.ge)
//            String ident = a.getIdent();
//            String name = a.getName();
//            String iso_country = a.getIso_country();
//            String coordinates = a.getCoordinates();
//            String[] lonILat = coordinates.split(",");
//            Lokacija l = new Lokacija();
//            l.postavi(lonILat[0].trim(), lonILat[1].trim());
//            Aerodrom aer = new Aerodrom(ident, name, iso_country, l);
//
//            aerodromi.add(aer);
//        }       
    }
    
    /**
     * Provjerava u myairports dali postoji aerodrom 
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @return true ako postoji inace ne 
     */
    public boolean imamAerodrom(String korIme, String lozinka, String ICAO) {
        if (!korisnikDAO.provjeriKorisnika(korIme, lozinka)) {
            return false;
        }
        String upit = "SELECT * FROM MYAIRPORTS WHERE "
                + "IDENT = '" + ICAO + "' AND USERNAME = '" + korIme + "'";
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
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL provjera imamAerodrom " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Vraca objekt Aerodrom prema unesenom icao, vraca korisnikov aerodroma prema icao
     * @param korIme
     * @param lozinka
     * @param icao
     * @return vraca aredrom ako postoji
     */
    public Aerodrom dohvatiAerodrom(String korIme, String lozinka, String icao) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        aerodromi= dohvatikAerodromeKorisnika(korIme,lozinka);
        if(!aerodromi.isEmpty()){
            for(Aerodrom a: aerodromi){
                if(a.getIcao().equals(icao))
                    return a;
            }                                      
        }
        return null;          
    }
    
    /**
     *  Dohvaca sve avione koji su poletjeli sa odredjenog aerodroma u periodu od do
     * @param korIme
     * @param lozinka
     * @param ICAO
     * @param od
     * @param drugi
     * @return vraca listu aviona
     */
    public List<AvionLeti> dajSveAvioneSaAerodromaOdDo(String korIme,String lozinka, String ICAO, long od, long drugi) {
        if (!korisnikDAO.provjeriKorisnika(korIme, lozinka)) {
            return null;
        }
//        List<AvionLeti> avioni = oSKlijent.getDepartures(ICAO, od/1000, drugi/1000);
//        if(!avioni.isEmpty()){
//            return avioni;
//        }
//        return null;
        String upit = "SELECT * FROM AIRPLANES WHERE FIRSTSEEN >= " + od + " AND LASTSEEN<=" + drugi + " AND ESTDEPARTUREAIRPORT='"+ICAO+"'";
        try {
            Class.forName(bpk.getDriverDatabase(server + baza));

            try (
                    Connection con = DriverManager.getConnection(server + baza, korisnik, lozinkaBP);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<AvionLeti> avioni = new ArrayList<>();

                while (rs.next()) {
                    String icao24 = rs.getString("icao24");
                    int type = rs.getInt("firstseen");
                    String name = rs.getString("estDepartureAirport");
                    int elevation_ft = rs.getInt("lastSeen");
                    String continent = rs.getString("estArrivalAirport");
                    String iso_country = rs.getString("callsign");
                    int iso_region = rs.getInt("estDepartureAirportHorizDistance");
                    int municipality = rs.getInt("estDepartureAirportVertDistance");
                    int gps_code = rs.getInt("estArrivalAirportHorizDistance");
                    int iata_code = rs.getInt("estArrivalAirportVertDistance");
                    int local_code = rs.getInt("departureAirportCandidatesCount");
                    int coordinates = rs.getInt("arrivalAirportCandidatesCount");
                    AvionLeti a = new AvionLeti(icao24, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);

                    avioni.add(a);
                }
                return avioni;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("SQL avioniOdDo " + ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
               
}
