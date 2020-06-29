/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.rest.serveri;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.lukkristi.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.lukkristi.ws.serveri.Aerodrom;
import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.podaci.OdgovorAerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * REST Web Service
 *
 * @author lukkristi
 */
@Path("aerodromi")
public class Zadaca2RestAerodromi {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Zadaca2RestAerodromi
     */
    public Zadaca2RestAerodromi() {
    }

    /**
     * Metoda koja dobiva odgvor od servisa za dohvacanje korisnikovih aerodroma
     * @param korisnik
     * @param lozinka
     * @return
     */
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomeKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {
        List<Aerodrom> aerodromi;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        aerodromi = zadaca2_1WS.dajAerodomeKorisnika(korisnik, lozinka);
        Odgovor odgovor = new Odgovor();
        if (aerodromi != null && !aerodromi.isEmpty()) {
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(aerodromi.toArray());
        } else {
            aerodromi = new ArrayList<>();
            odgovor.setStatus("40");
            odgovor.setPoruka("Korisnik, nema vlastitih aerodroma, ili su pogresni podaci korisnika");
            odgovor.setOdgovor(aerodromi.toArray());
        }

        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    /**
     * Metoda koja dobiva odgvor od servisa za dodavanje  aerodroma korisniku
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajMojAerodom(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @HeaderParam("icao") String icao) {
        boolean dodao;
        Object[] lista = new Object[1];
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        dodao = zadaca2_1WS.dodajMojAerodrom(korisnik, lozinka, icao);
        Odgovor odgovor = new Odgovor();
        if (dodao) {
            lista[0] = dodao;
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(lista);
        } else {
            odgovor.setStatus("40");
            odgovor.setPoruka("Pogresan ICAO ili nije prosla autetifikacija");
            odgovor.setOdgovor(lista);
        }

        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    /**
     * Metoda koja dobiva odgvor od servisa za dohvacanje  aerodroma prema nazivu i drzavi
     * @param korisnik
     * @param lozinka
     * @param naziv
     * @param drzava
     * @return
     */
    @Path("/svi")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomeDrzavaNaziv(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @QueryParam("naziv") String naziv,
            @QueryParam("drzava") String drzava) {
        List<Aerodrom> aerodromi;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        Odgovor odgovor = new Odgovor();
        if (naziv == null && drzava == null) {
            aerodromi = zadaca2_1WS.dajAerodomeNaziv(korisnik, lozinka, naziv);
            if (aerodromi == null || aerodromi.isEmpty()) {
                aerodromi = new ArrayList<>();
                odgovor.setStatus("40");
                odgovor.setPoruka("Korisnik je pogresan ili ne postoji pretraga sa nazivom " + naziv);
                odgovor.setOdgovor(aerodromi.toArray());
            }
        } else if (naziv == null && !drzava.isEmpty()) {
            aerodromi = zadaca2_1WS.dajAerodomeDrzava(korisnik, lozinka, drzava);
            if (aerodromi == null || aerodromi.isEmpty()) {
                aerodromi = new ArrayList<>();
                odgovor.setStatus("40");
                odgovor.setPoruka("Korisnik je pogresan ili ne postoji pretraga sa drzavom " + drzava);
                odgovor.setOdgovor(aerodromi.toArray());
            }
        } else if (drzava == null && !naziv.isEmpty()) {
            aerodromi = zadaca2_1WS.dajAerodomeNaziv(korisnik, lozinka, naziv);
            if (aerodromi == null || aerodromi.isEmpty()) {
                aerodromi = new ArrayList<>();
                odgovor.setStatus("40");
                odgovor.setPoruka("Korisnik je pogresan ili ne postoji pretraga sa drzavom " + drzava);
                odgovor.setOdgovor(aerodromi.toArray());
            }
        } else {
            aerodromi = new ArrayList<>();
            aerodromi.addAll(zadaca2_1WS.dajAerodomeDrzava(korisnik, lozinka, drzava));
            aerodromi.addAll(zadaca2_1WS.dajAerodomeNaziv(korisnik, lozinka, naziv));
            if (aerodromi == null || aerodromi.isEmpty()) {
                aerodromi = new ArrayList<>();
                odgovor.setStatus("40");
                odgovor.setPoruka("Korisnik je pogresan ili lozinka ");
                odgovor.setOdgovor(aerodromi.toArray());
            }
        }
        if (aerodromi != null && !aerodromi.isEmpty()) {
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(aerodromi.toArray());
        }

        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    /**
     * Metoda koja dobiva odgvor od servisa za dohvacanje korisnikovog aerodroma
     * @param korisnik
     * @param lozinka
     * @param icao
     * @return
     */
    @GET
    @Path("{icao}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @PathParam("icao") String icao) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        Aerodrom aerodrom;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        aerodrom = zadaca2_1WS.dajAerodomKorisnika(korisnik, lozinka, icao);
        Odgovor odgovor = new Odgovor();
        if (aerodrom != null) {
            aerodromi.add(aerodrom);
            odgovor.setStatus("10");
            odgovor.setPoruka("OK");
            odgovor.setOdgovor(aerodromi.toArray());
        } else {
            odgovor.setStatus("40");
            odgovor.setPoruka("ne postoji aerodrom " + icao+ " u vasoj kolekciji");
            odgovor.setOdgovor(aerodromi.toArray());
        }

        return Response
                .ok(odgovor)
                .status(200)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of Zadaca2RestAerodromi
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        Odgovor odgovor = new Odgovor();
        odgovor.setStatus("40");
        odgovor.setPoruka("POGRESKA, PUT metoda se ne koristi!");
        odgovor.setOdgovor(aerodromi.toArray());
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteJson(Response content){
    
        List<Aerodrom> aerodromi = new ArrayList<>();
        Odgovor odgovor = new Odgovor();
        odgovor.setStatus("40");
        odgovor.setPoruka("POGRESKA, DELETE metoda se ne koristi!");
        odgovor.setOdgovor(aerodromi.toArray());
    }

}
