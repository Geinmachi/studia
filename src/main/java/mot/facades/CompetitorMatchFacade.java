/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Account;
import entities.CompetitorMatch;
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
public class CompetitorMatchFacade extends AbstractFacade<CompetitorMatch> implements CompetitorMatchFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitorMatchFacade() {
        super(CompetitorMatch.class);
    }

    @Override
    public CompetitorMatch createWithReturn(CompetitorMatch entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public List<CompetitorMatch> getCompetitionCMGMappingsByCompetitionId(Integer idCompetition) {
        Query q = em.createNamedQuery("CompetitorMatch.findByCompetitionId");
        q.setParameter("idCompetition", idCompetition);
        return (List<CompetitorMatch>) q.getResultList();
    }

    @Override
    public List<CompetitorMatch> findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition) {
        Query q = em.createNamedQuery("CompetitorMatch.findByMatchNumberAndIdCompetition");
        q.setParameter("matchNumber", matchNumber);
        q.setParameter("idCompetition", idCompetition);
        return (List<CompetitorMatch>) q.getResultList();
    }

    @Override
    public List<CompetitorMatch> findCMGByIdMatch(Integer idMatch) {
        Query q = em.createNamedQuery("CompetitorMatch.findByIdMatch");
        q.setParameter("idMatch", idMatch);
        return (List<CompetitorMatch>) q.getResultList();
    }

    @Override
    public List<CompetitorMatch> findByCompetitionId(Integer idCompetition) {
        Query q = em.createNamedQuery("CompetitorMatch.findByCompetitionId");
        q.setParameter("idCompetition", idCompetition);
        
        return (List<CompetitorMatch>) q.getResultList();
    }

}
