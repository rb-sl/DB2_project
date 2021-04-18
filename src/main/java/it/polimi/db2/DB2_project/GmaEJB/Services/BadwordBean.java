package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Badwords;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BadwordBean {
    @PersistenceContext
    private EntityManager em;

    public List<Badwords> getBadwords() {
        return em.createNamedQuery("Badwords.findAll", Badwords.class).getResultList();
    }
}
