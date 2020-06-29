
package org.foi.nwtis.lukkristi.zadaca_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author lukkristi
 */
public class ZahtjevLeta extends Thread{
    
    private Socket socket;
    boolean radi = true;
    List<Avion> letovi;

    public ZahtjevLeta(List<Avion> letovi, ThreadGroup group, String name) {
        super(group, name);
        this.letovi = letovi;
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
                String line = zaprimanjeKomande();
                slanjeOdgovora(line);
                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
                if (radi) {
                    synchronized (this) {
                        wait();
                    }
                }
            } catch (IOException | InterruptedException | NumberFormatException | ParseException ex) {
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
    public void slanjeOdgovora(String line) throws IOException, NumberFormatException, ParseException {
        OutputStream os = socket.getOutputStream();

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
    public String zaprimanjeKomande() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
    private String provijeriKomandu(String komanda) throws NumberFormatException, ParseException {
        String poruka = "";
        String uzorak = "^(LET\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,});\\s+POLIJETANJE\\s+([0-9]{4}.(0[1-9]|1[0-2]).(0[1-9]|"
                + "[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]);\\s+SLIJETANJE\\s+([0-9]"
                + "{4}.(0[1-9]|1[0-2]).(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:"
                + "[0-5][0-9]);|POZICIJA\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,});)$";
        Pattern p = Pattern.compile(uzorak);
        Matcher m = p.matcher(komanda);
        if (m.matches()) {
            String korisnik = m.group(1);
            String lozinka = m.group(2);
            return odaberiAkciju(m, poruka);
        } else {
            return "ERROR 20; Naredba nije ispravna.";
        }
    }
    
    /**
     * Odabir i pozivanje  metode za željenu akciju
     *
     * @param m Metcher za provjeru regex upitima i dohvaćanje grupa
     * @param poruka poruka proslijeđena od ranijih metoda
     * @return poruka poruka izvršene akcije
     * @throws NumberFormatException exception koji se može javiti prilikom parsiranja brojeva
     */
    public String odaberiAkciju(Matcher m, String poruka) throws NumberFormatException, ParseException {
        String naredba = m.group(1);
        String parametri = "";
         if (naredba.startsWith("LET")) {             
            poruka = IzvrsiAkcijuZaAvion(naredba);
        } else if (naredba.startsWith("POZICIJA")) {
            parametri = SplitajNaredbu(naredba);
            if (postojiAvion(parametri)) {
                if(avionSletio(parametri)){                      
                    poruka = "OK; SLETIO;";
                }else{
                    poruka="OK; LETI;";
                }                               
            } else {
                poruka = "ERROR 23; Avion ne postoji u listi";}
        }
        else{
            poruka = "ERROR 20; Naredba nije ispravna.";
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
     * Izvrasava akcije za uneseni avion te vraca poruku odnosno prosljedjuje dalje komandu
     *
     * @param parametri podaci od komande LET
     * @return poruku za avion
     */
    private String IzvrsiAkcijuZaAvion(String parametri) throws ParseException {
        String poruka = "";
        String[] atributi = new String[3];
        String[] atr = parametri.split(";");
        urediAtribute(atr, atributi);       
        boolean imaAvion = postojiAvion(atributi[0]);
        if (imaAvion) {
            if(avionSletio(atributi[0])){
                obrisiLet(atributi[0]);
                poruka="OK;";
            }
            else
                poruka="ERROR 21; avion jos leti!";
        } else {
            poruka = dodajNoviLet(atributi);
        }       
        return poruka;
    }
        
     /**
     * Metoda uređuje atribute novog leta
     *
     * @param atr polje s proslijeđenim atributima
     * @param atributi polje u koje se spremaju uređeni atributi
     */
    private void urediAtribute(String[] atr, String[] atributi) {
        for (int i = 0; i < 3; i++) {
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
     * Provjera aviona u listi letova
     *
     * @param avion naziv aviona
     * @return vraca TRUE ako postoji avion inace false
     */
    private boolean postojiAvion(String avion) {
        return letovi.stream()
                .filter(x -> (x.getNaziv().equals(avion)))
                .collect(Collectors.toSet())
                .size() == 1;
    }
    
    /**
     * Dodavanje novog leta
     *
     * @param parametri atributi leta
     * @return poruka sa statusom
     */
    private String dodajNoviLet(String[] atributi) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");       
        Avion avion = new Avion();        
        avion.setNaziv(atributi[0]); 
        avion.setVrijemePolijetanja(df.parse(atributi[1]));
        avion.setVrijemeSlijetanja(df.parse(atributi[2]));
        letovi.add(avion);
        return "OK";
    }
    
    /**
     * Provjerava letove aviona ako su sletili
     *
     * @param atributi atributi komande za let
     * @return istina ako je avion sletio a suprotno laz
     */
    private boolean avionSletio(String atributi){
        Avion let= vratiLet(atributi);
        Calendar cal = Calendar.getInstance();
        if (cal.getTime().after(let.getVrijemeSlijetanja())) {
            return true;
        }
        else
            return false;
    }

    /**
     * Vracanje novog leta
     *
     * @param nazivAviona naziv aviona
     * @return avion
     */
    private Avion vratiLet(String nazivAviona) {
        Avion avion=null;
        for(Avion a:letovi){
            if (a.getNaziv().equals(nazivAviona)) {
                avion= a;
            }
        }
        return avion;
    }

    /**
     * brisanje aviona koji je sletio iz liste
     *
     * @param nazivAviona naziv aviona
     * 
     */
    private void obrisiLet(String nazivAviona) {
        for(Avion a:letovi){
            if (a.getNaziv().equals(nazivAviona)) {
                letovi.remove(a);
            }
        }
    }
}
