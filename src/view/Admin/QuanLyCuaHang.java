
package view.Admin;

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
    protected ArrayList<SanPham> dsSanPham = new ArrayList<>();
    protected ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
    protected ArrayList<SanPham> dsGioHang = new ArrayList<>();
    protected ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
    protected ArrayList<SanPham> dsBanRa = new ArrayList<>();
    
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
//        initGioiTinh();
        
    }
    
    public ArrayList<SanPham> getDsSanPham() {
        return dsSanPham;
    }
    public ArrayList<KhachHang> getDsKhachHang() {
        return dsKhachHang;
    }
    public ArrayList<SanPham> getDsGioHang() {
        return dsGioHang;
    }
    public ArrayList<HoaDon> getDsHoaDon() {
        return dsHoaDon;
    }
    public ArrayList<SanPham> getDsBanRa() {
        return dsBanRa;
    }

    // hàm load dữ liệu từ database và lưu vào các danh sách
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
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblkhachHangs = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        txtGioiTinhKhachH = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtHoTenKH = new javax.swing.JTextField();
        txtMaKH = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtTimMa_Ten = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnTimMa_ten = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnResetKH = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTimKh = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
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
        jLabel23 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblHDKH = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblBangKh = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        btnXoaTTKH = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        txtNameDsHoaDon = new javax.swing.JTextField();
        btnResetBangHh = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        txtTongTienHD = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtSDTHD = new javax.swing.JTextField();
        txtGtHD = new javax.swing.JTextField();
        txtDiaChiHD = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtMaHoaDonAddDs = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtTenHD = new javax.swing.JTextField();
        txtMaHD = new javax.swing.JTextField();
        btnFindMaHoaDon = new javax.swing.JButton();
        txtFindThongTin = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        txtThongTinXoa = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblKhoSP = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblFindSP = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        txtMinGia = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtMinSoLuong = new javax.swing.JTextField();
        txtMaxSoLuong = new javax.swing.JTextField();
        txtMaxGia = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnResetKho = new javax.swing.JButton();
        btnFindSP = new javax.swing.JButton();
        txtFindMaSP = new javax.swing.JTextField();
        txtFindTenSP = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblDoanhThu = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel8.setText("Danh Sách Khách Hàng");

        tblkhachHangs.setBackground(new java.awt.Color(204, 255, 255));
        tblkhachHangs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Khách Hàng", "Tên Khách Hàng", "Giới Tính", "Số Điện Thoại", "Địa Chỉ"
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

        jSeparator2.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 204));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Giới Tính");

        txtHoTenKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenKHActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Mã Khách Hàng");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Họ Tên");

        txtTimMa_Ten.setBackground(new java.awt.Color(204, 204, 204));
        txtTimMa_Ten.setText("Mã hoặc Tên");
        txtTimMa_Ten.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTimMa_TenMouseClicked(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel19.setText("Nhập mã hoặc tên");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Địa Chỉ:");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("Số Điện Thoại");

        btnTimMa_ten.setBackground(new java.awt.Color(255, 255, 204));
        btnTimMa_ten.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnTimMa_ten.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/magnifier (1).png"))); // NOI18N
        btnTimMa_ten.setText("Tìm Kiếm");
        btnTimMa_ten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimMa_tenActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 204));
        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        jButton1.setText("Thoát ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnResetKH.setBackground(new java.awt.Color(255, 255, 204));
        btnResetKH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload (1).png"))); // NOI18N
        btnResetKH.setText("Tải Lại");
        btnResetKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKHActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel7.setText("Thông Tin Khách Hàng");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnResetKH, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGioiTinhKhachH, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSDT)
                            .addComponent(txtDiaChi)
                            .addComponent(txtTimMa_Ten, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(btnTimMa_ten, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(36, 36, 36))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(txtTimMa_Ten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(txtGioiTinhKhachH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnResetKH)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimMa_ten, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        tblTimKh.setBackground(new java.awt.Color(204, 255, 204));
        tblTimKh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Khách Hàng", "Tên Đăng Nhập", "Giới Tính", "Số Điện Thoại", "Địa Chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTimKh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTimKhMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblTimKh);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(380, 380, 380))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Khách Hàng", jPanel3);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Mã Sản Phẩm");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("Tên Sản Phẩm");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));
        jPanel2.add(txtMaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 440, 30));
        jPanel2.add(txtTenSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 440, 30));
        jPanel2.add(txtSoLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 190, 30));
        jPanel2.add(txtGiaCa, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 190, 30));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Số Lượng");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Giá Cả");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 60, -1));

        btnLuuSP.setBackground(new java.awt.Color(153, 153, 153));
        btnLuuSP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnLuuSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upload (1).png"))); // NOI18N
        btnLuuSP.setText("Lưu Sản Phẩm");
        btnLuuSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnLuuSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 140, -1));

        btnXoaSP.setBackground(new java.awt.Color(153, 153, 153));
        btnXoaSP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/trash (1).png"))); // NOI18N
        btnXoaSP.setText("Xóa Sản Phẩm");
        btnXoaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnXoaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, 140, -1));

        btnCapNhatSP.setBackground(new java.awt.Color(153, 153, 153));
        btnCapNhatSP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnCapNhatSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Update.png"))); // NOI18N
        btnCapNhatSP.setText("Cập Nhật");
        btnCapNhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnCapNhatSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 100, 140, -1));

        btnResetSP.setBackground(new java.awt.Color(153, 153, 153));
        btnResetSP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload (1).png"))); // NOI18N
        btnResetSP.setText("Tải Lại");
        btnResetSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnResetSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 140, 140, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Thông Tin Sản Phẩm");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setText("Danh Sách Sản Phẩm");

        tblSanPhams.setBackground(new java.awt.Color(204, 255, 255));
        tblSanPhams.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSanPhams.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPhams);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jLabel23.setBackground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("(*) Ghi chú: 1e7 = 10 triệu");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(433, 433, 433))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(319, 319, 319)
                            .addComponent(jLabel2))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 914, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 918, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 918, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(46, 46, 46)))))
                .addContainerGap(24, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Sản Phẩm", jPanel1);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(1054, 811));

        tblHDKH.setBackground(new java.awt.Color(204, 255, 255));
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

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel31.setText("Thông Tin Hóa Đơn");

        tblBangKh.setBackground(new java.awt.Color(204, 255, 204));
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
        jLabel40.setText("DANH SÁCH KHÁCH HÀNG CÓ HÓA ĐƠN");

        btnXoaTTKH.setBackground(new java.awt.Color(255, 255, 204));
        btnXoaTTKH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTTKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/trash (1).png"))); // NOI18N
        btnXoaTTKH.setText("XÓA HÓA ĐƠN");
        btnXoaTTKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTTKHActionPerformed(evt);
            }
        });

        btnThoat.setBackground(new java.awt.Color(255, 255, 204));
        btnThoat.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        btnThoat.setText("THOÁT");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel45.setText("Nhập Thông Tin:");

        txtNameDsHoaDon.setBackground(new java.awt.Color(204, 204, 204));
        txtNameDsHoaDon.setText("Tên Sản Phẩm..");
        txtNameDsHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNameDsHoaDonMouseClicked(evt);
            }
        });
        txtNameDsHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameDsHoaDonActionPerformed(evt);
            }
        });

        btnResetBangHh.setBackground(new java.awt.Color(255, 255, 204));
        btnResetBangHh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetBangHh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload (1).png"))); // NOI18N
        btnResetBangHh.setText("TẢI LẠI");
        btnResetBangHh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetBangHhActionPerformed(evt);
            }
        });

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));

        jLabel46.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel46.setText("Ngày Tạo");

        jLabel36.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel36.setText("Tổng Tiền");

        jLabel35.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel35.setText("Địa Chỉ");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel33.setText("Giới Tính");

        jLabel34.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel34.setText("SDT");

        txtMaHoaDonAddDs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHoaDonAddDsActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel44.setText("Mã Hóa Đơn");

        jLabel30.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel30.setText("Mã Khách Hàng");

        jLabel32.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel32.setText("Tên Khách Hàng");

        btnFindMaHoaDon.setBackground(new java.awt.Color(255, 255, 204));
        btnFindMaHoaDon.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnFindMaHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/magnifier (1).png"))); // NOI18N
        btnFindMaHoaDon.setText("Tìm Kiếm Mã Hóa Đơn");
        btnFindMaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindMaHoaDonActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        jLabel22.setText("(*)Lấy Thông Tin Khi Nhập Mã Khách Hàng");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel46)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(18, 18, 18)
                                .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel22))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addGap(36, 36, 36)
                                .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtDiaChiHD, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(btnFindMaHoaDon)
                        .addGap(29, 29, 29))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(btnFindMaHoaDon)
                        .addGap(18, 18, 18)
                        .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel30)
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel33)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32)
                            .addComponent(jLabel35)
                            .addComponent(txtDiaChiHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36)
                            .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtThongTinXoa.setBackground(new java.awt.Color(204, 204, 204));
        txtThongTinXoa.setText("Nhập Mã Hóa Đơn");
        txtThongTinXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtThongTinXoaMouseClicked(evt);
            }
        });
        txtThongTinXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtThongTinXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38)
                        .addContainerGap(413, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNameDsHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnResetBangHh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnThoat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnXoaTTKH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtThongTinXoa)))
                        .addContainerGap(30, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 904, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(162, 162, 162)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel31)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel45)
                    .addComponent(txtNameDsHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btnResetBangHh)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaTTKH))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtThongTinXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThoat))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh Sách Hóa Đơn", jPanel12);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(null);

        tblKhoSP.setBackground(new java.awt.Color(255, 255, 204));
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

        jPanel5.add(jScrollPane3);
        jScrollPane3.setBounds(20, 60, 930, 278);

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel14.setText("DANH SACH SẢN PHẨM");
        jPanel5.add(jLabel14);
        jLabel14.setBounds(379, 25, 206, 21);

        tblFindSP.setBackground(new java.awt.Color(204, 255, 204));
        tblFindSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
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

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(20, 470, 936, 189);

        jPanel13.setBackground(new java.awt.Color(204, 204, 204));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel18.setText("Giá Thành");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel17.setText("Số Lượng");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel21.setText("TỐI ĐA");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel20.setText("TỐI THIỂU");

        jButton2.setBackground(new java.awt.Color(153, 153, 153));
        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        jButton2.setText("Thoát");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnResetKho.setBackground(new java.awt.Color(255, 255, 204));
        btnResetKho.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetKho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reload (1).png"))); // NOI18N
        btnResetKho.setText("Tải Lại");
        btnResetKho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKhoActionPerformed(evt);
            }
        });

        btnFindSP.setBackground(new java.awt.Color(255, 255, 204));
        btnFindSP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnFindSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/magnifier (1).png"))); // NOI18N
        btnFindSP.setText("Tìm Kiếm");
        btnFindSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindSPActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel16.setText("Tên Sản Phẩm");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setText("Mã Sản Phẩm");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(368, 368, 368)
                        .addComponent(jLabel20))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFindTenSP)
                            .addComponent(txtFindMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtMinGia, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(txtMinSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel21)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnResetKho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFindSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel21))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel13Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(15, 15, 15)
                                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(txtFindTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(txtFindMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(2, 2, 2))
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel13Layout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(txtMinSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(11, 11, 11))
                                .addGroup(jPanel13Layout.createSequentialGroup()
                                    .addComponent(btnFindSP)
                                    .addGap(18, 18, 18)))
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnResetKho)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(txtMinGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jButton2)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel13);
        jPanel13.setBounds(20, 347, 933, 100);
        jPanel5.add(jSeparator5);
        jSeparator5.setBounds(20, 460, 930, 10);

        jTabbedPane1.addTab("Kho Hàng", jPanel5);

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));

        tblDoanhThu.setBackground(new java.awt.Color(255, 255, 204));
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

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel41.setText("DOANH THU");

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel42.setText("TỔNG DOANH THU");

        jButton3.setBackground(new java.awt.Color(255, 255, 204));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        jButton3.setText("Thoát");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel42)
                                .addGap(18, 18, 18)
                                .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(jLabel41)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jLabel42)
                    .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
        );

        jTabbedPane1.addTab("Doanh Thu", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 976, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetBangHhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBangHhActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.tblBangKh.getModel();
        model.setRowCount(0);
        viewKhachHangDK();
    }//GEN-LAST:event_btnResetBangHhActionPerformed

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
        ArrayList<HoaDon> result = new ArrayList<>(); String hoten ;
        
        for (HoaDon hoaDon : dsHoaDon) {
            if(hoaDon.getTTKhachHang().getHoTenKhachHang().toLowerCase().contains(findName.toLowerCase()) || 
                    hoaDon.getTTKhachHang().getMaKhachHang().toLowerCase().contains(findName.toLowerCase()) ||
                    hoaDon.getMaHoaDon().toLowerCase().contains(findName.toLowerCase().trim())) {
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

    private void txtMaHoaDonAddDsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHoaDonAddDsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHoaDonAddDsActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION)
            System.exit(0);
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnXoaTTKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTTKHActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xóa Hóa Đơn Khach Hang","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;

        String maHoaDon = txtThongTinXoa.getText().trim();
        if(maHoaDon.equals("")){
            JOptionPane.showMessageDialog(this, "Không Tồn Tại Mã Khách Hàng!","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean check = ctlerHoaDon.checkMaHoaDon(maHoaDon);
        if(check == true) {
            for (HoaDon hoaDon : dsHoaDon) {
                if(hoaDon.getMaHoaDon().equals(maHoaDon)) {
                    for (SanPham dsSp : hoaDon.getDsMuaSanPham()) {
                        for (SanPham sanPham : dsSanPham) {
                            if(dsSp.getMaSanPham().equals(sanPham.getMaSanPham())) {
                                ctlerSanPham.tangSoLuong(sanPham.getMaSanPham(), sanPham.getSoLuong(), dsSp.getSoLuong());
                                sanPham.setSoLuong(sanPham.getSoLuong() + dsSp.getSoLuong());
                                break; // cập nhật số lượng thu hồi vào trong sql
                            }
                        }
                    }
                }
            }

            viewTableSP();// Câp nhật lại bảng sản phẩm
            HoaDon hoaDonXoa = null;
            for (HoaDon hoaDon : dsHoaDon) {// kiểm tra và xóa hóa đơn
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
            viewTableDoanhThu();//Tải lại bảng doanh thu
            viewKhachHangDK();//tải lại bảng DANH SÁCH KH CÓ HÓA ĐƠN

            //xóa chi tiết hóa đơn trước rồi mới xóa hóa đơn
            ctlerHoaDon.deleteHoaDon(maHoaDon);
            JOptionPane.showMessageDialog(this, "Hóa đơn và thông tin khách hàng đã được xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            txtThongTinXoa.setText("");
        }
        else
             JOptionPane.showMessageDialog(this, "Không Có Mã Hóa Đơn Phù Hợp");
    }//GEN-LAST:event_btnXoaTTKHActionPerformed

    private void tblBangKhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangKhMouseClicked
        // TODO add your handling code here:&
        posTableKH = tblBangKh.getSelectedRow();
        String mahoadon = tblBangKh.getValueAt(posTableKH, 1).toString();
        txtThongTinXoa.setText(mahoadon);
    }//GEN-LAST:event_tblBangKhMouseClicked

    private void btnFindMaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindMaHoaDonActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtFindThongTin.getText().equals(""))
        sb.append("Vui Lòng Nhập Mã Khách Hàng!");
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
            if (txtFindThongTin.getText().equals(hoaDon1.getMaHoaDon())) {
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

    private void btnResetKhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKhoActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.tblFindSP.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_btnResetKhoActionPerformed

    private void btnFindSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindSPActionPerformed
        // TODO add your handling code here:
        String maSanPham = txtFindMaSP.getText().trim();
        String tenSanPham = txtFindTenSP.getText().trim();
        int soluongMin = 0, soluongMax = 0;
        double giaMin= 0, giaMax = 0;
        
        // lấy số lượng
        StringBuilder sb = new StringBuilder();

        try { // kiểm tra tính hợp lệ của số lương, giá thành nếu nhập không phải số sẽ báo lỗi 
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
            JOptionPane.showMessageDialog(this,"Số Lượng Hoặc Giá Cả Không Hợp Lệ!\n","Loi",JOptionPane.ERROR_MESSAGE);
            return;

        }
        // tìm kiếm sản phẩm và lưu vào danh sách tạm và xếp lên bảng tìm kiếm
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
        fillTableFindKho(list);// tải lại bảng sản phẩm với danh sách list
    }//GEN-LAST:event_btnFindSPActionPerformed

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
                    System.out.println(sanPham);
                    break;
                }
            }
        }
    }//GEN-LAST:event_tblSanPhamsMouseClicked

    private void btnResetSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetSPActionPerformed
        // TODO add your handling code here:
        txtTenSP.setText("");
        txtGiaCa.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
        viewTableSP();
    }//GEN-LAST:event_btnResetSPActionPerformed

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
            JOptionPane.showMessageDialog(this,"Vui lòng nhập đầy đủ và chính xác thông tin!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,"Bạn có muốn cập nhật tin khong ","Hỏi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION) {
            return;
        }
            for (SanPham sanPham : dsSanPham) {
                if(sanPham.getMaSanPham().equals(txtMaSP.getText())) {
                    sanPham.setMaSanPham(txtMaSP.getText());
                    sanPham.setTenSanPham(txtTenSP.getText());
                    sanPham.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                    sanPham.setGia(Double.parseDouble(txtGiaCa.getText()));
                    break;
                }
            }

