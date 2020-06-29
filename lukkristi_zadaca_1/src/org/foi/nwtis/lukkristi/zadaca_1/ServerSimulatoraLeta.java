
package org.foi.nwtis.lukkristi.zadaca_1;

import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.lukkristi.konfiguracije.Konfiguracija;
import org.foi.nwtis.lukkristi.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.lukkristi.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.lukkristi.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author lukkristi
 */
public class ServerSimulatoraLeta {

    private List<Avion> letovi = null;
    boolean radi = true;
    private List<ZahtjevLeta> dretveZahtjevaleta = null;
    private ThreadGroup tgKorisnickeDretve;
    
    
    public ServerSimulatoraLeta() {
        dretveZahtjevaleta = Collections.synchronizedList(
                new ArrayList<>()
        );
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerSimulatoraLeta sal = new ServerSimulatoraLeta();
        String parametar = sal.pripremiParametre(args);       
        String[] parametri = parametar.split(" –-");
        
       
        boolean status = sal.provijeriParametre(parametar);
        if (!status) {
            System.err.println("Pogrešan unos parametra!");
            return;
        }
        String nazivDatoteke = parametri[0];
        Konfiguracija konfiguracija;
        try {
            konfiguracija = sal.ucitajKonfiguraciju(nazivDatoteke);
        } catch (Exception ex) {
            System.err.println("Problem s učitavanjem konfiguracije!");
            return;
        }
        sal.PripremiDretvu();
        sal.pokreniServer(konfiguracija);       
    }
    
    /**
     * Metoda uzima argumente i priprema ih za uporabu
     *
     * @param args argumenti s komandne linije
     * @return obrađeni parametri
     */
    public String pripremiParametre(String[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String parametri = sb.toString().trim();
        return parametri;
    }
    
    /**
     * Metoda provjerava jesu li valjani parametri (datoteka)
     *
     * @param parametri koji se provjeravaju (naziv datoteke)
     * @return status ispravnosti
     */
    public boolean provijeriParametre(String parametri) {
        String sintaksa = "^([^\\s]+)\\.(txt|xml|json|bin)$";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(parametri);
        boolean status = m.matches();
        return status;
    }
    
    /**
     * Metoda učitava konfiguraciju iz konfiguracijske datoteke
     *
     * @param nazivDatoteke naziv datoteke konfiguracije
     * @return konfiguracija programa
     * @throws Exception exception koji se može pojaviti prilikom čitanja iz datoteke
     */
    public Konfiguracija ucitajKonfiguraciju(String nazivDatoteke) throws Exception {
        try {
            Konfiguracija konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
            return konfiguracija;
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {

            throw new Exception(ex.getMessage());
        }
    }
    
    /**
     * Metoda otvara socket te osluškuje zahtjeve i dodjeljuje dretvu zahtjeva zahtjevu.Ukoliko nema slobodnih dretvi šalje       
     * korisniku poruku o grešci
     * @param konfiguracija konfiguracija programa
     */
    public void pokreniServer(Konfiguracija konfiguracija) {
        int port = parseInt(konfiguracija.dajPostavku("port.simulator"));
        int brojCekaca = parseInt(konfiguracija.dajPostavku("maks.cekaca"));
        try {
            ServerSocket ss = new ServerSocket(port, brojCekaca);
            while (radi) {
                Socket socket = ss.accept();
                ZahtjevLeta zahtjevaleta = dajSlobdonuDretvu();
                zahtjevaleta.setSocket(socket);
                if (zahtjevaleta.getState() == Thread.State.WAITING) {
                    synchronized (zahtjevaleta) {
                        zahtjevaleta.notify();
                    }
                } else {
                    zahtjevaleta.start();
                }
            }
            System.out.println("Kraj Servera!!!");
        } catch (IOException ex) {
            System.err.println("Ne može se otvoriti socket na zadanom portu!");}}
    
   /**
     * Kreira dretvu ZatjevAviona i sprema ju u listu
     * @return vraca dretvuleta
     */
    public void PripremiDretvu() {
        this.tgKorisnickeDretve = new ThreadGroup("lukkristi_KDS");
        ZahtjevLeta zl = new ZahtjevLeta(
                this.letovi,
                this.tgKorisnickeDretve,
                this.tgKorisnickeDretve.getName() + "_" + "1"                              
        );
        this.dretveZahtjevaleta.add(zl);
        
    }
    
    /**
     * Dohvaća prvu slobodnu dretvu, ukoliko nema slobodni dretvi vraća null
     *
     * @return slobodna dretva
     */
    public ZahtjevLeta dajSlobdonuDretvu() {
        for (ZahtjevLeta zl : dretveZahtjevaleta) {
            if (zl.getState() == Thread.State.NEW || zl.getState() == Thread.State.WAITING) {
                return zl;
            }
        }
        return null;
    }
}
