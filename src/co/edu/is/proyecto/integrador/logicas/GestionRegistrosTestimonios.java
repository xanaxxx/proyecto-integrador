/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.is.proyecto.integrador.logicas;

import co.edu.usbbog.is.proyecto.integrador.modelo.ModoOperandi;
import co.edu.usbbog.is.proyecto.integrador.modelo.Testimonio;
import co.edu.usbbog.is.proyecto.integrador.controlador.ModoOperandiJpaController;
import co.edu.usbbog.is.proyecto.integrador.controlador.TestimonioJpaController;
import java.util.List;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author orion
 */
public class GestionRegistrosTestimonios {
    public boolean registrarUsuario(Testimonio testimonio) {
        try {
            TestimonioJpaController tjp = new TestimonioJpaController(Persistence.createEntityManagerFactory("PROYECTO_INTEGRADORPU"));
       
            tjp.create(testimonio);
            return true;
        } catch (Exception e) {
            return false;
        }
        
    }
   
    
    
    
    
    
     public DefaultComboBoxModel crearModelo1Modo() {
        try {
            DefaultComboBoxModel combox = new DefaultComboBoxModel();
//            combox.addElement(combox);
            combox = cargarInformacioN(combox);
            System.out.println(""+combox);
            return combox;
        } catch (Exception e) {
            System.out.println("1");
            return null;
        }
    }
   


    
    

public DefaultComboBoxModel cargarInformacioN(DefaultComboBoxModel comboBox){
    
    try {
   ModoOperandiJpaController ujc = new ModoOperandiJpaController(Persistence.createEntityManagerFactory("PROYECTO_INTEGRADORPU"));
    
    List<ModoOperandi> listP = ujc.findModoOperandiEntities();
            //System.out.println(listP.size());
            for (int i = 0; i < listP.size(); i++) {
                
                Object fila[] = new Object[1];
                System.out.println("HEY MIREN AQUI"+fila[0]);

                fila[0]=listP.get(i).getDetalle();
                System.out.println("HEY MIREN AQUI"+fila[0]);
                comboBox.addElement(fila[0]);
                
            }
           return  comboBox;
    } catch (Exception e) {
        System.out.println(""+e);
    return null;
    }
    
    
}   
    
    
    
}
