package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Badwords;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Stateless
public class BadwordBean {
    @PersistenceContext
    private EntityManager em;

    public List<Badwords> getBadwords() {
        return em.createNamedQuery("Badwords.findAll", Badwords.class).getResultList();
    }

    public Boolean checkForBadwords(Map<Integer, String> answ) {
        List<Badwords> badwords = getBadwords();

        Boolean hasBadword = false;

        if (badwords != null && !badwords.isEmpty()) {
            Collection<String> answers = answ.values();
            Iterator<String> i = answers.iterator();
            String cur_answ;

            while(!hasBadword && i.hasNext()) {
                cur_answ = i.next();
                for (Badwords bad : badwords) {
                    hasBadword = hasBadword || cur_answ.contains(bad.getWord());
                }
            }
        }

        return hasBadword;
    }
}
