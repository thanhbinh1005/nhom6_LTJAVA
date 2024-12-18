/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.Login;

import view.User.QuanLyNguoiDung;
import controller.ControllerKhachHang;
import javax.swing.JOptionPane;
import java.sql.*;
import database.DBConnection;
import java.util.ArrayList;
import javax.swing.JTextField;
import model.KhachHang;
import view.Admin.QuanLyCuaHang;


/**
 *
 * @author Admin
 */
public class DangNhap extends javax.swing.JFrame {
    private ControllerKhachHang ctlerKhachHang = new ControllerKhachHang();
    private Connection conn = DBConnection.connect();
    private QuanLyCuaHang qly = new QuanLyCuaHang();
    private ArrayList<KhachHang> dsKhachHang = qly.getDsKhachHang();
    public static String currentMaKhachHang = "";
    

    /**
     * Creates new form DangNhap
     */
    public DangNhap() {
        initComponents();
        setLocationRelativeTo(null);
        loadKhachHangFromDB();
        
        
    }

    public String getTxtUsername() {
        return txtUsername.getText().trim();
    }
    
    
    public void loadKhachHangFromDB(){
        
//        ArrayList<KhachHang> dsKhachHang = qly.getDsKhachHang();
        dsKhachHang.clear();
        String sql = "SELECT * FROM khach_hang";
        try (Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String makh = rs.getString("ma_khach_hang");
                String matKhau = rs.getString("mat_khau");
                String tenkh = rs.getString("ho_ten");
                String chucVu = rs.getString("chuc_vu");
                String gioiTinh = rs.getString("gioi_tinh");
                String sdt = rs.getString("so_dien_thoai");
                String diaChi = rs.getString("dia_chi");
                
                dsKhachHang.add(new KhachHang(makh, matKhau, tenkh, chucVu, gioiTinh, sdt, diaChi));
            }
            System.out.println("Tai Du Lieu Thanh Cong");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu khách hàng từ CSDL!");

        }
//        for (KhachHang khachHang : dsKhachHang) {
//            System.out.println(khachHang);
//        }
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
        btnExit = new javax.swing.JButton();
        btnDangKyTK = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        rdbAdmin = new javax.swing.JRadioButton();
        rdbUser = new javax.swing.JRadioButton();
        lblQuenMatKhau = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        btnDangKyTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Update.png"))); // NOI18N
        btnDangKyTK.setText("Sign up");
        btnDangKyTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangKyTKActionPerformed(evt);
            }
        });
        getContentPane().add(btnDangKyTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, -1, 30));

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reset.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, -1, -1));

        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/login.png"))); // NOI18N
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, -1, -1));

        buttonGroup1.add(rdbAdmin);
        rdbAdmin.setText("Admin");
        getContentPane().add(rdbAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, 20));

        buttonGroup1.add(rdbUser);
        rdbUser.setText("User");
        getContentPane().add(rdbUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, -1, 20));

        lblQuenMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblQuenMatKhau.setText("Quên Mật Khẩu");
        lblQuenMatKhau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuenMatKhauMouseClicked(evt);
            }
        });
        getContentPane().add(lblQuenMatKhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, -1, -1));

        txtPassword.setText("Mã Khách Hàng....");
        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasswordMouseClicked(evt);
            }
        });
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 130, 250, -1));

        jLabel3.setText("Password");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 60, -1));

        jLabel2.setText("Username");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 60, -1));

        txtUsername.setText("Mã Khách Hàng....");
        txtUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUsernameMouseClicked(evt);
            }
        });
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 90, 250, -1));

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        jLabel1.setText("LOG In ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 380, 10));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GiaoDienDangNhap.png"))); // NOI18N
        jLabel5.setText("12");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 425, 260));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        loadKhachHangFromDB(); // load dữ liệu từ database 
        
        String userName = txtUsername.getText().trim();// lấy dữ liệu tên đăng nhập tử text
        String passWord = new String(txtPassword.getPassword()); //lấy dữ liệu tên đăng nhập tử passfield
        
        StringBuilder sb = new StringBuilder(); // kiểm tra xem có dữ liệu nhập ở text không
        if(userName.equals(""))
            sb.append("Username is empty!\n");
        if(passWord.equals(""))
            sb.append("Password is empty!\n");
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this, sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String selectedRole = "";
        if (rdbAdmin.isSelected()) {
            selectedRole = "Admin"; // Nếu chọn Admin
        } else if (rdbUser.isSelected()) {
            selectedRole = "User";  // Nếu chọn User
        }else { // kiểm tra chức vụ đã chọn chưa
            JOptionPane.showMessageDialog(this, "Yêu cầu chọn chức vụ","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ctlerKhachHang.checkTK(userName, passWord); // kiểm tra khách hàng có tồn tại trong database không

        boolean isLoggedIn = false;
        for (KhachHang user : dsKhachHang) {
            System.out.println("Checking user: " + user.getMaKhachHang());

            if (userName.trim().equals(user.getMaKhachHang().trim()) &&
                passWord.trim().equals(user.getMatkhau().trim()) &&
                user.getChucVu().trim().equalsIgnoreCase(selectedRole)) { // kiểm tra dữ liệu đã nhập đủ và đúng chưa

                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                currentMaKhachHang = user.getMaKhachHang();
                isLoggedIn = true;
                if(user.getChucVu().trim().equalsIgnoreCase("Admin")) {
                    QuanLyCuaHang qly = new QuanLyCuaHang();
                    qly.setVisible(true);// mở menu Admin
                    this.setVisible(false);
                }
                // Nếu là User, chuyển đến Quản Lý Người Dùng
                else if(user.getChucVu().trim().equalsIgnoreCase("User")) {
                    QuanLyNguoiDung qlyNguoiDung = new QuanLyNguoiDung();
                    qlyNguoiDung.setVisible(true); // mở menu người dùng
                    this.setVisible(false);
                }

                return;
                }
        }

        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(this, "Tên Đăng Nhập Hoặc Mật Khẩu Không Chính Xác", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        txtPassword.setText("");
        txtUsername.setText("");
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to exit","",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnDangKyTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangKyTKActionPerformed
        // TODO add your handling code here:
        DangKyTaiKhoan dky = new DangKyTaiKhoan();
        dky.setVisible(true);
    }//GEN-LAST:event_btnDangKyTKActionPerformed

    private void lblQuenMatKhauMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuenMatKhauMouseClicked
        // TODO add your handling code here:
        QuenMatKhau sqmk = new QuenMatKhau();
        sqmk.setVisible(true);
    }//GEN-LAST:event_lblQuenMatKhauMouseClicked

    private void txtUsernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsernameMouseClicked
        // TODO add your handling code here:
        if(txtUsername.getText().equals("Mã Khách Hàng....")) {
            txtUsername.setText("");
        }
        
    }//GEN-LAST:event_txtUsernameMouseClicked

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked
        // TODO add your handling code here:
        String passWord = new String(txtPassword.getPassword());
        if(passWord.equals("Mã Khách Hàng....")) {
            txtPassword.setText("");
        }
    }//GEN-LAST:event_txtPasswordMouseClicked

    
//    private void showTT(){
//        txtPassword.setText(rememPass);
//        txtUsername.setText(rememName);
//    }
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
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangNhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangKyTK;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnReset;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblQuenMatKhau;
    private javax.swing.JRadioButton rdbAdmin;
    private javax.swing.JRadioButton rdbUser;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
