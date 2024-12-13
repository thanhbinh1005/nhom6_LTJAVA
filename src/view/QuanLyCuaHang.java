
package view;

import controller.ControllerHoaDon;
import controller.ControllerKhachHang;
import controller.ControllerKho;
import controller.ControllerSQL;
import controller.controllerSanPham;
import database.DBConnection;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.KhachHang;
import model.SanPham;
import model.HoaDon;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;


public class QuanLyCuaHang extends javax.swing.JFrame {
    private ArrayList<SanPham> dsSanPham = new ArrayList<>();
    private ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
    private ArrayList<SanPham> dsGioHang = new ArrayList<>();
    private ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
    private ArrayList<SanPham> dsBanRa = new ArrayList<>();
    
    private Connection conn = DBConnection.connect();
    int posTableSP;
    int posTableHD;
    int posTableKH;
    
    ControllerKhachHang ctlerKhachHang = new ControllerKhachHang();
    controllerSanPham ctlerSanPham = new controllerSanPham();
    ControllerSQL controllerSQL = new ControllerSQL();
    ControllerHoaDon ctlerHoaDon = new ControllerHoaDon(controllerSQL);

    

    public QuanLyCuaHang() {
        initComponents();
        setLocationRelativeTo(null);
        loadKhachHangFromDB();
        loadSanPhamFromDB();
        loadHoaDonFromDB();
        viewTableKH();
        viewTableSP();
        viewKhachHangDK();
        capNhatDsBanRa();
        viewTableDoanhThu();
        initGioiTinh();
        
    }
   
