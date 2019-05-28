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
import java.awt.EventQueue;
public class ClsCliente {
private Connection connection=new ClsConexion().getConection();
    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarCliente(ClsEntidadCliente Cliente){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_I_Cliente(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pnombre",Cliente.getStrNombreCliente());
            statement.setString("pruc",Cliente.getStrRucCliente());
            statement.setString("pdni",Cliente.getStrDniCliente());
            statement.setString("pdireccion",Cliente.getStrDireccionCliente());
            statement.setString("ptelefono",Cliente.getStrTelefonoCliente());
            statement.setString("pkilometraje",Cliente.getStrKilometrajeCliente());
            statement.setString("paño",Cliente.getStrAñoCliente());
            statement.setString("pnombre1",Cliente.getStrNombre1Cliente());
            statement.setString("pdni1",Cliente.getStrDni1Cliente());
            statement.setString("ptelefono1",Cliente.getStrTelefono1Cliente());
            statement.setString("pdireccion1",Cliente.getStrDireccion1Cliente());
            statement.setString("pcumpleaños",Cliente.getStrCumpleañosCliente());
            statement.setString("pobsv",Cliente.getStrObsvCliente());
            statement.execute();
JOptionPane.showMessageDialog(null,"¡Vehiculo Agregado con éxito!","Mensaje del Sistema",1);           

        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }    
    public void modificarCliente(String codigo,ClsEntidadCliente Cliente){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_U_Cliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidcliente",codigo);
            statement.setString("pnombre",Cliente.getStrNombreCliente());
            statement.setString("pruc",Cliente.getStrRucCliente());
            statement.setString("pdni",Cliente.getStrDniCliente());
            statement.setString("pdireccion",Cliente.getStrDireccionCliente());
            statement.setString("ptelefono",Cliente.getStrTelefonoCliente());
            statement.setString("pkilometraje",Cliente.getStrKilometrajeCliente());
            statement.setString("paño",Cliente.getStrAñoCliente());
            statement.setString("pnombre1",Cliente.getStrNombre1Cliente());
            statement.setString("pdni1",Cliente.getStrDni1Cliente());
            statement.setString("ptelefono1",Cliente.getStrTelefono1Cliente());
            statement.setString("pdireccion1",Cliente.getStrDireccion1Cliente());
            statement.setString("pcumpleaños",Cliente.getStrCumpleañosCliente());
            statement.setString("pobsv",Cliente.getStrObsvCliente());
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"¡Vehiculo Actualizado!","Mensaje del Sistema",1);
    }
    public ArrayList<ClsEntidadCliente> listarCliente(){
        ArrayList<ClsEntidadCliente> clienteusuarios=new ArrayList<ClsEntidadCliente>();
        try{
            CallableStatement statement=connection.prepareCall("{call SP_S_Cliente}");
            ResultSet resultSet=statement.executeQuery();
            
            while (resultSet.next()){
                ClsEntidadCliente cliente=new ClsEntidadCliente();
                cliente.setStrIdCliente(resultSet.getString("IdCliente"));
                cliente.setStrNombreCliente(resultSet.getString("nombre"));
                cliente.setStrRucCliente(resultSet.getString("ruc"));
                cliente.setStrDniCliente(resultSet.getString("dni"));
                cliente.setStrDireccionCliente(resultSet.getString("direccion"));
                cliente.setStrTelefonoCliente(resultSet.getString("telefono"));
                cliente.setStrKilometrajeCliente(resultSet.getString("kilometraje"));
                cliente.setStrAñoCliente(resultSet.getString("año"));
                cliente.setStrNombre1Cliente(resultSet.getString("nombre1"));
                cliente.setStrDni1Cliente(resultSet.getString("dni1"));
                cliente.setStrTelefono1Cliente(resultSet.getString("telefono1"));
                cliente.setStrDireccion1Cliente(resultSet.getString("direccion1"));
                cliente.setStrCumpleañosCliente(resultSet.getString("cumpleaños"));
                cliente.setStrObsvCliente(resultSet.getString("obsv"));
                clienteusuarios.add(cliente);
            }
            return clienteusuarios;
         }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }      
    public ResultSet listarClientePorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ClientePorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
}
