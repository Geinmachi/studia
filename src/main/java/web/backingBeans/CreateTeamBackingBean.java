/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans;

import entities.Competitor;
import entities.Team;
import exceptions.TeamCreationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import web.controllers.CompetitionController;
import web.converters.CompetitorConverterData;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
@Named(value = "createTeamBackingBean")
@ViewScoped
public class CreateTeamBackingBean implements Serializable, CompetitorConverterData {

    private final Team team = new Team();

    private DualListModel competitors;

    private List<Competitor> competitorList;
    
    private boolean duplicatedCompetitorFlag;

    @Inject
    private CompetitionController controller;

    public Team getTeam() {
        return team;
    }

    public DualListModel getCompetitors() {
        return competitors;
    }

    public void setCompetitors(DualListModel competitors) {
        this.competitors = competitors;
    }

    public boolean isDuplicatedCompetitorFlag() {
        return duplicatedCompetitorFlag;
    }

    /**
     * Creates a new instance of CreateTeamBackingBean
     */
    public CreateTeamBackingBean() {
    }

    @PostConstruct
    private void init() {
        System.out.println("CreateTeamBackingBean#init() " + this);

        competitorList = controller.getAllTeamlessCompetitors();
        List<Competitor> competitorsSource = competitorList;
        List<Competitor> comeptitorsTarget = new ArrayList<>();

        competitors = new DualListModel(competitorsSource, comeptitorsTarget);

    }

    @Override
    public List<Competitor> getCompetitorList() {
        return this.competitorList;
    }

    public String createTeam() {
        team.setCompetitorList(competitors.getTarget());

        try {
            System.out.println("Competitors size BB " + team.getCompetitorList().size());
            controller.createTeam(team);
            return "/index.xhtml?faces-redirect=true";
        } catch (TeamCreationException e) {
            System.out.println("TEAMCREATIONEXCEPTION " + e.getLocalizedMessage());
            JsfUtils.addErrorMessage(e.getLocalizedMessage(), null, null);
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateTeamBackingBean.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

//    public void onTransfer(TransferEvent event) {
//        StringBuilder builder = new StringBuilder();
//        for (Object item : event.getItems()) {
//            builder.append(((Competitor) item).getIdPersonalInfo().getFirstName()).append("<br />");
//        }
//
//        System.out.println("SIZE " + competitors.getTarget().size());
//
//        FacesMessage msg = new FacesMessage();
//        msg.setSeverity(FacesMessage.SEVERITY_INFO);
//        msg.setSummary("Items Transferred");
//        msg.setDetail(builder.toString());
//
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }

    public void checkDuplicate() {
        if (!controller.vlidateCompetitorDuplicate((List<Competitor>) competitors.getTarget())) {
            System.out.println("Duplicated competitor");
            JsfUtils.addErrorMessage("Team contains duplicated competitor", null, null);
            duplicatedCompetitorFlag = true;
            return;
        }
        
        duplicatedCompetitorFlag = false;
    }
}