//            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            // cập nhật sản phẩm trong database
            ctlerSanPham.updateSanPhamToDB(txtMaSP.getText(), txtTenSP.getText(),Integer.parseInt(txtSoLuong.getText()), Double.parseDouble(txtGiaCa.getText()));
//        }
        viewTableSP();
        txtTenSP.setText("");
        txtGiaCa.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
    }//GEN-LAST:event_btnCapNhatSPActionPerformed

    private void btnXoaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSPActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();
        if(txtMaSP.getText().trim().equals("")){
            sb.append("Thiếu Mã Sản Phẩm!\n");
            txtMaSP.setBackground(Color.red);
        }else
        txtMaSP.setBackground(Color.white);

        if(sb.length()>0) {
            JOptionPane.showMessageDialog(this,sb.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,"Bạn Có Muốn Xóa Thông Tin Không","Hỏi",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION) {
            return;
        }
        boolean check =ctlerSanPham.checkTrungMaSP(txtMaSP.getText().trim());
        if(check == true){
            for (HoaDon hoaDon : dsHoaDon) { // duyệt xem sản phẩm có trong hóa đơn không
                for (SanPham spOnHoaDon : hoaDon.getDsMuaSanPham()) {
                    for (SanPham sanPham : dsSanPham) {
                        if(spOnHoaDon.getMaSanPham().equals(txtMaSP.getText().trim())) {
                                ctlerHoaDon.deleteHoaDon(hoaDon.getMaHoaDon());
    //                            JOptionPane.showMessageDialog(this, "Đã Xóa Các Hóa Đơn Chứa Sản Phẩm Sẽ Xóa!");
                                break;
                        }
                    }
                }
            }

            // nếu không báo lỗi được phép xóa sp
            for (int i = 0; i < dsSanPham.size(); i++) {
                if(dsSanPham.get(i).getMaSanPham().equals(txtMaSP.getText().trim())){
                    dsSanPham.remove(i);
                }
            }
        }

//            JOptionPane.showMessageDialog(this, "Xóa Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            // xóa sản phẩm khỏi dữ liệu database
        ctlerSanPham.deleteSanPhamToDB(txtMaSP.getText().trim());
        

        viewTableSP();
        txtTenSP.setText("");
        txtGiaCa.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
    }//GEN-LAST:event_btnXoaSPActionPerformed

    private void btnLuuSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuSPActionPerformed
        // TODO add your handling code here:
        String masp = txtMaSP.getText().trim();
        System.out.println(masp);
        String tensp = txtTenSP.getText().trim();
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
            JOptionPane.showMessageDialog(this,"Vui lòng nhập đầy đủ và chính xác thông tin!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            soluong = Integer.parseInt(txtSoLuong.getText());
            giaca = Double.parseDouble(txtGiaCa.getText());
            if (soluong <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ và chính xác thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoLuong.setBackground(Color.red);
                return;
            }else
            txtSoLuong.setBackground(Color.white);

            if (giaca <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ và chính xác thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGiaCa.setBackground(Color.red);
                return;
            }else
                txtGiaCa.setBackground(Color.white);

        }catch(NumberFormatException e){
            txtGiaCa.setBackground(Color.red);
            txtSoLuong.setBackground(Color.red);
            JOptionPane.showMessageDialog(this,"Vui lòng nhập đầy đủ và chính xác thông tin!","",JOptionPane.ERROR_MESSAGE);
            return;
        }
