/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.is.proyecto.integrador.logicas;

import co.edu.usbbog.is.proyecto.integrador.controlador.ModoOperandiJpaController;
import co.edu.usbbog.is.proyecto.integrador.modelo.Testimonio;
import co.edu.usbbog.is.proyecto.integrador.controlador.TestimonioJpaController;
import co.edu.usbbog.is.proyecto.integrador.modelo.ModoOperandi;
import java.util.List;
import javax.persistence.Persistence;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author orion
 */
public class GestionTestimonio {
    
    
 public DefaultTableModel crearModelo() {
        try {
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("DETALLE DEL TESTIMONOO");
            tableModel.addColumn("MODO OPERANDI");
            System.out.println("1");
            tableModel = cargarInformacion(tableModel);
            return tableModel;
        } catch (Exception e) {
            System.out.println("1");
            return null;
        }
    }

    public DefaultTableModel cargarInformacion(DefaultTableModel tableModel) {
        try {
            
            TestimonioJpaController tjp = new TestimonioJpaController(Persistence.createEntityManagerFactory("PROYECTO_INTEGRADORPU"));
            //System.out.println("adssa");
            List<Testimonio> listP = tjp.findTestimonioEntities();
            //System.out.println(listP.size());
            for (int i = 0; i < listP.size(); i++) {
               // System.out.println(listP.get(i).getDetalle());
                //System.out.println(listP.get(i).toString());
              
                /*System.out.println(listP.get(i).getIdTestimonio());
                System.out.println(listP.get(i).getDetalle());
                System.out.println(listP.get(i).getIdUsuario());
                System.out.println(listP.get(i).getDetallado());
                */
                Object fila[] = new Object[4];
                fila[0]=listP.get(i).getDetalle();
                fila[1]=listP.get(i).getDetallado();
                
                
                tableModel.addRow(fila);
            }
            //System.out.println("2");
            return tableModel;
        } catch (Exception e) {
            return null;
        }
    }
    
  
    public boolean verificarDetalle(String detalle) {
        //try {
            boolean b = false;
            ModoOperandiJpaController rjc = new ModoOperandiJpaController(Persistence.createEntityManagerFactory("PROYECTO_INTEGRADORPU"));
            List<ModoOperandi> listr = rjc.findModoOperandiEntities();
            for (int i = 0; i < listr.size(); i++) {
                String valor = listr.get(i).getDetalle();

                if (valor == detalle) {
                    
                    return true;
                }
            }
            return false;
        /*} catch (HeadlessException e) {

            return false;
        }*/

    }
    public ModoOperandi modoPorDetalle(String detalle){
        ModoOperandiJpaController rjc = new ModoOperandiJpaController(Persistence.createEntityManagerFactory("PROYECTO_INTEGRADORPU"));
        return rjc.findModoOperandi(detalle);
    }
    
       
    
    
    
    
    
}
