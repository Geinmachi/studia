/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Objects;
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
import javax.persistence.Version;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_match_match_type")
    private Integer idMatchMatchType;
    @JoinColumn(name = "id_match_type", referencedColumnName = "id_match_type")
    @ManyToOne(optional = false)
    private MatchType idMatchType;
    @JoinColumn(name = "id_match", referencedColumnName = "id_match")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Matchh idMatch;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private long version;

    public MatchMatchType() {
    }

    public MatchMatchType(Integer idMatchMatchType) {
        this.idMatchMatchType = idMatchMatchType;
    }

    public Integer getIdMatchMatchType() {
        return idMatchMatchType;
    }

    public void setIdMatchMatchType(Integer idMatchMatchType) {
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
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.idMatchMatchType);
        hash = 83 * hash + Objects.hashCode(this.idMatchType);
        hash = 83 * hash + Objects.hashCode(this.idMatch);
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
        final MatchMatchType other = (MatchMatchType) obj;
        if (!Objects.equals(this.idMatchMatchType, other.idMatchMatchType)) {
            return false;
        }
        if (!Objects.equals(this.idMatchType, other.idMatchType)) {
            return false;
        }
        if (!Objects.equals(this.idMatch, other.idMatch)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "entities.MatchMatchType[ idMatchMatchType=" + idMatchMatchType + " ]";
    }
    
}
