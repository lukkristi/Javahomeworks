/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.zadaca_1;

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
public class ServerSimulatoraLetaTest {
    
    public ServerSimulatoraLetaTest() {
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
     * Test of main method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ServerSimulatoraLeta.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pripremiParametre method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testPripremiParametre() {
        System.out.println("pripremiParametre");
        String[] args = null;
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        String expResult = "";
        String result = instance.pripremiParametre(args);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provijeriParametre method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testProvijeriParametre() {
        System.out.println("provijeriParametre");
        String parametri = "";
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        boolean expResult = false;
        boolean result = instance.provijeriParametre(parametri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ucitajKonfiguraciju method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testUcitajKonfiguraciju() throws Exception {
        System.out.println("ucitajKonfiguraciju");
        String nazivDatoteke = "";
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        Konfiguracija expResult = null;
        Konfiguracija result = instance.ucitajKonfiguraciju(nazivDatoteke);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pokreniServer method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testPokreniServer() {
        System.out.println("pokreniServer");
        Konfiguracija konfiguracija = null;
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        instance.pokreniServer(konfiguracija);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of PripremiDretvu method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testPripremiDretvu() {
        System.out.println("PripremiDretvu");
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        instance.PripremiDretvu();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dajSlobdonuDretvu method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testDajSlobdonuDretvu() {
        System.out.println("dajSlobdonuDretvu");
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        ZahtjevLeta expResult = null;
        ZahtjevLeta result = instance.dajSlobdonuDretvu();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
