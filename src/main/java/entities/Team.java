/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author java
 */
@Entity
@Table(name = "team")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t"),
    @NamedQuery(name = "Team.findByIdTeam", query = "SELECT t FROM Team t WHERE t.idTeam = :idTeam"),
    @NamedQuery(name = "Team.findByTeamName", query = "SELECT t FROM Team t WHERE t.teamName = :teamName"),
    @NamedQuery(name = "Team.findByVersion", query = "SELECT t FROM Team t WHERE t.version = :version")})
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Transient
    private UUID uuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_team")
    private Integer idTeam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "team_name")
    private String teamName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @OneToMany(mappedBy = "idTeam")
    private List<Competitor> competitorList;

    public Team() {
    }

    public Team(UUID uuid) {
        this.uuid = uuid;
    }
    
    public Team(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public Team(Integer idTeam, String teamName, long version) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.version = version;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @XmlTransient
    public List<Competitor> getCompetitorList() {
        return competitorList;
    }

    public void setCompetitorList(List<Competitor> competitorList) {
        this.competitorList = competitorList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.uuid);
        hash = 97 * hash + Objects.hashCode(this.idTeam);
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
        final Team other = (Team) obj;
        
        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (!Objects.equals(this.idTeam, other.idTeam)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "entities.Team[ idTeam=" + idTeam + " ]";
    }
    
}