    // hàm load dữ liệu từ database và lưu vào các danh sách
    public void loadKhachHangFromDB(){
        dsKhachHang.clear();
        String sql = "SELECT * FROM khach_hang";
        try (Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String makh = rs.getString("ma_khach_hang");
                String tenkh = rs.getString("ho_ten");
                String gioiTinh = rs.getString("gioi_tinh");
                String sdt = rs.getString("so_dien_thoai");
                String diaChi = rs.getString("dia_chi");
                
                dsKhachHang.add(new KhachHang(makh, tenkh, gioiTinh, sdt, diaChi));
            }
            System.out.println("Tai Du Lieu Thanh Cong");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu khách hàng từ CSDL!");

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
    
    public ArrayList<SanPham> loadChiTietHoaDonFromDB(String maHoaDon){
        ArrayList<SanPham> tmpDsSanPham = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_hoa_don Where ma_hoa_don = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, maHoaDon);
            
            try (ResultSet row1 = pst.executeQuery();){
                
                while(row1.next()) {
                    String masp = row1.getString("ma_san_pham");
                    int soluong = row1.getInt("so_luong");
                    double gia = row1.getDouble("gia");
                    String tensp = null;
                    for (SanPham sanPham : dsSanPham) {
                        if(sanPham.getMaSanPham().equals(masp)) {
                            tensp = sanPham.getTenSanPham(); 
                            break;
                        }
                    }
                    SanPham sp = new SanPham(masp, tensp, soluong, gia);
                    tmpDsSanPham.add(sp);
                
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải chi tiết hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return tmpDsSanPham;
    }
    
    public void loadHoaDonFromDB(){
        dsHoaDon.clear();
        String sql = "SELECT * FROM hoa_don";
        
        try(PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                String maHoaDon = rs.getString("ma_hoa_don");
                String makh = rs.getString("ma_khach_hang");
                String ngayLap = rs.getString("ngay_lap");
                double tongTien = rs.getDouble("tong_tien");
                
                ArrayList<SanPham> dsTam = loadChiTietHoaDonFromDB(maHoaDon);
                KhachHang kh = new KhachHang();
                for (KhachHang khachHang : dsKhachHang) {
                    if(khachHang.getMaKhachHang().equals(makh)) {
                        kh = khachHang;
                    }
                }
                
                HoaDon hDon = new HoaDon(maHoaDon, kh, ngayLap);
                for (SanPham sanPham : dsTam) {
                   hDon.themSanPham(sanPham);
                }
                dsHoaDon.add(hDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Tải Hóa Đơn","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //-----------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
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
        btnLuuKhachHang = new javax.swing.JButton();
        btnXoaKH = new javax.swing.JButton();
        btnCapNhatKH = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cbxGioiTinh = new javax.swing.JComboBox<>();
        btnResetKH = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblkhachHangs = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        txtGiaCa = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnLuuSP = new javax.swing.JButton();
        btnXoaSP = new javax.swing.JButton();
        btnCapNhatSP = new javax.swing.JButton();
        btnResetSP = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPhams = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblKhoSP = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtFindMaSP = new javax.swing.JTextField();
        txtFindTenSP = new javax.swing.JTextField();
        txtMinSoLuong = new javax.swing.JTextField();
        txtMinGia = new javax.swing.JTextField();
        btnFindSP = new javax.swing.JButton();
        txtMaxSoLuong = new javax.swing.JTextField();
        txtMaxGia = new javax.swing.JTextField();
        btnResetKho = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblFindSP = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDsSP = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        cbxKH = new javax.swing.JComboBox<>();
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
        btnXoaHD = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        txtMAXX = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtMaHoaDon = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblHDKH = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
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
        jScrollPane8 = new javax.swing.JScrollPane();
        tblBangKh = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        btnXoaTTKH = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        txtMaHoaDonAddDs = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtNameDsHoaDon = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtNgayTao = new javax.swing.JTextField();
        btnResetBangHh = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblDoanhThu = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel9.setText("Ma khach hang:");

        jLabel10.setText("Ho Ten khach hang: ");

        jLabel11.setText("So dien thoai: ");

        jLabel12.setText("Dia chi:");

        btnLuuKhachHang.setText("Luu Khach Hang");
        btnLuuKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuKhachHangActionPerformed(evt);
            }
        });

        btnXoaKH.setText("Xoa Khach Hang");
        btnXoaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKHActionPerformed(evt);
            }
        });

        btnCapNhatKH.setText("Cap Nhat Khach Hang");
        btnCapNhatKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatKHActionPerformed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 204));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel8.setText("Danh Sach Khach Hang");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel7.setText("Thong tin khach hang");

        jLabel13.setText("Gioi tinh: ");

        cbxGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nu", "Khac" }));

        btnResetKH.setText("Reset");
        btnResetKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(35, 35, 35))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addComponent(jLabel10)
                                            .addGap(27, 27, 27)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxGioiTinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnXoaKH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCapNhatKH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLuuKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnResetKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(21, 21, 21))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(331, 331, 331)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(328, 328, 328)
                                .addComponent(jLabel7)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(btnLuuKhachHang))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(btnXoaKH))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCapNhatKH))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cbxGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        tblkhachHangs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Ma Khach Hang", "Ten Khach Hang", "Gioi Tinh", "So Dien Thoai", "Dia Chi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblkhachHangs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblkhachHangsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblkhachHangs);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(184, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Khách Hàng", jPanel3);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Ma san pham: ");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel4.setText("Ten san pham: ");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
        jPanel2.add(txtMaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 460, 30));
        jPanel2.add(txtTenSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 460, 30));
        jPanel2.add(txtSoLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 210, 30));
        jPanel2.add(txtGiaCa, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 210, 30));

        jLabel5.setText("So luong: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel6.setText("Gia ca:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 60, -1));

        btnLuuSP.setText("Luu san pham");
        btnLuuSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnLuuSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 30, 140, -1));

        btnXoaSP.setText("Xoa san pham");
        btnXoaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnXoaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, 140, -1));

        btnCapNhatSP.setText("Cap nhat san pham");
        btnCapNhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnCapNhatSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 110, 140, -1));

        btnResetSP.setText("Reset ");
        btnResetSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnResetSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 150, 140, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Thong tin san pham");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setText("Danh Sach San Pham");

        tblSanPhams.setBackground(new java.awt.Color(204, 255, 255));
        tblSanPhams.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Ma San Pham", "Ten San Pham", "So Luong", "Gia ca"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPhams.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPhams);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(307, 307, 307)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(319, 319, 319)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
                        .addGap(13, 13, 13)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Sản Phẩm", jPanel1);

        tblKhoSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
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
        jScrollPane3.setViewportView(tblKhoSP);

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel14.setText("DANH SACH SAN PHAM");

        jLabel15.setText("Mã Sản Phẩm");

        jLabel16.setText("Tên Sản Phẩm");

        jLabel17.setText("So Luong");

        jLabel18.setText("Giá Thành");

        btnFindSP.setText("Tìm Kiếm");
        btnFindSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindSPActionPerformed(evt);
            }
        });

        btnResetKho.setText("Reset");
        btnResetKho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKhoActionPerformed(evt);
            }
        });

        jLabel20.setText("TỐI THIỂU");

        jLabel21.setText("TỐI ĐA");

        tblFindSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblFindSP);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(342, 342, 342)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFindTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(txtFindMaSP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel21)
                        .addGap(37, 37, 37))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap(557, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtMinGia, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                    .addComponent(txtMinSoLuong)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jScrollPane6)))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnResetKho, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFindSP, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(71, 71, 71))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel14)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtFindMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtFindTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtMinSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMinGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnFindSP)
                        .addGap(18, 18, 18)
                        .addComponent(btnResetKho))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Kho Hàng", jPanel5);

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

        cbxKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxKHActionPerformed(evt);
            }
        });

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

        btnXoaHD.setText("Xóa Sản Phẩm");
        btnXoaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHDActionPerformed(evt);
            }
        });

        jLabel37.setText("MAX");

        jLabel43.setText("Mã Hóa Đơn");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jSeparator3)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnAddGioHang)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel29)
                                .addGap(50, 50, 50)
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 435, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtSoLuongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMAXX, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                                .addComponent(btnLuuHD)
                                .addGap(42, 42, 42)
                                .addComponent(btnResetHD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)))
                        .addComponent(btnXoaHD)
                        .addGap(33, 33, 33))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbxKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
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
                        .addGap(151, 151, 151)))
                .addGap(16, 16, 16))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(387, 387, 387))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cbxKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
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
                                    .addComponent(txtMAXX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 59, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoaHD)
                            .addComponent(btnLuuHD)
                            .addComponent(btnResetHD))
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

        tblBangKh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "Mã Hóa đơn", "Mã Khách Hàng", "Tên Khách Hàng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangKh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangKhMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblBangKh);

        jLabel40.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel40.setText("DANH SÁCH KHACH HÀNG CÓ HÓA ĐƠN");

        btnXoaTTKH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTTKH.setText("XÓA THÔNG TIN");
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

        jLabel45.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel45.setText("TÌM TÊN");

        txtNameDsHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameDsHoaDonActionPerformed(evt);
            }
        });

        jLabel46.setText("Ngày Tạo");

        btnResetBangHh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetBangHh.setText("RESET");
        btnResetBangHh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetBangHhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel30)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(txtTenHD)
                    .addComponent(txtGtHD))
                .addGap(77, 77, 77)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                    .addComponent(jLabel36))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtTongTienHD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(txtDiaChiHD, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSDTHD, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(235, 235, 235))))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addComponent(jScrollPane7)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(112, 112, 112)
                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(jLabel31)))
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel44)
                                        .addGap(86, 86, 86)
                                        .addComponent(jLabel46)
                                        .addGap(120, 120, 120))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(67, 67, 67)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(btnXoaTTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(btnThoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(txtNameDsHoaDon)
                                                    .addComponent(btnResetBangHh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(89, 89, 89))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(btnFindMaHoaDon)
                                .addGap(12, 12, 12)
                                .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel44)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindMaHoaDon)
                    .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel30)
                        .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(txtDiaChiHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(txtNameDsHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnResetBangHh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXoaTTKH)
                        .addGap(37, 37, 37)
                        .addComponent(btnThoat)))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh Sách Hóa Đơn", jPanel12);

        tblDoanhThu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng bán", "Giá Bán", "Tổng Giá Bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tblDoanhThu);

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel41.setText("DOANH THU");

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel42.setText("TỔNG DOANH THU");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 878, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(jLabel41)))
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addGap(18, 18, 18)
                .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel41)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(254, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Doanh Thu", jPanel7);

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

    private void btnXoaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKHActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtMaKH.getText().equals("")){
            sb.append("Thieu ma san pham!\n");
            txtMaKH.setBackground(Color.red);
        }else
            txtMaKH.setBackground(Color.white);
        
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this,sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,"Ban co muon xoa thong tin khong ","Hỏi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION) {
            return;
        }
  //      
        boolean check = true;
        for (HoaDon hoaDon : dsHoaDon) { // duyệt xem sản phẩm có trong hóa đơn không nếu có báo lỗi k thể xóa 
                for (KhachHang kh : dsKhachHang) {
                    if(hoaDon.getTTKhachHang().getMaKhachHang().equals(txtMaKH.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Khách Hàng Đã Có Hóa Đơn!\n\tYêu Cầu Xóa Hóa Đơn Trước Khi Thay Đổi Thông Tin Sản Phẩm!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                        check = false;
                        break;
                    }
                }
        }
        
        if(check) {// nếu không báo lỗi được phép xóa sp
            for (int i = 0; i < dsKhachHang.size(); i++) {
                if(dsKhachHang.get(i).getMaKhachHang().equals(txtMaKH.getText())){
                    dsKhachHang.remove(i);
                }
            }

            JOptionPane.showMessageDialog(this, "Xóa Thành Công!","Thanh Cong",JOptionPane.INFORMATION_MESSAGE);
        ctlerKhachHang.deleteKhachHangToDB(txtMaKH.getText());// gọi hàm ở controllerKhachHang để xóa khách hàng
        }
 //       
        
        viewTableKH();

        
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
    }//GEN-LAST:event_btnXoaKHActionPerformed

    private void tblSanPhamsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamsMouseClicked
        // TODO add your handling code here:
        posTableSP = tblSanPhams.getSelectedRow();
        int selectRow = tblSanPhams.getSelectedRow();
        
        if(selectRow >= 0) {
            String sanphamid = (String) tblSanPhams.getValueAt(selectRow, 1);
            String tensp = (String) tblSanPhams.getValueAt(selectRow, 2);  // Tên sản phẩm (String)

            int soluong = (int) tblSanPhams.getValueAt(selectRow, 3);  // Số lượng (int)

            double giaca = (double) tblSanPhams.getValueAt(selectRow, 4);  // Giá cả (double)
            
            for (SanPham sanPham : dsSanPham) {
                if(sanphamid.equals(sanPham.getMaSanPham())) {
                    txtMaSP.setText(sanphamid);
                    txtTenSP.setText(tensp);
                    txtSoLuong.setText(String.valueOf(soluong));
                    txtGiaCa.setText(String.valueOf(giaca));
                    break;
                }
            }
        }
    }//GEN-LAST:event_tblSanPhamsMouseClicked

    private void btnXoaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSPActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtMaSP.getText().trim().equals("")){
            sb.append("Thieu ma san pham!\n");
            txtMaSP.setBackground(Color.red);
        }else
            txtMaSP.setBackground(Color.white);
        
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this,sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,"Bạn Có Muốn Xóa Thông Tin Không    ","Hỏi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION) {
            return;
        }
        
        boolean check = true;
        for (HoaDon hoaDon : dsHoaDon) { // duyệt xem sản phẩm có trong hóa đơn không nếu có báo lỗi k thể xóa 
            for (SanPham spOnHoaDon : hoaDon.getDsMuaSanPham()) {
                for (SanPham sanPham : dsSanPham) {
                    if(spOnHoaDon.getMaSanPham().equals(txtMaSP.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Sản Phẩm Tồn Tại Trong Hóa Đơn Của Khách Hàng!\n\tYêu Cầu Xóa Hóa Đơn Trước Khi Thay Đổi Thông Tin Sản Phẩm!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                        check = false;
                        break;
                    }
                }
            }
        }
        
        if(check) {// nếu không báo lỗi được phép xóa sp
            for (int i = 0; i < dsSanPham.size(); i++) {
                if(dsSanPham.get(i).getMaSanPham().equals(txtMaSP.getText().trim())){
                    dsSanPham.remove(i);
                }
            }

            JOptionPane.showMessageDialog(this, "Xóa Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            // xóa sản phẩm khỏi dữ liệu database
            ctlerSanPham.deleteSanPhamToDB(txtMaSP.getText().trim());
        }
        
        viewTableSP();
        txtTenSP.setText("");
            txtGiaCa.setText("");
            txtMaSP.setText("");
            txtSoLuong.setText("");
    }//GEN-LAST:event_btnXoaSPActionPerformed

    private void btnLuuSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuSPActionPerformed
        // TODO add your handling code here:
        String masp = txtMaSP.getText();
        String tensp = txtTenSP.getText();
        int soluong;
        double giaca;
        
        StringBuilder sb = new StringBuilder();
        if(masp.equals("")){
            sb.append("Thieu ma san pham!\n");
            txtMaSP.setBackground(Color.red);
        }else
            txtMaSP.setBackground(Color.white);
        if(tensp.equals("")) {
            sb.append("Thieu ten san pham!\n");
            txtTenSP.setBackground(Color.red);
        }else
            txtTenSP.setBackground(Color.white);
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this,sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            soluong = Integer.parseInt(txtSoLuong.getText());
            giaca = Double.parseDouble(txtGiaCa.getText());
            if (soluong <= 0) {
                JOptionPane.showMessageDialog(this, "So luong phai lon hon 0!", "Loi", JOptionPane.ERROR_MESSAGE);
                txtSoLuong.setBackground(Color.red);
                return;
            }else
                txtSoLuong.setBackground(Color.white);
            
            if (giaca <= 0) {
                JOptionPane.showMessageDialog(this, "Gia ca phai lon hon 0!", "Loi", JOptionPane.ERROR_MESSAGE);
                txtGiaCa.setBackground(Color.red);
                return;
            }else
                txtGiaCa.setBackground(Color.white);
            
        }catch(NumberFormatException e){
            txtGiaCa.setBackground(Color.red);
            txtSoLuong.setBackground(Color.red);
            JOptionPane.showMessageDialog(this,"so luong va gia ca phai hop le!\n","Loi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(ctlerSanPham.checkTrungMaSP(masp)) {
            JOptionPane.showMessageDialog(this, "Mã Sản Phẩm đã tồn tại!", "Error", JOptionPane.ERROR_MESSAGE);
            txtTenSP.setText("");
            txtGiaCa.setText("");
            txtMaSP.setText("");
            txtSoLuong.setText("");
            return;
        }
        //lưu sản phẩm vào database
        ctlerSanPham.saveSanPhamToDB(masp, tensp, soluong, giaca);
        
        //lưu sản phẩm vào danh sách sản phẩm
        SanPham sp = new SanPham(masp, tensp, soluong, giaca);
        dsSanPham.add(sp);
        viewTableSP(); // cập nhật lại bảng sản phẩm
        viewTableDoanhThu(); // cập nhật bảng doanh thu

        JOptionPane.showMessageDialog(this, "San pham da duoc luu!","Thanh Cong",JOptionPane.INFORMATION_MESSAGE);
        txtTenSP.setText("");
        txtGiaCa.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
    }//GEN-LAST:event_btnLuuSPActionPerformed

    private void btnLuuKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuKhachHangActionPerformed
        // Lấy dữ liệu từ các txt nhập

        String maKhachHang = txtMaKH.getText();
        String hoTenKhachHang = txtHoTenKH.getText();
        String gioiTinh = (String) cbxGioiTinh.getSelectedItem();
        String soDienThoai = txtSDT.getText();
        String diaChi = txtDiaChi.getText();
        
        // Tạo đối tượng StringBuilder để lưu thông báo lỗi và check lỗi ô trống khi lưu 
        StringBuilder sb = new StringBuilder();
        if(maKhachHang.equals("")){
            sb.append("Thieu Ma Khach Hang!\n");
            txtMaKH.setBackground(Color.red);
        } else
            txtMaKH.setBackground(Color.white);
        if(hoTenKhachHang.equals("")){
            sb.append("Thieu Ho Ten Khach Hang!\n");
            txtHoTenKH.setBackground(Color.red);
        } else
            txtHoTenKH.setBackground(Color.white);
        if(soDienThoai.equals("")){
            sb.append("Thieu SDT Khach Hang!\n");
            txtSDT.setBackground(Color.red);
        } else
            txtSDT.setBackground(Color.white);
        if(diaChi.equals("")){
            sb.append("Thieu Dia Chi Khach Hang!\n");
            txtDiaChi.setBackground(Color.red);
        } else
            txtDiaChi.setBackground(Color.white);
        
        if(sb.length()>0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(ctlerKhachHang.checkTrungMaKH(maKhachHang)) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng đã tồn tại!", "Error", JOptionPane.ERROR_MESSAGE);
            txtMaKH.setText("");
            txtHoTenKH.setText("");
            txtDiaChi.setText("");
            txtSDT.setText("");
            return;
        }
        //lưu vào trong database sql
        ctlerKhachHang.saveKhachHangToDB(maKhachHang, hoTenKhachHang, gioiTinh, soDienThoai, diaChi);
        
        //Thêm khách hàng vào danh sách khách hàng nhưng phải kiểm tra có trùng mã kh không
            KhachHang kh = new KhachHang(maKhachHang, hoTenKhachHang, gioiTinh, soDienThoai, diaChi);
            dsKhachHang.add(kh);
            JOptionPane.showMessageDialog(this, "Lưu Khách Hàng Thành Công","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            
        
        viewTableKH(); // cập nhật lại bảng hiện thị khách hàng
        //reset ô txt
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        
        
//        System.out.println(kh);
    }//GEN-LAST:event_btnLuuKhachHangActionPerformed

    private void btnCapNhatKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatKHActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtMaKH.getText().equals("")){
            sb.append("Thieu Ma Khach Hang!\n");
            txtMaKH.setBackground(Color.red);
        } else
            txtMaKH.setBackground(Color.white);
        if(txtHoTenKH.getText().equals("")){
            sb.append("Thieu Ho Ten Khach Hang!\n");
            txtHoTenKH.setBackground(Color.red);
        } else
            txtHoTenKH.setBackground(Color.white);
        if(txtSDT.getText().equals("")){
            sb.append("Thieu SDT Khach Hang!\n");
            txtSDT.setBackground(Color.red);
        } else
            txtSDT.setBackground(Color.white);
        if(txtDiaChi.getText().equals("")){
            sb.append("Thieu Dia Chi Khach Hang!\n");
            txtDiaChi.setBackground(Color.red);
        } else
            txtDiaChi.setBackground(Color.white);
        if(txtDiaChi.getText().equals("")){
            sb.append("Thieu Dia Chi Khach Hang!\n");
            txtDiaChi.setBackground(Color.red);
        } else
            txtDiaChi.setBackground(Color.white);
        
        if(sb.length()>0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, "Ban co muon cap nhat thong tin khong!","Hoi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION){
            return;
        }
        boolean check = true;
        for (HoaDon hoaDon : dsHoaDon) { // duyệt xem sản phẩm có trong hóa đơn không nếu có báo lỗi k thể xóa 
                for (KhachHang kh : dsKhachHang) {
                    if(hoaDon.getTTKhachHang().getMaKhachHang().equals(txtMaKH.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Khách Hàng Đã Có Hóa Đơn!\n\tYêu Cầu Xóa Hóa Đơn Trước Khi Thay Đổi Thông Tin Sản Phẩm!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                        check = false;
                        break;
                    }
                }
        }
        
        if(check) {// nếu không báo lỗi được phép xóa sp
            String gender = (String) cbxGioiTinh.getSelectedItem();// chuyển giới tính chọn bởi combobox thành dãy string cho dễ
        // set lại mọi thứ cho khách
            for (KhachHang khachHang : dsKhachHang) {
                if(khachHang.getMaKhachHang().equals(txtMaKH.getText())){
                    khachHang.setMaKhachHang(txtMaKH.getText());
                    khachHang.setHoTenKhachHang(txtHoTenKH.getText());
                    khachHang.setGioiTinh(gender);
                    khachHang.setSoDienThoai(txtSDT.getText());
                    khachHang.setDiaChi(txtDiaChi.getText());
                }
            }
            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
         //gọi hàm update để sửa thông tin khách
        ctlerKhachHang.updateKhachHangToDB(txtMaKH.getText(), txtHoTenKH.getText(), gender, txtSDT.getText(), txtDiaChi.getText());
        }

        viewTableKH();
        //reset ô txt
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
    }//GEN-LAST:event_btnCapNhatKHActionPerformed

    private void btnCapNhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatSPActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtMaSP.getText().equals("")){
            sb.append("Thieu ma san pham!\n");
            txtMaSP.setBackground(Color.red);
        }else
            txtMaSP.setBackground(Color.white);
        if(txtTenSP.getText().equals("")) {
            sb.append("Thieu ten san pham!\n");
            txtTenSP.setBackground(Color.red);
        }else
            txtTenSP.setBackground(Color.white);
        if(txtSoLuong.getText().equals("")) {
            sb.append("Thieu so luong san pham!\n");
            txtSoLuong.setBackground(Color.red);
        }else
            txtSoLuong.setBackground(Color.white);
        if(txtGiaCa.getText().equals("")) {
            sb.append("Thieu gia san pham!\n");
            txtGiaCa.setBackground(Color.red);
        }else
            txtGiaCa.setBackground(Color.white);
        
        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this,sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,"Ban co muon cap nhat thong tin khong ","Hỏi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION) {
            return;
        }
        boolean check = true;
        for (HoaDon hoaDon : dsHoaDon) { // duyệt xem sản phẩm có trong hóa đơn không nếu có báo lỗi k thể xóa 
            for (SanPham spOnHoaDon : hoaDon.getDsMuaSanPham()) {
                for (SanPham sanPham : dsSanPham) {
                    if(spOnHoaDon.getMaSanPham().equals(txtMaSP.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Sản Phẩm Tồn Tại Trong Hóa Đơn Của Khách Hàng!\n\tYêu Cầu Xóa Hóa Đơn Trước Khi Thay Đổi Thông Tin Sản Phẩm!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                        check = false;
                        break;
                    }
                }
            }
        }
        
        if(check) {// nếu không báo lỗi được phép xóa sp
            for (SanPham sanPham : dsSanPham) {
            if(sanPham.getMaSanPham().equals(txtMaSP.getText())) {
                sanPham.setMaSanPham(txtMaSP.getText());
                sanPham.setTenSanPham(txtTenSP.getText());
                sanPham.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                sanPham.setGia(Double.parseDouble(txtGiaCa.getText()));                
                break;
            }
        }

           JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            // cập nhật sản phẩm trong database
            ctlerSanPham.updateSanPhamToDB(txtMaSP.getText(), txtTenSP.getText(),Integer.parseInt(txtSoLuong.getText()), Double.parseDouble(txtGiaCa.getText()));
        }
        viewTableSP();
        txtTenSP.setText("");
            txtGiaCa.setText("");
            txtMaSP.setText("");
            txtSoLuong.setText("");
    }//GEN-LAST:event_btnCapNhatSPActionPerformed

    private void btnResetSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetSPActionPerformed
        // TODO add your handling code here:
        txtTenSP.setText("");
        txtGiaCa.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
        viewTableSP();
        
    }//GEN-LAST:event_btnResetSPActionPerformed

    private void btnResetKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKHActionPerformed
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        viewTableKH();
    }//GEN-LAST:event_btnResetKHActionPerformed

    private void tblkhachHangsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblkhachHangsMouseClicked
        // TODO add your handling code here:
        int selectRow = tblkhachHangs.getSelectedRow();
        
        if(selectRow >= 0) {
            String khachHangID = (String) tblkhachHangs.getValueAt(selectRow, 1);         
            for (KhachHang khachHang : dsKhachHang) {
                if(khachHang.getMaKhachHang().equals(khachHangID)){
                    txtMaKH.setText(khachHangID);
                    txtHoTenKH.setText(khachHang.getHoTenKhachHang());
                    cbxGioiTinh.setSelectedItem(khachHang.getGioiTinh());
                    txtSDT.setText(khachHang.getSoDienThoai());
                    txtDiaChi.setText(khachHang.getDiaChi());
                    break;
                }
            }
        }
        viewTableKH();
    }//GEN-LAST:event_tblkhachHangsMouseClicked

    private void btnFindSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindSPActionPerformed
        // TODO add your handling code here:
        String maSanPham = txtFindMaSP.getText().trim();
        String tenSanPham = txtFindTenSP.getText().trim();
        int soluongMin = 0, soluongMax = 0;
        double giaMin= 0, giaMax = 0;
