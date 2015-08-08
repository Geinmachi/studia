/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "competitor_match_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompetitorMatchGroup.findByIdMatch", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.idMatch.idMatch = :idMatch"),
    @NamedQuery(name = "CompetitorMatchGroup.findByMatchNumberAndIdCompetition", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.idMatch.matchNumber = :matchNumber AND c.idMatch.competition.idCompetition = :idCompetition"),
    @NamedQuery(name = "CompetitorMatchGroup.findByMatchNumber", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.idMatch.matchNumber = :matchNumber"),
    @NamedQuery(name = "CompetitorMatchGroup.findByCompetitionId", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.idMatch.competition.idCompetition = :idCompetition"),
    @NamedQuery(name = "CompetitorMatchGroup.findAll", query = "SELECT c FROM CompetitorMatchGroup c"),
    @NamedQuery(name = "CompetitorMatchGroup.findByIdCompetitorMatchGroup", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.idCompetitorMatchGroup = :idCompetitorMatchGroup"),
    @NamedQuery(name = "CompetitorMatchGroup.findByCompetitorMatchScore", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.competitorMatchScore = :competitorMatchScore"),
    @NamedQuery(name = "CompetitorMatchGroup.findByVersion", query = "SELECT c FROM CompetitorMatchGroup c WHERE c.version = :version")})
public class CompetitorMatchGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Transient
    private UUID uuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competitor_match_group")
    private Integer idCompetitorMatchGroup;
    @Column(name = "competitor_match_score")
    private Short competitorMatchScore;
    @Column(name = "placer")
    private Short placer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @JoinColumn(name = "id_competitor", referencedColumnName = "id_competitor")
    @ManyToOne(optional = true)
    private Competitor idCompetitor;
    @JoinColumn(name = "id_group", referencedColumnName = "id_group")
    @ManyToOne(optional = true)
    private Groupp idGroup;
    @JoinColumn(name = "id_match", referencedColumnName = "id_match")
    @ManyToOne(optional = false)
    private Matchh idMatch;

    public CompetitorMatchGroup() {
    }

    public CompetitorMatchGroup(UUID uuid) {
        this.uuid = uuid;
    }
    public CompetitorMatchGroup(Integer idCompetitorMatchGroup) {
        this.idCompetitorMatchGroup = idCompetitorMatchGroup;
    }

    public Integer getIdCompetitorMatchGroup() {
        return idCompetitorMatchGroup;
    }

    public void setIdCompetitorMatchGroup(Integer idCompetitorMatchGroup) {
        this.idCompetitorMatchGroup = idCompetitorMatchGroup;
    }

    public Short getCompetitorMatchScore() {
        return competitorMatchScore;
    }

    public void setCompetitorMatchScore(Short competitorMatchScore) {
        this.competitorMatchScore = competitorMatchScore;
    }

    public Short getPlacer() {
        return placer;
    }

    public void setPlacer(Short placer) {
        this.placer = placer;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Competitor getIdCompetitor() {
        return idCompetitor;
    }

    public void setIdCompetitor(Competitor idCompetitor) {
        this.idCompetitor = idCompetitor;
    }

    public Groupp getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Groupp idGroup) {
        this.idGroup = idGroup;
    }

    public Matchh getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Matchh idMatch) {
        this.idMatch = idMatch;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.uuid);
        hash = 59 * hash + Objects.hashCode(this.idCompetitorMatchGroup);
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
        final CompetitorMatchGroup other = (CompetitorMatchGroup) obj;
        
        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (!Objects.equals(this.idCompetitorMatchGroup, other.idCompetitorMatchGroup)) {
            return false;
        }
        return true;
    }

    

    

    @Override
    public String toString() {
        return "entities.CompetitorMatchGroup[ idCompetitorMatchGroup=" + idCompetitorMatchGroup + " ]";
    }
    
}
