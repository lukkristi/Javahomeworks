
package org.foi.nwtis.lukkristi.zadaca_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lukkristi
 */
public class KorisnikAviona {
    
    private int port;
    private String server;
    private boolean zahtjevAerodroma = false;
    private String datoteka;
    Matcher m;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KorisnikAviona ka = new KorisnikAviona();
        String komanda = ka.provijeriKomandu(args);
        if (!komanda.startsWith("ERROR")) {
            komanda = ka.pripremiParametre(args);           
        }
        if (komanda.startsWith("ERROR")) {
            System.err.println(komanda);
            System.exit(0);
        }
        try {
            Socket socket = new Socket(ka.server, ka.port);
            posaljiZahtjev(socket, komanda);
            zaprimiOdgovor(socket, ka);
            socket.shutdownInput();
            socket.close();
        } catch (IOException ex) {
            System.err.println("Greška prilikom spajanja na server.");
        }
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
    
    /**
     * Metoda zaprima odgovor od servera
     *
     * @param socket s kojeg s prima odgovor
     * @param ka pokazivač klase
     * @throws IOException exception koji se može pojaviti prilikom primanja poruke sa socketa
     */
    public static void zaprimiOdgovor(Socket socket, KorisnikAviona ka) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String readLine = "";
        String line = "";

        while ((readLine = in.readLine()) != null) {
            line += readLine + System.lineSeparator();
        }
        if (line.startsWith("ERROR")) {
            System.err.println("primljeno: "
                    + line);
        } else {           
            System.out.println("primljeno: "
                    + line);
        }
    }
    
    
    /**
     * Provjerava komandu i ako je dobra vraća status "OK", a ako nije vraća "ERROR 02" i tekst greške
     *
     * @param args argumenti komande zaprimljni preko komandne linije
     * @return status valjanosti komande
     */
    public String provijeriKomandu(String[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {            
            sb.append(args[i]).append(" ");            
        }
        String komanda = sb.toString().trim();
        String poruka = "";
        String uzorak = "-k\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-_]{3,10})\\s+-l\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-_#!]{3,10})\\s+-s\\s+"
                + "(^(([1-9][0-9]{0,2}|0)\\\\.){3}([1-9][0-9]{0,2}|0)$|^(\\\\w+\\\\.\\\\w+)+$|localhost)\\s+-p\\s+([9][0-9]{3})"
                + "\\s+(--kraj|--dodaj\\s+([1-9]|1\\d|20)|--uzletio\\s+(AerodromPolazište:\\s+([A-ZČĆĐŠŽ]{3,}),"
                + "\\s+AerodromOdredište:\\s+([A-ZČĆĐŠŽ]{3,}),\\s+Avion:\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,}),"
                + "\\s+trajanjeLeta:\\s+(\\d*))|--ispis\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,}))$";
        Pattern p = Pattern.compile(uzorak);
        m = p.matcher(komanda);
        if (m.matches()) {
            return "OK";
        } else {
            return "ERROR 02; Naredba nije ispravna.";
        }
    }
    
    /**
     * Priprema i provijera parametra
     *
     * @param args argumenti naredbe
     * @return pripremljna naredba ili error
     */
    private String pripremiParametre(String[] args) {
        StringBuffer sb = new StringBuffer();
        String parametri;
        if (provijeriParametarZaServer(m.group(3))) {
            server = m.group(3);
        } else {
            return "ERROR; Nije dobra adresa servera.";
        }
        if (provijeriParametarZaPort(m.group(8))) {
            port = parseInt(m.group(8));
        } else {
            return "ERROR; Nije dobar port.";
        }       
        String naredba = odaberiNaredbu(args);
        return naredba;
    }
    
    /**
     * SVE NAREDBE MI SE NALAZE U GRUPI 9 ali sa ostalim vrijednostima
     * Odabire akciju i kreira naredbu za nju
     * @param args argumenti naredbe
     * @return kreirana naredba
     */
    private String odaberiNaredbu(String[] args) {
        String naredba = "KORISNIK " + m.group(1) + "; LOZINKA " + m.group(2) + "; ";
        if (m.group(9).startsWith("--kraj")) {
            naredba += "KRAJ;";
        }
        else if (m.group(9).startsWith("--dodaj")) {
            naredba += "DODAJ " + m.group(10) + ";";
        }else if (m.group(9).startsWith("--uzletio")) {
            String atributi= urediParametreZaNoviAavion(m.group(11),m.group(14),m.group(12),m.group(13),m.group(15));
            naredba += atributi;
        }else if (m.group(9).startsWith("--ispis")) {
            naredba += "ISPIS " + m.group(16) + ";";
        }
        return naredba;
    }
    
    /**
     * Metoda provjerava je li dobar parametar za port
     *
     * @param port vrijednost za port unešena u komandi
     * @return true ako je ispravan, false ako nije
     */
    private boolean provijeriParametarZaPort(String port) {
        String uzorak = "^[9][0-9]{3}$";
        Pattern p = Pattern.compile(uzorak);
        Matcher m = p.matcher(port);
        if (m.matches()) {
            return true;
        }
        return false;
    }
    
     /**
     * Metoda provijerava je li dobar parametar za server
     *
     * @param server naziv ili ip adresa servera
     * @return true ako je ispravan, false ako nije
     */
    private boolean provijeriParametarZaServer(String server) {
        String uzorak = "^(([1-9][0-9]{0,2}|0)\\.){3}([1-9][0-9]{0,2}|0)$|^(\\w+\\.\\w+)+$|localhost";
        Pattern p = Pattern.compile(uzorak);
        Matcher m = p.matcher(server);
        if (m.matches()) {
            return true;
        }
        return false;
    }
    
    /**
     * Provjera parametra za novi avion
     *
     * @param parametri atributi aviona
     * @return true ako je ispravan, false ako nije
     */
    private boolean provijeriParametreNovogAviona(String parametri) {
        String uzorak = "(AerodromPolazište:\\s+([A-ZČĆĐŠŽ]{3,}),\\s+AerodromOdredište:\\s+([A-ZČĆĐŠŽ]{3,}),"
                + "\\s+Avion:\\s+([A-ZČĆĐŠŽa-zčćđšž0-9-]{3,}),\\s+trajanjeLeta:\\s+(\\d*))";
        Pattern p = Pattern.compile(uzorak);
        Matcher m = p.matcher(parametri);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * uređuje parametre za naredbu za dodavanje novog aerodroma
     * @param parametri novog aviona
     * @return uređeni parametar
     */
    private String urediParametreZaNoviAavion(String parametri,String avion, String icaP, String icaO, String trajanje) {
        if (!provijeriParametreNovogAviona(parametri)) {
            return "ERROR 02; Naredba nije ispravna.";
        }
        parametri = "UZLETIO " + avion + "; POLAZIŠTE " + icaP + "; ODREDIŠTE " + icaO + "; TRAJANJE " + trajanje + ";";

        return parametri;
    }
    
}
