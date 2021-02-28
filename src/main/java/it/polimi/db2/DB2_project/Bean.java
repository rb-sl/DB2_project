package it.polimi.db2.DB2_project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class Bean {
    @PersistenceContext
    private EntityManager em;

    public int getId(int n){
        Prova p = em.find(Prova.class, n);
        return p.getId();
    }
}
