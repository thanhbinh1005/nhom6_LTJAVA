/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.ControllerHoaDon;
import controller.ControllerKhachHang;
import controller.ControllerSQL;
import controller.controllerSanPham;
import database.DBConnection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.HoaDon;
import model.KhachHang;
import model.SanPham;
import view.QuanLyCuaHang;

/**
 *
 * @author Admin
 */
public class QuanLyNguoiDung extends javax.swing.JFrame {
    private Connection conn = DBConnection.connect();
    private QuanLyCuaHang qly = new QuanLyCuaHang();
    private ArrayList<KhachHang> dsKhachHang = qly.getDsKhachHang();
    private ArrayList<SanPham> dsSanPham = qly.getDsSanPham();
    private ArrayList<HoaDon> dsHoaDon = qly.getDsHoaDon();
    private ArrayList<SanPham> dsGioHang = qly.getDsGioHang();
    int posTableHD;
    int posTableDs;
    private String User_maKh = DangNhap.currentMaKhachHang; 
    
    ControllerKhachHang ctlerKhachHang = new ControllerKhachHang();
    controllerSanPham ctlerSanPham = new controllerSanPham();
    ControllerSQL controllerSQL = new ControllerSQL();
    ControllerHoaDon ctlerHoaDon = new ControllerHoaDon(controllerSQL);
    
    public QuanLyNguoiDung() {
        initComponents();
        loadKhachHangFromDB();
        ShowTTCaNhan();
        
        
        qly.loadSanPhamFromDB();
        qly.loadHoaDonFromDB();
        qly.viewTableSP();
        viewTableGioHang();
       viewTableSP();
    }
    
    public void ShowTTCaNhan(){
        for (KhachHang khachHang : dsKhachHang) {
            if(khachHang.getMaKhachHang().trim().equals(User_maKh)) {
                txtMaKH.setText(User_maKh);
                txtHoTenKH.setText(khachHang.getHoTenKhachHang());
                txtSDT.setText(khachHang.getSoDienThoai());
                txtDiaChi.setText(khachHang.getDiaChi());
                txtGioiTinhCaNhan.setText(khachHang.getGioiTinh());
                txtMatKhauCaNhan.setText(khachHang.getMatkhau());
                
                txtMaKhHD.setText(User_maKh);
                txtTenHD.setText(khachHang.getHoTenKhachHang());
                txtSDTHD.setText(khachHang.getSoDienThoai());
                txtDiaChiHD.setText(khachHang.getDiaChi());
                txtGtHD.setText(khachHang.getGioiTinh());
                
                txtHDMaKH.setText(User_maKh);
                txtHDTen.setText(khachHang.getHoTenKhachHang());
                txtHDSDT.setText(khachHang.getSoDienThoai());
                txtHDDiaChi.setText(khachHang.getDiaChi());
                txtHDGioiTinh.setText(khachHang.getGioiTinh());
//                System.out.println(khachHang.getMatkhau());
//                System.out.println(khachHang);
                break;
            }
        }
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
        for (KhachHang khachHang : dsKhachHang) {
            System.out.println(khachHang);
        }
    }
    
    public void loadSanPhamFromDB(){
        dsSanPham.clear();
        String sql = "SELECT * FROM san_pham";
        try (Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String masp = rs.getString("ma_san_pham");
                String tensp = rs.getString("ten_san_pham");
                int soluong = rs.getInt("so_luong");
                double giaca = rs.getDouble("gia_ca");
                
                dsSanPham.add(new SanPham(masp, tensp, soluong, giaca));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu Sản Phẩm từ CSDL!");
        }
    }
    
