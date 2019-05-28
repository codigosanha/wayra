/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Conexion.ClsConexion;
import Entidad.*;
import Negocio.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FrmCliente extends javax.swing.JInternalFrame {
    private Connection connection=new ClsConexion().getConection();
    String Total;
    String strCodigo;
    String accion;
    int registros;
    String id[]=new String[50];
    static int intContador;
    
    //-----------------------------------------------
    public String codigo;
    static Connection conn=null;
    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    String criterio,busqueda;
    
    public FrmCliente() {
        initComponents();
        
        tabCliente.setIconAt(tabCliente.indexOfComponent(pBuscar), new ImageIcon("src/iconos/busca_p1.png"));
        tabCliente.setIconAt(tabCliente.indexOfComponent(pNuevo), new ImageIcon("src/iconos/nuevo1.png"));
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnRuc);
        buttonGroup1.add(rbtnDni);
        
        mirar();
        actualizarTabla();
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(707, 426);
        CrearTabla();
        CantidadTotal();
        
    }

//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------
  void CrearTabla(){
   //--------------------PRESENTACION DE JTABLE----------------------
      
        TableCellRenderer render = new DefaultTableCellRenderer() { 

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                //Determinar Alineaciones   
                    if(column==0 || column==2 || column==3 || column==5){
                        l.setHorizontalAlignment(SwingConstants.CENTER); 
                    }else{
                        l.setHorizontalAlignment(SwingConstants.LEFT);
                    }

                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(new Color(203, 159, 41));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setForeground(Color.WHITE); 
                }else{
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        //l.setBackground(new Color(232, 232, 232));
                        l.setBackground(new Color(254, 227, 152));
                    }
                }         
                return l; 
            } 
        }; 
        
        //Agregar Render
        for (int i=0;i<tblCliente.getColumnCount();i++){
            tblCliente.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblCliente.setAutoResizeMode(tblCliente.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50,80,80,80,80,80,80,40,150,80,90,90,80,200};
        for(int i = 0; i < tblCliente.getColumnCount(); i++) {
            tblCliente.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
   void CantidadTotal(){
       Total= String.valueOf(tblCliente.getRowCount());   
       lblEstado.setText("Se cargaron " + Total + " registros");      
   }
   void limpiarCampos(){
       txtCodigo.setText("");
       txtNombre.setText("");
       txtRuc.setText("");
       txtDni.setText("");
       txtDireccion.setText("");
       txtTelefono.setText("");
       txtKilometraje.setText("");
         txtAño.setText("");
           txtNombre1.setText("");
             txtDni1.setText("");
               txtTelefono1.setText("");
                 txtDireccion1.setText("");
                   txtCumpleaños.setText("");
       txtObservacion.setText("");
       
       rbtnCodigo.setSelected(false);
       rbtnNombre.setSelected(false);
       rbtnRuc.setSelected(false);
       rbtnDni.setSelected(false);
       txtBusqueda.setText("");
   }
       
   void mirar(){
       tblCliente.setEnabled(true);
       btnNuevo.setEnabled(true);
       btnModificar.setEnabled(true);
       btnGuardar.setEnabled(false);
       btnCancelar.setEnabled(false);
       btnSalir.setEnabled(true);
        
       txtNombre.setEnabled(false);
       txtRuc.setEnabled(false);
       txtDni.setEnabled(false);
       txtDireccion.setEnabled(false);
       txtTelefono.setEnabled(false);
        txtKilometraje.setEnabled(false);
        txtAño.setEnabled(false);
        txtNombre1.setEnabled(false);
        txtDni1.setEnabled(false);
        txtTelefono1.setEnabled(false);
        txtDireccion1.setEnabled(false);
        txtCumpleaños.setEnabled(false);
       txtObservacion.setEnabled(false); 
   
   }
   
   void modificar(){
       tblCliente.setEnabled(false);
       btnNuevo.setEnabled(false);
       btnModificar.setEnabled(false);
       btnGuardar.setEnabled(true);
       btnCancelar.setEnabled(true);
       btnSalir.setEnabled(false);
        
       txtNombre.setEnabled(true);
       txtRuc.setEnabled(true);
       txtDni.setEnabled(true);
       txtDireccion.setEnabled(true);
       txtTelefono.setEnabled(true);
       txtKilometraje.setEnabled(true);
       txtAño.setEnabled(true);
       txtNombre1.setEnabled(true);
       txtDni1.setEnabled(true);
       txtTelefono1.setEnabled(true);
       txtDireccion1.setEnabled(true);
       txtCumpleaños.setEnabled(true);
       txtObservacion.setEnabled(true); 
       txtNombre.requestFocus();
   }
   
   
    void actualizarTabla(){
       String titulos[]={"ID","PLACA","MARCA","MODELO","COLOR","MOTOR","KILOMETRAJE","AÑO","NOMBRE DEL PROPIETARIO","DNI","TELEFONO","DIRECCION","FECHA DE CUMPLEAÑOS","Observación"};
              
       ClsCliente clientes=new ClsCliente();
       ArrayList<ClsEntidadCliente> cliente=clientes.listarCliente();
       Iterator iterator=cliente.iterator();
       DefaultTableModel defaultTableModel=new DefaultTableModel(null,titulos);
       
       String fila[]=new String[14];
       while(iterator.hasNext()){
           ClsEntidadCliente Cliente=new ClsEntidadCliente();
           Cliente=(ClsEntidadCliente) iterator.next();
           fila[0]=Cliente.getStrIdCliente();
           fila[1]=Cliente.getStrNombreCliente();       
           fila[2]=Cliente.getStrRucCliente();
           fila[3]=Cliente.getStrDniCliente();
           fila[4]=Cliente.getStrDireccionCliente();
           fila[5]=Cliente.getStrTelefonoCliente();
           fila[6]=Cliente.getStrKilometrajeCliente();
           fila[7]=Cliente.getStrAñoCliente();
           fila[8]=Cliente.getStrNombre1Cliente();
           fila[9]=Cliente.getStrDni1Cliente();
           fila[10]=Cliente.getStrTelefono1Cliente();
           fila[11]=Cliente.getStrDireccion1Cliente();
           fila[12]=Cliente.getStrCumpleañosCliente();
           fila[13]=Cliente.getStrObsvCliente();
           defaultTableModel.addRow(fila);               
       }
       tblCliente.setModel(defaultTableModel);
   }
   void BuscarCliente(){
        String titulos[]={"ID","PLACA","MARCA","MODELO","COLOR","MOTOR","KILOMETRAJE","AÑO","NOMBRE DEL PROPIETARIO","DNI","TELEFONO","DIRECCION","FECHA DE CUMPLEAÑOS","Observación"};
        dtm.setColumnIdentifiers(titulos);
        
        ClsCliente categoria=new ClsCliente();
        busqueda=txtBusqueda.getText();
        if(rbtnCodigo.isSelected()){
            criterio="id";
        }else if(rbtnNombre.isSelected()){
            criterio="nombre";
        }else if(rbtnRuc.isSelected()){
            criterio="nombre1";
        }else if(rbtnDni.isSelected()){
            criterio="dni1";
        }
        try{
            rs=categoria.listarClientePorParametro(criterio,busqueda);
            boolean encuentra=false;
            String Datos[]=new String[14];
            int f,i;
            f=dtm.getRowCount();
            if(f>0){
                for(i=0;i<f;i++){
                    dtm.removeRow(0);
                }
            }
            while(rs.next()){
                Datos[0]=(String) rs.getString(1);
                Datos[1]=(String) rs.getString(2);
                Datos[2]=(String) rs.getString(3);
                Datos[3]=(String) rs.getString(4);
                Datos[4]=(String) rs.getString(5);
                Datos[5]=(String) rs.getString(6);
                Datos[6]=(String) rs.getString(7);
                Datos[7]=(String) rs.getString(8);
                Datos[8]=(String) rs.getString(9);
                Datos[9]=(String) rs.getString(10);
                Datos[10]=(String) rs.getString(11);
                Datos[11]=(String) rs.getString(12);
                Datos[12]=(String) rs.getString(13);
                Datos[13]=(String) rs.getString(14);
                
                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblCliente.setModel(dtm);
    }
    void listardatos(){
        String estado;
        DefaultTableModel defaultTableModel=new DefaultTableModel();
        if(registros==-1){
            JOptionPane.showMessageDialog(null,"Se debe seleccionar un registro");
        }else{
            defaultTableModel=(DefaultTableModel) tblCliente.getModel();
            strCodigo=((String) defaultTableModel.getValueAt(registros,0));
            txtCodigo.setText((String)defaultTableModel.getValueAt(registros,0));
            txtNombre.setText((String)defaultTableModel.getValueAt(registros,1));
            txtRuc.setText((String)defaultTableModel.getValueAt(registros,2));
            txtDni.setText((String)defaultTableModel.getValueAt(registros,3));
            txtDireccion.setText((String)defaultTableModel.getValueAt(registros,4));
            txtTelefono.setText((String)defaultTableModel.getValueAt(registros,5));
            txtKilometraje.setText((String)defaultTableModel.getValueAt(registros,6));
            txtAño.setText((String)defaultTableModel.getValueAt(registros,7));
            txtNombre1.setText((String)defaultTableModel.getValueAt(registros,8));
            txtDni1.setText((String)defaultTableModel.getValueAt(registros,9));
            txtTelefono1.setText((String)defaultTableModel.getValueAt(registros,10));
            txtDireccion1.setText((String)defaultTableModel.getValueAt(registros,11));
            txtCumpleaños.setText((String)defaultTableModel.getValueAt(registros,12));
            txtObservacion.setText((String)defaultTableModel.getValueAt(registros,13));
            tblCliente.setRowSelectionInterval(registros,registros);
        }
    
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tabCliente = new javax.swing.JTabbedPane();
        pBuscar = new javax.swing.JPanel();
        lblEstado = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        rbtnDni = new javax.swing.JRadioButton();
        rbtnRuc = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        pNuevo = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtKilometraje = new javax.swing.JTextField();
        txtAño = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtNombre1 = new javax.swing.JTextField();
        txtDni1 = new javax.swing.JTextField();
        txtTelefono1 = new javax.swing.JTextField();
        txtDireccion1 = new javax.swing.JTextField();
        txtCumpleaños = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Vehiculo");
        getContentPane().setLayout(null);

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setIconTextGap(0);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar);
        btnModificar.setBounds(595, 170, 81, 70);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setIconTextGap(0);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar);
        btnCancelar.setBounds(595, 240, 81, 70);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIconTextGap(0);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo);
        btnNuevo.setBounds(595, 30, 81, 70);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/principal.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setIconTextGap(0);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir);
        btnSalir.setBounds(595, 310, 81, 70);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar);
        btnGuardar.setBounds(595, 100, 81, 70);

        jLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mantenimiento"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(585, 10, 100, 380);

        pBuscar.setBackground(new java.awt.Color(255, 255, 255));
        pBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pBuscar.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 200, 20));

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        pBuscar.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 360, -1));

        rbtnCodigo.setText("ID Cliente");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        pBuscar.add(rbtnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 80, -1));

        rbtnNombre.setForeground(new java.awt.Color(255, 255, 255));
        rbtnNombre.setText("N° PLACA");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        rbtnNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnNombreActionPerformed(evt);
            }
        });
        pBuscar.add(rbtnNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 100, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/report.png"))); // NOI18N
        jButton3.setText("Reporte");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        pBuscar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 28, 120, 50));

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCliente.setRowHeight(22);
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliente);

        pBuscar.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 540, 230));

        rbtnDni.setForeground(new java.awt.Color(255, 255, 255));
        rbtnDni.setText("D.N.I.");
        rbtnDni.setOpaque(false);
        pBuscar.add(rbtnDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, 60, -1));

        rbtnRuc.setForeground(new java.awt.Color(255, 255, 255));
        rbtnRuc.setText("NOMBRE CLIENTE");
        rbtnRuc.setOpaque(false);
        pBuscar.add(rbtnRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 130, -1));

        jLabel10.setBackground(new java.awt.Color(255, 51, 51));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterios de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jLabel10.setOpaque(true);
        pBuscar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, 80));

        tabCliente.addTab("Buscar", pBuscar);

        pNuevo.setBackground(new java.awt.Color(255, 51, 51));
        pNuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS DEL VEHICULO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pNuevo.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 570, 310));

        txtCodigo.setEnabled(false);
        pNuevo.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 110, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID.VEHICULO:");
        pNuevo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 90, 20));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("N° PLACA:");
        pNuevo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 120, 20));

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        pNuevo.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 110, -1));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("MARCA:");
        pNuevo.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 50, 20));

        txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRucKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucKeyTyped(evt);
            }
        });
        pNuevo.add(txtRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 80, -1));

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
        });
        pNuevo.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 160, -1));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("COLOR:");
        pNuevo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 60, 20));

        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyReleased(evt);
            }
        });
        pNuevo.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 160, -1));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MODELO:");
        pNuevo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 60, 20));

        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniKeyTyped(evt);
            }
        });
        pNuevo.add(txtDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 160, -1));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("OBSERVACION:");
        pNuevo.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 80, 20));

        txtObservacion.setColumns(20);
        txtObservacion.setRows(5);
        txtObservacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtObservacionKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txtObservacion);

        pNuevo.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 330, 50));

        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Los campos marcado con un asterísco (*) son obligatorios");
        pNuevo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 280, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("*");
        pNuevo.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 20, 20));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("N° MOTOR:");
        pNuevo.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("KILOMETRAJE:");
        pNuevo.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("AÑO:");
        pNuevo.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, -1));

        txtKilometraje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKilometrajeKeyReleased(evt);
            }
        });
        pNuevo.add(txtKilometraje, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 140, -1));

        txtAño.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAñoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAñoKeyTyped(evt);
            }
        });
        pNuevo.add(txtAño, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 140, -1));

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("NOMBRE DEL PROPIETARIO:");
        pNuevo.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, -1, 20));

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("DNI:");
        pNuevo.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, -1, -1));

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("TELEFONO:");
        pNuevo.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, -1, -1));

        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("DIRECCION:");
        pNuevo.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, -1, -1));

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("FEC.CUMPLEAÑOS:");
        pNuevo.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, -1, -1));

        txtNombre1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombre1KeyReleased(evt);
            }
        });
        pNuevo.add(txtNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 230, -1));

        txtDni1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDni1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDni1KeyTyped(evt);
            }
        });
        pNuevo.add(txtDni1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 230, -1));

        txtTelefono1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefono1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefono1KeyTyped(evt);
            }
        });
        pNuevo.add(txtTelefono1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 170, 230, -1));

        txtDireccion1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccion1KeyReleased(evt);
            }
        });
        pNuevo.add(txtDireccion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 230, -1));

        txtCumpleaños.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCumpleañosKeyReleased(evt);
            }
        });
        pNuevo.add(txtCumpleaños, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 230, -1));

        tabCliente.addTab("Nuevo / Modificar", pNuevo);

        getContentPane().add(tabCliente);
        tabCliente.setBounds(10, 10, 570, 380);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        int fila;
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        fila = tblCliente.getSelectedRow();

        if (fila == -1){
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
        }else{
            defaultTableModel = (DefaultTableModel)tblCliente.getModel();
            strCodigo =  ((String) defaultTableModel.getValueAt(fila, 0));
            txtCodigo.setText((String) defaultTableModel.getValueAt(fila, 0));
            txtNombre.setText((String) defaultTableModel.getValueAt(fila, 1));
            txtRuc.setText((String)defaultTableModel.getValueAt(fila,2));
            txtDni.setText((String)defaultTableModel.getValueAt(fila,3));
            txtDireccion.setText((String)defaultTableModel.getValueAt(fila,4));
            txtTelefono.setText((String)defaultTableModel.getValueAt(fila,5));
            txtKilometraje.setText((String)defaultTableModel.getValueAt(fila,6));
            txtAño.setText((String)defaultTableModel.getValueAt(fila,7));
            txtNombre1.setText((String)defaultTableModel.getValueAt(fila,8));
            txtDni1.setText((String)defaultTableModel.getValueAt(fila,9));
            txtTelefono1.setText((String)defaultTableModel.getValueAt(fila,10));
            txtDireccion1.setText((String)defaultTableModel.getValueAt(fila,11));
            txtCumpleaños.setText((String)defaultTableModel.getValueAt(fila,12));
            txtObservacion.setText((String)defaultTableModel.getValueAt(fila,13));
        }
        mirar();
    }//GEN-LAST:event_tblClienteMouseClicked

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
    txtNombre.setBackground(Color.WHITE);
                
    //char car = evt.getKeyChar();
    //if((car<'a' || car>'z') && (car<'A' || car>'Z')) evt.consume();
    }//GEN-LAST:event_txtNombreKeyTyped

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
    if(tblCliente.getSelectedRows().length > 0 ) { 
        accion="Modificar";
        modificar();
        tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pNuevo));
    }else{
        JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro!");
    }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pBuscar));

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion="Nuevo";
        modificar();
        limpiarCampos();
        tblCliente.setEnabled(false);
        tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pNuevo));       
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
//----------------------VALIDACIÓN DE DATOS-------------------------------------
    public boolean validardatos(){
        if (txtNombre.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Ingrese el numero de la placa del vehiculo");
            txtNombre.requestFocus();
            txtNombre.setBackground(Color.YELLOW);
            return false;

        }else{
            return true;
        }

    }
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    if(validardatos()==true){  
        if(accion.equals("Nuevo")){
            ClsCliente clientes=new ClsCliente();
            ClsEntidadCliente cliente=new ClsEntidadCliente();
            cliente.setStrNombreCliente(txtNombre.getText());
            cliente.setStrRucCliente(txtRuc.getText());
            cliente.setStrDniCliente(txtDni.getText());
            cliente.setStrDireccionCliente(txtDireccion.getText());
            cliente.setStrTelefonoCliente(txtTelefono.getText());
            cliente.setStrKilometrajeCliente(txtKilometraje.getText());
            cliente.setStrAñoCliente(txtAño.getText());
            cliente.setStrNombre1Cliente(txtNombre1.getText());
            cliente.setStrDni1Cliente(txtDni1.getText());
            cliente.setStrTelefono1Cliente(txtTelefono1.getText());
            cliente.setStrDireccion1Cliente(txtDireccion1.getText());
            cliente.setStrCumpleañosCliente(txtCumpleaños.getText());
            cliente.setStrObsvCliente(txtObservacion.getText());
            clientes.agregarCliente(cliente);
            actualizarTabla();
            CantidadTotal();
        }
        if(accion.equals("Modificar")){
            ClsCliente clientes=new ClsCliente();
            ClsEntidadCliente cliente=new ClsEntidadCliente();
            cliente.setStrNombreCliente(txtNombre.getText());
            cliente.setStrRucCliente(txtRuc.getText());
            cliente.setStrDniCliente(txtDni.getText());
            cliente.setStrDireccionCliente(txtDireccion.getText());
            cliente.setStrTelefonoCliente(txtTelefono.getText());
            cliente.setStrKilometrajeCliente(txtKilometraje.getText());
            cliente.setStrAñoCliente(txtAño.getText());
            cliente.setStrNombre1Cliente(txtNombre1.getText());
            cliente.setStrDni1Cliente(txtDni1.getText());
            cliente.setStrTelefono1Cliente(txtTelefono1.getText());
            cliente.setStrDireccion1Cliente(txtDireccion1.getText());
            cliente.setStrCumpleañosCliente(txtCumpleaños.getText());
            cliente.setStrObsvCliente(txtObservacion.getText());
            clientes.modificarCliente(strCodigo, cliente);
            actualizarTabla();
            limpiarCampos();
            modificar();
            CantidadTotal();
        }
        CrearTabla();
        mirar();
        tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pBuscar)); 
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarCliente();
        CrearTabla();
        CantidadTotal();
        //----------SELECCIONA LA PRIMERA FILA DE LA TABLA-----------------
        //tblCliente.changeSelection(0,0,false,true);
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        String cadena= (txtNombre.getText()).toUpperCase();
        txtNombre.setText(cadena);
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtRuc.requestFocus();
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucKeyTyped
      //restricion de letras ycantidad de dijitos 
        /* char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtRuc.getText().length();
        if(txtRuc.getText().trim().length()<11){

        }else{
            i=10;
            String com=txtRuc.getText().substring(0, 10);
            txtRuc.setText("");
            txtRuc.setText(com);
        }*/
    }//GEN-LAST:event_txtRucKeyTyped

    private void txtDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyTyped
       //restriccion de3 ltras y cantidad de dijitos
       /*char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtDni.getText().length();
        if(txtDni.getText().trim().length()<8){

        }else{
            i=10;
            String com=txtDni.getText().substring(0, 10);
            txtDni.setText("");
            txtDni.setText(com);
        }*/
    }//GEN-LAST:event_txtDniKeyTyped

    private void txtRucKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtDni.requestFocus();
    }//GEN-LAST:event_txtRucKeyReleased

    private void txtDniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtDireccion.requestFocus();        
    }//GEN-LAST:event_txtDniKeyReleased

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtTelefono.requestFocus();        
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtKilometraje.requestFocus();       
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Map p=new HashMap();
        p.put("busqueda",txtBusqueda.getText());
        if(rbtnCodigo.isSelected()){
            p.put("criterio", "id");
        }
        else if(rbtnNombre.isSelected()){
            p.put("criterio", "nombre");
        }else if(rbtnRuc.isSelected()){
            p.put("criterio", "nombre1");
        }else if(rbtnDni.isSelected()){
            p.put("criterio", "dni1");
        }else{
            p.put("criterio", "");
        }
        JasperReport report;
        JasperPrint print;
 
        
        try{


            report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCliente.jrxml");
            print=JasperFillManager.fillReport(report, p,connection);
            
//            InputStream in = getClass().getClassLoader().getResourceAsStream("/src/Reportes/RptCliente.jasper");
//            JasperReport report1 = (JasperReport) JRLoader.loadObject(in);
//            JasperPrint  print1=JasperFillManager.fillReport(report1, p,connection);
//            JasperViewer view1=new JasperViewer(print1,false);
            
            JasperViewer view=new JasperViewer(print,false);
            view.setTitle("Reporte de Clientes");
            view.setVisible(true);
        }catch(JRException e){
            e.printStackTrace();
        
        
    
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void rbtnNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnNombreActionPerformed

    private void txtKilometrajeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrajeKeyReleased
        // TODO add your handling code here:
          int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtAño.requestFocus();  
    }//GEN-LAST:event_txtKilometrajeKeyReleased

    private void txtAñoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAñoKeyReleased
        // TODO add your handling code here:
          int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtNombre1.requestFocus();  
    }//GEN-LAST:event_txtAñoKeyReleased

    private void txtDni1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDni1KeyReleased
        // TODO add your handling code here:
          int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtTelefono1.requestFocus();  
    }//GEN-LAST:event_txtDni1KeyReleased

    private void txtTelefono1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefono1KeyReleased
        // TODO add your handling code here:
          int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtDireccion1.requestFocus();  
    }//GEN-LAST:event_txtTelefono1KeyReleased

    private void txtDireccion1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccion1KeyReleased
        // TODO add your handling code here:
          int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtCumpleaños.requestFocus();  
    }//GEN-LAST:event_txtDireccion1KeyReleased

    private void txtCumpleañosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCumpleañosKeyReleased
        // TODO add your handling code here:
          int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtObservacion.requestFocus();  
    }//GEN-LAST:event_txtCumpleañosKeyReleased

    private void txtObservacionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionKeyReleased
  int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) btnGuardar.requestFocus();          // TODO add your handling code here:
    }//GEN-LAST:event_txtObservacionKeyReleased

    private void txtDni1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDni1KeyTyped
        // TODO add your handling code here:
  char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtDni1.getText().length();
        if(txtDni1.getText().trim().length()<14){

        }else{
            i=10;
            String com=txtDni1.getText().substring(0, 10);
            txtDni1.setText("");
            txtDni1.setText(com);
        }
    }//GEN-LAST:event_txtDni1KeyTyped

    private void txtTelefono1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefono1KeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtTelefono1.getText().length();
        if(txtTelefono1.getText().trim().length()<14){

        }else{
            i=10;
            String com=txtTelefono1.getText().substring(0, 10);
            txtTelefono1.setText("");
            txtTelefono1.setText(com);
        }
    }//GEN-LAST:event_txtTelefono1KeyTyped

    private void txtAñoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAñoKeyTyped
char car = evt.getKeyChar();
        if((car<'0' || car>'7')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtAño.getText().length();
        if(txtAño.getText().trim().length()<4){

        }else{
            i=10;
            String com=txtAño.getText().substring(0, 10);
            txtAño.setText("");
            txtAño.setText(com);}        // TODO add your handling code here:
    }//GEN-LAST:event_txtAñoKeyTyped

    private void txtNombre1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombre1KeyReleased
        // TODO add your handling code here:
         int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER)txtDni1.requestFocus(); 
    }//GEN-LAST:event_txtNombre1KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JPanel pBuscar;
    private javax.swing.JPanel pNuevo;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDni;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JRadioButton rbtnRuc;
    private javax.swing.JTabbedPane tabCliente;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtAño;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCumpleaños;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDireccion1;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtDni1;
    private javax.swing.JTextField txtKilometraje;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombre1;
    private javax.swing.JTextArea txtObservacion;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTelefono1;
    // End of variables declaration//GEN-END:variables
}
