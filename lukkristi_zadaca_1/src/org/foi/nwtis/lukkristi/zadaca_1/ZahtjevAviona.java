package org.foi.nwtis.lukkristi.zadaca_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.foi.nwtis.lukkristi.konfiguracije.Konfiguracija;

/**
 *
 * @author lukkristi
 */
public class ZahtjevAviona extends Thread {

    private Socket socket;
    private Konfiguracija konfiguracija;
    private List<Korisnik> korisnici;
    List<Avion> avioni;
    private List<Aerodrom> aerodromi;
    boolean radi = true;
    private ServerAviona serverAviona;

    /**
     * Konstruktor klase, postavlja grupu dretve, ime dretve, konfiguraciju, dohvaća korisnike, aerdrome i avione
     *
     * @param group grupa dretve
     * @param name naziv dretve
     * @param konfiguracija konfiguracija programa
     * @param korisnici lista korisnika
     * @param aerodromi lista aerodroma
     * @param avioni lista aviona
     */
    public ZahtjevAviona(ServerAviona server, ThreadGroup group, String name, Konfiguracija konfiguracija, List<Korisnik> korisnici,
            List<Aerodrom> aerodromi, List<Avion> avioni) {
        super(group, name);
        this.aerodromi = aerodromi;
        this.avioni = avioni;
        this.korisnici = korisnici;
        this.konfiguracija = konfiguracija;
        this.serverAviona = server;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        while (radi) {
            System.out.println(this.getName() + " run");
            try {
                String line = zaprimanjeKomande(socket);
                slanjeOdgovora(line,socket);
                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
                if (radi) {
                    synchronized (this) {
                        wait();
                    }
                }
            } catch (IOException | InterruptedException ex) {
                System.err.println(this.getName() + "prekid");
            }
        }
    }

    /**
     * Metoda šalje odgovor korisniku
     *
     * @param line odgovor za korisnika
     * @throws IOException exception koji se može pojaviti prilikom slanja poruke na socket
     */
    public void slanjeOdgovora(String line, Socket soket) throws IOException {
        OutputStream os = soket.getOutputStream();

        String odgvor = provijeriKomandu(line);
        os.write(odgvor.getBytes());
        os.flush();
    }

    /**
     * Metoda zaprima komandu od korisnika
     *
     * @return komandu primljenu od korisnika
     * @throws IOException exception koji se može pojaviti prilikom čitanja poruke sa socketa
     */
    public String zaprimanjeKomande(Socket soket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(soket.getInputStream(), "UTF-8"));
        String readLine = "";
        String line = "";
        while ((readLine = in.readLine()) != null) {
            line += readLine;
        }
        System.out.println(this.getName() + " primljeno: "
                + line);
        return line;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * Postavlja socket
     *
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Provjerava komandu i ako je dobra prosljeđuje naredbu metodi za odabir akcija
     *
     * @param komanda komanda zaprimljena od korisnika
     * @return odabrana akcija ili error
     */
    private String provijeriKomandu(String komanda) throws NumberFormatException, IOException {
        String poruka = "";
        String uzorak = "KORISNIK\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-_]{3,10});\\s+LOZINKA\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-#!]{3,10});\\s+"
                + "(KRAJ;|DODAJ\\s+([1-9]|1\\d|20);|UZLETIO\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,});\\s+POLAZIŠTE\\s"
                + "([A-ZČĆĐŠŽ]{3,});\\s+ODREDIŠTE\\s+([A-ZČĆĐŠŽ]{3,});\\s"
                + "TRAJANJE\\s+(\\d*);|ISPIS\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,});)$";
        Pattern p = Pattern.compile(uzorak);
        Matcher m = p.matcher(komanda);
        if (m.matches()) {
            String korisnik = m.group(1);
            String lozinka = m.group(2);
            if (provijeriKorisnika(korisnik, lozinka)) {
                return odaberiAkciju(m, poruka);
            } else {
                return "ERROR 3; Krivi korisnik ili lozinka.";
            }
        } else {
            return "ERROR 02; Naredba nije ispravna.";
        }
    }

