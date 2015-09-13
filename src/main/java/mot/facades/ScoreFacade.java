/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Score;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
public class ScoreFacade extends AbstractFacade<Score> implements ScoreFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScoreFacade() {
        super(Score.class);
    }

    @Override
    public Score findByIdCompetitorAndIdCompetition(Integer idCompetition, Integer idCompetitor) {
        Query q = em.createNamedQuery("Score.findByIdCompetitionAndIdCompetitor");
        q.setParameter("idCompetition", idCompetition);
        q.setParameter("idCompetitor", idCompetitor);
        
        return (Score)q.getSingleResult();
    }

    @Override
    public List<Score> findScoreByIdCompetition(int idCompetition) {
        Query q = em.createNamedQuery("Score.findByIdCompetition");
        q.setParameter("idCompetition", idCompetition);
        
        return (List<Score>)q.getResultList();
    }
    
}
