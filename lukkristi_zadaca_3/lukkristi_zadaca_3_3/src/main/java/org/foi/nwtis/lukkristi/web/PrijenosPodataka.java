/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author lukkristi
 */
@ServerEndpoint("/prijenosPodataka")
public class PrijenosPodataka {
    static List<Session> sjednice = new ArrayList<>();

    @OnMessage
    public void onMessage(String message) {
        for (Session s : sjednice) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    Logger.getLogger(PrijenosPodataka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        sjednice.add(session);
        System.out.println("WS open: " + session.getId());
    }

    @OnClose
    public void onClose(Session session, EndpointConfig conf) {
        System.out.println("WS close: " + session.getId());
        sjednice.remove(session);
    }

    public static void saljiPoruku(String poruka) {
        for (Session s : sjednice) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(poruka);
                } catch (IOException ex) {
                    Logger.getLogger(PrijenosPodataka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
