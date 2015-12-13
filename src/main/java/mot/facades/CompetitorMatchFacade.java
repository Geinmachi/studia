/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Account;
import entities.CompetitorMatch;
import entities.MatchMatchType;
import entities.Matchh;
import java.util.List;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ejbCommon.TrackerInterceptor;
import exceptions.ApplicationException;
import exceptions.MatchOptimisticLockException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.OptimisticLockException;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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

    @Override
    public List<CompetitorMatch> findByIdMatch(Integer idMatch) {
        Query q = em.createNamedQuery("CompetitorMatch.findByIdMatch");
        q.setParameter("idMatch", idMatch);

        return (List<CompetitorMatch>) q.getResultList();
    }

    @Override
    public void edit(CompetitorMatch entity) {
        em.merge(entity);
        em.flush();

    }

    @Override
    public CompetitorMatch find(Object id) {
        CompetitorMatch entity = em.find(CompetitorMatch.class, id);
        em.flush();

        return entity;
    }

    @Override
    public CompetitorMatch editWithReturn(CompetitorMatch entity) throws ApplicationException {
        for (MatchMatchType mmt : entity.getIdMatch().getMatchMatchTypeList()) {
            em.lock(em.find(MatchMatchType.class, mmt.getIdMatchMatchType()), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        }

        Query q = em.createNamedQuery("CompetitorMatch.findOtherByIdMatch");
        q.setParameter("idMatch", entity.getIdMatch().getIdMatch());
        q.setParameter("idCompetitorMatch", entity.getIdCompetitorMatch());

        CompetitorMatch otherCompetitorMatch = (CompetitorMatch) q.getSingleResult();
        System.out.println("otherCompetitorMatch ID " + otherCompetitorMatch.getIdCompetitorMatch());
        em.lock(otherCompetitorMatch, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        for (CompetitorMatch cm : entity.getIdMatch().getCompetitorMatchList()) {
            if (cm.getIdCompetitorMatch().equals(otherCompetitorMatch.getIdCompetitorMatch())) {
                int index = entity.getIdMatch().getCompetitorMatchList().indexOf(cm);
                entity.getIdMatch().getCompetitorMatchList().set(index, otherCompetitorMatch);
            }
        }

        System.out.println("VERSION BEFORE UPDATE CompetitorMatch " + entity.getVersion());
        CompetitorMatch editedCompetitorMatch;

        try {
            editedCompetitorMatch = em.merge(entity);
            em.flush();
        } catch (OptimisticLockException e) {
            throw MatchOptimisticLockException.optimisticLock(e);
        }
        
        System.out.println("VERSION AFTER UPDATE CompetitorMatch " + entity.getVersion());

        return editedCompetitorMatch;
    }

    @Override
    public Matchh editWithReturnAdvancing(Matchh storedMatch) throws ApplicationException {

        try {
        for (int i = 0; i < storedMatch.getCompetitorMatchList().size(); i++) {
            storedMatch.getCompetitorMatchList().set(i, em.merge(storedMatch.getCompetitorMatchList().get(i)));
        }

//        em.lock(em.find(Matchh.class, storedMatch.getIdMatch()), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        em.flush();
        } catch (OptimisticLockException e) {
            throw MatchOptimisticLockException.optimisticLock(e);
        }

        return storedMatch;
    }
}
