/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    @NamedQuery(name = "Competitor.findByIdCompetitor", query = "SELECT c FROM Competitor c WHERE c.idCompetitor = :idCompetitor")})
public class Competitor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_competitor")
    private Long idCompetitor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetitor")
    private List<CompetitorMatchGroup> competitorMatchGroupList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetitor")
    private List<Score> scoreList;
    @JoinColumn(name = "id_personal_info", referencedColumnName = "id_personal_info")
    @OneToOne(optional = false)
    private PersonalInfo idPersonalInfo;
    @JoinColumn(name = "id_team", referencedColumnName = "id_team")
    @ManyToOne(optional = false)
    private Team idTeam;

    public Competitor() {
    }

    public Competitor(Long idCompetitor) {
        this.idCompetitor = idCompetitor;
    }

    public Long getIdCompetitor() {
        return idCompetitor;
    }

    public void setIdCompetitor(Long idCompetitor) {
        this.idCompetitor = idCompetitor;
    }

    @XmlTransient
    public List<CompetitorMatchGroup> getCompetitorMatchGroupList() {
        return competitorMatchGroupList;
    }

    public void setCompetitorMatchGroupList(List<CompetitorMatchGroup> competitorMatchGroupList) {
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

    public Team getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Team idTeam) {
        this.idTeam = idTeam;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompetitor != null ? idCompetitor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Competitor)) {
            return false;
        }
        Competitor other = (Competitor) object;
        if ((this.idCompetitor == null && other.idCompetitor != null) || (this.idCompetitor != null && !this.idCompetitor.equals(other.idCompetitor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Competitor[ idCompetitor=" + idCompetitor + " ]";
    }
    
}
