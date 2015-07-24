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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author java
 */
@Entity
@Table(name = "matchh")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matchh.findAll", query = "SELECT m FROM Matchh m"),
    @NamedQuery(name = "Matchh.findByIdMatch", query = "SELECT m FROM Matchh m WHERE m.idMatch = :idMatch"),
    @NamedQuery(name = "Matchh.findByMatchDate", query = "SELECT m FROM Matchh m WHERE m.matchDate = :matchDate"),
    @NamedQuery(name = "Matchh.findByRoundd", query = "SELECT m FROM Matchh m WHERE m.roundd = :roundd"),
    @NamedQuery(name = "Matchh.findByVersion", query = "SELECT m FROM Matchh m WHERE m.version = :version")})
public class Matchh implements Serializable {
    private static final long serialVersionUID = 1L;  
    
    @Transient
    private UUID uuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_match")
    private Integer idMatch;
    @Basic(optional = false)
    @NotNull
    @Column(name = "match_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date matchDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "roundd")
    private short roundd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMatch")
    private List<MatchMatchType> matchMatchTypeList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMatch")
    private List<CompetitorMatchGroup> competitorMatchGroupList = new ArrayList<>();

    public Matchh() {
    }

    public Matchh(UUID uuid) {
        this.uuid = uuid;
    }
    
    public Matchh(Integer idMatch) {
        this.idMatch = idMatch;
    }

    public Matchh(Integer idMatch, Date matchDate, short roundd, long version) {
        this.idMatch = idMatch;
        this.matchDate = matchDate;
        this.roundd = roundd;
        this.version = version;
    }

    public Integer getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Integer idMatch) {
        this.idMatch = idMatch;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public short getRoundd() {
        return roundd;
    }

    public void setRoundd(short roundd) {
        this.roundd = roundd;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @XmlTransient
    public List<MatchMatchType> getMatchMatchTypeList() {
        return matchMatchTypeList;
    }

    public void setMatchMatchTypeList(List<MatchMatchType> matchMatchTypeList) {
        this.matchMatchTypeList = matchMatchTypeList;
    }

    @XmlTransient
    public List<CompetitorMatchGroup> getCompetitorMatchGroupList() {
        return competitorMatchGroupList;
    }

    public void setCompetitorMatchGroupList(List<CompetitorMatchGroup> competitorMatchGroupList) {
        this.competitorMatchGroupList = competitorMatchGroupList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.uuid);
        hash = 67 * hash + Objects.hashCode(this.idMatch);
        hash = 67 * hash + this.roundd;
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
        final Matchh other = (Matchh) obj;
        
        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            }
        }
        if (!Objects.equals(this.idMatch, other.idMatch)) {
            return false;
        }
        if (this.roundd != other.roundd) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "entities.Matchh[ idMatch=" + idMatch + " ]";
    }
    
}
