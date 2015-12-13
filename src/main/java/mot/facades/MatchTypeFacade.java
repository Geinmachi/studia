/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Account;
import entities.MatchType;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MatchTypeFacade extends AbstractFacade<MatchType> implements MatchTypeFacadeLocal {

    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MatchTypeFacade() {
        super(MatchType.class);
    }

    @Override
    public List<MatchType> findEndUserMatchTypes() {
        Query q = em.createNamedQuery("MatchType.findByEndUser");
        q.setParameter("endUser", true);
        return (List<MatchType>) q.getResultList();
    }

    @Override
    public MatchType findByMatchTypeName(String name) {
        Query q = em.createNamedQuery("MatchType.findByMatchTypeName");
        q.setParameter("matchTypeName", name);
        
        return (MatchType)q.getSingleResult();
    }

}
