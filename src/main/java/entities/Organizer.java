/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "organizer")
@DiscriminatorValue("organizer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organizer.findAll", query = "SELECT o FROM Organizer o"),
    @NamedQuery(name = "Organizer.findByIdAccessLevel", query = "SELECT o FROM Organizer o WHERE o.idAccessLevel = :idAccessLevel"),
    @NamedQuery(name = "Organizer.findByVersion", query = "SELECT o FROM Organizer o WHERE o.version = :version")})
public class Organizer extends AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    public Organizer() {
    }

    @Override
    public String toString() {
        return "Organizer{" + '}';
    }
    
    
}
