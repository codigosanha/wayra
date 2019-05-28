/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Conexion.ClsConexion;
import Entidad.*;
import Negocio.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import java.util.HashMap;
import java.util.Map;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class FrmVenta extends javax.swing.JInternalFrame {
    
    private Connection connection = new ClsConexion().getConection();
    String Total;
    String strCodigo;
    String accion;
    String numVenta, tipoDocumento;
    
    int registros;
    String id[] = new String[50];
    //String Datos[]=new String[50];

    static int intContador;
    public String IdEmpleado, NombreEmpleado;
    int idventa, nidventa;
    String idventa_print;
    //-----------------------------------------------
    public String codigo;
    static Connection conn = null;
    
    static ResultSet rs = null;
    static ResultSet rsp = null;
    DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel dtmDetalle = new DefaultTableModel();
    
    String criterio, busqueda;
    
    public FrmVenta() {
        initComponents();
        //---------------------FECHA ACTUAL-------------------------------
        Date date = new Date();
        String format = new String("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format);
        txtFecha.setDate(date);
        //---------------------GENERA NUMERO DE VENTA---------------------
        numVenta = generaNumVenta();
        txtNumero.setText(numVenta);
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(955, 505);
        cargarComboTipoDocumento();
        
        lblIdProducto.setVisible(false);
        lblIdCliente.setVisible(false);
        txtDescripcionProducto.setVisible(false);
        txtCostoProducto.setVisible(false);
        mirar();
        //--------------------JTABLE - DETALLEPRODUCTO--------------------"TECNICO","OBS",

        String titulos[] = {"ID", "CÓDIGO", "PROD./SERV.", "DESCRIPCIÓN", "CANTIDAD", "COSTO", "PRECIO", "TOTAL", "TECNICO", "OBSERVACION"};
        dtmDetalle.setColumnIdentifiers(titulos);
        tblDetalleProducto.setModel(dtmDetalle);
        CrearTablaDetalleProducto();
    }
//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------

    public String generaNumVenta() {
        
        ClsVenta oVenta = new ClsVenta();
        try {
            
            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                if (rs.getString(1) != null) {
                    Scanner s = new Scanner(rs.getString(1));
                    int c = s.useDelimiter("C").nextInt() + 1;
                    
                    if (c < 10) {
                        return "C0000" + c;
                    }
                    if (c < 100) {
                        return "C000" + c;
                    }
                    if (c < 1000) {
                        return "C00" + c;
                    }
                    if (c < 10000) {
                        return "C0" + c;
                    } else {
                        return "C" + c;
                    }
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return "C00001";
        
    }
    
    void updateEstadoVentaPendiente() {
        ClsVentaPendiente ventas = new ClsVentaPendiente();
        ClsEntidadVenta venta = new ClsEntidadVenta();
        venta.setStrEstadoVenta("FINALIZADO");
        ventas.actualizarVentaEstado(lblIdVentaPendiente.getText(), venta);
    }

    void eliminarDetalleVentaPendiente() {
        ClsDetalleVentaPendiente detalle = new ClsDetalleVentaPendiente();
        detalle.eliminarDetalleVenta(lblIdVentaPendiente.getText());
    }

    void reponerStockDetalleVentaPendiente() {
        try {
            ClsDetalleVentaPendiente detalle = new ClsDetalleVentaPendiente();
            rs = detalle.listarDetalleVentaPorParametro("id", lblIdVentaPendiente.getText());
            while (rs.next()) {
                updateStockProducto(rs.getString(2), Double.parseDouble(rs.getString(6)));
            }
        } catch (Exception ex) {
            Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    void updateStockProducto(String idProducto, Double CantidadDetalle) {
        System.out.println(idProducto +" "+CantidadDetalle);
        try {
            double stockActual = 0.00, nuevoStock = 0.00;
            ClsProducto productos = new ClsProducto();
            ClsEntidadProducto producto = new ClsEntidadProducto();
            rsp = productos.listarProductoPorParametro("id", idProducto);
            if (rsp.next()) {
                stockActual = Double.parseDouble(rsp.getString(5));
            }
            
            nuevoStock = stockActual + CantidadDetalle;
            
            producto.setStrStockProducto(String.valueOf(nuevoStock));
            productos.actualizarProductoStock(idProducto, producto);
            
        } catch (Exception ex) {
            Logger.getLogger(FrmVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void CrearTablaDetalleProducto() {
        //--------------------PRESENTACION DE JTABLE----------------------

        TableCellRenderer render = new DefaultTableCellRenderer() {
            
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (column == 0 || column == 1 || column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    l.setHorizontalAlignment(SwingConstants.LEFT);
                }

                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(new Color(203, 159, 41));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setForeground(Color.WHITE);
                } else {
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
        for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
            tblDetalleProducto.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        tblDetalleProducto.setAutoResizeMode(tblDetalleProducto.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50, 100, 110, 100, 70, 70, 70, 70, 70, 90};
        for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
            tblDetalleProducto.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //Ocultar columa
        setOcultarColumnasJTable(tblDetalleProducto, new int[]{0, 5});
        
    }
    
    void limpiarCampos() {
        
        txtTotalVenta.setText("0.0");
        txtDescuento.setText("0.0");
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalPagar.setText("0.0");
        
        lblIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtStockProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtTotalProducto.setText("");
        txtTecnico.setText("");
        txtObs.setText("");
        txtCodigoProducto.requestFocus();
    }
    
    void mirar() {
        btnMostrarPendientes.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
        
        cboTipoDocumento.setEnabled(false);
        txtCodigoProducto.setEnabled(false);
        txtSerie.setEnabled(false);
        txtCantidadProducto.setEnabled(false);
        txtFecha.setEnabled(false);
        txtNumero.setEnabled(false);
        //txtTecnicoProducto.setEnabled(false);
        //txtObsProducto.setEnabled(false);

        btnBuscarCliente.setEnabled(false);
        btnBuscarProducto.setEnabled(false);
        btnAgregarProducto.setEnabled(false);
        btnEliminarProducto.setEnabled(false);
        btnLimpiarTabla.setEnabled(false);
        chkCambiarNumero.setEnabled(false);
        chkCambiarNumero.setSelected(false);
        
        txtTotalVenta.setText("0.0");
        txtDescuento.setText("0.0");
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalPagar.setText("0.0");
        lblIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtStockProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtTotalProducto.setText("");
        txtTecnico.setText("");
        txtObs.setText("");
        txtCodigoProducto.requestFocus();
    }
    
    void modificar() {
        
        btnMostrarPendientes.setEnabled(false);
        
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
        
        cboTipoDocumento.setEnabled(true);
        txtCodigoProducto.setEnabled(true);
        txtSerie.setEnabled(true);
        txtCantidadProducto.setEnabled(true);
        txtFecha.setEnabled(true);
        txtTecnico.setEnabled(true);
        txtObs.setEnabled(true);
        
        btnBuscarCliente.setEnabled(true);
        btnBuscarProducto.setEnabled(true);
        btnAgregarProducto.setEnabled(true);
        btnEliminarProducto.setEnabled(true);
        btnLimpiarTabla.setEnabled(true);
        chkCambiarNumero.setEnabled(true);
        
        txtCodigoProducto.requestFocus();
    }
    
    void cargarComboTipoDocumento() {
        ClsTipoDocumento tipodocumento = new ClsTipoDocumento();
        ArrayList<ClsEntidadTipoDocumento> tipodocumentos = tipodocumento.listarTipoDocumento();
        Iterator iterator = tipodocumentos.iterator();
        DefaultComboBoxModel DefaultComboBoxModel = new DefaultComboBoxModel();
        DefaultComboBoxModel.removeAllElements();
        
        cboTipoDocumento.removeAll();
        String fila[] = new String[2];
        intContador = 0;
        
        while (iterator.hasNext()) {
            ClsEntidadTipoDocumento TipoDocumento = new ClsEntidadTipoDocumento();
            TipoDocumento = (ClsEntidadTipoDocumento) iterator.next();
            id[intContador] = TipoDocumento.getStrIdTipoDocumento();
            fila[0] = TipoDocumento.getStrIdTipoDocumento();
            fila[1] = TipoDocumento.getStrDescripcionTipoDocumento();
            DefaultComboBoxModel.addElement(TipoDocumento.getStrDescripcionTipoDocumento());
            intContador++;
        }
        cboTipoDocumento.setModel(DefaultComboBoxModel);
    }
    
    void BuscarProductoPorCodigo() {
        String busqueda = null;
        busqueda = txtCodigoProducto.getText();
        try {
            ClsProducto oProducto = new ClsProducto();
            
            rs = oProducto.listarProductoActivoPorParametro("codigo", busqueda);
            while (rs.next()) {
                if (rs.getString(2).equals(busqueda)) {
                    
                    lblIdProducto.setText(rs.getString(1));
                    txtNombreProducto.setText(rs.getString(3));
                    txtDescripcionProducto.setText(rs.getString(4));
                    //DescripcionProducto=rs.getString(4);
                    txtStockProducto.setText(rs.getString(5));
                    txtCostoProducto.setText(rs.getString(6));
                    txtPrecioProducto.setText(rs.getString(7));
                    txtTecnico.setText(rs.getString(8));
                    txtObs.setText(rs.getString(9));
                }
                break;
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
    }
    
    void BuscarClientePorDefecto() {
        try {
            ClsCliente oCliente = new ClsCliente();
            rs = oCliente.listarClientePorParametro("id", "1");
            while (rs.next()) {
                lblIdCliente.setText(rs.getString(1));
                txtNombreCliente.setText(rs.getString(2));
                break;
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
    }
    
    private void setOcultarColumnasJTable(JTable tbl, int columna[]) {
        for (int i = 0; i < columna.length; i++) {
            tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }

//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------
    void obtenerUltimoIdVenta_print() {
        try {
            ClsVenta oVenta = new ClsVenta();
            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                idventa_print = String.valueOf(rs.getInt(1));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnImporte = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnImprimir1 = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnMostrarPendientes = new javax.swing.JButton();
        btnVentaPendiente = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtTotalProducto = new javax.swing.JTextField();
        btnEliminarProducto = new javax.swing.JButton();
        btnLimpiarTabla = new javax.swing.JButton();
        lblIdVentaPendiente = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        txtNumero = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JLabel();
        txtStockProducto = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        lblIdProducto = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtCostoProducto = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObs = new javax.swing.JTextArea();
        txtTecnico = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBuscarCliente = new javax.swing.JButton();
        txtNombreCliente = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cboTipoDocumento = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        lblIdCliente = new javax.swing.JLabel();
        chkCambiarNumero = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        txtIGV = new javax.swing.JTextField();
        txtTotalPagar = new javax.swing.JTextField();
        txtTotalVenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDetalleProducto = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Orden De Trabajo");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/venta.png"))); // NOI18N
        btnGuardar.setText("Generar Venta");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel4.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 110, 70));

        btnImporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/importe.png"))); // NOI18N
        btnImporte.setText("Importe");
        btnImporte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImporte.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImporteActionPerformed(evt);
            }
        });
        jPanel4.add(btnImporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 110, 70));

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
        jPanel4.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 110, 70));

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
        jPanel4.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 110, 70));

        btnImprimir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/printer.png"))); // NOI18N
        btnImprimir1.setText("Recibo");
        btnImprimir1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimir1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 110, 70));

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
        jPanel4.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 70));

        btnMostrarPendientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar.png"))); // NOI18N
        btnMostrarPendientes.setText("Mostrar Pendientes");
        btnMostrarPendientes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarPendientes.setIconTextGap(0);
        btnMostrarPendientes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMostrarPendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarPendientesActionPerformed(evt);
            }
        });
        jPanel4.add(btnMostrarPendientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 110, 70));

        btnVentaPendiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/printer.png"))); // NOI18N
        btnVentaPendiente.setText("Guardar como Pendiente");
        btnVentaPendiente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVentaPendiente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVentaPendiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaPendienteActionPerformed(evt);
            }
        });
        jPanel4.add(btnVentaPendiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 110, 80));

        getContentPane().add(jPanel4);
        jPanel4.setBounds(803, 10, 130, 650);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("CANTIDAD:");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 9, 70, 30));

        txtCantidadProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCantidadProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });
        jPanel3.add(txtCantidadProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 9, 60, 30));

        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Agregar_p1.png"))); // NOI18N
        btnAgregarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarProductoMouseClicked(evt);
            }
        });
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });
        btnAgregarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnAgregarProductoKeyReleased(evt);
            }
        });
        jPanel3.add(btnAgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 5, 50, 40));

        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("TOTAL:");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 9, 50, 30));

        txtTotalProducto.setBackground(new java.awt.Color(204, 255, 204));
        txtTotalProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotalProducto.setForeground(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalProducto.setDisabledTextColor(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setEnabled(false);
        jPanel3.add(txtTotalProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 9, 80, 30));

        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Remove.png"))); // NOI18N
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        jPanel3.add(btnEliminarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 5, 50, 40));

        btnLimpiarTabla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/nuevo1.png"))); // NOI18N
        btnLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTablaActionPerformed(evt);
            }
        });
        jPanel3.add(btnLimpiarTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 5, 50, 40));
        jPanel3.add(lblIdVentaPendiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 30, 20));

        jPanel5.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 530, 50));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("SERIE");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 170, 60, 20));

        txtSerie.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSerie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerie.setText("001");
        jPanel5.add(txtSerie, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 190, 59, -1));

        txtNumero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(txtNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 110, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Nº DE VENTA");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 170, 110, 20));

        jPanel2.setBackground(new java.awt.Color(255, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Producto // Servicio"));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("CÓDIGO:");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 50, 20));

        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });
        jPanel2.add(txtCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 190, -1));

        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 40, 35));

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("NOMBRE:");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 20));

        txtNombreProducto.setEnabled(false);
        jPanel2.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 190, -1));

        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("STOCK:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));
        jPanel2.add(txtDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 30, 20));

        txtStockProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtStockProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStockProducto.setEnabled(false);
        txtStockProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockProductoActionPerformed(evt);
            }
        });
        jPanel2.add(txtStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 90, -1));

        txtPrecioProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioProducto.setEnabled(false);
        jPanel2.add(txtPrecioProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 90, -1));
        jPanel2.add(lblIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 20, 20));

        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("PRECIO:");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 50, -1));
        jPanel2.add(txtCostoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 90, 20));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TECNICO:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("OBSERVACION:");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(278, 60, -1, 20));

        txtObs.setColumns(20);
        txtObs.setRows(5);
        jScrollPane1.setViewportView(txtObs);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 160, 50));
        jPanel2.add(txtTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 160, -1));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 530, 105));

        jPanel1.setBackground(new java.awt.Color(255, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Vehiculo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("N° Placa:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 60, 20));

        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarCliente.setAlignmentY(1.0F);
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 28, 40, 35));

        txtNombreCliente.setEnabled(false);
        jPanel1.add(txtNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 190, -1));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("DOCUMENTO:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 15, 100, -1));

        cboTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cboTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 35, 130, 20));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FECHA:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 15, 80, 20));
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 35, 100, -1));
        jPanel1.add(lblIdCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 20, 20));

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 530, 70));

        chkCambiarNumero.setText("Cambiar Número");
        chkCambiarNumero.setOpaque(false);
        chkCambiarNumero.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkCambiarNumeroStateChanged(evt);
            }
        });
        jPanel5.add(chkCambiarNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 220, 110, -1));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Banned User.png"))); // NOI18N
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 240, 140));

        getContentPane().add(jPanel5);
        jPanel5.setBounds(10, 10, 790, 250);

        jPanel6.setBackground(new java.awt.Color(255, 246, 227));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("SUB TOTAL");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 3, 100, 20));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("I.G.V.");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 3, 100, 20));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TOTAL A PAGAR");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(448, 3, 130, 20));

        txtSubTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.setEnabled(false);
        jPanel6.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 25, 100, 30));

        txtIGV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtIGV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIGV.setEnabled(false);
        jPanel6.add(txtIGV, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 25, 100, 30));

        txtTotalPagar.setBackground(new java.awt.Color(0, 0, 0));
        txtTotalPagar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtTotalPagar.setForeground(new java.awt.Color(0, 255, 102));
        txtTotalPagar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalPagar.setDisabledTextColor(new java.awt.Color(0, 255, 102));
        txtTotalPagar.setEnabled(false);
        jPanel6.add(txtTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 25, 135, 30));

        txtTotalVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTotalVenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVenta.setEnabled(false);
        jPanel6.add(txtTotalVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 25, 100, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("VALOR VENTA");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 3, 100, 20));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("DESCUENTO");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 3, 90, 20));

        txtDescuento.setBackground(new java.awt.Color(255, 255, 204));
        txtDescuento.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtDescuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescuentoKeyReleased(evt);
            }
        });
        jPanel6.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 25, 90, 30));

        getContentPane().add(jPanel6);
        jPanel6.setBounds(210, 403, 590, 65);

        jPanel7.setBackground(new java.awt.Color(247, 254, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("IMPORTE");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, 80, 20));

        txtImporte.setBackground(new java.awt.Color(0, 0, 0));
        txtImporte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtImporte.setForeground(new java.awt.Color(255, 255, 255));
        txtImporte.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImporte.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtImporte.setEnabled(false);
        jPanel7.add(txtImporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 80, 30));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("CAMBIO");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 3, 80, 20));

        txtCambio.setBackground(new java.awt.Color(0, 0, 0));
        txtCambio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCambio.setForeground(new java.awt.Color(255, 255, 0));
        txtCambio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCambio.setDisabledTextColor(new java.awt.Color(255, 255, 0));
        txtCambio.setEnabled(false);
        jPanel7.add(txtCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 25, 80, 30));

        getContentPane().add(jPanel7);
        jPanel7.setBounds(10, 403, 190, 65);

        tblDetalleProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDetalleProducto.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetalleProducto.setRowHeight(22);
        jScrollPane3.setViewportView(tblDetalleProducto);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(10, 260, 790, 137);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
