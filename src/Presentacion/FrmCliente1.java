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
//import org.eclipse.persistence.internal.core.helper.CoreClassConstants;

public class FrmCliente1 extends javax.swing.JInternalFrame {
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
    
    public FrmCliente1() {
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
                //aqui obtengo el render de la clase superior 
                JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                //Determinar Alineaciones   
                    if(column==0 || column==2 || column==3 || column==5 || column==6){//agregado || column==6
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
        int[] anchos = {50,200,80,80,80,150,80,80,80,80,200};//{50,200,80,80,150,80,200} original ,,,agregados 4x80     
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
       txtEspecialidad.setText("");
       txtRuc.setText("");
       txtDni.setText("");
       txtDireccion.setText("");
       txtTelefono1.setText("");
       txtFecNac.setText("");
       txtInicTrab.setText("");
       txtFinTrab.setText("");
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
       txtEspecialidad.setEnabled(false);
       txtRuc.setEnabled(false);
       txtDni.setEnabled(false);
       txtDireccion.setEnabled(false);
       txtTelefono1.setEnabled(false);
       txtFecNac.setEnabled(false);
       txtInicTrab.setEnabled(false);
       txtFinTrab.setEnabled(false);
       txtObservacion.setEnabled(false); 
   
   }
   
   void modificar(){
       tblCliente.setEnabled(false);
       btnNuevo.setEnabled(false);
       btnModificar.setEnabled(false);
       btnGuardar.setEnabled(true);
       btnCancelar.setEnabled(true);
       btnSalir.setEnabled(false);
        
      /* txtNombre.setEnabled(true);
       txtRuc.setEnabled(true);
       txtDni.setEnabled(true);
       txtDireccion.setEnabled(true);
       txtTelefono1.setEnabled(true);
       txtObservacion.setEnabled(true); 
       txtNombre.requestFocus();*/
      
       txtNombre.setEnabled(true);
       txtEspecialidad.setEnabled(true);
       txtRuc.setEnabled(true);
       txtDni.setEnabled(true);
       txtDireccion.setEnabled(true);
       txtTelefono1.setEnabled(true);
       txtFecNac.setEnabled(true);
       txtInicTrab.setEnabled(true);
       txtFinTrab.setEnabled(true);
       txtObservacion.setEnabled(true);
       txtNombre.requestFocus();
   }
   
   
    void actualizarTabla(){
       //String titulos[]={"ID","Nombre o Razón Social","RUC","DNI","Dirección","Teléfono","Observación"};
       String titulos[]={"ID","Nombre / Apellido","Especialidad","RUC","DNI","Dirección","Teléfono","Fec.Nac","Inic.Trab","Fin.Trab","Observación"};
        ClsCliente1 clientes1=new ClsCliente1();
       //*ClsCliente clientes=new ClsCliente();
       //*ArrayList<ClsEntidadCliente> cliente=clientes.listarCliente();
        ArrayList<ClsEntidadCliente1> cliente1=clientes1.listarCliente1();///llama al metodo listarcliente en la tabla
       Iterator iterator=cliente1.iterator();
       DefaultTableModel defaultTableModel=new DefaultTableModel(null,titulos);
       
       String fila[]=new String[11];
       while(iterator.hasNext()){
           ClsEntidadCliente1 Cliente1=new ClsEntidadCliente1();
           Cliente1=(ClsEntidadCliente1) iterator.next();
           fila[0]=Cliente1.getStrIdCliente1();
           fila[1]=Cliente1.getStrNombreCliente1();
           fila[2]=Cliente1.getStrEspecialidadCliente1();///----------
           fila[3]=Cliente1.getStrRucCliente1();
           fila[4]=Cliente1.getStrDniCliente1();
           fila[5]=Cliente1.getStrDireccionCliente1();
           fila[6]=Cliente1.getStrTelefonoCliente1();
           fila[7]=Cliente1.getStrFecNacimientoCliente1();//----------
           fila[8]=Cliente1.getStrFecIniTrabajoCliente1();//----------
           fila[9]=Cliente1.getStrFecFinTrabajoCliente1();//----------
           fila[10]=Cliente1.getStrObsvCliente1();
           defaultTableModel.addRow(fila);
           
           /*
            ClsEntidadCliente1 Cliente1=new ClsEntidadCliente1();
           Cliente1=(ClsEntidadCliente1) iterator.next();
           fila[0]=Cliente1.getStrIdCliente();
           fila[1]=Cliente1.getStrNombreCliente();
            fila[1]=Cliente1.getStrEspecialidad();
           fila[2]=Cliente1.getStrRucCliente();
           fila[3]=Cliente1.getStrDniCliente();
           fila[4]=Cliente1.getStrDireccionCliente();
           fila[5]=Cliente1.getStrTelefonoCliente();
            fila[1]=Cliente1.getStrFecNacimiento();
             fila[1]=Cliente1.getStrFecIniTrabajo();
              fila[1]=Cliente1.getStrFecFinTrabajo();
           fila[6]=Cliente1.getStrObsvCliente();
           defaultTableModel.addRow(fila);
           */
                
       }
       tblCliente.setModel(defaultTableModel);
   }
   void BuscarCliente(){
        //String titulos[]={"ID","Nombre o Razón Social","RUC","DNI","Dirección","Teléfono","Observación"}; origgen cabecera actualizar tabla
        //String titulos[]={"ID","Nombre o Razón Social","RUC","DNI","Dirección","Teléfono","Observación"};//cab natural de ventas origen
        String titulos[]={"ID","Nombre / Apellido","Especialidad","RUC","DNI","Dirección","Teléfono","Fec.Nac","Inic.Trab","Fin.Trab","Observación"};
        dtm.setColumnIdentifiers(titulos);
        
        ClsCliente1 categoria=new ClsCliente1();
        busqueda=txtBusqueda.getText();
        if(rbtnCodigo.isSelected()){
            criterio="id";
        }else if(rbtnNombre.isSelected()){
            criterio="nombre";
        }else if(rbtnRuc.isSelected()){
            criterio="ruc";
        }else if(rbtnDni.isSelected()){
            criterio="dni";
        }
        try{
            rs=categoria.listarClientePorParametro(criterio,busqueda);
            boolean encuentra=false;
            String Datos[]=new String[11];
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
                //agregados
                Datos[7]=(String) rs.getString(8);
                Datos[8]=(String) rs.getString(9);
                Datos[9]=(String) rs.getString(10);
                Datos[10]=(String) rs.getString(11);
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
            txtEspecialidad.setText((String)defaultTableModel.getValueAt(registros,2));//--------------
            txtRuc.setText((String)defaultTableModel.getValueAt(registros,3));
            txtDni.setText((String)defaultTableModel.getValueAt(registros,4));
            txtDireccion.setText((String)defaultTableModel.getValueAt(registros,5));
            txtTelefono1.setText((String)defaultTableModel.getValueAt(registros,6));
            txtFecNac.setText((String)defaultTableModel.getValueAt(registros,7));//--------------
            txtInicTrab.setText((String)defaultTableModel.getValueAt(registros,8));//--------------
            txtFinTrab.setText((String)defaultTableModel.getValueAt(registros,9));//--------------
            txtObservacion.setText((String)defaultTableModel.getValueAt(registros,10));
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
        jLabel6 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtFinTrab = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTelefono1 = new javax.swing.JTextField();
        txtFecNac = new javax.swing.JTextField();
        txtInicTrab = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtEspecialidad = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("DATOS DEL TECNICO ");
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

        rbtnCodigo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbtnCodigo.setForeground(new java.awt.Color(255, 255, 255));
        rbtnCodigo.setText("ID Cliente");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        pBuscar.add(rbtnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 100, -1));

        rbtnNombre.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbtnNombre.setForeground(new java.awt.Color(255, 255, 255));
        rbtnNombre.setText("Nombre / Apellido");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        pBuscar.add(rbtnNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 140, -1));

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

        rbtnDni.setText("D.N.I.");
        rbtnDni.setOpaque(false);
        pBuscar.add(rbtnDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 60, -1));