    //tải lại bảng Sản Phẩm
    public void viewTableSP(){
        DefaultTableModel modelHoaDon = (DefaultTableModel) this.tblDsSP.getModel();

        modelHoaDon.setRowCount(0);
        int n = 1;
        int m = 1;
        int k = 1;
        for (SanPham sanPham : dsSanPham) {
            modelHoaDon.addRow(new Object[]{
                k++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
        }
    }
    
    
//tải lại bảng Giỏ Hàng
    public void viewTableGioHang(){
        DefaultTableModel modelGioHang = (DefaultTableModel) this.TblGioHang.getModel();
        
        modelGioHang.setRowCount(0);

        int k = 1;
        for (SanPham sanPham : dsGioHang) {
            modelGioHang.addRow(new Object[]{
                k++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia(),sanPham.getSoLuong() * sanPham.getGia()});
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        txtHoTenKH = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnCapNhatKH = new javax.swing.JButton();
        txtGioiTinhCaNhan = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtMatKhauCaNhan = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDsSP = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtHDMaKH = new javax.swing.JTextField();
        txtHDTen = new javax.swing.JTextField();
        txtHDGioiTinh = new javax.swing.JTextField();
        txtHDSDT = new javax.swing.JTextField();
        txtHDDiaChi = new javax.swing.JTextField();
        btnResetHD = new javax.swing.JButton();
        btnLuuHD = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        TblGioHang = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        btnAddGioHang = new javax.swing.JButton();
        txtSoLuongSP = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btnXoaSpTrongHdon = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        txtMAXX = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtMaHoaDon = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblHDKH = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtMaKhHD = new javax.swing.JTextField();
        txtTenHD = new javax.swing.JTextField();
        txtGtHD = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtSDTHD = new javax.swing.JTextField();
        txtDiaChiHD = new javax.swing.JTextField();
        txtTongTienHD = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtFindThongTin = new javax.swing.JTextField();
        btnFindMaHoaDon = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        btnXoaTTKH = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        txtMaHoaDonAddDs = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtNgayTao = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDsHoaDonCuaKhach = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtTimMaHoaDon = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Ma khach hang:");

        jLabel10.setText("Ho Ten khach hang: ");

        jLabel11.setText("So dien thoai: ");

        jLabel12.setText("Dia chi:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel7.setText("Thông Tin Cá Nhân");

        jLabel13.setText("Gioi tinh: ");

        btnCapNhatKH.setText("Cập nhật");
        btnCapNhatKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatKHActionPerformed(evt);
            }
        });

        jLabel1.setText("Mật Khẩu");

        jButton1.setText("Xóa tài Khoản");

        jButton2.setText("Thoát");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(91, 91, 91)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDiaChi)
                            .addComponent(txtMatKhauCaNhan)
                            .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMaKH)
                            .addComponent(txtHoTenKH))
                        .addGap(409, 409, 409))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtGioiTinhCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(btnCapNhatKH)
                        .addGap(170, 170, 170)
                        .addComponent(jButton1)
                        .addGap(214, 214, 214)
                        .addComponent(jButton2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(297, 297, 297)
                        .addComponent(jLabel7)))
                .addContainerGap(223, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel11)))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtGioiTinhCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatKhauCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhatKH)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(378, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông Tin Cá Nhân", jPanel3);

        tblDsSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Giá Thành"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDsSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDsSPMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblDsSP);

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel19.setText("Tạo Hóa Đơn");

        jLabel22.setText("THONG TIN KHACH HANG");

        jLabel23.setText("Mã Khách hàng");

        jLabel24.setText("Tên khách hàng");

        jLabel25.setText("Giới tính");

        jLabel26.setText("Số điện thoại");

        jLabel27.setText("Địa chỉ");

        txtHDMaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHDMaKHActionPerformed(evt);
            }
        });

        btnResetHD.setText("Reset");
        btnResetHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetHDActionPerformed(evt);
            }
        });

        btnLuuHD.setText("Lưu Hóa Đơn");
        btnLuuHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuHDActionPerformed(evt);
            }
        });

        TblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Số lượng", "Giá Sản phảm", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(TblGioHang);

        jLabel28.setText("GIỎ HÀNG");

        btnAddGioHang.setText("Thêm vào giỏ hàng");
        btnAddGioHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGioHangActionPerformed(evt);
            }
        });

        jLabel29.setText("Số Lượng");

        btnXoaSpTrongHdon.setText("Xóa Sản Phẩm");
        btnXoaSpTrongHdon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSpTrongHdonActionPerformed(evt);
            }
        });

        jLabel37.setText("MAX");

        jLabel43.setText("Mã Hóa Đơn");

        jButton3.setText("Thoát");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnAddGioHang)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel29)
                                .addGap(50, 50, 50)
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 605, Short.MAX_VALUE)
                                .addComponent(btnXoaSpTrongHdon)
                                .addGap(49, 49, 49))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtSoLuongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMAXX, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(137, 137, 137)
                                .addComponent(btnLuuHD)
                                .addGap(50, 50, 50)
                                .addComponent(btnResetHD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHDTen, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                    .addComponent(txtHDMaKH, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtMaHoaDon))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7)
                                        .addComponent(txtHDGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtHDSDT, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                            .addComponent(txtHDDiaChi))))))
                        .addGap(167, 167, 167))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(jSeparator3)
                            .addComponent(jScrollPane5))
                        .addGap(16, 16, 16))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(394, 394, 394)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel19)
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtHDSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtHDDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtHDGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtHDMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtHDTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(8, 8, 8)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel29)
                                .addComponent(jLabel37))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnAddGioHang)
                                    .addComponent(txtSoLuongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMAXX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnResetHD)
                                    .addComponent(btnLuuHD)
                                    .addComponent(jButton3))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXoaSpTrongHdon)
                        .addGap(123, 123, 123))))
        );

        jTabbedPane1.addTab("Hóa Đơn", jPanel6);

        tblHDKH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Giá Thành", "Tổng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblHDKH);

        jLabel30.setText("Mã Khách Hàng");

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel31.setText("Thông Tin Hóa Đơn");

        jLabel32.setText("Tên Khách Hàng");

        jLabel33.setText("Giới Tính");

        jLabel34.setText("SDT");

        jLabel35.setText("Địa Chỉ");

        jLabel36.setText("Tổng Tiền");

        btnFindMaHoaDon.setText("Tìm Kiếm Mã Hóa Đơn");
        btnFindMaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindMaHoaDonActionPerformed(evt);
            }
        });

        btnXoaTTKH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTTKH.setText("Xóa Hóa Đơn");
        btnXoaTTKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTTKHActionPerformed(evt);
            }
        });

        btnThoat.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        jLabel44.setText("Mã Hóa Đơn");

        txtMaHoaDonAddDs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHoaDonAddDsActionPerformed(evt);
            }
        });

        jLabel46.setText("Ngày Tạo");

        tblDsHoaDonCuaKhach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Hóa Đơn", "Sản Phẩm", "Số Lượng", "Giá Thành"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDsHoaDonCuaKhach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDsHoaDonCuaKhachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDsHoaDonCuaKhach);

        jLabel2.setText("Tìm Mã Hóa Đơn");

        txtTimMaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1)
                        .addGap(68, 68, 68)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnThoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXoaTTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(txtTimMaHoaDon))
                        .addGap(190, 190, 190))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addComponent(jScrollPane7)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(jLabel31)
                                        .addGap(33, 33, 33)
                                        .addComponent(btnFindMaHoaDon)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                                .addComponent(jLabel38)
                                .addGap(388, 388, 388))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel30)
                                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel44))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtMaKhHD, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                        .addComponent(txtTenHD)
                                        .addComponent(txtGtHD))
                                    .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(92, 92, 92)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel46)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                            .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                            .addGap(80, 80, 80)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                                .addComponent(txtDiaChiHD)
                                                .addComponent(txtTongTienHD)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(btnFindMaHoaDon)
                    .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(16, 16, 16)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(txtMaKhHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44))
                        .addGap(52, 52, 52)))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(txtDiaChiHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jLabel39)
                .addGap(24, 24, 24)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaTTKH)
                        .addGap(18, 18, 18)
                        .addComponent(btnThoat)))
                .addContainerGap(89, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông Tin Hóa Đơn", jPanel12);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 846, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaHoaDonAddDsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHoaDonAddDsActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtMaHoaDonAddDsActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);
        
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnXoaTTKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTTKHActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xóa Hóa Đơn Khach Hang","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;
        
        String maHoaDon = tblDsHoaDonCuaKhach.getValueAt(posTableDs, 1).toString();
        System.out.println(maHoaDon);

        for (HoaDon hoaDon : dsHoaDon) {
            if(hoaDon.getMaHoaDon().equals(maHoaDon)) {
                for (SanPham dsSp : hoaDon.getDsMuaSanPham()) {
                    for (SanPham sanPham : dsSanPham) {
                        if(dsSp.getMaSanPham().equals(sanPham.getMaSanPham())) {
                            sanPham.setSoLuong(sanPham.getSoLuong() + dsSp.getSoLuong());
                            break;
                        }
                    }
                }
            }
        }

        qly.viewTableSP();
        HoaDon hoaDonXoa = null;
        for (HoaDon hoaDon : dsHoaDon) {
            if (hoaDon.getMaHoaDon().equalsIgnoreCase(maHoaDon)) {
                hoaDonXoa = hoaDon;
                break;
            }
        }

        if (hoaDonXoa != null) {
            dsHoaDon.remove(hoaDonXoa);
        }
        qly.capNhatBanRaXoa(hoaDonXoa);// cập nhật lại số lượng sản phẩm sau khi hủy hóa đơn
        qly.capNhatDsBanRa();// cập nhật lại bảng doanh thu và tổng tiền doanh thu
        //        for (HoaDon hoaDon : dsHoaDon) {
            //            System.out.println(hoaDon);
            //        }

        qly.viewTableDoanhThu();
        qly.viewKhachHangDK();

        //xóa chi tiết hóa đơn trước rồi mới xóa hóa đơn
        ctlerHoaDon.deleteHoaDon(maHoaDon);
        JOptionPane.showMessageDialog(this, "Hóa đơn và thông tin khách hàng đã được xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnXoaTTKHActionPerformed

    private void btnXoaSpTrongHdonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpTrongHdonActionPerformed
        // TODO add your handling code here:
        int selectRow = TblGioHang.getSelectedRow();

        int choice = JOptionPane.showConfirmDialog(this, "Ban có muốn xóa không","Thông Báo!",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;
        else {
            for (int i = 0; i < dsGioHang.size(); i++) {
                if(dsGioHang.get(i).getMaSanPham().equals(dsGioHang.get(selectRow).getMaSanPham())) {
                    dsGioHang.remove(i);
                }
            }
        }
        viewTableGioHang();
    }//GEN-LAST:event_btnXoaSpTrongHdonActionPerformed

    private void btnAddGioHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGioHangActionPerformed
        //        dsGioHang.clear();
        DefaultTableModel model = (DefaultTableModel) this.TblGioHang.getModel();
        model.setRowCount(0);
        String inputSoLuongSP = txtSoLuongSP.getText().trim();
        int soLuongNew = 0;
        try {
            soLuongNew = Integer.parseInt(inputSoLuongSP);
            if(soLuongNew <= 0) {
                JOptionPane.showMessageDialog(this, "Số Lượng Phải Lớn Hơn 0)!\n","Lỗi",JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số lượng!\n","Lỗi",JOptionPane.ERROR_MESSAGE);
            viewTableGioHang();
            return;
        }

        SanPham selectSP = dsSanPham.get(posTableHD);
        String masp = selectSP.getMaSanPham();
        String tensp = selectSP.getTenSanPham();
        int soluong;
        double giaca = selectSP.getGia();

        if(soLuongNew > selectSP.getSoLuong()) {
            JOptionPane.showMessageDialog(this, "Vượt quá số lượng cho phép!","lỗi",JOptionPane.ERROR_MESSAGE);
            txtSoLuongSP.setText("");
            viewTableGioHang();
            return;
        }
        else {
            soluong = soLuongNew;
        }
        SanPham s1 = new SanPham(masp, tensp, soluong, giaca);
        int n = 1;
        for (SanPham sanPham : dsGioHang) {
            if(sanPham.getMaSanPham().equals(selectSP.getMaSanPham())) {
                JOptionPane.showMessageDialog(this, "Đã Tồn Tại Sản Phẩm Trong Giỏ Hàng","Thông Báo!",JOptionPane.INFORMATION_MESSAGE);
                txtSoLuongSP.setText("");
                viewTableGioHang();
                return;
            }
        }
        dsGioHang.add(s1);
        for (SanPham sanPham : dsGioHang) {
            model.addRow(new Object[]{
                n++,
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.getSoLuong(),
                sanPham.getGia(),
                sanPham.getGia() * sanPham.getSoLuong()
            });
        }
        txtSoLuongSP.setText("");
        txtMAXX.setText("");
    }//GEN-LAST:event_btnAddGioHangActionPerformed

    private void btnLuuHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuHDActionPerformed

        // Lấy thông tin khách hàng và hóa đơn
        String maKh = txtHDMaKH.getText().trim();
        String tenKh = txtHDTen.getText().trim();
        String gioiTinh = txtHDGioiTinh.getText().trim();
        String sdt = txtHDSDT.getText().trim();
        String diaChi = txtHDDiaChi.getText().trim();
        String maHoaDon = txtMaHoaDon.getText().trim();

        // lấy thời gian hiện tại
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");// định dạng và đưa về String
        String dateString = sdf.format(d);

        // Kiểm tra nếu thông tin khách hàng không hợp lệ
        if (maKh.isEmpty() || tenKh.isEmpty() || gioiTinh.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || maHoaDon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra nếu giỏ hàng trống
        if(dsGioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn và nhập số lượng sản phẩm!", "Thông Báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (HoaDon hoaDon : dsHoaDon) {
            if(maHoaDon.equals(hoaDon.getMaHoaDon())) {
                JOptionPane.showMessageDialog(this, "Đã Tồn Tại Mã Hóa Đơn Trong Danh Sách!","Lỗi",JOptionPane.ERROR_MESSAGE);
                txtMaHoaDon.setText("");
                return;
            }
        }

        // Tạo đối tượng khách hàng mới
        KhachHang newkh = new KhachHang(maKh, tenKh, gioiTinh, sdt, diaChi);

        // Tạo đối tượng hóa đơn mới
        HoaDon newHD = new HoaDon(maHoaDon, newkh,dateString);

        // Thêm sản phẩm vào hóa đơn từ giỏ hàng
        for (SanPham sanPham : dsGioHang) {
            newHD.themSanPham(sanPham);
        }

        // Thêm hóa đơn vào danh sách
        dsHoaDon.add(newHD);

        // Cập nhật lại các bảng
        qly.capNhatBanRaLuu(newHD);
        qly.viewTableDoanhThu();

        // Cập nhật số lượng sản phẩm trong danh sách sản phẩm
        for (SanPham giohang : dsGioHang) {
            for (SanPham sanPham : dsSanPham) {
                if (sanPham.getMaSanPham().equals(giohang.getMaSanPham())) {
                    sanPham.setSoLuong(sanPham.getSoLuong() - giohang.getSoLuong());
                }
            }
        }

        // Cập nhật lại bảng sản phẩm
        qly.viewTableSP();

        //Lưu hóa đơn vào database
        ctlerHoaDon.saveHoaDon1(newHD);
        ctlerHoaDon.saveHoaDon2(newHD);

        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(this, "Hóa đơn đã được lưu thành công!", "Lưu Hóa Đơn", JOptionPane.INFORMATION_MESSAGE);

        // Làm sạch các trường nhập
        txtHDDiaChi.setText("");
        txtHDGioiTinh.setText("");
        txtHDMaKH.setText("");
        txtHDSDT.setText("");
        txtHDTen.setText("");
        txtMaHoaDon.setText("");
        txtMAXX.setText("");
        qly.capNhatDsBanRa();

        // Làm sạch giỏ hàng và bảng giỏ hàng
        dsGioHang.clear();
        DefaultTableModel model2 = (DefaultTableModel) this.TblGioHang.getModel();
        model2.setNumRows(0);

        // Cập nhật lại danh sách khách hàng
        qly.viewKhachHangDK();
    }//GEN-LAST:event_btnLuuHDActionPerformed

    private void btnResetHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetHDActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn reset hóa đơn không","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;
        else {
            txtHDDiaChi.setText("");
            txtHDGioiTinh.setText("");
            txtHDMaKH.setText("");
            txtHDSDT.setText("");
            txtHDTen.setText("");
            txtMAXX.setText("");
            dsGioHang.clear();
            DefaultTableModel model = (DefaultTableModel) this.TblGioHang.getModel();
            model.setNumRows(0);
        }
    }//GEN-LAST:event_btnResetHDActionPerformed

    private void txtHDMaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHDMaKHActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.tblHDKH.getModel();
        model.setRowCount(0);
        HoaDon hoaDon = new HoaDon();
        txtHDMaKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = 1;
                for (KhachHang khachHang : dsKhachHang) {
                    if(txtHDMaKH.getText().equals(khachHang.getMaKhachHang())) {
                        txtHDTen.setText(khachHang.getHoTenKhachHang());
                        txtHDSDT.setText(khachHang.getSoDienThoai());
                        txtHDGioiTinh.setText(khachHang.getGioiTinh());
                        txtHDDiaChi.setText(khachHang.getDiaChi());
                        HoaDon hoaDon = new HoaDon();

                        for (SanPham sanPham : hoaDon.getDsMuaSanPham()) {
                            model.addRow(new Object[]{
                                n++,
                                sanPham.getMaSanPham(),
                                sanPham.getTenSanPham(),
                                sanPham.getSoLuong(),
                                sanPham.getGia(),
                                sanPham.getGia() * sanPham.getSoLuong()
                            });
                        }
                    }
                }
            }
        });
    }//GEN-LAST:event_txtHDMaKHActionPerformed

    private void tblDsSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDsSPMouseClicked
        // TODO add your handling code here:
        posTableHD = tblDsSP.getSelectedRow();
        int selectRow = tblDsSP.getSelectedRow();

        if(selectRow >= 0) {
            String sanphamid = (String) tblDsSP.getValueAt(selectRow, 1);
            String tensp = (String) tblDsSP.getValueAt(selectRow, 2);  // Tên sản phẩm (String)

            int soluong = (int) tblDsSP.getValueAt(selectRow, 3);  // Số lượng (int)

            double giaca = (double) tblDsSP.getValueAt(selectRow, 4);  // Giá cả (double)

            for (SanPham sanPham : dsSanPham) {
                if(sanphamid.equals(sanPham.getMaSanPham())) {
                    txtMAXX.setText(String.valueOf(soluong));
                    break;
                }
            }
        }
    }//GEN-LAST:event_tblDsSPMouseClicked

    private void btnCapNhatKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatKHActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtMaKH.getText().trim().equals("")){
            sb.append("Thieu Ma Khach Hang!\n");
            txtMaKH.setBackground(Color.red);
        } else
        txtMaKH.setBackground(Color.white);
        if(txtHoTenKH.getText().trim().equals("")){
            sb.append("Thieu Ho Ten Khach Hang!\n");
            txtHoTenKH.setBackground(Color.red);
        } else
        txtHoTenKH.setBackground(Color.white);
        if(txtSDT.getText().trim().equals("")){
            sb.append("Thieu SDT Khach Hang!\n");
            txtSDT.setBackground(Color.red);
        } else
            txtSDT.setBackground(Color.white);
        if(txtGioiTinhCaNhan.getText().trim().equals("") ){
            sb.append("Thieu SDT Khach Hang!\n");
            txtGioiTinhCaNhan.setBackground(Color.red);
        } else if(txtGioiTinhCaNhan.getText().toUpperCase().trim().equals("Nam") || txtGioiTinhCaNhan.getText().toUpperCase().trim().equals("Nữ")){
            return;
        }else
            JOptionPane.showMessageDialog(this, "vui lòng Nhập đúng giới tính","Lỗi",JOptionPane.ERROR_MESSAGE);
        if(txtDiaChi.getText().trim().equals("")){
            sb.append("Thieu Dia Chi Khach Hang!\n");
            txtDiaChi.setBackground(Color.red);
        } else
            txtDiaChi.setBackground(Color.white);
        if(txtDiaChi.getText().trim().equals("")){
            sb.append("Thieu Dia Chi Khach Hang!\n");
            txtDiaChi.setBackground(Color.red);
        } else
            txtDiaChi.setBackground(Color.white);
        if(txtMatKhauCaNhan.getText().trim().equals("")){
            sb.append("Thieu Dia Chi Khach Hang!\n");
            txtMatKhauCaNhan.setBackground(Color.red);
        } else
            txtMatKhauCaNhan.setBackground(Color.white);

        if(sb.length()>0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Ban co muon cap nhat thong tin khong!","Hoi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION){
            return;
        }
        boolean check = true;
        for (HoaDon hoaDon : dsHoaDon) { // duyệt xem khách hàng có trong hóa đơn không nếu có báo lỗi k thể xóa
            for (KhachHang kh : dsKhachHang) {
                if(hoaDon.getTTKhachHang().getMaKhachHang().equals(txtMaKH.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Khách Hàng Đã Có Hóa Đơn!\n\tYêu Cầu Xóa Hóa Đơn Trước Khi Thay Đổi Thông Tin Sản Phẩm!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                    check = false;
                    break;
                }
            }
        }

        if(check) {// nếu không báo lỗi được phép xóa sp
            // chuyển giới tính chọn bởi combobox thành dãy string cho dễ
            // set lại mọi thứ cho khách
            for (KhachHang khachHang : dsKhachHang) {
                if(khachHang.getMaKhachHang().equals(txtMaKH.getText().trim())){
                    khachHang.setMaKhachHang(txtMaKH.getText().trim());
                    khachHang.setHoTenKhachHang(txtHoTenKH.getText().trim());
                    khachHang.setGioiTinh(txtGioiTinhCaNhan.getText().trim());
                    khachHang.setSoDienThoai(txtSDT.getText().trim());
                    khachHang.setDiaChi(txtDiaChi.getText().trim());
                    khachHang.setMatkhau(txtMatKhauCaNhan.getText().trim());
                }
            }
            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            //gọi hàm update để sửa thông tin khách
            ctlerKhachHang.updateKhachHangToDB(txtMaKH.getText(),txtMatKhauCaNhan.getText().trim(), txtHoTenKH.getText(),txtGioiTinhCaNhan.getText().trim(), txtSDT.getText(), txtDiaChi.getText());
        }

        qly.viewTableKH();
        //reset ô txt
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
    }//GEN-LAST:event_btnCapNhatKHActionPerformed

    private void btnFindMaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindMaHoaDonActionPerformed
        // TODO add your handling code here:

        StringBuilder sb = new StringBuilder();
        if(txtFindThongTin.getText().equals(""))
        sb.append("Vui Lòng Nhập Tên Khách Hàng Trước Khi Nhấn Tìm Kiếm!");
        if(sb.length()> 0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) this.tblHDKH.getModel();
        model.setRowCount(0);
        HoaDon hoaDon = new HoaDon();
        int n = 1;
        double sum = 0;
        boolean check = false;
        for (HoaDon hoaDon1 : dsHoaDon) {
            //            System.out.println("Checking: " + hoaDon1.getTTKhachHang().getMaKhachHang());
            if (txtFindThongTin.getText().toLowerCase().contains(hoaDon1.getMaHoaDon())) {
                txtMaHoaDonAddDs.setText(hoaDon1.getMaHoaDon());
                txtMaKhHD.setText(hoaDon1.getTTKhachHang().getMaKhachHang());
                txtTenHD.setText(hoaDon1.getTTKhachHang().getHoTenKhachHang());
                txtSDTHD.setText(hoaDon1.getTTKhachHang().getSoDienThoai());
                txtGtHD.setText(hoaDon1.getTTKhachHang().getGioiTinh());
                txtDiaChiHD.setText(hoaDon1.getTTKhachHang().getDiaChi());
                txtNgayTao.setText(hoaDon1.getNgayLap());
                check = true;

                // Lặp qua danh sách sản phẩm trong hóa đơn để hiển thị lên bảng
                for (SanPham sanPham : hoaDon1.getDsMuaSanPham()) {
                    sum+=sanPham.getGia() * sanPham.getSoLuong();
                    model.addRow(new Object[]{
                        n++,  // Số thứ tự
                        sanPham.getMaSanPham(),
                        sanPham.getTenSanPham(),
                        sanPham.getSoLuong(),
                        sanPham.getGia(),
                        sanPham.getGia() * sanPham.getSoLuong()  // Tính tổng tiền cho sản phẩm
                    });
                    System.out.println(sum);
                }
                txtTongTienHD.setText(String.valueOf(sum));
                break;
            }
        }
        if(!check) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy Thông Tin Trong Danh Sách!");
        }
    }//GEN-LAST:event_btnFindMaHoaDonActionPerformed

    private void tblDsHoaDonCuaKhachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDsHoaDonCuaKhachMouseClicked
        // TODO add your handling code here:
        posTableDs = tblDsHoaDonCuaKhach.getSelectedRow();
    }//GEN-LAST:event_tblDsHoaDonCuaKhachMouseClicked

    private void txtTimMaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaHoaDonActionPerformed
        // TODO add your handling code here:
        String maHoaDon = txtTimMaHoaDon.getText().trim();
        
        StringBuilder sb = new StringBuilder();
        if(txtTimMaHoaDon.getText().equals(""))
            sb.append("Vui Lòng Nhập Mã Hóa Đơn Trước Khi Nhấn Tìm Kiếm!");
        if(sb.length()> 0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<HoaDon> result = new ArrayList<>();
        for (HoaDon hoaDon : dsHoaDon) {
           if(hoaDon.getMaHoaDon().trim().equals(maHoaDon)) {
               result.add(hoaDon);
           }
        }
        
        DefaultTableModel model = (DefaultTableModel) this.tblDsHoaDonCuaKhach.getModel();
        if(result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
        } else {
            model.setRowCount(0);
            int n = 1;
            for (HoaDon hoaDon : result) {
                for (SanPham sanPham : hoaDon.getDsMuaSanPham()) {
                    model.addRow(new Object[] {
                    n++,
                    hoaDon.getMaHoaDon(),sanPham.getMaSanPham(),sanPham.getSoLuong(),sanPham.getGia()*sanPham.getSoLuong()
                });
                }
                
            }
        }
    }//GEN-LAST:event_txtTimMaHoaDonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