//        txtIdEmpleado.setText(IdEmpleado);
        BuscarClientePorDefecto();
        cargarComboTipoDocumento();
        

    }//GEN-LAST:event_formComponentShown

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        Consultas.FrmBuscarProducto_Venta producto = new Consultas.FrmBuscarProducto_Venta();
        Presentacion.FrmPrincipal.Escritorio.add(producto);
        producto.toFront();
        producto.setVisible(true);
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        Consultas.FrmBuscarCliente_Venta cliente = new Consultas.FrmBuscarCliente_Venta();
        Presentacion.FrmPrincipal.Escritorio.add(cliente);
        cliente.toFront();
        cliente.setVisible(true);
    }//GEN-LAST:event_btnBuscarClienteActionPerformed
    
    void CalcularTotal() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        double precio_prod = 0, cant_prod = 0, total_prod = 0;
        precio_prod = Double.parseDouble(txtPrecioProducto.getText());
        cant_prod = Double.parseDouble(txtCantidadProducto.getText());
        total_prod = precio_prod * cant_prod;
        txtTotalProducto.setText(String.valueOf(formateador.format(total_prod)));
    }
    
    public int recorrer(int id) {
        int fila = 0, valor = -1;
        
        fila = tblDetalleProducto.getRowCount();
        
        for (int f = 0; f < fila; f++) {
            if (Integer.parseInt(String.valueOf(dtmDetalle.getValueAt(f, 0))) == id) {
                
                valor = f;
                //JOptionPane.showMessageDialog(null, "te encontre!");
                break;
                
            } else {
                //JOptionPane.showMessageDialog(null, "no estas!");
                valor = -1;
            }
            
        }
        return valor;
    }
    
    void agregardatos(int item, String cod, String nom, String descrip, double cant, double cost, double pre, double tot, String tec, String obs) {
        
        int p = recorrer(item);
        double n_cant, n_total;
        if (p > -1) {
            
            n_cant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 4))) + cant;
            tblDetalleProducto.setValueAt(n_cant, p, 4);
            
            n_total = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 4))) * Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 5)));
            tblDetalleProducto.setValueAt(n_total, p, 7);

            //JOptionPane.showMessageDialog(null, "IGUALL!");
        } else {
            String Datos[] = {String.valueOf(item), cod, nom, descrip, String.valueOf(cant), String.valueOf(cost), String.valueOf(pre), String.valueOf(tot), tec, obs};
            dtmDetalle.addRow(Datos);
        }
        tblDetalleProducto.setModel(dtmDetalle);
    }
    
    void CalcularValor_Venta() {
        int fila = 0;
        double valorVenta = 0;
        fila = dtmDetalle.getRowCount();
        for (int f = 0; f < fila; f++) {
            valorVenta += Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 7)));
        }
        txtTotalVenta.setText(String.valueOf(valorVenta));
    }
    
    void CalcularSubTotal() {
        double subtotal = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        subtotal = Double.parseDouble(txtTotalPagar.getText()) / 1.18;
        txtSubTotal.setText(String.valueOf(formateador.format(subtotal)));
    }
    
    void CalcularIGV() {
        double igv = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        igv = Double.parseDouble(txtSubTotal.getText()) * 0.18;
        txtIGV.setText(String.valueOf(formateador.format(igv)));
    }
    
    void CalcularTotal_Pagar() {
        double totalpagar = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        totalpagar = Double.parseDouble(txtTotalVenta.getText()) - Double.parseDouble(txtDescuento.getText());
        txtTotalPagar.setText(String.valueOf(formateador.format(totalpagar)));
    }
    
    void limpiarTabla() {
        try {
            int filas = tblDetalleProducto.getRowCount();
            for (int i = 0; filas > i; i++) {
                dtmDetalle.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        double stock, cant;
        
        if (!txtCantidadProducto.getText().equals("")) {
            if (txtCantidadProducto.getText().equals("")) {
                txtCantidadProducto.setText("0");
                cant = Double.parseDouble(txtCantidadProducto.getText());
            } else {
                cant = Double.parseDouble(txtCantidadProducto.getText());
            }
            
            if (cant > 0) {
                stock = Double.parseDouble(txtStockProducto.getText());
                cant = Double.parseDouble(txtCantidadProducto.getText());
                if (cant <= stock) {
                    int d1 = Integer.parseInt(lblIdProducto.getText());
                    String d2 = txtCodigoProducto.getText();
                    String d3 = txtNombreProducto.getText();
                    String d4 = txtDescripcionProducto.getText();
                    double d5 = Double.parseDouble(txtCantidadProducto.getText());
                    double d6 = Double.parseDouble(txtCostoProducto.getText());
                    double d7 = Double.parseDouble(txtPrecioProducto.getText());
                    double d8 = Double.parseDouble(txtTotalProducto.getText());
                    String d9 = txtTecnico.getText();
                    String d10 = txtObs.getText();
                    agregardatos(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10);
                    
                    CalcularValor_Venta();
                    CalcularTotal_Pagar();
                    CalcularSubTotal();
                    CalcularIGV();

                    //txtCantidadProducto.setText("");
                    txtTotalProducto.setText("");
                    
                    txtCodigoProducto.setText("");
                    txtNombreProducto.setText("");
                    txtStockProducto.setText("");
                    txtTecnico.setText("");
                    txtObs.setText("");
                    txtPrecioProducto.setText("");
                    txtCodigoProducto.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Stock Insuficiente");
                    txtCantidadProducto.requestFocus();
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad mayor a 0");
                txtCantidadProducto.requestFocus();
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
            txtCantidadProducto.requestFocus();
        }

    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void txtCantidadProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyReleased
        CalcularTotal();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            btnAgregarProducto.requestFocus();
        }

    }//GEN-LAST:event_txtCantidadProductoKeyReleased

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        int fila = tblDetalleProducto.getSelectedRow();
        if (fila > 0) {
            dtmDetalle.removeRow(fila);
            CalcularValor_Venta();
            CalcularSubTotal();
            CalcularIGV();
        } else if (fila == 0) {
            dtmDetalle.removeRow(fila);
            
            txtTotalVenta.setText("0.0");
            txtDescuento.setText("0.0");
            txtSubTotal.setText("0.0");
            txtIGV.setText("0.0");
            txtTotalPagar.setText("0.0");
            CalcularValor_Venta();
            CalcularTotal_Pagar();
            CalcularSubTotal();
            CalcularIGV();
        }
        CalcularValor_Venta();
        CalcularTotal_Pagar();
        CalcularSubTotal();
        CalcularIGV();
        txtCodigoProducto.requestFocus();
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void txtCodigoProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyReleased
        BuscarProductoPorCodigo();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            txtCantidadProducto.requestFocus();
        }

    }//GEN-LAST:event_txtCodigoProductoKeyReleased

    private void txtDescuentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescuentoKeyReleased
        CalcularTotal_Pagar();
        CalcularSubTotal();
        CalcularIGV();
    }//GEN-LAST:event_txtDescuentoKeyReleased

    private void btnLimpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTablaActionPerformed
        limpiarTabla();
        txtTotalVenta.setText("0.0");
        txtDescuento.setText("0.0");
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalPagar.setText("0.0");
        txtCodigoProducto.requestFocus();
    }//GEN-LAST:event_btnLimpiarTablaActionPerformed

    private void chkCambiarNumeroStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkCambiarNumeroStateChanged
        if (chkCambiarNumero.isSelected()) {
            txtNumero.setText("");
            txtNumero.setEnabled(true);
        } else {
            txtNumero.setEnabled(false);
            numVenta = generaNumVenta();
            txtNumero.setText(numVenta);
        }
    }//GEN-LAST:event_chkCambiarNumeroStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        limpiarTabla();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnVentaPendienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaPendienteActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "¿Desea Guardar la Venta como Pendiente?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (accion.equals("Nuevo")) {
                ClsVentaPendiente ventas = new ClsVentaPendiente();
                ClsEntidadVenta venta = new ClsEntidadVenta();
                
                venta.setStrIdCliente(lblIdCliente.getText());
                venta.setStrIdEmpleado(IdEmpleado);
                venta.setStrFechaVenta(txtFecha.getDate());
                venta.setStrTotalVenta(txtTotalVenta.getText());
                venta.setStrDescuentoVenta(txtDescuento.getText());
                venta.setStrSubTotalVenta(txtSubTotal.getText());
                venta.setStrIgvVenta(txtIGV.getText());
                venta.setStrTotalPagarVenta(txtTotalPagar.getText());
                
                venta.setStrEstadoVenta("PENDIENTE");
                if (lblIdVentaPendiente.getText().equals("")) {
                    ventas.agregarVenta(venta);
                } else {
                    ventas.modificarVenta(lblIdVentaPendiente.getText(), venta);
                    reponerStockDetalleVentaPendiente();
                    eliminarDetalleVentaPendiente();
                }
                
                guardarDetallePendiente();
                
            }
            
            mirar();
            tipoDocumento = cboTipoDocumento.getSelectedItem().toString();
            limpiarTabla();
            numVenta = generaNumVenta();
            txtNumero.setText(numVenta);
            BuscarClientePorDefecto();
            
        }
        
        if (result == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Venta Anulada!");
        }
    }//GEN-LAST:event_btnVentaPendienteActionPerformed

    private void btnImporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImporteActionPerformed
        // TODO add your handling code here:
        String ingreso = JOptionPane.showInputDialog(null, "Ingrese Importe a Cancelar", "0.0");
        Double importe, cambio;
        if (ingreso.compareTo("") != 0) {
            importe = Double.parseDouble(ingreso);
            txtImporte.setText(String.valueOf(importe));
            cambio = Double.parseDouble(txtImporte.getText()) - Double.parseDouble(txtTotalPagar.getText());
            txtCambio.setText(String.valueOf(cambio));
        } else {
            txtImporte.setText("0.0");
        }
    }//GEN-LAST:event_btnImporteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "¿Desea Generar la Venta?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (accion.equals("Nuevo")) {
                ClsVenta ventas = new ClsVenta();
                ClsEntidadVenta venta = new ClsEntidadVenta();
                venta.setStrIdTipoDocumento(id[cboTipoDocumento.getSelectedIndex()]);
                venta.setStrIdCliente(lblIdCliente.getText());
                venta.setStrIdEmpleado(IdEmpleado);
                venta.setStrSerieVenta(txtSerie.getText());
                venta.setStrNumeroVenta(txtNumero.getText());
                venta.setStrFechaVenta(txtFecha.getDate());
                venta.setStrTotalVenta(txtTotalVenta.getText());
                venta.setStrDescuentoVenta(txtDescuento.getText());
                venta.setStrSubTotalVenta(txtSubTotal.getText());
                venta.setStrIgvVenta(txtIGV.getText());
                venta.setStrTotalPagarVenta(txtTotalPagar.getText());
                
                venta.setStrEstadoVenta("EMITIDO");
                venta.setStrIdOrden(lblIdVentaPendiente.getText());
                ventas.agregarVenta(venta);
                if (!lblIdVentaPendiente.getText().equals("")) {
                    updateEstadoVentaPendiente();
                    reponerStockDetalleVentaPendiente();
                }
                guardarDetalle();
                
                
            }
            
            mirar();
            tipoDocumento = cboTipoDocumento.getSelectedItem().toString();
            limpiarTabla();
            numVenta = generaNumVenta();
            txtNumero.setText(numVenta);
            BuscarClientePorDefecto();
