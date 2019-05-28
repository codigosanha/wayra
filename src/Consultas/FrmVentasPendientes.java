/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Conexion.ClsConexion;
import Entidad.*;
import Negocio.*;
import Presentacion.FrmVenta;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FrmVentasPendientes extends javax.swing.JInternalFrame {

    private Connection connection = new ClsConexion().getConection();
    static ResultSet rs = null;
    DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel dtm1 = new DefaultTableModel();
    String id[] = new String[50];
    static int intContador;
    Date fecha_ini, fecha_fin;
    String documento, criterio, busqueda, Total;
    boolean valor = true;
    int n = 0;

    public FrmVentasPendientes() {
        initComponents();
        lblIdVenta.setVisible(false);

        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(769, 338);

        //---------------------FECHA ACTUAL-------------------------------
        Date date = new Date();
        String format = new String("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format);
        //dcFechaini.setDate(date);
        //dcFechafin.setDate(date);

    }

//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------
    void CrearTabla() {
        //--------------------PRESENTACION DE JTABLE----------------------

        TableCellRenderer render = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (column == 0 || column == 2 || column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {
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
        for (int i = 0; i < tblVenta.getColumnCount(); i++) {
            tblVenta.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        tblVenta.setAutoResizeMode(tblVenta.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50, 80, 70, 150, 80, 40, 60, 60, 80, 80, 80};
        for (int i = 0; i < tblVenta.getColumnCount(); i++) {
            tblVenta.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

    }

    void PasarVenta() {
       
        ClsVentaPendiente venta = new ClsVentaPendiente();
        ClsDetalleVentaPendiente detalle = new ClsDetalleVentaPendiente();
        DefaultTableModel dtm = (DefaultTableModel) FrmVenta.tblDetalleProducto.getModel();

        try {
            rs = venta.listarVentaPorParametro("id", lblIdVenta.getText());

            if (rs.next()) {
                FrmVenta.lblIdCliente.setText((String) rs.getString(11));
                FrmVenta.txtNombreCliente.setText((String) rs.getString(2));
                FrmVenta.txtTotalVenta.setText((String) rs.getString(5));
                FrmVenta.txtDescuento.setText((String) rs.getString(6));
                FrmVenta.txtSubTotal.setText((String) rs.getString(7));
                FrmVenta.txtIGV.setText((String) rs.getString(8));
                FrmVenta.txtTotalPagar.setText((String) rs.getString(9));
                FrmVenta.lblIdVentaPendiente.setText((String) rs.getString(1));
            }

            rs = detalle.listarDetalleVentaPorParametro("id", lblIdVenta.getText());

            String Datos[] = new String[11];
            String titulos[] = {"ID", "CÓDIGO", "PROD./SERV.", "DESCRIPCIÓN", "CANTIDAD", "COSTO", "PRECIO", "TOTAL", "TECNICO", "OBSERVACION"};

            while (rs.next()) {
                Datos[0] = (String) rs.getString(2);
                Datos[1] = (String) rs.getString(3);
                Datos[2] = (String) rs.getString(4);
                Datos[3] = (String) rs.getString(5);
                Datos[4] = (String) rs.getString(6);
                Datos[5] = (String) rs.getString(7);
                Datos[6] = (String) rs.getString(8);
                Datos[7] = (String) rs.getString(9);
                Datos[8] = (String) rs.getString(10);
                Datos[9] = (String) rs.getString(11);
                dtm.addRow(Datos);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void BuscarVenta() {
        String placa = txtPlaca.getText();
        String titulos[] = {"ID", "Placa", "Fecha", "Empleado", "Estado", "Valor Venta", "Descuento", "Total"};
        dtm.setColumnIdentifiers(titulos);

        ClsVentaPendiente venta = new ClsVentaPendiente();

        //fecha_ini=dcFechaini.getDate();
        //fecha_fin=dcFechafin.getDate();
        try {
            rs = venta.listarVentaPorParametro("nombre", placa);
            boolean encuentra = false;
            String Datos[] = new String[11];
            int f, i;
            f = dtm.getRowCount();
            if (f > 0) {
                for (i = 0; i < f; i++) {
                    dtm.removeRow(0);
                }
            }
            while (rs.next()) {
                Datos[0] = (String) rs.getString(1);
                Datos[1] = (String) rs.getString(2);
                Datos[2] = (String) rs.getString(4);
                Datos[3] = (String) rs.getString(3);
                Datos[4] = (String) rs.getString(10);
                Datos[5] = (String) rs.getString(5);
                Datos[6] = (String) rs.getString(6);
                Datos[7] = (String) rs.getString(9);

                dtm.addRow(Datos);
                encuentra = true;

            }
            if (encuentra == false) {
                JOptionPane.showMessageDialog(null, "¡No hay resultados en la busqueda!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tblVenta.setModel(dtm);
    }

    void CrearTablaDetalle() {
        //--------------------PRESENTACION DE JTABLE DETALLE VENTA----------------------

        TableCellRenderer render = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (column == 0 || column == 1 || column == 2 || column == 5 || column == 6) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    l.setHorizontalAlignment(SwingConstants.LEFT);
                }

                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(new Color(51, 152, 255));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setForeground(Color.WHITE);
                } else {
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        //l.setBackground(new Color(232, 232, 232));
                        l.setBackground(new Color(229, 246, 245));
                    }
                }
                return l;
            }
        };

        //Agregar Render
        for (int i = 0; i < tblDetalleVenta.getColumnCount(); i++) {
            tblDetalleVenta.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        tblDetalleVenta.setAutoResizeMode(tblVenta.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50, 60, 80, 100, 100, 60, 60, 60, 80, 80};
        for (int i = 0; i < tblDetalleVenta.getColumnCount(); i++) {
            tblDetalleVenta.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //Ocultar Columnas
        ocultarColumnas(tblDetalleVenta, new int[]{1});
    }

    void BuscarVentaDetalle() {
        String titulos[] = {"ID", "ID Prod.", "Cód Producto", "Nombre Prodc./serv.", "Descripción", "Cantidad", "Precio", "Total", "Tecnico", "Observacion"};
        dtm1.setColumnIdentifiers(titulos);
        ClsDetalleVentaPendiente detalleventa = new ClsDetalleVentaPendiente();
        busqueda = lblIdVenta.getText();

        try {
            rs = detalleventa.listarDetalleVentaPorParametro("id", busqueda);
            boolean encuentra = false;
            String Datos[] = new String[10];
            int f, i;
            f = dtm1.getRowCount();
            if (f > 0) {
                for (i = 0; i < f; i++) {
                    dtm1.removeRow(0);
                }
            }
            while (rs.next()) {
                Datos[0] = (String) rs.getString(1);
                Datos[1] = (String) rs.getString(2);
                Datos[2] = (String) rs.getString(3);
                Datos[3] = (String) rs.getString(4);
                Datos[4] = (String) rs.getString(5);
                Datos[5] = (String) rs.getString(6);
                Datos[6] = (String) rs.getString(7);
                Datos[7] = (String) rs.getString(8);
                Datos[8] = (String) rs.getString(9);
                Datos[9] = (String) rs.getString(10);
                dtm1.addRow(Datos);
                encuentra = true;

            }
            if (encuentra = false) {
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tblDetalleVenta.setModel(dtm1);
    }

    void CantidadTotal() {
        Total = String.valueOf(tblVenta.getRowCount());
        lblEstado.setText("Se cargaron " + Total + " registros");
    }

    void restablecerCantidades() {
        String strId;
        String idventa = lblIdVenta.getText();
        ClsProducto productos = new ClsProducto();
        ClsEntidadProducto producto = new ClsEntidadProducto();
        int fila = 0;
        double cant = 0, ncant, stock;
        fila = tblDetalleVenta.getRowCount();
        for (int f = 0; f < fila; f++) {
            try {
                ClsProducto oProducto = new ClsProducto();

                rs = oProducto.listarProductoActivoPorParametro("id", ((String) tblDetalleVenta.getValueAt(f, 1)));
                while (rs.next()) {
                    cant = Double.parseDouble(rs.getString(5));
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                System.out.println(ex.getMessage());
            }

            strId = ((String) tblDetalleVenta.getValueAt(f, 1));
            ncant = Double.parseDouble(String.valueOf(tblDetalleVenta.getModel().getValueAt(f, 5)));
            stock = cant + ncant;
            producto.setStrStockProducto(String.valueOf(stock));
            productos.actualizarProductoStock(strId, producto);

        }
    }

    private void ocultarColumnas(JTable tbl, int columna[]) {
        for (int i = 0; i < columna.length; i++) {
            tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane5 = new javax.swing.JScrollPane();
        tblVenta = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        lblIdVenta = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        btnConcluir = new javax.swing.JButton();
        btnVerDetalle = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDetalleVenta = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Reporte de Ventas Realizadas");
        setToolTipText("");
        getContentPane().setLayout(null);

        tblVenta.setModel(new javax.swing.table.DefaultTableModel(
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
        tblVenta.setRowHeight(22);
        tblVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVentaMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblVenta);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(10, 110, 730, 140);

        jPanel1.setBackground(new java.awt.Color(255, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones de busqueda y anulación"));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Nro.  Placa");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 20, 70, 20);

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_32.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar);
        btnBuscar.setBounds(140, 20, 110, 50);
        jPanel1.add(lblIdVenta);
        lblIdVenta.setBounds(320, 20, 40, 20);

        txtPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaActionPerformed(evt);
            }
        });
        jPanel1.add(txtPlaca);
        txtPlaca.setBounds(20, 40, 110, 20);

        btnConcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_32.png"))); // NOI18N
        btnConcluir.setText("Pasar a Venta");
        btnConcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConcluirActionPerformed(evt);
            }
        });
        jPanel1.add(btnConcluir);
        btnConcluir.setBounds(570, 20, 140, 50);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 10, 730, 90);

        btnVerDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda_detallada.png"))); // NOI18N
        btnVerDetalle.setText("Ver Detalle");
        btnVerDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetalleActionPerformed(evt);
            }
        });
        getContentPane().add(btnVerDetalle);
        btnVerDetalle.setBounds(600, 260, 140, 40);
        getContentPane().add(lblEstado);
        lblEstado.setBounds(10, 250, 230, 20);

        tblDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetalleVenta.setRowHeight(22);
        tblDetalleVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetalleVentaMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblDetalleVenta);

        getContentPane().add(jScrollPane6);
        jScrollPane6.setBounds(10, 310, 730, 140);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVentaMouseClicked
        int fila;
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        fila = tblVenta.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
        } else {
            defaultTableModel = (DefaultTableModel) tblVenta.getModel();
            //strCodigo =  ((String) defaultTableModel.getValueAt(fila, 0));
            lblIdVenta.setText((String) defaultTableModel.getValueAt(fila, 0));

        }
        BuscarVentaDetalle();
        CrearTablaDetalle();
    }//GEN-LAST:event_tblVentaMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        BuscarVenta();
        CrearTabla();
        CantidadTotal();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnVerDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerDetalleActionPerformed
        if (tblVenta.getSelectedRows().length > 0) {

            if (n == 0) {
                this.setSize(769, 507);
                n = 1;
                btnVerDetalle.setText("Ocultar Detalle");
            } else if (n == 1) {
                this.setSize(769, 338);
                n = 0;
                btnVerDetalle.setText("Ver Detalle");
            }
            BuscarVentaDetalle();
            CrearTablaDetalle();
        } else {
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro de venta!");
        }

    }//GEN-LAST:event_btnVerDetalleActionPerformed

    private void tblDetalleVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleVentaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetalleVentaMouseClicked

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void btnConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConcluirActionPerformed
        // TODO add your handling code here:
        if (lblIdVenta.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila");
        } else {
            FrmVenta.btnNuevo.doClick();
            PasarVenta();
        }

    }//GEN-LAST:event_btnConcluirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnConcluir;
    private javax.swing.JButton btnVerDetalle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblIdVenta;
    private javax.swing.JTable tblDetalleVenta;
    private javax.swing.JTable tblVenta;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
