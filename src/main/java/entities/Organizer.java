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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "organizer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organizer.findAll", query = "SELECT o FROM Organizer o"),
    @NamedQuery(name = "Organizer.findByIdAccessLevel", query = "SELECT o FROM Organizer o WHERE o.idAccessLevel = :idAccessLevel"),
    @NamedQuery(name = "Organizer.findByVersion", query = "SELECT o FROM Organizer o WHERE o.version = :version")})
public class Organizer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_access_level")
    private Long idAccessLevel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @JoinColumn(name = "id_access_level", referencedColumnName = "id_access_level", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private AccessLevel accessLevel;

    public Organizer() {
    }

    public Organizer(Long idAccessLevel) {
        this.idAccessLevel = idAccessLevel;
    }

    public Organizer(Long idAccessLevel, long version) {
        this.idAccessLevel = idAccessLevel;
        this.version = version;
    }

    public Long getIdAccessLevel() {
        return idAccessLevel;
    }

    public void setIdAccessLevel(Long idAccessLevel) {
        this.idAccessLevel = idAccessLevel;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAccessLevel != null ? idAccessLevel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organizer)) {
            return false;
        }
        Organizer other = (Organizer) object;
        if ((this.idAccessLevel == null && other.idAccessLevel != null) || (this.idAccessLevel != null && !this.idAccessLevel.equals(other.idAccessLevel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Organizer[ idAccessLevel=" + idAccessLevel + " ]";
    }
    
}