//------------ Imprimir Venta --------------            
            if (cboTipoDocumento.getSelectedItem().equals("TICKET")) {
                //Dese imprimir el Comprobante?
                int imprime = JOptionPane.showConfirmDialog(this, "¿Desea Imprimir el Ticket?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
                if (imprime == JOptionPane.YES_OPTION) {
                    
                    obtenerUltimoIdVenta_print();
                    Map p = new HashMap();
                    p.put("busqueda", idventa_print);
                    
                    JasperReport report;
                    JasperPrint print;
                    try {
                        
                        report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptVentaTicket.jrxml");
                        print = JasperFillManager.fillReport(report, p, connection);
                        JasperViewer view = new JasperViewer(print, false);
                        JasperPrintManager.printReport(print, false);
                        
                    } catch (JRException e) {
                        e.printStackTrace();
                    }
                }
            }
            //fin imprimir            

        }
        
        if (result == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Venta Anulada!");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnMostrarPendientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarPendientesActionPerformed
        Consultas.FrmVentasPendientes venta_pendiente = new Consultas.FrmVentasPendientes();
        Presentacion.FrmPrincipal.Escritorio.add(venta_pendiente);
        venta_pendiente.toFront();
        venta_pendiente.setVisible(true);
    }//GEN-LAST:event_btnMostrarPendientesActionPerformed

    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCantidadProductoKeyTyped

    private void txtCodigoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCodigoProductoKeyTyped

    private void btnAgregarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAgregarProductoKeyReleased

    }//GEN-LAST:event_btnAgregarProductoKeyReleased

    private void btnAgregarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarProductoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarProductoMouseClicked

    private void txtStockProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockProductoActionPerformed

    private void btnImprimir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimir1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimir1ActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        accion = "Nuevo";
        modificar();
        limpiarCampos();
    }//GEN-LAST:event_btnNuevoActionPerformed
    void obtenerUltimoIdVenta() {
        try {
            ClsVenta oVenta = new ClsVenta();
            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                idventa = rs.getInt(1);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }
    
    void obtenerUltimoIdVentaPendiente() {
        try {
            ClsVentaPendiente oVenta = new ClsVentaPendiente();
            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                idventa = rs.getInt(1);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }
    
    void guardarDetalle() {
        
        obtenerUltimoIdVenta();
        ClsDetalleVenta detalleventas = new ClsDetalleVenta();
        ClsEntidadDetalleVenta detalleventa = new ClsEntidadDetalleVenta();
        ClsProducto productos = new ClsProducto();
        String codigo, strId;
        ClsEntidadProducto producto = new ClsEntidadProducto();
        int fila = 0;
        
        double cant = 0, ncant = 0, stock = 0;
        fila = tblDetalleProducto.getRowCount();
        for (int f = 0; f < fila; f++) {
            detalleventa.setStrIdVenta(String.valueOf(idventa));
            detalleventa.setStrIdProducto(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 0)));
            detalleventa.setStrCantidadDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            detalleventa.setStrCostoDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 5)));
            detalleventa.setStrPrecioDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 6)));
            detalleventa.setStrTotalDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 7)));
            detalleventa.setStrTecnicoDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 8)));
            detalleventa.setStrObsDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 9)));
            detalleventas.agregarDetalleVenta(detalleventa);
            
            try {
                ClsProducto oProducto = new ClsProducto();
                
                rs = oProducto.listarProductoActivoPorParametro("id", ((String) tblDetalleProducto.getValueAt(f, 0)));
                while (rs.next()) {
                    cant = Double.parseDouble(rs.getString(5));
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                System.out.println(ex.getMessage());
            }
            
            strId = ((String) tblDetalleProducto.getValueAt(f, 0));
            
            ncant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            
            stock = cant - ncant;
            producto.setStrStockProducto(String.valueOf(stock));
            productos.actualizarProductoStock(strId, producto);
            
        }
    }
    
    void guardarDetallePendiente() {
        
        obtenerUltimoIdVentaPendiente();
        ClsDetalleVentaPendiente detalleventas = new ClsDetalleVentaPendiente();
        ClsEntidadDetalleVenta detalleventa = new ClsEntidadDetalleVenta();
        ClsProducto productos = new ClsProducto();
        String codigo, strId;
        ClsEntidadProducto producto = new ClsEntidadProducto();
        int fila = 0;
        
        double cant = 0, ncant = 0, stock = 0;
        fila = tblDetalleProducto.getRowCount();
        for (int f = 0; f < fila; f++) {
            detalleventa.setStrIdVenta(String.valueOf(idventa));
            detalleventa.setStrIdProducto(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 0)));
            detalleventa.setStrCantidadDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            detalleventa.setStrCostoDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 5)));
            detalleventa.setStrPrecioDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 6)));
            detalleventa.setStrTotalDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 7)));
            detalleventa.setStrTecnicoDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 8)));
            detalleventa.setStrObsDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 9)));
            detalleventas.agregarDetalleVenta(detalleventa);
            
            try {
                ClsProducto oProducto = new ClsProducto();
                
                rs = oProducto.listarProductoActivoPorParametro("id", ((String) tblDetalleProducto.getValueAt(f, 0)));
                while (rs.next()) {
                    cant = Double.parseDouble(rs.getString(5));
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                System.out.println(ex.getMessage());
            }
            
            strId = ((String) tblDetalleProducto.getValueAt(f, 0));
            
            ncant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            
            stock = cant - ncant;
            producto.setStrStockProducto(String.valueOf(stock));
            productos.actualizarProductoStock(strId, producto);
            
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImporte;
    private javax.swing.JButton btnImprimir1;
    private javax.swing.JButton btnLimpiarTabla;
    private javax.swing.JButton btnMostrarPendientes;
    public static javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVentaPendiente;
    private javax.swing.JComboBox cboTipoDocumento;
    private javax.swing.JCheckBox chkCambiarNumero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JLabel lblIdCliente;
    public static javax.swing.JLabel lblIdProducto;
    public static javax.swing.JLabel lblIdVentaPendiente;
    public static javax.swing.JTable tblDetalleProducto;
    private javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtCantidadProducto;
    public static javax.swing.JTextField txtCodigoProducto;
    public static javax.swing.JLabel txtCostoProducto;
    public static javax.swing.JLabel txtDescripcionProducto;
    public static javax.swing.JTextField txtDescuento;
    private com.toedter.calendar.JDateChooser txtFecha;
    public static javax.swing.JTextField txtIGV;
    private javax.swing.JTextField txtImporte;
    public static javax.swing.JTextField txtNombreCliente;
    public static javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextArea txtObs;
    public static javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtSerie;
    public static javax.swing.JTextField txtStockProducto;
    public static javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTecnico;
    public static javax.swing.JTextField txtTotalPagar;
    private javax.swing.JTextField txtTotalProducto;
    public static javax.swing.JTextField txtTotalVenta;
    // End of variables declaration//GEN-END:variables
}
