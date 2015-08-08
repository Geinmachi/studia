/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Account;
import entities.CompetitorMatchGroup;
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
public class CompetitorMatchGroupFacade extends AbstractFacade<CompetitorMatchGroup> implements CompetitorMatchGroupFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitorMatchGroupFacade() {
        super(CompetitorMatchGroup.class);
    }

    @Override
    public CompetitorMatchGroup createWithReturn(CompetitorMatchGroup entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public List<CompetitorMatchGroup> getCompetitionCMGMappingsByCompetitionId(Integer idCompetition) {
        Query q = em.createNamedQuery("CompetitorMatchGroup.findByCompetitionId");
        q.setParameter("idCompetition", idCompetition);
        return (List<CompetitorMatchGroup>) q.getResultList();
    }

    @Override
    public List<CompetitorMatchGroup> findByMatchNumberAndIdCompetition(short matchNumber, int idCompetition) {
        Query q = em.createNamedQuery("CompetitorMatchGroup.findByMatchNumberAndIdCompetition");
        q.setParameter("matchNumber", matchNumber);
        q.setParameter("idCompetition", idCompetition);
        return (List<CompetitorMatchGroup>) q.getResultList();
    }

    @Override
    public List<CompetitorMatchGroup> findCMGByIdMatch(Integer idMatch) {
        Query q = em.createNamedQuery("CompetitorMatchGroup.findByIdMatch");
        q.setParameter("idMatch", idMatch);
        return (List<CompetitorMatchGroup>) q.getResultList();
    }

}