//        this.setVisible(false);
    int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
//        this.setVisible(false);
        int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

//    //tải lại bảng khách Hàng
//    public void viewTableKH(){
//        DefaultTableModel model2 = (DefaultTableModel) this.tblkhachHangs.getModel();
//        int n = 1;//stt
//        model2.setRowCount(0);
//        
//        for (KhachHang khachHang : dsKhachHang) {
//            model2.addRow(new Object[]{
//                n++,
//            khachHang.getMaKhachHang(),khachHang.getHoTenKhachHang(),khachHang.getGioiTinh(),khachHang.getSoDienThoai(),khachHang.getDiaChi()});
//        }
//        
//        DefaultComboBoxModel cbxDsKH = (DefaultComboBoxModel) cbxKH.getModel();
//        if (cbxDsKH == null) {
//            cbxKH = new JComboBox<>();
//        } else {
//            cbxKH.removeAllItems();
//        }
//
//        for (KhachHang khachHang : dsKhachHang) {
//            cbxDsKH.addElement(khachHang);
//        }
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
            java.util.logging.Logger.getLogger(QuanLyNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyNguoiDung().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TblGioHang;
    private javax.swing.JButton btnAddGioHang;
    private javax.swing.JButton btnCapNhatKH;
    private javax.swing.JButton btnFindMaHoaDon;
    private javax.swing.JButton btnLuuHD;
    private javax.swing.JButton btnResetHD;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnXoaSpTrongHdon;
    private javax.swing.JButton btnXoaTTKH;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblDsHoaDonCuaKhach;
    private javax.swing.JTable tblDsSP;
    private javax.swing.JTable tblHDKH;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiaChiHD;
    private javax.swing.JTextField txtFindThongTin;
    private javax.swing.JTextField txtGioiTinhCaNhan;
    private javax.swing.JTextField txtGtHD;
    private javax.swing.JTextField txtHDDiaChi;
    private javax.swing.JTextField txtHDGioiTinh;
    private javax.swing.JTextField txtHDMaKH;
    private javax.swing.JTextField txtHDSDT;
    private javax.swing.JTextField txtHDTen;
    private javax.swing.JTextField txtHoTenKH;
    private javax.swing.JTextField txtMAXX;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtMaHoaDonAddDs;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKhHD;
    private javax.swing.JTextField txtMatKhauCaNhan;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDTHD;
    private javax.swing.JTextField txtSoLuongSP;
    private javax.swing.JTextField txtTenHD;
    private javax.swing.JTextField txtTimMaHoaDon;
    private javax.swing.JTextField txtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
