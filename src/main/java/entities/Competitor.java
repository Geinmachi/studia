/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author java
 */
@Entity
@Table(name = "competitor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competitor.findAll", query = "SELECT c FROM Competitor c"),
    @NamedQuery(name = "Competitor.findUserCompetitors", query = "SELECT c FROM Competitor c WHERE c.idCreator.idAccessLevel = :idAccessLevel"),
    @NamedQuery(name = "Competitor.findByFirstnameLastnameGlobal", query = "SELECT c FROM Competitor c WHERE c.idPersonalInfo.firstName = :firstName AND c.idPersonalInfo.lastName = :lastName AND c.idCreator IS NULL"),
    @NamedQuery(name = "Competitor.findByFirstnameLastnameCreator", query = "SELECT c FROM Competitor c WHERE c.idPersonalInfo.firstName = :firstName AND c.idPersonalInfo.lastName = :lastName AND c.idCreator.idAccessLevel = :idCreator"),
    @NamedQuery(name = "Competitor.findAllTeamless", query = "SELECT c FROM Competitor c WHERE c.idTeam IS NULL"),
    @NamedQuery(name = "Competitor.findAllAllowedTeamless", query = "SELECT c FROM Competitor c WHERE c.idTeam IS NULL AND (c.idCreator.idAccessLevel = :idAccessLevel OR c.idCreator IS NULL)"),
    @NamedQuery(name = "Competitor.findGlobalCompetitors", query = "SELECT c FROM Competitor c WHERE c.idCreator IS NULL"),
    @NamedQuery(name = "Competitor.findByIdCompetitor", query = "SELECT c FROM Competitor c WHERE c.idCompetitor = :idCompetitor")}) 
public class Competitor implements Serializable, Comparable<Competitor> {

    private static final long serialVersionUID = 1L;

    @Transient
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competitor")
    private Integer idCompetitor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetitor")
    private List<CompetitorMatch> competitorMatchGroupList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetitor")
    private List<Score> scoreList = new ArrayList<>();
    @JoinColumn(name = "id_personal_info", referencedColumnName = "id_personal_info")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private PersonalInfo idPersonalInfo;
    @JoinColumn(name = "id_team", referencedColumnName = "id_team")
    @ManyToOne
    private Team idTeam;
    @JoinColumn(name = "id_creator", referencedColumnName = "id_access_level")
    @ManyToOne
    private AccessLevel idCreator;
    @OneToMany(mappedBy = "idCompetitor")
    private List<GroupCompetitor> groupCompetitorList = new ArrayList<>();
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private long version;

    public Competitor() {
    }

    public Competitor(UUID uuid) {
        this.uuid = uuid;
    }

    public Competitor(Integer idCompetitor) {
        this.idCompetitor = idCompetitor;
    }

    public Integer getIdCompetitor() {
        return idCompetitor;
    }

    public void setIdCompetitor(Integer idCompetitor) {
        this.idCompetitor = idCompetitor;
    }

    @XmlTransient
    public List<CompetitorMatch> getCompetitorMatchGroupList() {
        return competitorMatchGroupList;
    }

    public void setCompetitorMatchGroupList(List<CompetitorMatch> competitorMatchGroupList) {
        this.competitorMatchGroupList = competitorMatchGroupList;
    }

    @XmlTransient
    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public PersonalInfo getIdPersonalInfo() {
        return idPersonalInfo;
    }

    public void setIdPersonalInfo(PersonalInfo idPersonalInfo) {
        this.idPersonalInfo = idPersonalInfo;
    }

    public AccessLevel getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(AccessLevel idCreator) {
        this.idCreator = idCreator;
    }

    public Team getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Team idTeam) {
        this.idTeam = idTeam;
    }

    public List<GroupCompetitor> getGroupCompetitorList() {
        return groupCompetitorList;
    }

    public void setGroupCompetitorList(List<GroupCompetitor> groupCompetitorList) {
        this.groupCompetitorList = groupCompetitorList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.uuid);
        hash = 89 * hash + Objects.hashCode(this.idCompetitor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Competitor other = (Competitor) obj;

        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            } else {
                return false;
            }
        }

        if (!Objects.equals(this.idCompetitor, other.idCompetitor)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Competitor[ idCompetitor=" + idCompetitor + " ]";
    }

    @Override
    public int compareTo(Competitor o) {
        return (this.idPersonalInfo.getFirstName() + " " + this.idPersonalInfo.getLastName()).compareTo(
                o.getIdPersonalInfo().getFirstName() + " " + o.getIdPersonalInfo().getLastName());
    }

}
