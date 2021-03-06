/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Score.findByIdCompetition", query = "SELECT s FROM Score s WHERE s.idCompetition.idCompetition = :idCompetition"),
    @NamedQuery(name = "Score.findByIdCompetitionAndIdCompetitor", query = "SELECT s FROM Score s WHERE s.idCompetition.idCompetition = :idCompetition AND s.idCompetitor.idCompetitor = :idCompetitor"),
    @NamedQuery(name = "Score.findAll", query = "SELECT s FROM Score s"),
    @NamedQuery(name = "Score.findByIdScore", query = "SELECT s FROM Score s WHERE s.idScore = :idScore"),
    @NamedQuery(name = "Score.findPlacementCount", query = "SELECT COUNT(s) FROM Score s WHERE s.idCompetitor.idCompetitor = :idCompetitor AND s.place = :place AND s.idCompetition.global = true"),
    @NamedQuery(name = "Score.findCompetitorOccuranceCount", query = "SELECT COUNT(s) FROM Score s WHERE s.idCompetitor.idCompetitor = :idCompetitor AND s.idCompetition.global = true"),
    @NamedQuery(name = "Score.findCompetitorsCountByGlobalCompetitionName", query = "SELECT COUNT(s) FROM Score s WHERE s.idCompetition.competitionName = :competitionName and s.idCompetition.global = TRUE"),
    @NamedQuery(name = "Score.findByScore", query = "SELECT s FROM Score s WHERE s.score = :score")})
public class Score implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_score")
    private Integer idScore;
    @Column(name = "score")
    private Short score;
    @JoinColumn(name = "id_competition", referencedColumnName = "id_competition")
    @ManyToOne(optional = false)
    private Competition idCompetition;
    @JoinColumn(name = "id_competitor", referencedColumnName = "id_competitor")
    @ManyToOne(optional = false)
    private Competitor idCompetitor;
    @Column(name = "place")
    private short place;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private long version;

    public Score() {
    }

    public Score(Integer idScore) {
        this.idScore = idScore;
    }

    public Integer getIdScore() {
        return idScore;
    }

    public void setIdScore(Integer idScore) {
        this.idScore = idScore;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public Competition getIdCompetition() {
        return idCompetition;
    }

    public void setIdCompetition(Competition idCompetition) {
        this.idCompetition = idCompetition;
    }

    public Competitor getIdCompetitor() {
        return idCompetitor;
    }

    public void setIdCompetitor(Competitor idCompetitor) {
        this.idCompetitor = idCompetitor;
    }

    public short getPlace() {
        return place;
    }

    public void setPlace(short place) {
        this.place = place;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idScore != null ? idScore.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Score)) {
            return false;
        }
        Score other = (Score) object;
        if ((this.idScore == null && other.idScore != null) || (this.idScore != null && !this.idScore.equals(other.idScore))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Score[ idScore=" + idScore + " ]";
    }
    
}
