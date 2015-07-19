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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "match_match_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatchMatchType.findAll", query = "SELECT m FROM MatchMatchType m"),
    @NamedQuery(name = "MatchMatchType.findByIdMatchMatchType", query = "SELECT m FROM MatchMatchType m WHERE m.idMatchMatchType = :idMatchMatchType")})
public class MatchMatchType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_match_match_type")
    private Long idMatchMatchType;
    @JoinColumn(name = "id_match_type", referencedColumnName = "id_match_type")
    @ManyToOne(optional = false)
    private MatchType idMatchType;
    @JoinColumn(name = "id_match", referencedColumnName = "id_match")
    @ManyToOne(optional = false)
    private Matchh idMatch;

    public MatchMatchType() {
    }

    public MatchMatchType(Long idMatchMatchType) {
        this.idMatchMatchType = idMatchMatchType;
    }

    public Long getIdMatchMatchType() {
        return idMatchMatchType;
    }

    public void setIdMatchMatchType(Long idMatchMatchType) {
        this.idMatchMatchType = idMatchMatchType;
    }

    public MatchType getIdMatchType() {
        return idMatchType;
    }

    public void setIdMatchType(MatchType idMatchType) {
        this.idMatchType = idMatchType;
    }

    public Matchh getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Matchh idMatch) {
        this.idMatch = idMatch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMatchMatchType != null ? idMatchMatchType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatchMatchType)) {
            return false;
        }
        MatchMatchType other = (MatchMatchType) object;
        if ((this.idMatchMatchType == null && other.idMatchMatchType != null) || (this.idMatchMatchType != null && !this.idMatchMatchType.equals(other.idMatchMatchType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MatchMatchType[ idMatchMatchType=" + idMatchMatchType + " ]";
    }
    
}
