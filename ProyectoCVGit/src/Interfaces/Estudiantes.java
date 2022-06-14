/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import ConexionSQL.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Spc
 */
public class Estudiantes extends javax.swing.JFrame {

    /**
     * Creates new form Estudiantes
     */
    Integer fila;

    public Estudiantes() {
        initComponents();
        cargarTablaEstudiantes();
        bloquearTextos();
        bloquearBotones();
        cargarTextos();
    }

    public void desbloquearTextos() {

        jtxtCedula.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtDireccion.setEnabled(true);
        jtxtTelefono.setEnabled(true);
    }

    public void desbloquearBotones() {

        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(true);
        jbtnActualizar.setEnabled(false);
        jbtnBorrar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
    }

    public void cargarTablaEstudiantes() {
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            String[] titulos = {"cedula", "nombre", "apellido", "direccion", "telefono"};
            String[] registros = new String[5];
            modelo = new DefaultTableModel(null, titulos);
            Conexion cc = new Conexion();
            java.sql.Connection cn = cc.conectar();
            String sql = "";
            sql = "select* from estudiantes where est_est='activo'";
            java.sql.Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("est_cedula");
                registros[1] = rs.getString("est_nombre");
                registros[2] = rs.getString("est_apellido");
                registros[3] = rs.getString("est_direccion");
                registros[4] = rs.getString("est_telefono");
                modelo.addRow(registros);
            }
            jtblRegistros.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(null, ex);
        }

    }

    public void limpiarTextos() {
        jtxtCedula.setText(" ");
        jtxtNombre.setText(" ");
        jtxtApellido.setText(" ");
        jtxtDireccion.setText(" ");
        jtxtTelefono.setText(" ");
    }

    public void bloquearBotones() {
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
        jbtnActualizar.setEnabled(false);
        jbtnBorrar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
    }

    public void bloquearTextos() {
        jtxtCedula.setEnabled(false);
        jtxtNombre.setEnabled(false);
        jtxtApellido.setEnabled(false);
        jtxtDireccion.setEnabled(false);
        jtxtTelefono.setEnabled(false);
    }

    public void desbloquearBotonesEditarEliminar() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
        jbtnActualizar.setEnabled(true);
        jbtnBorrar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
    }

    public void bloquearTextoCedula() {
        jtxtCedula.setEnabled(false);
        jtxtNombre.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtDireccion.setEnabled(true);
        jtxtTelefono.setEnabled(true);
    }

    public void cargarTextos() {
        jtblRegistros.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtblRegistros.getSelectedRow() != -1) {
                    fila = jtblRegistros.getSelectedRow();
                    jtxtCedula.setText(String.valueOf(jtblRegistros.getValueAt(fila, 0)));
                    jtxtNombre.setText(String.valueOf(jtblRegistros.getValueAt(fila, 1)));
                    jtxtApellido.setText(String.valueOf(jtblRegistros.getValueAt(fila, 2)));
                    jtxtDireccion.setText(String.valueOf(jtblRegistros.getValueAt(fila, 3)));
                    jtxtTelefono.setText(String.valueOf(jtblRegistros.getValueAt(fila, 4)));
                    desbloquearBotonesEditarEliminar();
                    bloquearTextoCedula();
                    cargarTablaEstudiantes();
                }

            }
        });
    }

    public void guardar() {

        String valorTelefono = "S/N";
        if (jtxtCedula.getText().isEmpty() || jtxtCedula.getText() == "") {
            JOptionPane.showMessageDialog(this, "Debe ingresar la cedula");
            jtxtCedula.requestFocus();
        } else if (jtxtNombre.getText().isEmpty() || jtxtNombre.getText() == "") {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre");
            jtxtNombre.requestFocus();

        } else if (jtxtApellido.getText().isEmpty() || jtxtApellido.getText() == "") {
            JOptionPane.showMessageDialog(this, "Debe ingresar el apellido");
            jtxtApellido.requestFocus();

        } else if (jtxtDireccion.getText().isEmpty() || jtxtDireccion.getText() == "") {
            JOptionPane.showMessageDialog(this, "Debe ingresar la direccion");
            jtxtDireccion.requestFocus();

        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "INSERT INTO estudiantes(est_cedula,est_nombre,est_apellido,est_telefono,est_direccion, est_est) values(?,?,?,?,?,'activo')";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, jtxtCedula.getText());
                psd.setString(2, jtxtNombre.getText());
                psd.setString(3, jtxtApellido.getText());
                psd.setString(4, jtxtDireccion.getText());
                if (jtxtTelefono.getText().isEmpty() || jtxtTelefono.getText() == "") {
                    psd.setString(5, valorTelefono);
                } else {
                    psd.setString(5, jtxtTelefono.getText());
                }
                int n = psd.executeUpdate();
                if (n > 0) {
                    //  JOptionPane.showMessageDialog(null, "Se inserto correctamente");
                }
                limpiarTextos();
                bloquearBotones();
                bloquearTextos();
                cargarTablaEstudiantes();

            } catch (SQLException ex) {
                //Logger.getLogger(Estudiantes.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex);

            }
        }
    }

    public void editarDatos() {
        String valorTelefono = "S/N";
        if (jtxtCedula.getText().isEmpty() || jtxtCedula.getText() == "") {
            JOptionPane.showMessageDialog(null, "Debe ingresar la cedula");
            jtxtCedula.requestFocus();
        } else if (jtxtNombre.getText().isEmpty() || jtxtNombre.getText() == "") {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
            jtxtNombre.requestFocus();
        } else if (jtxtApellido.getText().isEmpty() || jtxtApellido.getText() == "") {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Apellido");
            jtxtApellido.requestFocus();
        } else if (jtxtDireccion.getText().isEmpty() || jtxtDireccion.getText() == "") {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Direccion");
            jtxtDireccion.requestFocus();
        } else {
            try {
                Conexion c = new Conexion();
                Connection cn = c.conectar();
                String sql = "";
                if (jtxtTelefono.getText().isEmpty() || jtxtTelefono.getText() == "") {
                    sql = "update estudiantes set est_nombre='" + jtxtNombre.getText()
                            + "' ,est:apellido='" + jtxtApellido.getText() + "' ,est_direccion='"
                            + jtxtDireccion.getText() + "' ,est_telefono='" + valorTelefono
                            + "' WHERE est_cedula='"
                            + jtxtCedula.getText() + "'";
                } else {
                    sql = "update estudiantes set est_nombre='" + jtxtNombre.getText()
                            + "' ,est_apellido='" + jtxtApellido.getText() + "' ,est_direccion='"
                            + jtxtDireccion.getText() + "' ,est_telefono='" + jtxtTelefono.getText()
                            + "' WHERE est_cedula='"
                            + jtxtCedula.getText() + "'";
                }

                PreparedStatement psd = cn.prepareStatement(sql);

                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Se actualizo correctamente");
                    cargarTablaEstudiantes();
                    limpiarTextos();
                    bloquearBotones();
                    bloquearTextos();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void eliminar() {
        if (JOptionPane.showConfirmDialog(new JInternalFrame(),
                "Estas seguro de borrar el registro",
                "Borrar registros", JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

        }
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = " ";
            sql = "DELETE FROM estudiantes WHERE est_cedula='" + jtxtCedula.getText() + "'";
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            // JOptionPane.showMessageDialog(null, psd);
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se elimino correctamente");
                //limpiarTextos();
                cargarTablaEstudiantes();
                bloquearBotones();
                bloquearTextos();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtTelefono = new javax.swing.JTextField();
        jtxtDireccion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblRegistros = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnActualizar = new javax.swing.JButton();
        jbtnBorrar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("CEDULA");

        jLabel2.setText("NOMBRE");

        jLabel3.setText("APELLIDO");

        jLabel4.setText("TELEFONO");

        jLabel5.setText("DIRECCION");

        jtxtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtTelefonoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtApellido)
                            .addComponent(jtxtCedula)
                            .addComponent(jtxtNombre)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(68, 68, 68))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jtblRegistros);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnActualizar.setText("Actualizar");
        jbtnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnActualizarActionPerformed(evt);
            }
        });

        jbtnBorrar.setText("Borrar");

        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jButton1.setText("Inactivar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnBorrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnActualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addComponent(jbtnNuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnNuevo)
                .addGap(18, 18, 18)
                .addComponent(jbtnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jbtnActualizar)
                .addGap(29, 29, 29)
                .addComponent(jbtnBorrar)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jbtnCancelar)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTelefonoActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed

        desbloquearTextos();
        desbloquearBotones();

    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnActualizarActionPerformed
        editarDatos();
    }//GEN-LAST:event_jbtnActualizarActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        // TODO add your handling code here:
        limpiarTextos();
        bloquearBotones();
        bloquearTextos();
        
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Estudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Estudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Estudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Estudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Estudiantes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnActualizar;
    private javax.swing.JButton jbtnBorrar;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JTable jtblRegistros;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtDireccion;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    // End of variables declaration//GEN-END:variables
}