//        DefaultTableModel model = (DefaultTableModel) this.tblFindSP.getModel();
        // lấy số lượng
        StringBuilder sb = new StringBuilder();
            
            try {
                if(!txtMinSoLuong.getText().isEmpty()) {
                    soluongMin = Integer.parseInt(txtMinSoLuong.getText().trim());
                }
                if(!txtMaxSoLuong.getText().isEmpty()) {
                    soluongMax = Integer.parseInt(txtMaxSoLuong.getText().trim());
                }
                if(!txtMinGia.getText().isEmpty()) {
                    giaMin = Double.parseDouble(txtMinGia.getText().trim());
                }
                if(!txtMaxGia.getText().isEmpty()) {
                    giaMax = Double.parseDouble(txtMaxGia.getText().trim());
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this,"so luong va gia ca phai hop le!\n","Loi",JOptionPane.ERROR_MESSAGE);
                return;
                
            }
//            System.out.println(soluongMax);
            
        ArrayList<SanPham> list = ControllerKho.timKiemSanPham(dsSanPham, maSanPham, tenSanPham, soluongMin, soluongMax, giaMin, giaMax);
        
        if(list.isEmpty()){
            DefaultTableModel model = (DefaultTableModel) this.tblFindSP.getModel();
            JOptionPane.showMessageDialog(this, "Không Tìm Thấy Sản Phẩm Phù Hợp.\nVui Lòng Nhập Lại Thông Tin","Thông báo",JOptionPane.INFORMATION_MESSAGE);
            model.setRowCount(0);
        }

        txtFindMaSP.setText("");
        txtFindTenSP.setText("");
        txtMinGia.setText("");
        txtMinSoLuong.setText("");
        txtMaxGia.setText("");
        txtMaxSoLuong.setText("");
        fillTableFindKho(list);
    }//GEN-LAST:event_btnFindSPActionPerformed

        private void fillTableFindKho(ArrayList<SanPham> ds) {
            DefaultTableModel modelFind = (DefaultTableModel) tblFindSP.getModel();
            modelFind.setRowCount(0); // Xóa dữ liệu cũ
            int n = 1;
            for (SanPham sp : ds) {
                modelFind.addRow(new Object[]{
                    n++,sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getSoLuong(),
                    sp.getGia()
                });
            }
        }

    private void btnResetKhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKhoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetKhoActionPerformed

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

    private void cbxKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxKHActionPerformed
        cbxKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KhachHang selectedKhachHang = (KhachHang) cbxKH.getSelectedItem();
                if (selectedKhachHang != null) {
                    txtHDMaKH.setText(selectedKhachHang.getMaKhachHang());
                    txtHDTen.setText(selectedKhachHang.getHoTenKhachHang());
                    txtHDGioiTinh.setText(selectedKhachHang.getGioiTinh());
                    txtHDSDT.setText(selectedKhachHang.getSoDienThoai());
                    txtHDDiaChi.setText(selectedKhachHang.getDiaChi());
                }
            }
        });
    }//GEN-LAST:event_cbxKHActionPerformed

    private void tblDsSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDsSPMouseClicked
        // TODO add your handling code here:
        posTableHD = tblDsSP.getSelectedRow();
        int selectRow = tblDsSP.getSelectedRow();
        
        if(selectRow >= 0) {
            String sanphamid = (String) tblSanPhams.getValueAt(selectRow, 1);
            String tensp = (String) tblSanPhams.getValueAt(selectRow, 2);  // Tên sản phẩm (String)

            int soluong = (int) tblSanPhams.getValueAt(selectRow, 3);  // Số lượng (int)

            double giaca = (double) tblSanPhams.getValueAt(selectRow, 4);  // Giá cả (double)
            
            for (SanPham sanPham : dsSanPham) {
                if(sanphamid.equals(sanPham.getMaSanPham())) {
                    txtMAXX.setText(String.valueOf(soluong));
                    break;
                }
            }
        }
        
    }//GEN-LAST:event_tblDsSPMouseClicked

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

    public void viewKhachHangDK(){
        DefaultTableModel model = (DefaultTableModel) this.tblBangKh.getModel();
        model.setRowCount(0);
        int n = 1;
        for (HoaDon hoaDon : dsHoaDon) {
            model.addRow(new Object[] {
                n++,
                hoaDon.getMaHoaDon(),hoaDon.getTTKhachHang().getMaKhachHang(),hoaDon.getTTKhachHang().getHoTenKhachHang()
            });
        }
    }
    
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
    capNhatBanRaLuu(newHD);
    viewTableDoanhThu();

    // Cập nhật số lượng sản phẩm trong danh sách sản phẩm
    for (SanPham giohang : dsGioHang) {
        for (SanPham sanPham : dsSanPham) {
            if (sanPham.getMaSanPham().equals(giohang.getMaSanPham())) {
                sanPham.setSoLuong(sanPham.getSoLuong() - giohang.getSoLuong());
            }
        }
    }


    // Cập nhật lại bảng sản phẩm
    viewTableSP();
    
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
    capNhatDsBanRa();
    
    // Làm sạch giỏ hàng và bảng giỏ hàng
    dsGioHang.clear();
    DefaultTableModel model2 = (DefaultTableModel) this.TblGioHang.getModel();
    model2.setNumRows(0);

    // Cập nhật lại danh sách khách hàng
    viewKhachHangDK();


    }//GEN-LAST:event_btnLuuHDActionPerformed

    private void btnXoaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHDActionPerformed
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
    }//GEN-LAST:event_btnXoaHDActionPerformed

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
                txtMaHD.setText(hoaDon1.getTTKhachHang().getMaKhachHang());
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

    private void tblBangKhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangKhMouseClicked
        // TODO add your handling code here:&
        posTableKH = tblBangKh.getSelectedRow();
    }//GEN-LAST:event_tblBangKhMouseClicked

    private void btnXoaTTKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTTKHActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xóa Hóa Đơn Khach Hang","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        
        String maHoaDon = tblBangKh.getValueAt(posTableKH, 1).toString();
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
        
        
        
        viewTableSP();
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
        capNhatBanRaXoa(hoaDonXoa);// cập nhật lại số lượng sản phẩm sau khi hủy hóa đơn
        capNhatDsBanRa();// cập nhật lại bảng doanh thu và tổng tiền doanh thu
