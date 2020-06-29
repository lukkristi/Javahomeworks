/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.lukkristi.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.foi.nwtis.lukkristi.ejb.eb.Airports;
import org.foi.nwtis.lukkristi.ejb.eb.Airports_;

/**
 *
 * @author lukkristi
 */
@Stateless
public class AirportsFacade extends AbstractFacade<Airports> implements AirportsFacadeLocal {

    @PersistenceContext(unitName = "NWTiS_DZ3_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AirportsFacade() {
        super(Airports.class);
    }

    /**
     * metoda koja pretrazuje aerodrome po nazivu i koristi JPQL upit
     * @param naziv
     * @return vraca listu pronadjenih aerodroma
     */
    @Override
    public List<Airports> pretraziPoNazivuJPQL(String naziv) {
        String naziv2="%"+naziv+"%";
        return getEntityManager().createQuery("SELECT a FROM Airports a WHERE a.name LIKE :naziv ")
                .setParameter("naziv", naziv2)
                .getResultList();
    }

    /**
     * metoda koja pretrazuje aerodrome po nazivu i koristi CApi upit
     * @param naziv
     * @return vraca listi aerodroma koje je pronasao prema nazivu
     */
    @Override
    public List<Airports> pretraziPoNazivuCApi(String naziv) {
        String naziv2="%"+naziv+"%";
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Airports> aerodromi = cq.from(Airports.class);
        cq.select(aerodromi).where(cb.like(aerodromi.get(Airports_.name), naziv2));
        Query q = em.createQuery(cq);
        return q.getResultList();
    }
    
}
