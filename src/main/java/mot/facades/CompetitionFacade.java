/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mot.facades;

import entities.AccessLevel;
import entities.Competition;
import entities.Competitor;
import entities.GroupCompetitor;
import entities.GroupDetails;
import exceptions.ApplicationException;
import exceptions.CompetitionGeneralnfoException;
import exceptions.CompetitorCreationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author java
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
    public void competitionConstraints(Competition competition) throws CompetitionGeneralnfoException {
        Query q = null;

        AccessLevel creator = competition.getIdOrganizer();

        if (competition.isGlobal()) {
            q = em.createNamedQuery("Competition.findByCompetitionNameGlobal");
        } else {
            q = em.createNamedQuery("Competition.findByCompetitionNamePrivateOrganizer");
            q.setParameter("idCreator", creator.getIdAccessLevel());
        }

        q.setParameter("competitionName", competition.getCompetitionName());

        try {
            Competition c = (Competition) q.getSingleResult();
            em.flush();
        } catch (NoResultException e) {
            System.out.println("No existing competition found with given criteria, entitled to create");
        } catch (NonUniqueResultException e) {
//            System.out.println("NONunique " + e.getMessage());
//            e.printStackTrace();
            if (competition.isGlobal()) {
                throw CompetitionGeneralnfoException.globalDuplicate(e);
            } else {
                throw CompetitionGeneralnfoException.privateDuplicate(e);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void competitionConstraintsNotCommited(Competition competition) throws CompetitionGeneralnfoException {
        Query q = null;

        AccessLevel creator = competition.getIdOrganizer();

        if (competition.isGlobal()) {
            q = em.createNamedQuery("Competition.findByCompetitionNameGlobal");
        } else {
            q = em.createNamedQuery("Competition.findByCompetitionNamePrivateOrganizer");
            q.setParameter("idCreator", creator.getIdAccessLevel());
        }

        q.setParameter("competitionName", competition.getCompetitionName());
        try {
            Competition c = (Competition) q.getSingleResult();

            if (competition.isGlobal()) {
                throw CompetitionGeneralnfoException.globalDuplicate();
            } else {
                throw CompetitionGeneralnfoException.privateDuplicate();
            }
        } catch (NoResultException e) {
            System.out.println("No existing competition found with given criteria, entitled to create");
        } catch (NonUniqueResultException e) {
//            System.out.println("NONunique " + e.getMessage());
//            e.printStackTrace();
            System.out.println("Should never happen - duplicate in competition");
            throw CompetitionGeneralnfoException.databaseDuplicate(e);
        }
    }

    @Override
    public Competition createWithReturn(Competition entity) throws ApplicationException {
        try {
            em.persist(entity);
            em.flush();

            competitionConstraints(entity);
        } catch (PersistenceException e) {
            throw e;
        }

        return entity;
    }

    @Override
    public Competition findAndInitializeGD(Integer idCompetition) {
        Competition entity = em.find(Competition.class, idCompetition);
        //     entity.getGroupDetailsList().size();
        for (GroupDetails gd : entity.getGroupDetailsList()) {

            List<GroupCompetitor> gc = new ArrayList<>(gd.getGroupCompetitorList());
            Collections.sort(gc, new Comparator<GroupCompetitor>() {

                @Override
                public int compare(GroupCompetitor o1, GroupCompetitor o2) {
                    return o1.getIdCompetitor().getIdPersonalInfo().getLastName().compareTo(
                            o2.getIdCompetitor().getIdPersonalInfo().getLastName());
                }

            });

            gd.setGroupCompetitorList(gc);
        }

        List<GroupDetails> gdList = new ArrayList<>(entity.getGroupDetailsList());
        Collections.sort(gdList);

        entity.setGroupDetailsList(gdList);

        return entity;
    }

    @Override
    public void edit(Competition entity) throws ApplicationException {
//        Query q = em.createNamedQuery("CompetitorMatch.findByCompetitionId");
//        q.setParameter("idCompetition", entity.getIdCompetition());
//        
//        List<CompetitorMatch> competitorMatchList = (List<CompetitorMatch>)q.getResultList();
//        
//        for (CompetitorMatch cm : competitorMatchList) {
//            em.lock(cm, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//        }

        em.merge(entity);
        em.flush();

        competitionConstraints(entity);
    }

    @Override
    public List<Competition> findUserCompetitions(Integer idAccessLevel) {
        Query q = em.createNamedQuery("Competition.findByIdAccessLevel");
        q.setParameter("idAccessLevel", idAccessLevel);

        return (List<Competition>) q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Competition> findGlobalCompetitions() {
        Query q = em.createNamedQuery("Competition.findGlobalCompetitions");

        return (List<Competition>) q.getResultList();
    }

    @Override
    public Competition editWithReturn(Competition storedCompetition) throws ApplicationException {
        try {
            competitionConstraintsNotCommited(storedCompetition);
            Competition entity = em.merge(storedCompetition);
            em.flush();
            
            return entity;
        } catch (OptimisticLockException e) {
            throw CompetitionGeneralnfoException.optimisticLock(e);
        }
    }

    @Override
    public List<Object[]> getCompetitionPodiumStatistics() {
        Query q = em.createNamedQuery("Competition.getCompetitionPodiumStatistics");
        
        return (List<Object[]>)q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Competition> findAll() {
        return super.findAll(); //To change body of generated methods, choose Tools | Templates.
    }

}