        rbtnRuc.setText("R.U.C.");
        rbtnRuc.setOpaque(false);
        pBuscar.add(rbtnRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 60, -1));

        jLabel10.setBackground(new java.awt.Color(255, 0, 0));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterios de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jLabel10.setOpaque(true);
        pBuscar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, 80));

        tabCliente.addTab("Buscar", pBuscar);

        pNuevo.setBackground(new java.awt.Color(255, 255, 255));
        pNuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Tecnico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pNuevo.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 11, 540, 330));

        txtCodigo.setEnabled(false);
        pNuevo.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 70, -1));

        jLabel3.setText("ID Tecnixo:");
        pNuevo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 60, 20));

        jLabel2.setText("       Nombre / Apellido:");
        pNuevo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 120, 20));

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        pNuevo.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 380, -1));

        jLabel5.setText("R.U.C.:");
        pNuevo.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 50, 20));

        txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRucKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucKeyTyped(evt);
            }
        });
        pNuevo.add(txtRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 160, -1));

        jLabel6.setText("Dirección:");
        pNuevo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 60, 20));

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
        });
        pNuevo.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 380, -1));

        jLabel7.setText("   Inic Trab:");
        pNuevo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 60, 20));

        txtFinTrab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFinTrabKeyReleased(evt);
            }
        });
        pNuevo.add(txtFinTrab, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 160, -1));

        jLabel8.setText("DNI:");
        pNuevo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, 30, 20));

        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniKeyTyped(evt);
            }
        });
        pNuevo.add(txtDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 160, -1));

        jLabel9.setText("Observación:");
        pNuevo.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 80, 20));

        txtObservacion.setColumns(20);
        txtObservacion.setRows(5);
        txtObservacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtObservacionKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txtObservacion);

        pNuevo.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 380, 50));

        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Los campos marcado con un asterísco (*) son obligatorios");
        pNuevo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 280, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("*");
        pNuevo.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 20, 20));

        txtTelefono1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefono1KeyReleased(evt);
            }
        });
        pNuevo.add(txtTelefono1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 160, -1));

        txtFecNac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFecNacKeyReleased(evt);
            }
        });
        pNuevo.add(txtFecNac, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 190, 160, -1));

        txtInicTrab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInicTrabKeyReleased(evt);
            }
        });
        pNuevo.add(txtInicTrab, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 160, -1));

        jLabel13.setText("   Teléfono:");
        pNuevo.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 60, 20));

        jLabel14.setText("    Fec. Nac");
        pNuevo.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, 60, 20));

        jLabel15.setText("     Fin.Trab:");
        pNuevo.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 230, 60, 20));

        txtEspecialidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEspecialidadKeyReleased(evt);
            }
        });
        pNuevo.add(txtEspecialidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 160, -1));

        jLabel16.setText("     Especialidad:");
        pNuevo.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 80, 20));

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
            /*
            defaultTableModel = (DefaultTableModel)tblCliente.getModel();
            strCodigo =  ((String) defaultTableModel.getValueAt(fila, 0));
            txtCodigo.setText((String) defaultTableModel.getValueAt(fila, 0));
            txtNombre.setText((String) defaultTableModel.getValueAt(fila, 1));
            txtRuc.setText((String)defaultTableModel.getValueAt(fila,2));
            txtDni.setText((String)defaultTableModel.getValueAt(fila,3));
            txtDireccion.setText((String)defaultTableModel.getValueAt(fila,4));
            txtFinTrab.setText((String)defaultTableModel.getValueAt(fila,5));
            txtObservacion.setText((String)defaultTableModel.getValueAt(fila,6));
            */
            /* */
            defaultTableModel=(DefaultTableModel) tblCliente.getModel();
            strCodigo=((String) defaultTableModel.getValueAt(fila,0));
            txtCodigo.setText((String)defaultTableModel.getValueAt(fila,0));
            txtNombre.setText((String)defaultTableModel.getValueAt(fila,1));
            txtEspecialidad.setText((String)defaultTableModel.getValueAt(fila,2));//--------------
            txtRuc.setText((String)defaultTableModel.getValueAt(fila,3));
            txtDni.setText((String)defaultTableModel.getValueAt(fila,4));
            txtDireccion.setText((String)defaultTableModel.getValueAt(fila,5));
            txtTelefono1.setText((String)defaultTableModel.getValueAt(fila,6));
            txtFecNac.setText((String)defaultTableModel.getValueAt(fila,7));//--------------
            txtInicTrab.setText((String)defaultTableModel.getValueAt(fila,8));//--------------
            txtFinTrab.setText((String)defaultTableModel.getValueAt(fila,9));//--------------
            txtObservacion.setText((String)defaultTableModel.getValueAt(registros,10));
            
           /* */
        }
        mirar();
        /*tblCliente.setEnabled(true);
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
       txtObservacion.setEnabled(false); */
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
        JOptionPane.showMessageDialog(null, "¡Seleccione un registro para modificar !!!");
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
            JOptionPane.showMessageDialog(null, "Acceso Denegado:: Ingrese Nombre / Apellido del Tecnico::. campo --> Obligatorio!!");
            txtNombre.requestFocus();
            txtNombre.setBackground(Color.RED);
            return false;

        }else{
            return true;
        }

    }
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    if(validardatos()==true){  
        if(accion.equals("Nuevo")){
            ClsCliente1 clientes1=new ClsCliente1();
            ClsEntidadCliente1 cliente1=new ClsEntidadCliente1();
            cliente1.setStrNombreCliente1(txtNombre.getText());
            cliente1.setStrEspecialidadCliente1(txtEspecialidad.getText());//------
            cliente1.setStrRucCliente1(txtRuc.getText());
            cliente1.setStrDniCliente1(txtDni.getText());
            cliente1.setStrDireccionCliente1(txtDireccion.getText());
            cliente1.setStrTelefonoCliente1(txtTelefono1.getText());
            cliente1.setStrFecNacimientoCliente1(txtFecNac.getText());//------
            cliente1.setStrFecIniTrabajoCliente1(txtInicTrab.getText());//------
            cliente1.setStrFecFinTrabajoCliente1(txtFinTrab.getText());//------
            cliente1.setStrObsvCliente1(txtObservacion.getText());
            clientes1.agregarCliente(cliente1);
            actualizarTabla();
            CantidadTotal();
        }
        if(accion.equals("Modificar")){
            ClsCliente1 clientes=new ClsCliente1();
            ClsEntidadCliente1 cliente1=new ClsEntidadCliente1();
            /*cliente.setStrNombreCliente(txtNombre.getText());
            cliente.setStrRucCliente(txtRuc.getText());
            cliente.setStrDniCliente(txtDni.getText());
            cliente.setStrDireccionCliente(txtDireccion.getText());
            cliente.setStrTelefonoCliente(txtFinTrab.getText());
            cliente.setStrObsvCliente(txtObservacion.getText());*/
            cliente1.setStrNombreCliente1(txtNombre.getText());
            cliente1.setStrEspecialidadCliente1(txtEspecialidad.getText());//------
            cliente1.setStrRucCliente1(txtRuc.getText());
            cliente1.setStrDniCliente1(txtDni.getText());
            cliente1.setStrDireccionCliente1(txtDireccion.getText());
            cliente1.setStrTelefonoCliente1(txtTelefono1.getText());
            cliente1.setStrFecNacimientoCliente1(txtFecNac.getText());//------
            cliente1.setStrFecIniTrabajoCliente1(txtInicTrab.getText());//------
            cliente1.setStrFecFinTrabajoCliente1(txtFinTrab.getText());//------
            cliente1.setStrObsvCliente1(txtObservacion.getText());
            clientes.modificarCliente(strCodigo, cliente1);
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
        if (keyCode==KeyEvent.VK_ENTER) txtEspecialidad.requestFocus();
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucKeyTyped
        char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtRuc.getText().length();
        if(txtRuc.getText().trim().length()<11){

        }else{
            i=10;
            String com=txtRuc.getText().substring(0, 10);
            txtRuc.setText("");
            txtRuc.setText(com);
        }
    }//GEN-LAST:event_txtRucKeyTyped

    private void txtDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyTyped
        char car = evt.getKeyChar();
        if((car<'0' || car>'9')) evt.consume();
        //----------------Poner limite de caracteres--------------------
        int i = txtDni.getText().length();
        if(txtDni.getText().trim().length()<8){

        }else{
            i=10;
            String com=txtDni.getText().substring(0, 7);
            txtDni.setText("");
            txtDni.setText(com);
        }
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
        if (keyCode==KeyEvent.VK_ENTER) txtTelefono1.requestFocus();        
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtFinTrabKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFinTrabKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtObservacion.requestFocus();       
    }//GEN-LAST:event_txtFinTrabKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Map p=new HashMap();
        p.put("busqueda",txtBusqueda.getText());
        if(rbtnCodigo.isSelected()){
            p.put("criterio", "id");
        }
        else if(rbtnNombre.isSelected()){
            p.put("criterio", "nombre");
        }else if(rbtnRuc.isSelected()){
            p.put("criterio", "ruc");
        }else if(rbtnDni.isSelected()){
            p.put("criterio", "dni");
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

    private void txtTelefono1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefono1KeyReleased
        // TODO add your handling code here:
         String cadena= (txtTelefono1.getText()).toUpperCase();
        txtTelefono1.setText(cadena);
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtFecNac.requestFocus();
    }//GEN-LAST:event_txtTelefono1KeyReleased

    private void txtFecNacKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFecNacKeyReleased
        // TODO add your handling code here:
          String cadena= (txtFecNac.getText()).toUpperCase();
        txtFecNac.setText(cadena);
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtInicTrab.requestFocus();
    }//GEN-LAST:event_txtFecNacKeyReleased

    private void txtInicTrabKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInicTrabKeyReleased
        // TODO add your handling code here:
          String cadena= (txtInicTrab.getText()).toUpperCase();
        txtInicTrab.setText(cadena);
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtFinTrab.requestFocus();
    }//GEN-LAST:event_txtInicTrabKeyReleased

    private void txtEspecialidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEspecialidadKeyReleased
        // TODO add your handling code here:
          String cadena= (txtEspecialidad.getText()).toUpperCase();
        txtEspecialidad.setText(cadena);
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) txtRuc.requestFocus();
    }//GEN-LAST:event_txtEspecialidadKeyReleased

    private void txtObservacionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionKeyReleased
        // TODO add your handling code here:
         String cadena= (txtObservacion.getText()).toUpperCase();
        txtObservacion.setText(cadena);
        int keyCode = evt.getKeyCode();
        if (keyCode==KeyEvent.VK_ENTER) btnGuardar.requestFocus();
        
    }//GEN-LAST:event_txtObservacionKeyReleased

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtEspecialidad;
    private javax.swing.JTextField txtFecNac;
    private javax.swing.JTextField txtFinTrab;
    private javax.swing.JTextField txtInicTrab;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextArea txtObservacion;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono1;
    // End of variables declaration//GEN-END:variables
}
