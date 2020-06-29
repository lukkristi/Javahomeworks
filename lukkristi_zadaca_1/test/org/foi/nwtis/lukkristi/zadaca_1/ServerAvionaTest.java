/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.zadaca_1;

import java.net.Socket;
import java.util.List;
import org.foi.nwtis.lukkristi.konfiguracije.Konfiguracija;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author lukkristi
 */
public class ServerAvionaTest {
    
    public ServerAvionaTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of main method, of class ServerAviona.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        ServerAviona.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pripremiParametre method, of class ServerAviona.
     */
    @Test
    public void testPripremiParametre() {
        System.out.println("pripremiParametre");
        String[] args = null;
        ServerAviona instance = new ServerAviona();
        String expResult = "";
        String result = instance.pripremiParametre(args);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provijeriParametre method, of class ServerAviona.
     */
    @Test
    public void testProvijeriParametre() {
        System.out.println("provijeriParametre");
        String parametri = "";
        ServerAviona instance = new ServerAviona();
        boolean expResult = false;
        boolean result = instance.provijeriParametre(parametri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ucitajKonfiguraciju method, of class ServerAviona.
     */
    @Test
    public void testUcitajKonfiguraciju() throws Exception {
        System.out.println("ucitajKonfiguraciju");
        String nazivDatoteke = "";
        ServerAviona instance = new ServerAviona();
        Konfiguracija expResult = null;
        Konfiguracija result = instance.ucitajKonfiguraciju(nazivDatoteke);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pokreniServisneDretve method, of class ServerAviona.
     */
    @Test
    public void testPokreniServisneDretve() {
        System.out.println("pokreniServisneDretve");
        Konfiguracija konfiguracija = null;
        ServerAviona instance = new ServerAviona();
        instance.pokreniServisneDretve(konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of kreirajKorisnickuDretvu method, of class ServerAviona.
     */
    @Test
    public void testKreirajKorisnickuDretvu() {
        System.out.println("kreirajKorisnickuDretvu");
        Konfiguracija konfiguracija = null;
        ServerAviona instance = new ServerAviona();
        instance.kreirajKorisnickuDretvu(konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of PostojiSlobodnaDretva method, of class ServerAviona.
     */
    @Test
    public void testPostojiSlobodnaDretva() {
        System.out.println("PostojiSlobodnaDretva");
        ServerAviona instance = new ServerAviona();
        boolean expResult = false;
        boolean result = instance.PostojiSlobodnaDretva();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dajSlobdonuDretvu method, of class ServerAviona.
     */
    @Test
    public void testDajSlobdonuDretvu() {
        System.out.println("dajSlobdonuDretvu");
        ServerAviona instance = new ServerAviona();
        ZahtjevAviona expResult = null;
        ZahtjevAviona result = instance.dajSlobdonuDretvu();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pokreniServer method, of class ServerAviona.
     */
    @Test
    public void testPokreniServer() {
        System.out.println("pokreniServer");
        Konfiguracija konfiguracija = null;
        int paramBrojDretvi = 0;
        ServerAviona instance = new ServerAviona();
        instance.pokreniServer(konfiguracija, paramBrojDretvi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of vratiPorukuOGresciKorisniku method, of class ServerAviona.
     */
    @Test
    public void testVratiPorukuOGresciKorisniku() throws Exception {
        System.out.println("vratiPorukuOGresciKorisniku");
        Socket socket = null;
        ServerAviona instance = new ServerAviona();
        instance.vratiPorukuOGresciKorisniku(socket);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ucitajAvione method, of class ServerAviona.
     */
    @Test
    public void testUcitajAvione() {
        System.out.println("ucitajAvione");
        Konfiguracija konfiguracija = null;
        ServerAviona instance = new ServerAviona();
        instance.ucitajAvione(konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ucitajKorisnike method, of class ServerAviona.
     */
    @Test
    public void testUcitajKorisnike() throws Exception {
        System.out.println("ucitajKorisnike");
        Konfiguracija konfiguracija = null;
        ServerAviona instance = new ServerAviona();
        instance.ucitajKorisnike(konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ucitajAerodrome method, of class ServerAviona.
     */
    @Test
    public void testUcitajAerodrome() throws Exception {
        System.out.println("ucitajAerodrome");
        Konfiguracija konfiguracija = null;
        ServerAviona instance = new ServerAviona();
        instance.ucitajAerodrome(konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of krajPrograma method, of class ServerAviona.
     */
    @Test
    public void testKrajPrograma() {
        System.out.println("krajPrograma");
        ServerAviona serverAviona = null;
        ServerAviona instance = new ServerAviona();
        instance.krajPrograma(serverAviona);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pripremiDretvu method, of class ServerAviona.
     */
    @Test
    public void testPripremiDretvu() {
        System.out.println("pripremiDretvu");
        int brojDretvi = 0;
        Konfiguracija konfiguracija = null;
        ServerAviona instance = new ServerAviona();
        instance.pripremiDretvu(brojDretvi, konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of UrediPodatkeAerodroma method, of class ServerAviona.
     */
    @Test
    public void testUrediPodatkeAerodroma() {
        System.out.println("UrediPodatkeAerodroma");
        String aerodrom = "";
        ServerAviona instance = new ServerAviona();
        String[] expResult = null;
        String[] result = instance.UrediPodatkeAerodroma(aerodrom);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ispisiAvione method, of class ServerAviona.
     */
    @Test
    public void testIspisiAvione() throws Exception {
        System.out.println("ispisiAvione");
        List<Avion> avioni = null;
        ServerAviona instance = new ServerAviona();
        instance.ispisiAvione(avioni);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
