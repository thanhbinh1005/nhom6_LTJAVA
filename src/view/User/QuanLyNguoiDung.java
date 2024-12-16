/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.User;

import controller.ControllerHoaDon;
import controller.ControllerKhachHang;
import controller.ControllerKho;
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
//import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.HoaDon;
import model.KhachHang;
import model.SanPham;
import view.Login.DangNhap;
import view.Admin.QuanLyCuaHang;

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
        setLocationRelativeTo(null);
        loadKhachHangFromDB();
        ShowTTCaNhan();
        
        
        qly.loadSanPhamFromDB();
        qly.loadHoaDonFromDB();
        
        qly.viewTableSP();
        viewTableGioHang();
        viewTableSP();
        viewHoaDonOfKhachHang();
       qly.capNhatDsBanRa();
        System.out.println("\n\naaaaaaaaaaaaaaaaa\t"+User_maKh);
    }
    
    public void ShowTTCaNhan(){
        for (KhachHang khachHang : dsKhachHang) {
            if(khachHang.getMaKhachHang().trim().equals(User_maKh)) {
                txtHoTenKH.setText(khachHang.getHoTenKhachHang());
                txtSDT.setText(khachHang.getSoDienThoai());
                txtDiaChi.setText(khachHang.getDiaChi());
                cbxGioiTinh.setSelectedItem(khachHang.getGioiTinh());
                txtMatKhauCaNhan.setText(khachHang.getMatkhau());
                
                txtMaKhHD.setText(User_maKh);
                txtTenHD.setText(khachHang.getHoTenKhachHang());
                txtSDTHD.setText(khachHang.getSoDienThoai());
                txtDiaChiHD.setText(khachHang.getDiaChi());
                txtGtHD.setText(khachHang.getGioiTinh());
                
//                txtHDMaKH.setText(User_maKh);
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
        
        for (SanPham sanPham : dsSanPham) {
            System.out.println(sanPham);
        }
    }
    
    //tải lại bảng Sản Phẩm
    public void viewTableSP(){
        DefaultTableModel modelHoaDon = (DefaultTableModel) this.tblDsSP.getModel();
        DefaultTableModel modelKho = (DefaultTableModel) this.tblKhoSP.getModel();
        modelHoaDon.setRowCount(0);
        modelKho.setRowCount(0);  
        int n = 1;
        int k = 1;
        for (SanPham sanPham : dsSanPham) {
//            System.out.println(sanPham);
            modelHoaDon.addRow(new Object[]{
                k++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
            modelKho.addRow(new Object[]{
                n++,sanPham.getMaSanPham(), sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()});
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
    
    //
    public void viewHoaDonOfKhachHang(){
        DefaultTableModel model = (DefaultTableModel) this.tblDsHoaDonCuaKhach.getModel();
        model.setRowCount(0);
        int n = 1;

        
        for (HoaDon hoaDon : dsHoaDon) {
            if(hoaDon.getTTKhachHang().getMaKhachHang().equals(User_maKh)) {
                for (SanPham sanPham : hoaDon.getDsMuaSanPham()) {
                    model.addRow(new Object[] {
                    n++,
                    hoaDon.getMaHoaDon(),sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()*sanPham.getSoLuong()
                });
                }
            }
        }
    }
    
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDsSP = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        TblGioHang = new javax.swing.JTable();
        btnXoaSpTrongHdon = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txtMaHoaDon = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtHDTen = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtHDSDT = new javax.swing.JTextField();
        txtHDDiaChi = new javax.swing.JTextField();
        txtHDGioiTinh = new javax.swing.JTextField();
        btnAddGioHang = new javax.swing.JButton();
        txtSoLuongSP = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtMAXX = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnResetHD = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnLuuHD = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblHDKH = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnThoat = new javax.swing.JButton();
        btnXoaTTKH = new javax.swing.JButton();
        txtTimMaHoaDon = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDsHoaDonCuaKhach = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtVtriHoaDon = new javax.swing.JTextField();
        btnResetBangHh = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        txtTongTienHD = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtSDTHD = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        txtDiaChiHD = new javax.swing.JTextField();
        txtGtHD = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtMaKhHD = new javax.swing.JTextField();
        txtMaHoaDonAddDs = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtTenHD = new javax.swing.JTextField();
        btnFindMaHoaDon = new javax.swing.JButton();
        txtFindThongTin = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
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
        btnResetKho = new javax.swing.JButton();
        btnFindSP = new javax.swing.JButton();
        txtFindMaSP = new javax.swing.JTextField();
        txtFindTenSP = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnCapNhatKH = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnXoaTaiKhoan = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtMatKhauCaNhan = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtHoTenKH = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbxGioiTinh = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tblDsSP.setBackground(new java.awt.Color(204, 255, 255));
        tblDsSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
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
        tblDsSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDsSPMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblDsSP);

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel19.setText("Tạo Hóa Đơn");

        TblGioHang.setBackground(new java.awt.Color(204, 255, 255));
        TblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
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

        btnXoaSpTrongHdon.setBackground(new java.awt.Color(255, 255, 204));
        btnXoaSpTrongHdon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Rác1.png"))); // NOI18N
        btnXoaSpTrongHdon.setText("Xóa Sản Phẩm");
        btnXoaSpTrongHdon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSpTrongHdonActionPerformed(evt);
            }
        });

        jLabel43.setText("Mã Hóa Đơn");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel22.setText("Thông Tin Khách Hàng");

        jLabel24.setText("Tên khách hàng");

        jLabel27.setText("Địa chỉ");

        jLabel25.setText("Giới tính");

        jLabel26.setText("Số điện thoại");

        btnAddGioHang.setBackground(new java.awt.Color(255, 255, 204));
        btnAddGioHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Scasadđ24-12-15 173130.png"))); // NOI18N
        btnAddGioHang.setText("Thêm vào giỏ hàng");
        btnAddGioHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGioHangActionPerformed(evt);
            }
        });

        jLabel29.setText("Số Lượng");

        jLabel37.setText("MAX");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        jLabel4.setText("(*) Có thể thay đôi thông tin khách hàng");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel24)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtHDTen, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtMaHoaDon)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHDGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHDSDT, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                    .addComponent(txtHDDiaChi)))
                            .addComponent(jLabel4))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMAXX)
                                    .addComponent(txtSoLuongSP))))
                        .addGap(78, 78, 78))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(txtHDSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(txtMAXX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtHDGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtHDDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoLuongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel24)
                            .addComponent(txtHDTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddGioHang)
                            .addComponent(jLabel4))))
                .addGap(13, 13, 13))
        );

        jLabel28.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel28.setText("GIỎ HÀNG");

        btnResetHD.setBackground(new java.awt.Color(255, 255, 204));
        btnResetHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/TaiLai1.png"))); // NOI18N
        btnResetHD.setText("Tải Lại");
        btnResetHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetHDActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 204));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Exit2024-12-15 173511.png"))); // NOI18N
        jButton3.setText("Thoát");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnLuuHD.setBackground(new java.awt.Color(255, 255, 204));
        btnLuuHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mua2024-12-15 173326.png"))); // NOI18N
        btnLuuHD.setText("Lưu Hóa Đơn");
        btnLuuHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuHDActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Ghi Chú 1e7 = 10 triệu");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLuuHD)
                        .addGap(40, 40, 40)
                        .addComponent(btnXoaSpTrongHdon)
                        .addGap(60, 60, 60)
                        .addComponent(btnResetHD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))))
                .addGap(626, 626, 626))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(382, 382, 382)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetHD)
                    .addComponent(btnLuuHD)
                    .addComponent(jButton3)
                    .addComponent(btnXoaSpTrongHdon))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Hóa Đơn", jPanel6);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        tblHDKH.setBackground(new java.awt.Color(204, 255, 255));
        tblHDKH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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

        btnThoat.setBackground(new java.awt.Color(255, 255, 204));
        btnThoat.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Exit2024-12-15 173511.png"))); // NOI18N
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        btnXoaTTKH.setBackground(new java.awt.Color(255, 255, 204));
        btnXoaTTKH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTTKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Rác1.png"))); // NOI18N
        btnXoaTTKH.setText("Xóa Hóa Đơn");
        btnXoaTTKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTTKHActionPerformed(evt);
            }
        });

        txtTimMaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaHoaDonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Nhập Mã Hóa Đơn");

        tblDsHoaDonCuaKhach.setBackground(new java.awt.Color(204, 255, 255));
        tblDsHoaDonCuaKhach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Hóa Đơn", "Tên Sản Phẩm", "Số Lượng", "Giá Thành"
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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Danh Sách Hóa Đơn");

        txtVtriHoaDon.setText("Mã Hóa Đơn Chọn");
        txtVtriHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtVtriHoaDonMouseClicked(evt);
            }
        });

        btnResetBangHh.setBackground(new java.awt.Color(255, 255, 204));
        btnResetBangHh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetBangHh.setText("TẢI LẠI");
        btnResetBangHh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetBangHhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnThoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXoaTTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTimMaHoaDon)
                        .addComponent(txtVtriHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnResetBangHh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResetBangHh, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoaTTKH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtVtriHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThoat)))
                .addContainerGap())
        );

        jLabel36.setText("Tổng Tiền");

        jLabel35.setText("Địa Chỉ");

        jLabel46.setText("Ngày Tạo");

        jLabel34.setText("SDT");

        jLabel33.setText("Giới Tính");

        jLabel32.setText("Tên Khách Hàng");

        jLabel30.setText("Mã Khách Hàng");

        txtMaHoaDonAddDs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHoaDonAddDsActionPerformed(evt);
            }
        });

        jLabel44.setText("Mã Hóa Đơn");

        btnFindMaHoaDon.setBackground(new java.awt.Color(255, 255, 204));
        btnFindMaHoaDon.setText("Tìm Kiếm Mã Hóa Đơn");
        btnFindMaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindMaHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel30)
                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel44))
                .addGap(37, 37, 37)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtMaKhHD)
                        .addComponent(txtTenHD)
                        .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDiaChiHD)
                                    .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFindMaHoaDon)
                            .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFindMaHoaDon))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(txtDiaChiHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel30)
                                    .addComponent(txtMaKhHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel44))
                                .addGap(52, 52, 52)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 845, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(97, 97, 97)
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel31)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(98, 98, 98)
                .addComponent(jLabel38)
                .addGap(394, 394, 394))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(192, 192, 192)
                        .addComponent(jLabel39))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông Tin Hóa Đơn", jPanel12);

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
        jScrollPane3.setBounds(20, 60, 850, 278);

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel14.setText("DANH SACH SẢN PHẨM");
        jPanel5.add(jLabel14);
        jLabel14.setBounds(330, 20, 206, 21);

        tblFindSP.setBackground(new java.awt.Color(204, 255, 204));
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

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(20, 470, 850, 189);

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel18.setText("Giá Thành");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel17.setText("Số Lượng");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel21.setText("TỐI ĐA");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel20.setText("TỐI THIỂU");

        btnResetKho.setBackground(new java.awt.Color(204, 204, 204));
        btnResetKho.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnResetKho.setText("Tải Lại");
        btnResetKho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKhoActionPerformed(evt);
            }
        });

        btnFindSP.setBackground(new java.awt.Color(204, 204, 204));
        btnFindSP.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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
                        .addGap(369, 369, 369)
                        .addComponent(jLabel20))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFindTenSP)
                            .addComponent(txtFindMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFindSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetKho, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel21))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addComponent(btnFindSP)
                            .addGap(20, 20, 20)
                            .addComponent(btnResetKho))
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17)
                                .addComponent(txtMinSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(11, 11, 11)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel18)
                                .addComponent(txtMinGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtFindMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15))
                            .addGap(12, 12, 12)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtFindTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel13);
        jPanel13.setBounds(20, 347, 850, 110);
        jPanel5.add(jSeparator4);
        jSeparator4.setBounds(20, 460, 850, 10);

        jTabbedPane1.addTab("Kho Hàng", jPanel5);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnCapNhatKH.setBackground(new java.awt.Color(255, 255, 204));
        btnCapNhatKH.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnCapNhatKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/TaiLai1.png"))); // NOI18N
        btnCapNhatKH.setText("Cập nhật");
        btnCapNhatKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatKHActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setText("Mật Khẩu");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Giới Tính");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Địa Chỉ");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("Số Điện Thoại");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Họ Tên");

        btnXoaTaiKhoan.setBackground(new java.awt.Color(255, 255, 204));
        btnXoaTaiKhoan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnXoaTaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Rác1.png"))); // NOI18N
        btnXoaTaiKhoan.setText("Xóa Tài Khoản");
        btnXoaTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTaiKhoanActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 204));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Exit2024-12-15 173511.png"))); // NOI18N
        jButton2.setText("Đăng Xuất");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel7.setText("Thông Tin Cá Nhân");

        cbxGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMatKhauCaNhan, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxGioiTinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnXoaTaiKhoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(299, 299, 299)
                        .addComponent(btnCapNhatKH, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(308, 308, 308)
                        .addComponent(jLabel7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel7)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cbxGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaTaiKhoan))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatKhauCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCapNhatKH)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 31, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(294, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông Tin Cá Nhân", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCapNhatKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatKHActionPerformed
        // TODO add your handling code here:
        StringBuilder sb = new StringBuilder();

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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ và chính xác thông tin!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn cập nhật thông tin không!","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
                
            // chuyển giới tính chọn bởi combobox thành dãy string cho dễ
            // set lại mọi thứ cho khách
            String gioiTinh = (String) cbxGioiTinh.getSelectedItem();
            for (KhachHang khachHang : dsKhachHang) {
                if(khachHang.getMaKhachHang().equals(User_maKh.trim())){
                    khachHang.setMaKhachHang(User_maKh.trim());
                    khachHang.setHoTenKhachHang(txtHoTenKH.getText().trim());
                    khachHang.setGioiTinh(gioiTinh);
                    khachHang.setSoDienThoai(txtSDT.getText().trim());
                    khachHang.setDiaChi(txtDiaChi.getText().trim());
                    khachHang.setMatkhau(txtMatKhauCaNhan.getText().trim());
                }
            }
            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            //gọi hàm update để sửa thông tin khách
            ctlerKhachHang.updateKhachHangToDB(User_maKh,txtMatKhauCaNhan.getText().trim(), txtHoTenKH.getText(),gioiTinh,txtSDT.getText(), txtDiaChi.getText());
        }
        qly.viewTableKH();
        ShowTTCaNhan();
    }//GEN-LAST:event_btnCapNhatKHActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