    /**
     * Provijera postoji li korisnik i ako da valja li lozinka korisnika
     *
     * @param korisnik korisnik za provjeru
     * @param lozinka lozinka korisnika
     * @return postoji li korisnik
     */
    private boolean provijeriKorisnika(String korisnik, String lozinka) {
        for (Korisnik k : this.korisnici) {
            if (k.getKorime().equals(korisnik) && k.getLozinka().equals(lozinka)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Odabir i pozivanje metode za željenu akciju
     *
     * @param m Metcher za provjeru regex upitima i dohvaćanje grupa
     * @param poruka poruka proslijeđena od ranijih metoda
     * @return poruka poruka izvršene akcije
     * @throws NumberFormatException exception koji se može javiti prilikom parsiranja brojeva
     */
    public String odaberiAkciju(Matcher m, String poruka) throws NumberFormatException, IOException {
        String naredba = m.group(3);
        String komandaZaSim="";
        String parametri = "";
        if (naredba.startsWith("DODAJ")) {
            parametri = SplitajNaredbu(naredba);
            int brojDretvi = parseInt(parametri);
            serverAviona.maxBrojDretvi += brojDretvi;
            poruka = "OK";
        } else if (naredba.startsWith("UZLETIO")) {
            poruka = IzvrsiAkcijuZaAvion(naredba);
            // Ako se   od   servera   za   simulator   leta   dobije   odgovor   OK; korisniku se vraća odgovor OK;UDALJENOST 
        } else if (naredba.startsWith("ISPIS")) {
            parametri = SplitajNaredbu(naredba);
            if (postojiAvion(parametri)) {
                komandaZaSim="POZICIJA "+parametri+";";
                serverAviona.simulator=new Socket(serverAviona.server, serverAviona.portSimulatora);
                posaljiZahtjev(serverAviona.simulator,komandaZaSim);
                String a=zaprimanjeKomande(serverAviona.simulator);
                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
                if(a.startsWith("OK;")){poruka = "OK;";}
                else poruka="ERROR 16; Uneseni naziv aviona ne postoji!";
            } else {
                poruka = "ERROR 16; Uneseni naziv aviona ne postoji!";
            }
        } else if (naredba.equals("KRAJ;")) {
            zavrsiRad();
        } else {
            poruka = "ERROR 02; Naredba nije ispravna.";
        }
        return poruka;
    }

    /**
     * Splitanje komande na dva dijela tako da vrijednsot mozemo proslijediti metodi
     *
     * @param naredba komanda koju je korisnik unjeo da se izvrsi
     * @return vraca vrijednost naredbe/komande unesene od korisnika
     */
    private String SplitajNaredbu(String naredba) {
        String parametri;
        String[] nar = naredba.split(" ");
        parametri = nar[1];
        parametri = parametri.replace(";", "");
        return parametri;
    }

    /**
     * Završetak rada programa na zahtjev korisnika
     */
    private void zavrsiRad() {
        try {
            String odgvor = "OK;";
            OutputStream os = socket.getOutputStream();
            os.write(odgvor.getBytes());
            os.flush();
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(ZahtjevAviona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Provjera aerodroma u listi
     *
     * @param polazniAerodrom Icao polaznog aerodroma
     * @param odredisniAerodrom Icao odredisnog aerodroma
     * @return vraca TRUE ako postoje aerodromi inace false
     */
    private boolean postojeAerodromi(String polazniAerodrom, String odredisniAerodrom) {
        return aerodromi.stream()
                .filter(x -> (x.getIcao().equals(polazniAerodrom) || x.getIcao().equals(odredisniAerodrom)))
                .collect(Collectors.toSet())
                .size() == 2;
    }

    /**
     * Provjera aviona u listi
     *
     * @param avion naziv aviona
     * @return vraca TRUE ako postoji avion inace false
     */
    private boolean postojiAvion(String avion) {
        return avioni.stream()
                .filter(x -> (x.getNaziv().equals(avion)))
                .collect(Collectors.toSet())
                .size() == 1;
    }

    /**
     * Provjera Aviona i njegovog polaznog i odredisnog aerodroma
     *
     * @param polazniAerodrom Icao polaznog aerodroma
     * @param odredisniAerodrom Icao odredisnog aerodroma
     * @param avion naziv aviona
     * @return vraca TRUE ako postoje polazni i odredisni aerodrom i avion inace false
     */
    private boolean postojeAvionIAerodromi(String avion, String polazniAerodrom, String odredisniAerodrom) {
        return avioni.stream().anyMatch((a) -> (a.getNaziv().equals(avion)
                && a.getPolazniAerodrom().getIcao().equals(polazniAerodrom)
                && a.getOdredisniAerodrom().getIcao().equals(odredisniAerodrom)));
    }

    /**
     * Izvrasava akcije za uneseni avion te vraca poruku odnsno prosljedjuje dalje komandu
     *
     * @param parametri podaci od komande UZLETIO
     * @return poruku za avion
     */
    private String IzvrsiAkcijuZaAvion(String parametri) {
        String poruka = "";
        String[] atributi = new String[4];
        String[] atr = parametri.split(";");
        urediAtribute(atr, atributi);
        boolean imajuAerodromi = postojeAerodromi(atributi[1], atributi[2]);
        boolean imaAvion = postojiAvion(atributi[0]);
        boolean imaAvionIAerodrom = postojeAvionIAerodromi(atributi[0], atributi[1], atributi[2]);
        if (imajuAerodromi) {
            if (imaAvion) {
                if (imaAvionIAerodrom) {
                    poruka = "Salji komandu za LET";
                } else {
                    poruka = "ERROR 14; Avion postoji ali odrediste ili polaziste nije isto!";
                }
            } else {
                poruka = dodajNoviAvion(atributi);
            }
        } else {
            poruka = "ERROR 13; Aerodrom polazista ili odredista ne postoje!";
        }
        return poruka;
    }

    /**
     * Metoda uređuje atribute novog aviona
     *
     * @param atr polje s proslijeđenim atributima
     * @param atributi polje u koje se spremaju uređeni atributi
     */
    private void urediAtribute(String[] atr, String[] atributi) {
        for (int i = 0; i < 4; i++) {
            String[] pom = atr[i].trim().split(" ");
            atributi[i] = pom[1];
            if (pom.length > 2) {
                for (int j = 2; j < pom.length; j++) {
                    atributi[i] += " " + pom[j];
                }
            }
        }
    }

    /**
     * Dodavanje novog aviona
     *
     * @param parametri atributi aviona
     * @return poruka sa statusom
     */
    private String dodajNoviAvion(String[] atributi) {
        Avion avion = new Avion();
        Calendar trenutnoVrijeme = Calendar.getInstance();
        avion.setNaziv(atributi[0]);
        avion.setPolazniAerodrom(vratiAerodrom(atributi[1]));
        avion.setOdredisniAerodrom(vratiAerodrom(atributi[2]));
        avion.setVrijemePolijetanja(trenutnoVrijeme.getTime());
        Date vrijemeDolaska = izracunajVrijemeDolaska(trenutnoVrijeme, atributi[3]);
        avion.setVrijemeSlijetanja(vrijemeDolaska);
        avioni.add(avion);
        return "Salje se komanda let";
    }

    /**
     * trazi u listi i vraca aerodrom
     *
     * @param icao icao aerodroma
     * @return aerodrom vraca pronadjeni aerodrom
     */
    private Aerodrom vratiAerodrom(String icao) {
        Aerodrom aerodrom = null;
        for (Aerodrom a : aerodromi) {
            if (a.getIcao().equals(icao)) {
                aerodrom = a;
            }
        }
        return aerodrom;
    }

    /**
     * Racuna vrijeme sliejtanja aviona
     *
     * @param trenutnoVrijeme
     * @param sekunde korisnikov unos za trajanje leta
     * @return
     */
    public Date izracunajVrijemeDolaska(Calendar trenutnoVrijeme, String sekunde) {
        int sec = parseInt(sekunde);
        trenutnoVrijeme.add(Calendar.SECOND, sec);
        return trenutnoVrijeme.getTime();
    }
    
    /**
     * Metoda šalje zadani zahtjev na server
     *
     * @param socket na koji se šalje naredba
     * @param komanda naredba koja se šalje
     * @throws IOException exception koji se  može dogoditi prilikom slanja na socket
     */
    public static void posaljiZahtjev(Socket socket, String komanda) throws IOException {
        OutputStream os = socket.getOutputStream();

        os.write(komanda.getBytes());
        os.flush();
        socket.shutdownOutput();
    }
    

}