//        for (HoaDon hoaDon : dsHoaDon) {
//            System.out.println(hoaDon);
//        }

        viewTableDoanhThu();
        viewKhachHangDK();
        
        //xóa chi tiết hóa đơn trước rồi mới xóa hóa đơn
        ctlerHoaDon.deleteHoaDon(maHoaDon);
             JOptionPane.showMessageDialog(this, "Hóa đơn và thông tin khách hàng đã được xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_btnXoaTTKHActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnThoatActionPerformed

    private void txtMaHoaDonAddDsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHoaDonAddDsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHoaDonAddDsActionPerformed

    private void txtNameDsHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameDsHoaDonActionPerformed
        // TODO add your handling code here:
        String findName = txtNameDsHoaDon.getText().trim();
        
        StringBuilder sb = new StringBuilder();
        if(txtNameDsHoaDon.equals(""))
            sb.append("Vui Lòng Nhập Tên Khách Hàng Trước Khi Nhấn Tìm Kiếm!");
        if(sb.length()> 0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<HoaDon> result = new ArrayList<>();
        for (HoaDon hoaDon : dsHoaDon) {
           if(hoaDon.getTTKhachHang().getHoTenKhachHang().toLowerCase().contains(findName.toLowerCase())) {
               result.add(hoaDon);
           }
        }
        
        DefaultTableModel model = (DefaultTableModel) this.tblBangKh.getModel();
        if(result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
        } else {
            model.setRowCount(0);
            int n = 1;
            for (HoaDon hoaDon : result) {
                model.addRow(new Object[] {
                    n++,
                    hoaDon.getMaHoaDon(),hoaDon.getTTKhachHang().getMaKhachHang(),hoaDon.getTTKhachHang().getHoTenKhachHang()
                });
            }
        }
        
        
        
        
    }//GEN-LAST:event_txtNameDsHoaDonActionPerformed

    private void btnResetBangHhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBangHhActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.tblBangKh.getModel();
        model.setRowCount(0);
        viewKhachHangDK();
    }//GEN-LAST:event_btnResetBangHhActionPerformed
    
