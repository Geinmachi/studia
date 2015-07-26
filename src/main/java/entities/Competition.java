/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "competition")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competition.findAll", query = "SELECT c FROM Competition c"),
    @NamedQuery(name = "Competition.findByIdCompetition", query = "SELECT c FROM Competition c WHERE c.idCompetition = :idCompetition"),
    @NamedQuery(name = "Competition.findByCompetitionName", query = "SELECT c FROM Competition c WHERE c.competitionName = :competitionName"),
    @NamedQuery(name = "Competition.findByCreationDate", query = "SELECT c FROM Competition c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "Competition.findByStartDate", query = "SELECT c FROM Competition c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "Competition.findByEndDate", query = "SELECT c FROM Competition c WHERE c.endDate = :endDate"),
    @NamedQuery(name = "Competition.findByVersion", query = "SELECT c FROM Competition c WHERE c.version = :version")})
public class Competition implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Transient
    private UUID uuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competition")
    private Integer idCompetition;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "competition_name")
    private String competitionName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @JoinColumn(name = "id_organizer", referencedColumnName = "id_access_level")
    @ManyToOne(optional = false)
    private AccessLevel idOrganizer;
    @JoinColumn(name = "id_competition_type", referencedColumnName = "id_competition_type")
    @ManyToOne(optional = false)
    private CompetitionType idCompetitionType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetition")
    private List<Score> scoreList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetition")
    private List<GroupCompetition> groupCompetitionList = new ArrayList<>();

    public Competition() {
    }

    public Competition(UUID uuid) {
        this.uuid = uuid;
    }
    
    public Competition(Integer idCompetition) {
        this.idCompetition = idCompetition;
    }

    public Competition(Integer idCompetition, String competitionName, Date creationDate, Date startDate, Date endDate, long version) {
        this.idCompetition = idCompetition;
        this.competitionName = competitionName;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.version = version;
    }

    public Integer getIdCompetition() {
        return idCompetition;
    }

    public void setIdCompetition(Integer idCompetition) {
        this.idCompetition = idCompetition;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public AccessLevel getIdOrganizer() {
        return idOrganizer;
    }

    public void setIdOrganizer(AccessLevel idOrganizer) {
        this.idOrganizer = idOrganizer;
    }

    public CompetitionType getIdCompetitionType() {
        return idCompetitionType;
    }

    public void setIdCompetitionType(CompetitionType idCompetitionType) {
        this.idCompetitionType = idCompetitionType;
    }

    @XmlTransient
    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @XmlTransient
    public List<GroupCompetition> getGroupCompetitionList() {
        return groupCompetitionList;
    }

    public void setGroupCompetitionList(List<GroupCompetition> groupCompetitionList) {
        this.groupCompetitionList = groupCompetitionList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.uuid);
        hash = 43 * hash + Objects.hashCode(this.idCompetition);
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
        final Competition other = (Competition) obj;
        
        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (!Objects.equals(this.idCompetition, other.idCompetition)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "entities.Competition[ idCompetition=" + idCompetition + " ]";
    }
    
}
