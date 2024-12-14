/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.ControllerKhachHang;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.KhachHang;

/**
 *
 * @author Admin
 */
public class QuenMatKhau extends javax.swing.JFrame {
    private QuanLyCuaHang qly = new QuanLyCuaHang();
    private ArrayList<KhachHang> dsKhachHang = qly.getDsKhachHang();
    private ControllerKhachHang ctlerkh = new ControllerKhachHang();
    

    /**
     * Creates new form QuenMatKhau
     */
    public QuenMatKhau() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTenTaiKhoan = new javax.swing.JTextField();
        txtTenNguoiDung = new javax.swing.JTextField();
        txtMatKhauMoi = new javax.swing.JTextField();
        btnXacNhanSua = new javax.swing.JButton();
        btnQuayLai = new javax.swing.JButton();
        btnDoiMK = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdbAdmin = new javax.swing.JRadioButton();
        rdbUser = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setText("Lấy Lại Mật Khẩu");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Tên Tài Khoản");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Họ Tên");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("Mật Khẩu Mới");

        btnXacNhanSua.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXacNhanSua.setText("Xác Nhận");
        btnXacNhanSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanSuaActionPerformed(evt);
            }
        });

        btnQuayLai.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnQuayLai.setText("Quay Lại");
        btnQuayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLaiActionPerformed(evt);
            }
        });

        btnDoiMK.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnDoiMK.setText("Đổi MK");
        btnDoiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMKActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Mật Khẩu Hiện Tại");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        jLabel6.setText("(*)Nhập mật khẩu mới nếu thay đổi ");

        buttonGroup1.add(rdbAdmin);
        rdbAdmin.setText("Admin");

        buttonGroup1.add(rdbUser);
        rdbUser.setText("User");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnQuayLai)
                        .addGap(34, 34, 34))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(9, 9, 9))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnDoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(14, 14, 14)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(106, 106, 106)
                                                .addComponent(rdbUser))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(rdbAdmin)
                                        .addGap(98, 98, 98))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTenNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(44, 44, 44)
                                        .addComponent(btnXacNhanSua))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                    .addComponent(txtMatKhauMoi)))
                            .addComponent(jLabel6))
                        .addContainerGap(90, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTenNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbUser)
                    .addComponent(rdbAdmin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatKhauMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDoiMK)
                    .addComponent(btnQuayLai)
                    .addComponent(btnXacNhanSua))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLaiActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnQuayLaiActionPerformed

    private void btnDoiMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMKActionPerformed
        // TODO add your handling code here:
        String tentk = txtTenTaiKhoan.getText().trim();
        String hoten = txtTenNguoiDung.getText().trim();
        String mkmoi = txtMatKhauMoi.getText().trim();
        StringBuilder sb = new StringBuilder();
        
        if(tentk.equals(""))
            sb.append("Thiếu Tên Tài Khoản (Mã Khách Hàng)");
        if(hoten.equals(""))
            sb.append("Thiếu Họ Tên Người Dùng");
        if(mkmoi.equals(""))
            sb.append("Nhập Mật Khẩu Mới Trước Khi Lưu");
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this, sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        } 
        
        String selectedRole = "";
        if (rdbAdmin.isSelected()) {
            selectedRole = "Admin"; // Nếu chọn Admin
        } else if (rdbUser.isSelected()) {
            selectedRole = "User";  // Nếu chọn User
        }else {
            JOptionPane.showMessageDialog(this, "Yêu cầu chọn chức vụ","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println(tentk);
        boolean check = false;
        for (KhachHang khachHang : dsKhachHang) {
            if(khachHang.getChucVu().equals(selectedRole)) {
                if(khachHang.getHoTenKhachHang().equals(hoten) && khachHang.getMaKhachHang().equals(tentk)) {
                    ctlerkh.doiMk(tentk, mkmoi);
                    check = true;
                    break;
                }
            }
        }
        if(!check){
           JOptionPane.showMessageDialog(this, "Tên Tài Khoản Hoặc Họ Tên Không Hợp lệ!","Lỗi",JOptionPane.ERROR_MESSAGE);
           return;
        }
    }//GEN-LAST:event_btnDoiMKActionPerformed

    private void btnXacNhanSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanSuaActionPerformed
        // TODO add your handling code here:
        String tentk = txtTenTaiKhoan.getText().trim();
        String hoten = txtTenNguoiDung.getText().trim();
        StringBuilder sb = new StringBuilder();
        
        if(tentk.equals(""))
            sb.append("Thiếu Tên Tài Khoản (Mã Khách Hàng)");
        if(hoten.equals(""))
            sb.append("Thiếu Họ Tên Người Dùng");
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this, sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String selectedRole = "";
        if (rdbAdmin.isSelected()) {
            selectedRole = "Admin"; // Nếu chọn Admin
        } else if (rdbUser.isSelected()) {
            selectedRole = "User";  // Nếu chọn User
        }else {
            JOptionPane.showMessageDialog(this, "Yêu cầu chọn chức vụ","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean check = false;
        for (KhachHang khachHang : dsKhachHang) {
            if(khachHang.getChucVu().equals(selectedRole)) {
                if(khachHang.getHoTenKhachHang().equals(hoten) && khachHang.getMaKhachHang().equals(tentk)) {
                    txtMatKhau.setText(khachHang.getMatkhau());
                    check = true;
                    break;
                }
            }
        }
        if(!check){
           JOptionPane.showMessageDialog(this, "Tên Tài Khoản Hoặc Họ Tên Không Hợp lệ!","Lỗi",JOptionPane.ERROR_MESSAGE);
           return;
        }
    }//GEN-LAST:event_btnXacNhanSuaActionPerformed

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
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuenMatKhau().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiMK;
    private javax.swing.JButton btnQuayLai;
    private javax.swing.JButton btnXacNhanSua;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton rdbAdmin;
    private javax.swing.JRadioButton rdbUser;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtMatKhauMoi;
    private javax.swing.JTextField txtTenNguoiDung;
    private javax.swing.JTextField txtTenTaiKhoan;
    // End of variables declaration//GEN-END:variables
}