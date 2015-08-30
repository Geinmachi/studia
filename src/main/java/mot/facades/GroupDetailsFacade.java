/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupDetails;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author java
 */
@Stateless
public class GroupDetailsFacade extends AbstractFacade<GroupDetails> implements GroupDetailsFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupDetailsFacade() {
        super(GroupDetails.class);
    }

    @Override
    public GroupDetails createWithReturn(GroupDetails gc) {
        em.persist(gc);
        em.flush();
        System.out.println("GD IDDDDDDDDDddd " + gc.getIdGroupDetails());
        return gc;
    }
    
}
