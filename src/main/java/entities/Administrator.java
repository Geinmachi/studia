/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "administrator")
@DiscriminatorValue("administrator")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
    @NamedQuery(name = "Administrator.findByIdAccessLevel", query = "SELECT a FROM Administrator a WHERE a.idAccessLevel = :idAccessLevel"),
    @NamedQuery(name = "Administrator.findByVersion", query = "SELECT a FROM Administrator a WHERE a.version = :version")})
public class Administrator extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "assigned_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;
    
    public Administrator() {
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    @Override
    public String toString() {
        return "Administrator{" + '}';
    }
}
