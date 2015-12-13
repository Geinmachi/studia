/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitorMatch;
import entities.MatchMatchType;
import exceptions.ApplicationException;
import exceptions.MatchOptimisticLockException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MatchMatchTypeFacade extends AbstractFacade<MatchMatchType> implements MatchMatchTypeFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MatchMatchTypeFacade() {
        super(MatchMatchType.class);
    }

    @Override
    public MatchMatchType editWithReturn(MatchMatchType matchMatchType) throws ApplicationException {
        for (CompetitorMatch cm : matchMatchType.getIdMatch().getCompetitorMatchList()) {
            System.out.println("VERSION bbbb " + cm.getVersion());
            em.lock(em.find(CompetitorMatch.class, cm.getIdCompetitorMatch()), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        }

        MatchMatchType entity;
        try {
            entity = em.merge(matchMatchType);
            em.flush();
        } catch (OptimisticLockException e) {
            throw MatchOptimisticLockException.optimisticLock(e);
        }
        System.out.println("VERSION aaa " + matchMatchType.getIdMatch().getCompetitorMatchList().get(0).getVersion());
        return entity;
    }

}