//        boolean check = 
        if(ctlerSanPham.checkTrungMaSP(masp)) {
            JOptionPane.showMessageDialog(this, "Thông báo mã sản phẩm đã " +
            "tồn yêu cầu nhập mã sản phẩm khác.", "Error", JOptionPane.ERROR_MESSAGE);
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

//        JOptionPane.showMessageDialog(this, "San pham da duoc luu!","Thanh Cong",JOptionPane.INFORMATION_MESSAGE);
        txtTenSP.setText("");
        txtGiaCa.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
    }//GEN-LAST:event_btnLuuSPActionPerformed

    private void tblkhachHangsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblkhachHangsMouseClicked
        // TODO add your handling code here:
        int selectRow = tblkhachHangs.getSelectedRow();

        if(selectRow >= 0) {
            String khachHangID = (String) tblkhachHangs.getValueAt(selectRow, 1);
            for (KhachHang khachHang : dsKhachHang) {
                if(khachHang.getMaKhachHang().equals(khachHangID)){
                    txtMaKH.setText(khachHangID);
                    txtHoTenKH.setText(khachHang.getHoTenKhachHang());
                    txtSDT.setText(khachHang.getSoDienThoai());
                    txtDiaChi.setText(khachHang.getDiaChi());
                    txtGioiTinhKhachH.setText(khachHang.getGioiTinh());
                    break;
                }
            }
        }
        viewTableKH();
    }//GEN-LAST:event_tblkhachHangsMouseClicked

    private void btnTimMa_tenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimMa_tenActionPerformed
            String maORten = txtTimMa_Ten.getText().trim();// lấy thông in từ text
            if(maORten.equals("")) {
                JOptionPane.showMessageDialog(this, "Nhập thông tin trước khi tìm kiếm");
                return;
            }
            ArrayList<KhachHang> findKH = new ArrayList<>();
            String sql = "Select * from khach_hang where (ma_khach_hang like ? OR ho_ten like ? ) AND  (chuc_vu = 'user' OR chuc_vu != 'admin')";
            try (PreparedStatement pst = conn.prepareStatement(sql)){
                pst.setString(1,"%" + maORten +"%");
                pst.setString(2, "%" + maORten + "%");
                try (ResultSet row = pst.executeQuery()){
                while(row.next()) {
                    txtMaKH.setText(row.getString( "ma_khach_hang"));
                    txtHoTenKH.setText(row.getString("ho_ten"));
                    txtDiaChi.setText(row.getString("dia_chi"));
                    txtSDT.setText(row.getString("so_dien_thoai"));
                    txtGioiTinhKhachH.setText(row.getString("gioi_tinh"));
                    KhachHang kh = new KhachHang(row.getString( "ma_khach_hang"), row.getString("ho_ten"), 
                            row.getString("gioi_tinh"),row.getString("so_dien_thoai"),row.getString("dia_chi"));
                    findKH.add(kh);
                }
            
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không tồn Tại!","",JOptionPane.ERROR_MESSAGE);
        }
            
        DefaultTableModel model = (DefaultTableModel) this.tblTimKh.getModel();
        model.setRowCount(0);
        if(findKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
        } else {
            model.setRowCount(0);
            int n = 1;
            for (KhachHang kh : findKH) {
                model.addRow(new Object[] {
                    n++,
                    kh.getMaKhachHang(),kh.getHoTenKhachHang(),kh.getGioiTinh(),kh.getSoDienThoai(),kh.getDiaChi()
                });
            }
        }
    }//GEN-LAST:event_btnTimMa_tenActionPerformed

    private void btnResetKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKHActionPerformed
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtGioiTinhKhachH.setText("");
//        viewTableKH();
    }//GEN-LAST:event_btnResetKHActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION)
            System.exit(0);       // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION)
            System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION)
            System.exit(0);    // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtHoTenKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoTenKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoTenKHActionPerformed

    private void txtNameDsHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNameDsHoaDonMouseClicked
        // TODO add your handling code here:
        if(txtNameDsHoaDon.getText().equals("Tên Sản Phẩm..")) {
            txtNameDsHoaDon.setText("");
        }
    }//GEN-LAST:event_txtNameDsHoaDonMouseClicked

    private void txtTimMa_TenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTimMa_TenMouseClicked
        // TODO add your handling code here:
        if(txtTimMa_Ten.getText().equals("Mã hoặc Tên")) {
            txtTimMa_Ten.setText("");
        }
    }//GEN-LAST:event_txtTimMa_TenMouseClicked

    private void tblTimKhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTimKhMouseClicked
        // TODO add your handling code here:
        int selectRow = tblTimKh.getSelectedRow();

        if(selectRow >= 0) {
            String khachHangID = (String) tblTimKh.getValueAt(selectRow, 1);
            for (KhachHang khachHang : dsKhachHang) {
                if(khachHang.getMaKhachHang().equals(khachHangID)){
                    txtMaKH.setText(khachHangID);
                    txtHoTenKH.setText(khachHang.getHoTenKhachHang());
                    txtSDT.setText(khachHang.getSoDienThoai());
                    txtDiaChi.setText(khachHang.getDiaChi());
                    txtGioiTinhKhachH.setText(khachHang.getGioiTinh());
                    break;
                }
            }
        }
        viewTableKH();
    }//GEN-LAST:event_tblTimKhMouseClicked

    private void txtThongTinXoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtThongTinXoaMouseClicked
        // TODO add your handling code here:
        if(txtThongTinXoa.getText().equals("Nhập Mã Hóa Đơn")) {
            txtThongTinXoa.setText("");
        }
    }//GEN-LAST:event_txtThongTinXoaMouseClicked

    private void txtThongTinXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtThongTinXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThongTinXoaActionPerformed

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
        
