/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.GroupName;
import java.util.ArrayList;
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
public class GroupNameFacade extends AbstractFacade<GroupName> implements GroupNameFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupNameFacade() {
        super(GroupName.class);
    }

    @Override
    public GroupName createWithReturn(GroupName entity) {
        em.persist(entity);
        em.flush();
        
        return entity;
    }

    @Override
    public List<GroupName> findAll() {
        
//        List<GroupName> gn = new ArrayList<>();
//        System.out.println("pobranie 1 dla id 241");
//        gn.add(find(706));
//        gn.add(find(707));
//        gn.add(find(708));
//        gn.add(find(709));
//        gn.add(find(721));
//        gn.add(find(721));
//        gn.add(find(722));
//        gn.add(find(723));
//        gn.add(find(724));
//        System.out.println("koniec pobrania");
        
        Query q = em.createNamedQuery("GroupName.findAll");
        System.out.println("Przed pobraniem");
        List<GroupName> gn = (List<GroupName>)q.getResultList();
        System.out.println("po pobraniu size ");
        
        for (GroupName g : gn) {
            System.out.println("GN: " + g.getGroupName() + " id: " + g.getIdGroupName());
        }
        
        return gn;
    }
    
}