//tải lại bảng Sản Phẩm
    public void viewTableSP(){
        DefaultTableModel model1 = (DefaultTableModel) this.tblSanPhams.getModel();
        DefaultTableModel modelKho = (DefaultTableModel) this.tblKhoSP.getModel();
        DefaultTableModel modelHoaDon = (DefaultTableModel) this.tblDsSP.getModel();

        model1.setRowCount(0);
        modelKho.setRowCount(0);
        modelHoaDon.setRowCount(0);
        int n = 1;
        int m = 1;
        int k = 1;
        for (SanPham sanPham : dsSanPham) {
            model1.addRow(new Object[]{
                n++,
                sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()}); 
            modelKho.addRow(new Object[]{
                m++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
            modelHoaDon.addRow(new Object[]{
                k++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
        }
    }
    
//tải lại bảng khách Hàng
    public void viewTableKH(){
        DefaultTableModel model2 = (DefaultTableModel) this.tblkhachHangs.getModel();
        int n = 1;//stt
        model2.setRowCount(0);
        
        for (KhachHang khachHang : dsKhachHang) {
            model2.addRow(new Object[]{
                n++,
            khachHang.getMaKhachHang(),khachHang.getHoTenKhachHang(),khachHang.getGioiTinh(),khachHang.getSoDienThoai(),khachHang.getDiaChi()});
        }
        
        DefaultComboBoxModel cbxDsKH = (DefaultComboBoxModel) cbxKH.getModel();
        if (cbxDsKH == null) {
            cbxKH = new JComboBox<>();
        } else {
            cbxKH.removeAllItems();
        }

        for (KhachHang khachHang : dsKhachHang) {
            cbxDsKH.addElement(khachHang);
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
    
//tải lại bảng Doanh Thu
    public void viewTableDoanhThu() {
        DefaultTableModel model = (DefaultTableModel) this.tblDoanhThu.getModel();
        model.setRowCount(0); // Xóa các dòng cũ
        int n = 1;

        for (SanPham spBanRa : dsBanRa) {
            model.addRow(new Object[]{
                n++,
                spBanRa.getMaSanPham(),
                spBanRa.getTenSanPham(),
                spBanRa.getSoLuong(),
                spBanRa.getGia(),
                spBanRa.getSoLuong() * spBanRa.getGia()
            });
        }
    }

    private void capNhatBanRaLuu(HoaDon newHD) {
        for (SanPham sanPhamMua : newHD.getDsMuaSanPham()) {
            boolean found = false;

            // Kiểm tra xem sản phẩm đã tồn tại trong dsBanRa chưa
            for (SanPham spBanRa : dsBanRa) {
                if (spBanRa.getMaSanPham().equals(sanPhamMua.getMaSanPham())) {
                    spBanRa.setSoLuong(spBanRa.getSoLuong() + sanPhamMua.getSoLuong());
                    found = true;
                    break;
                }
            }

            // Nếu chưa tồn tại, thêm mới vào dsBanRa
            if (!found) {
                dsBanRa.add(new SanPham(
                    sanPhamMua.getMaSanPham(),
                    sanPhamMua.getTenSanPham(),
                    sanPhamMua.getSoLuong(),
                    sanPhamMua.getGia()
                ));
            }
        }
    }
    
    private void capNhatBanRaXoa(HoaDon hoaDonXoa) {
    for (SanPham sanPhamMua : hoaDonXoa.getDsMuaSanPham()) {
        for (int i = 0; i < dsBanRa.size(); i++) {
            SanPham spBanRa = dsBanRa.get(i);
            if (spBanRa.getMaSanPham().equals(sanPhamMua.getMaSanPham())) {
                spBanRa.setSoLuong(spBanRa.getSoLuong() - sanPhamMua.getSoLuong());
                
                // Nếu số lượng bán giảm về 0, xóa sản phẩm khỏi dsBanRa
                if (spBanRa.getSoLuong() <= 0) {
                    dsBanRa.remove(i);
                }
                break;
            }
        }
    }
}
    
    public void capNhatDsBanRa() {
    // Xóa hết dsBanRa trước khi cập nhật
    dsBanRa.clear();

    // Duyệt qua tất cả các hóa đơn trong dsHoaDon
    for (HoaDon hoaDon : dsHoaDon) {
        // Duyệt qua tất cả các sản phẩm trong mỗi hóa đơn
        for (SanPham sanPham : hoaDon.getDsMuaSanPham()) {
            boolean found = false;

            // Kiểm tra xem sản phẩm đã có trong dsBanRa chưa
            for (SanPham spBanRa : dsBanRa) {
                // Nếu có, cộng dồn số lượng sản phẩm bán ra
                if (spBanRa.getMaSanPham().equals(sanPham.getMaSanPham())) {
                    spBanRa.setSoLuong(spBanRa.getSoLuong() + sanPham.getSoLuong());
                    found = true;
                    break;
                }
            }

            // Nếu sản phẩm chưa có trong dsBanRa, thêm mới sản phẩm vào dsBanRa
            if (!found) {
                dsBanRa.add(new SanPham(
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(),
                    sanPham.getSoLuong(),
                    sanPham.getGia()
                ));
            }
        }
    }
    double sum = 0;
        for (SanPham sanPham : dsBanRa) {
            sum += sanPham.getGia()*sanPham.getSoLuong();
        }
        txtTongDoanhThu.setText(String.valueOf(sum));
}


    @Override
    public void print(Graphics g) {
        super.print(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    private void initGioiTinh(){
        String[] gender = new String[]{"Nam","Nu","Khac"};
        
        DefaultComboBoxModel<String> cbxModel = new DefaultComboBoxModel<>(gender);
        
        cbxGioiTinh.setModel(cbxModel);
    }
    
    
    
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
            java.util.logging.Logger.getLogger(QuanLyCuaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyCuaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyCuaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyCuaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyCuaHang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TblGioHang;
    private javax.swing.JButton btnAddGioHang;
    private javax.swing.JButton btnCapNhatKH;
    private javax.swing.JButton btnCapNhatSP;
    private javax.swing.JButton btnFindMaHoaDon;
    private javax.swing.JButton btnFindSP;
    private javax.swing.JButton btnLuuHD;
    private javax.swing.JButton btnLuuKhachHang;
    private javax.swing.JButton btnLuuSP;
    private javax.swing.JButton btnResetBangHh;
    private javax.swing.JButton btnResetHD;
    private javax.swing.JButton btnResetKH;
    private javax.swing.JButton btnResetKho;
    private javax.swing.JButton btnResetSP;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnXoaHD;
    private javax.swing.JButton btnXoaKH;
    private javax.swing.JButton btnXoaSP;
    private javax.swing.JButton btnXoaTTKH;
    private javax.swing.JComboBox<String> cbxGioiTinh;
    private javax.swing.JComboBox<String> cbxKH;
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblBangKh;
    private javax.swing.JTable tblDoanhThu;
    private javax.swing.JTable tblDsSP;
    private javax.swing.JTable tblFindSP;
    private javax.swing.JTable tblHDKH;
    private javax.swing.JTable tblKhoSP;
    private javax.swing.JTable tblSanPhams;
    private javax.swing.JTable tblkhachHangs;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiaChiHD;
    private javax.swing.JTextField txtFindMaSP;
    private javax.swing.JTextField txtFindTenSP;
    private javax.swing.JTextField txtFindThongTin;
    private javax.swing.JTextField txtGiaCa;
    private javax.swing.JTextField txtGtHD;
    private javax.swing.JTextField txtHDDiaChi;
    private javax.swing.JTextField txtHDGioiTinh;
    private javax.swing.JTextField txtHDMaKH;
    private javax.swing.JTextField txtHDSDT;
    private javax.swing.JTextField txtHDTen;
    private javax.swing.JTextField txtHoTenKH;
    private javax.swing.JTextField txtMAXX;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtMaHoaDonAddDs;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtMaxGia;
    private javax.swing.JTextField txtMaxSoLuong;
    private javax.swing.JTextField txtMinGia;
    private javax.swing.JTextField txtMinSoLuong;
    private javax.swing.JTextField txtNameDsHoaDon;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDTHD;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtSoLuongSP;
    private javax.swing.JTextField txtTenHD;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
