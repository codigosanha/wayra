/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ClsDetalleVentaPendiente {

    private Connection connection = new ClsConexion().getConection();

    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarDetalleVenta(ClsEntidadDetalleVenta DetalleVenta) {
        try {
            CallableStatement statement = connection.prepareCall("{call SP_I_DetallePendiente(?,?,?,?,?,?,?,?)}");
            statement.setString("pidventa", DetalleVenta.getStrIdVenta());
            statement.setString("pidproducto", DetalleVenta.getStrIdProducto());
            statement.setString("pcantidad", DetalleVenta.getStrCantidadDet());
            statement.setString("pcosto", DetalleVenta.getStrCostoDet());
            statement.setString("pprecio", DetalleVenta.getStrPrecioDet());
            statement.setString("ptotal", DetalleVenta.getStrTotalDet());
            statement.setString("ptecnico", DetalleVenta.getStrTecnicoDet());
            statement.setString("pobs", DetalleVenta.getStrObsDet());
            statement.execute();

            //JOptionPane.showMessageDialog(null,"¡Venta Realizada con éxito!","Mensaje del Sistema",1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void modificarDetalleVenta(String codigo, ClsEntidadDetalleVenta DetalleVenta) {
        try {
            CallableStatement statement = connection.prepareCall("{call SP_U_DetalleVenta(?,?,?,?,?,?,?,?)}");
            statement.setString("pidventa", codigo);
            statement.setString("pidproducto", DetalleVenta.getStrIdProducto());
            statement.setString("pcantidad", DetalleVenta.getStrCantidadDet());
            statement.setString("pcosto", DetalleVenta.getStrCostoDet());
            statement.setString("pprecio", DetalleVenta.getStrPrecioDet());
            statement.setString("ptotal", DetalleVenta.getStrTotalDet());
            statement.setString("ptecnico", DetalleVenta.getStrTecnicoDet());
            statement.setString("pobs", DetalleVenta.getStrObsDet());
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //JOptionPane.showMessageDialog(null,"¡Venta Actualizada!","Mensaje del Sistema",1);
    }
    
    public void eliminarDetalleVenta(String idVenta){
        try {
            CallableStatement statement = connection.prepareCall("{call SP_D_EliminarDetalleVentaPendiente(?)}");
            statement.setString("pidventa", idVenta);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClsDetalleVentaPendiente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public ResultSet listarDetalleVentaPorParametro(String criterio, String busqueda) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_DetallePendientePorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }

}
