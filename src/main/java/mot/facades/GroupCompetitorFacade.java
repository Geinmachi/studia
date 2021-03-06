/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupCompetitor;
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
public class GroupCompetitorFacade extends AbstractFacade<GroupCompetitor> implements GroupCompetitorFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupCompetitorFacade() {
        super(GroupCompetitor.class);
    }

    @Override
    public List<GroupCompetitor> findByCompetitionId(Integer idCompetition) {
        Query q = em.createNamedQuery("GroupCompetitor.findByCompetitionId");
        q.setParameter("idCompetition", idCompetition);
        
        return (List<GroupCompetitor>) q.getResultList();
    }
    
}