//        this.setVisible(false);
    int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnFindMaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindMaHoaDonActionPerformed
         StringBuilder sb = new StringBuilder();
        if(txtFindThongTin.getText().equals(""))
        sb.append("Vui Lòng Nhập Tên Khách Hàng Trước Khi Nhấn Tìm Kiếm!");
        if(sb.length()> 0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) this.tblHDKH.getModel();
        model.setRowCount(0);
//        HoaDon hoaDon = new HoaDon();
        int n = 1;
        double sum = 0;
        boolean check = false;
        for (HoaDon hoaDon1 : dsHoaDon) {
            if (txtFindThongTin.getText().equals(hoaDon1.getMaHoaDon().trim()) && hoaDon1.getTTKhachHang().getMaKhachHang().equals(User_maKh)) {
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

    private void txtMaHoaDonAddDsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHoaDonAddDsActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.tblHDKH.getModel();
        model.setRowCount(0);
        double sum = 0;
        int n = 1;
//        HoaDon hoaDon = new HoaDon();
        txtMaHoaDonAddDs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = 1;
                for (HoaDon hd : dsHoaDon) {
                    if(txtMaHoaDonAddDs.getText().trim().equals(hd.getMaHoaDon())) {
                        txtMaHoaDonAddDs.setText(hd.getMaHoaDon());
                        txtMaKhHD.setText(hd.getTTKhachHang().getMaKhachHang());
                        txtTenHD.setText(hd.getTTKhachHang().getHoTenKhachHang());
                        txtSDTHD.setText(hd.getTTKhachHang().getSoDienThoai());
                        txtGtHD.setText(hd.getTTKhachHang().getGioiTinh());
                        txtDiaChiHD.setText(hd.getTTKhachHang().getDiaChi());
                        txtNgayTao.setText(hd.getNgayLap());
                        break;
                    }
                }
            }
        });
        
        for (HoaDon hd : dsHoaDon) {
            if(hd.getMaHoaDon().equals(txtMaHoaDonAddDs.getText().trim())) {
                for (SanPham sanPham : hd.getDsMuaSanPham()) {
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
            }
        }
    }//GEN-LAST:event_txtMaHoaDonAddDsActionPerformed

    private void tblDsHoaDonCuaKhachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDsHoaDonCuaKhachMouseClicked
        // TODO add your handling code here:
        posTableDs = tblDsHoaDonCuaKhach.getSelectedRow();
        String vtri = tblDsHoaDonCuaKhach.getValueAt(posTableDs, 1).toString();
        txtVtriHoaDon.setText(vtri);
    }//GEN-LAST:event_tblDsHoaDonCuaKhachMouseClicked

    private void txtTimMaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaHoaDonActionPerformed
        // TODO add your handling code here:
        String thongtin = txtTimMaHoaDon.getText().trim().toLowerCase();

        StringBuilder sb = new StringBuilder();
        if(txtTimMaHoaDon.getText().equals(""))
        sb.append("Vui Lòng Thông Tin Hóa Đơn Trước Khi Nhấn Tìm Kiếm!");
        if(sb.length()> 0){
            JOptionPane.showMessageDialog(this, sb.toString(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<HoaDon> result = new ArrayList<>();
        ArrayList<HoaDon> tmp = new ArrayList<>();
        for (HoaDon hoaDon : dsHoaDon) {
            for (SanPham sanPham : hoaDon.getDsMuaSanPham()) {
                if(hoaDon.getMaHoaDon().toLowerCase().contains(thongtin) ) {
                    result.add(hoaDon);
                    break;
                }
            }
        }
        
        DefaultTableModel model = (DefaultTableModel) this.tblDsHoaDonCuaKhach.getModel();
        if(result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm Thông Tin phù hợp!");
        } else {
            model.setRowCount(0);
            int n = 1;
            for (HoaDon hoaDon : result) {
                for (SanPham sanPham : hoaDon.getDsMuaSanPham()) {
                    if(hoaDon.getTTKhachHang().getMaKhachHang().equals(User_maKh)){
                        System.out.println(sanPham);
                            model.addRow(new Object[] {
                            n++,
                            hoaDon.getMaHoaDon(),sanPham.getTenSanPham(),sanPham.getSoLuong(),sanPham.getGia()*sanPham.getSoLuong()
                        });
                    }
                }
            }
        }
    }//GEN-LAST:event_txtTimMaHoaDonActionPerformed

    private void btnXoaTTKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTTKHActionPerformed
        // TODO add your handling code here:
        controllerSanPham ctler = new controllerSanPham();
        int choice = JOptionPane.showConfirmDialog(this, "Xóa Hóa Đơn Khach Hang","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;

        String maHoaDon = txtVtriHoaDon.getText().trim();
        if(maHoaDon.equals("")){
            JOptionPane.showMessageDialog(this, "Không Tồn Tại Mã Khách Hàng!","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
         boolean check = ctlerHoaDon.checkMaHoaDon(maHoaDon);
         if(check == true){ // kiểm tra xem có mã hóa đơn trong danh sách hóa đơn không
            for (HoaDon hoaDon : dsHoaDon) {
                if(hoaDon.getMaHoaDon().equals(maHoaDon)) {
                    for (SanPham dsSp : hoaDon.getDsMuaSanPham()) {
                        for (SanPham sanPham : dsSanPham) {
                            if(dsSp.getMaSanPham().equals(sanPham.getMaSanPham())) {
                                ctler.tangSoLuong(sanPham.getMaSanPham(), sanPham.getSoLuong(), dsSp.getSoLuong());
                                sanPham.setSoLuong(sanPham.getSoLuong() + dsSp.getSoLuong());
                                break;
                            }
                        }
                    }
                }
            }

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

            viewHoaDonOfKhachHang();
            viewTableSP();
            qly.viewTableDoanhThu();
            qly.viewKhachHangDK();

            //xóa chi tiết hóa đơn trước rồi mới xóa hóa đơn
            ctlerHoaDon.deleteHoaDon(maHoaDon);
            JOptionPane.showMessageDialog(this, "Hóa đơn và thông tin khách hàng đã được xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            txtVtriHoaDon.setText("");
         }
         else
             JOptionPane.showMessageDialog(this, "Không Có Mã Hóa Đơn Phù Hợp");
    }//GEN-LAST:event_btnXoaTTKHActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;
        System.exit(0);

    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnLuuHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuHDActionPerformed
        // Lấy thông tin khách hàng và hóa đơn
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
        if ( tenKh.isEmpty() || gioiTinh.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || maHoaDon.isEmpty()) {
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
        KhachHang newkh = new KhachHang(User_maKh, tenKh, gioiTinh, sdt, diaChi);

        // Tạo đối tượng hóa đơn mới
        HoaDon newHD = new HoaDon(maHoaDon, newkh,dateString);

        // Thêm sản phẩm vào hóa đơn từ giỏ hàng
        for (SanPham sanPham : dsGioHang) 
            newHD.themSanPham(sanPham);
        
        // Thêm hóa đơn vào danh sách
        dsHoaDon.add(newHD);

        // Cập nhật lại các bảng
        qly.capNhatBanRaLuu(newHD);
        viewHoaDonOfKhachHang();
        controllerSanPham ctlerSP = new controllerSanPham();

        // Cập nhật số lượng sản phẩm trong danh sách sản phẩm
        for (SanPham giohang : dsGioHang) {
            for (SanPham sanPham : dsSanPham) {
                if (sanPham.getMaSanPham().equals(giohang.getMaSanPham())) {
                    ctlerSP.giamSoLuong(sanPham.getMaSanPham(), sanPham.getSoLuong(), giohang.getSoLuong());
                    int newsolg = sanPham.getSoLuong() - giohang.getSoLuong();
                    sanPham.setSoLuong(newsolg);
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
        txtMaHoaDon.setText("");// Làm sạch các trường nhập
        txtMAXX.setText("");
        qly.capNhatDsBanRa();
        // Làm sạch giỏ hàng và bảng giỏ hàng
        dsGioHang.clear();
        DefaultTableModel model2 = (DefaultTableModel) this.TblGioHang.getModel();
        model2.setNumRows(0);

        // Cập nhật lại danh sách khách hàng
        qly.viewKhachHangDK();
    }//GEN-LAST:event_btnLuuHDActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //        this.setVisible(false);
        int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnResetHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetHDActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn reset hóa đơn không","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
        return;
        else {
//            txtHDDiaChi.setText("");
//            txtHDGioiTinh.setText("");
//            txtHDMaKH.setText("");
//            txtHDSDT.setText("");
//            txtHDTen.setText("");
            txtMAXX.setText("");
            dsGioHang.clear();
            DefaultTableModel model = (DefaultTableModel) this.TblGioHang.getModel();
            model.setNumRows(0);
        }
    }//GEN-LAST:event_btnResetHDActionPerformed

    private void btnAddGioHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGioHangActionPerformed
        //        dsGioHang.clear();
        DefaultTableModel model = (DefaultTableModel) this.TblGioHang.getModel();
        model.setRowCount(0);
        String inputSoLuongSP = txtSoLuongSP.getText().trim();
        int soLuongNew = 0;
        try {// kiểm tra tính hợp lệ của số lượng sản phẩm muốn nhập
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
        SanPham selectSP = dsSanPham.get(posTableHD);//gắn evnt đê lấy thông tin sản phẩm tại ví trí click
        String masp = selectSP.getMaSanPham();
        String tensp = selectSP.getTenSanPham();
        int soluong;
        double giaca = selectSP.getGia();

        if(soLuongNew > selectSP.getSoLuong()) { // kiểm tra số lượng 
            JOptionPane.showMessageDialog(this, "Vượt quá số lượng cho phép!","lỗi",JOptionPane.ERROR_MESSAGE);
            txtSoLuongSP.setText("");
            viewTableGioHang();
            return;
        }
        else {
            soluong = soLuongNew;
        }
        
        SanPham s1 = new SanPham(masp, tensp, soluong, giaca); // tạo sản phẩm mới 
        int n = 1;
        for (SanPham sanPham : dsGioHang) {
            if(sanPham.getMaSanPham().equals(selectSP.getMaSanPham())) {// kiểm tra sản phẩm có trong giỏ hàng chưa
                JOptionPane.showMessageDialog(this, "Đã Có Trong Giỏ Hàng","Thông Báo!",JOptionPane.INFORMATION_MESSAGE);
                txtSoLuongSP.setText("");
                viewTableGioHang();// tải lại bảng giỏ hàng
                return;
            }
        }
        dsGioHang.add(s1);// lưu sản phẩm vào trong ds
        for (SanPham sanPham : dsGioHang) {// hiện thị sản phẩm lên bảng giỏ hàng
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
        JOptionPane.showMessageDialog(this, "Xóa Thành Công");
        viewTableGioHang();
    }//GEN-LAST:event_btnXoaSpTrongHdonActionPerformed

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

    private void btnXoaTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTaiKhoanActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Xóa Tài Khoản","Xác nhận",JOptionPane.YES_NO_OPTION) ;
        if(choice == JOptionPane.NO_OPTION){
            return;
        }
        
        ControllerKhachHang ctlerKh = new ControllerKhachHang();
        ControllerHoaDon ctlerHoaDon = new ControllerHoaDon(controllerSQL);
        
        boolean check = false;
        for (HoaDon hoaDon : dsHoaDon) {
            if(hoaDon.getTTKhachHang().getHoTenKhachHang().equals(User_maKh.trim())) {
                ctlerHoaDon.deleteHoaDon(hoaDon.getMaHoaDon());
                System.out.println("Xóa hóa đơn thành công!");
                check = true;
                
            }
        }
        if(check) {
            ctlerKh.deleteKhachHangToDB(User_maKh.trim());
            System.out.println("xóa khách hàng thành công");
            System.exit(0);
        }
        
    }//GEN-LAST:event_btnXoaTaiKhoanActionPerformed

    private void txtVtriHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVtriHoaDonMouseClicked
        // TODO add your handling code here:
        if(txtVtriHoaDon.getText().equals("Mã Hóa Đơn Chọn")) {
            txtVtriHoaDon.setText("");
        }
    }//GEN-LAST:event_txtVtriHoaDonMouseClicked

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
            JOptionPane.showMessageDialog(this,"Số Lượng Và Giá Cả Không Hợp Lệ!\n","Loi",JOptionPane.ERROR_MESSAGE);
            return;

        }

        ArrayList<SanPham> list = ControllerKho.timKiemSanPham(dsSanPham, maSanPham, tenSanPham, soluongMin, soluongMax, giaMin, giaMax);
        if(list.isEmpty()){
            DefaultTableModel model = (DefaultTableModel) this.tblFindSP.getModel();
            JOptionPane.showMessageDialog(this, "Không Tìm Thấy Sản Phẩm Phù Hợp.","Thông báo",JOptionPane.INFORMATION_MESSAGE);
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

    private void btnResetBangHhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBangHhActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.tblDsHoaDonCuaKhach.getModel();
        model.setRowCount(0);
        viewHoaDonOfKhachHang();
    }//GEN-LAST:event_btnResetBangHhActionPerformed

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
    private javax.swing.JButton btnFindSP;
    private javax.swing.JButton btnLuuHD;
    private javax.swing.JButton btnResetBangHh;
    private javax.swing.JButton btnResetHD;
    private javax.swing.JButton btnResetKho;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnXoaSpTrongHdon;
    private javax.swing.JButton btnXoaTTKH;
    private javax.swing.JButton btnXoaTaiKhoan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxGioiTinh;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
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
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblDsHoaDonCuaKhach;
    private javax.swing.JTable tblDsSP;
    private javax.swing.JTable tblFindSP;
    private javax.swing.JTable tblHDKH;
    private javax.swing.JTable tblKhoSP;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiaChiHD;
    private javax.swing.JTextField txtFindMaSP;
    private javax.swing.JTextField txtFindTenSP;
    private javax.swing.JTextField txtFindThongTin;
    private javax.swing.JTextField txtGtHD;
    private javax.swing.JTextField txtHDDiaChi;
    private javax.swing.JTextField txtHDGioiTinh;
    private javax.swing.JTextField txtHDSDT;
    private javax.swing.JTextField txtHDTen;
    private javax.swing.JTextField txtHoTenKH;
    private javax.swing.JTextField txtMAXX;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtMaHoaDonAddDs;
    private javax.swing.JTextField txtMaKhHD;
    private javax.swing.JTextField txtMatKhauCaNhan;
    private javax.swing.JTextField txtMaxGia;
    private javax.swing.JTextField txtMaxSoLuong;
    private javax.swing.JTextField txtMinGia;
    private javax.swing.JTextField txtMinSoLuong;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDTHD;
    private javax.swing.JTextField txtSoLuongSP;
    private javax.swing.JTextField txtTenHD;
    private javax.swing.JTextField txtTimMaHoaDon;
    private javax.swing.JTextField txtTongTienHD;
    private javax.swing.JTextField txtVtriHoaDon;
    // End of variables declaration//GEN-END:variables
}
