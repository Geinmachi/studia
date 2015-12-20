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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "competitor_match")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompetitorMatch.findOtherByIdMatch", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch.idMatch = :idMatch AND c.idCompetitorMatch != :idCompetitorMatch"),
    @NamedQuery(name = "CompetitorMatch.findByCompetitionId", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch.competition.idCompetition = :idCompetition"),
    @NamedQuery(name = "CompetitorMatch.findByIdMatch", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch.idMatch = :idMatch"),
    @NamedQuery(name = "CompetitorMatch.findByMatchNumberAndIdCompetition", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch.matchNumber = :matchNumber AND c.idMatch.competition.idCompetition = :idCompetition"),
    @NamedQuery(name = "CompetitorMatch.findByMatchNumber", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch.matchNumber = :matchNumber"),
    @NamedQuery(name = "CompetitorMatch.findByCompetitionId", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch.competition.idCompetition = :idCompetition"),
    @NamedQuery(name = "CompetitorMatch.findAll", query = "SELECT c FROM CompetitorMatch c"),
    @NamedQuery(name = "CompetitorMatch.findByIdCompetitorMatch", query = "SELECT c FROM CompetitorMatch c WHERE c.idCompetitorMatch = :idCompetitorMatch"),
    @NamedQuery(name = "CompetitorMatch.findByCompetitorMatchScore", query = "SELECT c FROM CompetitorMatch c WHERE c.competitorMatchScore = :competitorMatchScore"),
    @NamedQuery(name = "CompetitorMatch.findCompetitorMatchesStatistics", query = "SELECT c FROM CompetitorMatch c WHERE c.idMatch IN (SELECT cc.idMatch FROM CompetitorMatch cc WHERE cc.idCompetitor.idCompetitor = :idCompetitor)"),
    @NamedQuery(name = "CompetitorMatch.findByVersion", query = "SELECT c FROM CompetitorMatch c WHERE c.version = :version")})
public class CompetitorMatch implements Serializable, Comparable<CompetitorMatch> {

    private static final long serialVersionUID = 1L;

    @Transient
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competitor_match")
    private Integer idCompetitorMatch;
    @Column(name = "competitor_match_score")
    private Short competitorMatchScore;
    @Column(name = "placer")
    private Short placer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private long version;
    @JoinColumn(name = "id_competitor", referencedColumnName = "id_competitor")
    @ManyToOne(optional = true)
    private Competitor idCompetitor;
//    @JoinColumn(name = "id_group", referencedColumnName = "id_group")
//    @ManyToOne(optional = true)
//    private GroupName idGroup;
    @JoinColumn(name = "id_match", referencedColumnName = "id_match")
    @ManyToOne(optional = false)
    private Matchh idMatch;

    public CompetitorMatch() {
    }

    public CompetitorMatch(UUID uuid) {
        this.uuid = uuid;
    }

    public CompetitorMatch(Integer idCompetitorMatch) {
        this.idCompetitorMatch = idCompetitorMatch;
    }

    public Integer getIdCompetitorMatch() {
        return idCompetitorMatch;
    }

    public void setIdCompetitorMatch(Integer idCompetitorMatch) {
        this.idCompetitorMatch = idCompetitorMatch;
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

//    public GroupName getIdGroup() {
//        return idGroup;
//    }
//
//    public void setIdGroup(GroupName idGroup) {
//        this.idGroup = idGroup;
//    }
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
        hash = 59 * hash + Objects.hashCode(this.idCompetitorMatch);
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
        final CompetitorMatch other = (CompetitorMatch) obj;

        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            } else {
                return false;
            }
        }

        if (!Objects.equals(this.idCompetitorMatch, other.idCompetitorMatch)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CompetitorMatch[ idCompetitorMatch=" + idCompetitorMatch + " ]";
    }

    @Override
    public int compareTo(CompetitorMatch o) {
        System.out.println("COMPARATOR");

        if (this.placer == null && o.getPlacer() == null) {
            System.out.println("OBA NULE");
            return 0;
        }
        if (this.placer == null) {
            System.out.println("PIerwszy null + placer o.getPlacer " + o.getPlacer());
            System.out.println("warunek: " + o.getPlacer().equals(1));
            if (o.getPlacer().equals((short)1)) {
                return 1;
            } else {
                return -1;
            }
        }
        if (o.getPlacer() == null) {
            System.out.println("drugi null + placer this,plcare " + this.placer);
            System.out.println("warunek: " + this.placer.equals(1));
            if (this.placer.equals((short)1)) {
                return -1;
            } else {
                return 1;
            }
        }
        return this.placer.compareTo(o.getPlacer());
    }

}
