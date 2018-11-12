/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.is.proyecto.integrador.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author orion
 */
@Entity
@Table(name = "modo_operandi", catalog = "trata_personas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModoOperandi.findAll", query = "SELECT m FROM ModoOperandi m")
    , @NamedQuery(name = "ModoOperandi.findByDetalle", query = "SELECT m FROM ModoOperandi m WHERE m.detalle = :detalle")})
public class ModoOperandi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "detalle", nullable = false, length = 200)
    private String detalle;
    @OneToMany(mappedBy = "detallado")
    private List<Testimonio> testimonioList;

    public ModoOperandi() {
    }

    public ModoOperandi(String detalle) {
        this.detalle = detalle;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @XmlTransient
    public List<Testimonio> getTestimonioList() {
        return testimonioList;
    }

    public void setTestimonioList(List<Testimonio> testimonioList) {
        this.testimonioList = testimonioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalle != null ? detalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModoOperandi)) {
            return false;
        }
        ModoOperandi other = (ModoOperandi) object;
        if ((this.detalle == null && other.detalle != null) || (this.detalle != null && !this.detalle.equals(other.detalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return detalle;
    }
    
}
