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
import javax.swing.JOptionPane;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;

public class ClsCliente1 {
private Connection connection=new ClsConexion().getConection();
    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarCliente(ClsEntidadCliente1 Cliente1){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_I_Cliente1(?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pnombre",Cliente1.getStrNombreCliente1());
            statement.setString("pespecialidad",Cliente1.getStrEspecialidadCliente1());
            statement.setString("pruc",Cliente1.getStrRucCliente1());
            statement.setString("pdni",Cliente1.getStrDniCliente1());
            statement.setString("pdireccion",Cliente1.getStrDireccionCliente1());
            statement.setString("ptelefono",Cliente1.getStrTelefonoCliente1());
            statement.setString("pfecnacimiento",Cliente1.getStrFecNacimientoCliente1());//-----
            statement.setString("pfecinitrabajo",Cliente1.getStrFecIniTrabajoCliente1());//------
            statement.setString("pfecfintrabajo",Cliente1.getStrFecFinTrabajoCliente1());//------
            statement.setString("pobsv",Cliente1.getStrObsvCliente1());
            statement.execute();

            JOptionPane.showMessageDialog(null,"¡Tecnico Agregado con éxito!","Mensaje del Sistema",1);           

        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }    
    public void modificarCliente(String codigo,ClsEntidadCliente1 Cliente1){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_U_Cliente1(?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidcliente",codigo);
           /* statement.setString("pnombre",Cliente1.getStrNombreCliente1());
            statement.setString("pruc",Cliente1.getStrRucCliente1());
            statement.setString("pdni",Cliente1.getStrDniCliente1());
            statement.setString("pdireccion",Cliente1.getStrDireccionCliente1());
            statement.setString("ptelefono",Cliente1.getStrTelefonoCliente1());
            statement.setString("pobsv",Cliente1.getStrObsvCliente1());*/
            statement.setString("pnombre",Cliente1.getStrNombreCliente1());
            statement.setString("pespecialidad",Cliente1.getStrEspecialidadCliente1());
            statement.setString("pruc",Cliente1.getStrRucCliente1());
            statement.setString("pdni",Cliente1.getStrDniCliente1());
            statement.setString("pdireccion",Cliente1.getStrDireccionCliente1());
            statement.setString("ptelefono",Cliente1.getStrTelefonoCliente1());
            statement.setString("pfecnacimiento",Cliente1.getStrFecNacimientoCliente1());//-----
            statement.setString("pfecinitrabajo",Cliente1.getStrFecIniTrabajoCliente1());//------
            statement.setString("pfecfintrabajo",Cliente1.getStrFecFinTrabajoCliente1());//------
            statement.setString("pobsv",Cliente1.getStrObsvCliente1());
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"¡Datos Actualizados!!","Mensaje del Sistema",1);
    }
    public ArrayList<ClsEntidadCliente1> listarCliente1(){
        ArrayList<ClsEntidadCliente1> clienteusuarios1=new ArrayList<ClsEntidadCliente1>();
        try{
            CallableStatement statement=connection.prepareCall("{call SP_S_Cliente1}");
            ResultSet resultSet=statement.executeQuery();
            
            while (resultSet.next()){
                ClsEntidadCliente1 cliente1=new ClsEntidadCliente1();
                cliente1.setStrIdCliente1(resultSet.getString("IdCliente"));
                cliente1.setStrNombreCliente1(resultSet.getString("nombre"));
                cliente1.setStrEspecialidadCliente1(resultSet.getString("especialidad"));//------
                cliente1.setStrRucCliente1(resultSet.getString("ruc"));
                cliente1.setStrDniCliente1(resultSet.getString("dni"));
                cliente1.setStrDireccionCliente1(resultSet.getString("direccion"));
                cliente1.setStrTelefonoCliente1(resultSet.getString("telefono"));
                cliente1.setStrFecNacimientoCliente1(resultSet.getString("fecnacimiento"));//--------
                cliente1.setStrFecIniTrabajoCliente1(resultSet.getString("fecinitrabajo"));//-----
                cliente1.setStrFecFinTrabajoCliente1(resultSet.getString("fecfintrabajo"));//----
                cliente1.setStrObsvCliente1(resultSet.getString("obsv"));
                clienteusuarios1.add(cliente1);
            }
            return clienteusuarios1;
         }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }      
    public ResultSet listarClientePorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_Cliente1PorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
}
