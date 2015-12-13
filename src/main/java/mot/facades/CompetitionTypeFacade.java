/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.CompetitionType;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CompetitionTypeFacade extends AbstractFacade<CompetitionType> implements CompetitionTypeFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitionTypeFacade() {
        super(CompetitionType.class);
    }
    
}
