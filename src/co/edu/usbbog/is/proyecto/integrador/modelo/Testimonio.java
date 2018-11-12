/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.is.proyecto.integrador.modelo;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author orion
 */
@Entity
@Table(name = "testimonio", catalog = "trata_personas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Testimonio.findAll", query = "SELECT t FROM Testimonio t")
    , @NamedQuery(name = "Testimonio.findByIdTestimonio", query = "SELECT t FROM Testimonio t WHERE t.idTestimonio = :idTestimonio")
    , @NamedQuery(name = "Testimonio.findByDetalle", query = "SELECT t FROM Testimonio t WHERE t.detalle = :detalle")})
public class Testimonio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_testimonio", nullable = false)
    private Integer idTestimonio;
    @Basic(optional = false)
    @Column(name = "detalle", nullable = false, length = 400)
    private String detalle;
    @JoinColumn(name = "detallado", referencedColumnName = "detalle")
    @ManyToOne
    private ModoOperandi detallado;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Victima idUsuario;

    public Testimonio() {
    }

    public Testimonio(Integer idTestimonio) {
        this.idTestimonio = idTestimonio;
    }

    public Testimonio(Integer idTestimonio, String detalle) {
        this.idTestimonio = idTestimonio;
        this.detalle = detalle;
    }

    public Integer getIdTestimonio() {
        return idTestimonio;
    }

    public void setIdTestimonio(Integer idTestimonio) {
        this.idTestimonio = idTestimonio;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public ModoOperandi getDetallado() {
        return detallado;
    }

    public void setDetallado(ModoOperandi detallado) {
        this.detallado = detallado;
    }

    public Victima getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Victima idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTestimonio != null ? idTestimonio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Testimonio)) {
            return false;
        }
        Testimonio other = (Testimonio) object;
        if ((this.idTestimonio == null && other.idTestimonio != null) || (this.idTestimonio != null && !this.idTestimonio.equals(other.idTestimonio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.usbbog.is.proyecto.integrador.modelo.Testimonio[ idTestimonio=" + idTestimonio + " ]";
    }
    
}
