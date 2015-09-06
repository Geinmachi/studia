/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.Account;
import entities.Competition;
import entities.GroupDetails;
import java.util.Collections;
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
public class CompetitionFacade extends AbstractFacade<Competition> implements CompetitionFacadeLocal {
    @PersistenceContext(unitName = "mot_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompetitionFacade() {
        super(Competition.class);
    }

    @Override
    public List<Competition> findUserCompetitionsByIdAccessLevel(Object id) {
        Query q = em.createNamedQuery("Competition.findByIdAccessLevel");
        q.setParameter("idAccessLevel", id);
        return (List<Competition>) q.getResultList();
    }

//    @Override
//    public Competition findAndInitializeGCLists(Object id) {
//        Competition managedEntity = em.find(Competition.class, id);
//        for(GroupCompetition gc : managedEntity.getGroupCompetitionList()) {
//            gc.getIdGroup().getCompetitorMatchGroupList().size();
//        }
//        return managedEntity;
//    }

    @Override
    public Competition createWithReturn(Competition entity) {
        em.persist(entity);
        em.flush();
        
        return entity;
    }

    @Override
    public Competition findAndInitializeGD(Integer idCompetition) {
        Competition entity = em.find(Competition.class, idCompetition);
   //     entity.getGroupDetailsList().size();
        for(GroupDetails gd : entity.getGroupDetailsList()) {
            gd.getGroupCompetitorList().size();
        }
        return entity;
    }

}
