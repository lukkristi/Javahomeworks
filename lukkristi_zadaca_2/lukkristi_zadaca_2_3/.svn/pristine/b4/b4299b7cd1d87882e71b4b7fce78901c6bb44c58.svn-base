/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:Zadaca2RestAerodromi [aerodromi]<br>
 * USAGE:
 * <pre>
 *        Zadaca2_2RS client = new Zadaca2_2RS();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author lukkristi
 */
public class Zadaca2_2RS {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/lukkristi_zadaca_2_2/webresources";
    private String korisnik="";
    private String lozinka="";
    private String icao="";
    private String drzava="";
    private String naziv="";

    /**
     *
     */
    public Zadaca2_2RS() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
    }
    
    /**
     * konstruktor sa korime i lozinkom 
     * @param korisnik
     * @param lozinka
     */
    public Zadaca2_2RS(String korisnik, String lozinka) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
        this.korisnik=korisnik;
        this.lozinka=lozinka;
    }
    
    /**
     * konstruktor sa korime , lozinkom i icao 
     * @param korisnik
     * @param lozinka
     * @param icao
     */
    public Zadaca2_2RS(String korisnik, String lozinka, String icao) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
        this.korisnik=korisnik;
        this.lozinka=lozinka;
        this.icao=icao;
    }

    /**
     *
     * @param requestEntity
     * @throws ClientErrorException
     */
    public void putJson(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * metoda koja dohvaca odgvor nakon slanja zahtjeva za dodavanje  aerodroma korisniku
     * @param requestEntity
     * @return
     * @throws ClientErrorException
     */
    public Response dodajMojAerodom(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .header("icao", icao)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    /**
     * metoda koja dohvaca odgvor nakon slanja zahtjeva za dohavacanje korisnikovog aerodroma
     * @param <T>
     * @param responseType
     * @param icao
     * @return
     * @throws ClientErrorException
     */
    public <T> T dajAerodomKorisnika(Class<T> responseType, String icao) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{icao}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .get(responseType);
    }

    /**
     * metoda koja dohvaca odgvor nakon slanja zahtjeva za aerodrome s unesenom drzavom ili nazivom
     * @param <T>
     * @param responseType
     * @param drzava
     * @param naziv
     * @return
     * @throws ClientErrorException
     */
    public <T> T dajAerodomeDrzavaNaziv(Class<T> responseType, String drzava, String naziv) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (drzava != null && !drzava.isEmpty()) {
            resource = resource.queryParam("drzava", drzava);
        }
        if (naziv != null &&!naziv.isEmpty()) {
            resource = resource.queryParam("naziv", naziv);
        }
        resource = resource.path("svi");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .get(responseType);
    }

    /**
     *
     * @param requestEntity
     * @throws ClientErrorException
     */
    public void deleteJson(Object requestEntity) throws ClientErrorException {
        
    }

    /**
     * metoda koja dohvaca odgvor nako slanja zahtjeva za korisnikove aerodrome
     * @param <T>
     * @param responseType
     * @return
     * @throws ClientErrorException
     */
    public <T> T dajAerodomeKorisnika(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)               
                .header("korisnik", korisnik)                 
                .header("lozinka", lozinka)
                .get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