//tải lại bảng Sản Phẩm
    public void viewTableSP(){
        DefaultTableModel model1 = (DefaultTableModel) this.tblSanPhams.getModel();
        DefaultTableModel modelKho = (DefaultTableModel) this.tblKhoSP.getModel();
        
        model1.setRowCount(0);
        modelKho.setRowCount(0);
        int n = 1;
        int m = 1;
        int k = 1;
        for (SanPham sanPham : dsSanPham) {
            model1.addRow(new Object[]{
                n++,
                sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()}); 
            modelKho.addRow(new Object[]{
                m++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
//            modelHoaDon.addRow(new Object[]{
//                k++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
        }
    }
    
//tải lại bảng khách Hàng
    public void viewTableKH(){
        DefaultTableModel model2 = (DefaultTableModel) this.tblkhachHangs.getModel();
        int n = 1;//stt
        model2.setRowCount(0);
        
        for (KhachHang khachHang : dsKhachHang) {
            if(khachHang.getChucVu().equalsIgnoreCase("User")) {
            model2.addRow(new Object[]{
                n++,
            khachHang.getMaKhachHang(),khachHang.getHoTenKhachHang(),khachHang.getGioiTinh(),khachHang.getSoDienThoai(),khachHang.getDiaChi()});
            }
        }
        
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
    }
//    
//tải lại bảng Giỏ Hàng
//    public void viewTableGioHang(){
//        DefaultTableModel modelGioHang = (DefaultTableModel) this.TblGioHang.getModel();
//        
//        modelGioHang.setRowCount(0);
//
//        int k = 1;
//        for (SanPham sanPham : dsGioHang) {
//            modelGioHang.addRow(new Object[]{
//                k++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia(),sanPham.getSoLuong() * sanPham.getGia()});
//        }
//    }
    
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

    public void capNhatBanRaLuu(HoaDon newHD) {
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
    
    public void capNhatBanRaXoa(HoaDon hoaDonXoa) {
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
    
    
//    private void initGioiTinh(){
//        String[] gender = new String[]{"Nam","Nu","Khac"};
//        
//        DefaultComboBoxModel<String> cbxModel = new DefaultComboBoxModel<>(gender);
//        
//        cbxGioiTinh.setModel(cbxModel);
//    }
    
    
    
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
    private javax.swing.JButton btnCapNhatSP;
    private javax.swing.JButton btnFindMaHoaDon;
    private javax.swing.JButton btnFindSP;
    private javax.swing.JButton btnLuuSP;
    private javax.swing.JButton btnResetBangHh;
    private javax.swing.JButton btnResetKH;
    private javax.swing.JButton btnResetKho;
    private javax.swing.JButton btnResetSP;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnTimMa_ten;
    private javax.swing.JButton btnXoaSP;
    private javax.swing.JButton btnXoaTTKH;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
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
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblBangKh;
    private javax.swing.JTable tblDoanhThu;
    private javax.swing.JTable tblFindSP;
    private javax.swing.JTable tblHDKH;
    private javax.swing.JTable tblKhoSP;
    private javax.swing.JTable tblSanPhams;
    private javax.swing.JTable tblTimKh;
    private javax.swing.JTable tblkhachHangs;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiaChiHD;
    private javax.swing.JTextField txtFindMaSP;
    private javax.swing.JTextField txtFindTenSP;
    private javax.swing.JTextField txtFindThongTin;
    private javax.swing.JTextField txtGiaCa;
    private javax.swing.JTextField txtGioiTinhKhachH;
    private javax.swing.JTextField txtGtHD;
    private javax.swing.JTextField txtHoTenKH;
    private javax.swing.JTextField txtMaHD;
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
    private javax.swing.JTextField txtTenHD;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtThongTinXoa;
    private javax.swing.JTextField txtTimMa_Ten;
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
