/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.backingBeans.mot.team;

import entities.Competitor;
import entities.Team;
import exceptions.ApplicationException;
import exceptions.TeamCreationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DualListModel;
import web.converters.ConverterHelper;
import web.utils.JsfUtils;
import web.utils.PageConstants;
import web.validators.CompetitorsDualListSetter;
import web.validators.DuplicatedCompetitors;

/**
 *
 * @author java
 */
@Named(value = "createTeamBackingBean")
@ConverterHelper(viewId = PageConstants.ORGANIZER_CREATE_TEAM)
@ViewScoped
public class CreateTeamBackingBean extends TeamBackingBean implements Serializable, CompetitorsDualListSetter, DuplicatedCompetitors {

    private final Team team = new Team();

    private boolean isGlobal;

    public Team getTeam() {
        return team;
    }

    public boolean isIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public DualListModel getCompetitors() {
        return competitors;
    }

    @Override
    public void setCompetitors(DualListModel competitors) {
        this.competitors = competitors;
    }

    public boolean isDuplicatedCompetitorFlag() {
        return duplicatedCompetitorFlag;
    }

    @Override
    public void setDuplicatedCompetitorFlag(boolean duplicatedCompetitorFlag) {
        this.duplicatedCompetitorFlag = duplicatedCompetitorFlag;
    }

    /**
     * Creates a new instance of CreateTeamBackingBean
     */
    public CreateTeamBackingBean() {
    }

    @PostConstruct
    private void init() {
        System.out.println("CreateTeamBackingBean#init() " + this);

        try {
            competitorList = controller.getAllAllowedTeamlessCompetitors();
        } catch (ApplicationException ex) {
            Logger.getLogger(CreateTeamBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Competitor> competitorsSource = competitorList;
        List<Competitor> comeptitorsTarget = new ArrayList<>();

        competitors = new DualListModel(competitorsSource, comeptitorsTarget);

    }

    public List<Competitor> getCompetitorList() {
        return this.competitorList;
    }

    public String createTeam() {
        team.setCompetitorList(competitors.getTarget());

        try {
            System.out.println("Competitors size BB " + team.getCompetitorList().size());
            controller.createTeam(team, isGlobal);

            return JsfUtils.successPageRedirect(PageConstants.ORGANIZER_CREATE_TEAM);
        } catch (TeamCreationException e) {
            System.out.println("TEAMCREATIONEXCEPTION " + e.getLocalizedMessage());
            JsfUtils.addErrorMessage(e.getLocalizedMessage(), null, null);
        } catch (Exception e) {
            System.out.println("Wyjatek przy twrzoeniu");
            Logger.getLogger(CreateTeamBackingBean.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    @Override
    public void checkDuplicate() {
        super.checkDuplicate();
    }

    @Override
    public List<Competitor> getFetchedData() {
        System.out.println("Fechuje z CreateTeam ---------------------");
        return this.competitorList;
    }
}
