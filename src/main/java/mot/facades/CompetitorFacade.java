/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Competitor;
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
public class CompetitorFacade extends AbstractFacade<Competitor> implements CompetitorFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitorFacade() {
        super(Competitor.class);
    }

    @Override
    public Competitor findAndInitializeGroups(Integer idCompetitor) {
        Competitor entity = em.find(Competitor.class, idCompetitor);
        entity.getGroupCompetitorList().size();
        
        return entity;
    }

    @Override
    public Competitor editWithReturn(Competitor competitor) {
        Competitor entity = em.merge(competitor);
        em.flush();
        
        return entity;
    }

    @Override
    public List<Competitor> findAllTeamless() {
        Query q = em.createNamedQuery("Competitor.findAllTeamless");
        
        return (List<Competitor>) q.getResultList();
    }
    
}
