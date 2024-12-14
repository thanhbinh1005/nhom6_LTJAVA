
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

    public void setDsSanPham(ArrayList<SanPham> dsSanPham) {
        this.dsSanPham = dsSanPham;
    }

    public ArrayList<KhachHang> getDsKhachHang() {
        return dsKhachHang;
    }

    public void setDsKhachHang(ArrayList<KhachHang> dsKhachHang) {
        this.dsKhachHang = dsKhachHang;
    }

    public ArrayList<SanPham> getDsGioHang() {
        return dsGioHang;
    }

    public void setDsGioHang(ArrayList<SanPham> dsGioHang) {
        this.dsGioHang = dsGioHang;
    }

    public ArrayList<HoaDon> getDsHoaDon() {
        return dsHoaDon;
    }

    public void setDsHoaDon(ArrayList<HoaDon> dsHoaDon) {
        this.dsHoaDon = dsHoaDon;
    }

    public ArrayList<SanPham> getDsBanRa() {
        return dsBanRa;
    }

    public void setDsBanRa(ArrayList<SanPham> dsBanRa) {
        this.dsBanRa = dsBanRa;
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
        jSeparator2 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        txtHoTenKH = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnResetKH = new javax.swing.JButton();
        btnXoaKH = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblkhachHangs = new javax.swing.JTable();
        txtGioiTinhKhachH = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
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
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblDoanhThu = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
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
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();

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

        jSeparator2.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jSeparator2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel7.setText("Thông Tin Khách Hàng");

        jLabel9.setText("Ma khach hang:");

        jLabel10.setText("Ho Ten khach hang: ");

        jLabel11.setText("So dien thoai: ");

        jLabel13.setText("Gioi tinh: ");

        jLabel12.setText("Dia chi:");

        btnResetKH.setText("Reset");
        btnResetKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKHActionPerformed(evt);
            }
        });

        btnXoaKH.setText("Xoa Khach Hang");
        btnXoaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKHActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel8.setText("Danh Sach Khach Hang");

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

        jButton1.setText("Thoát ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(460, 460, 460)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(35, 35, 35))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(jLabel10)
                                            .addGap(27, 27, 27)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtMaKH)
                                            .addComponent(txtHoTenKH, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(57, 57, 57)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnXoaKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnResetKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(txtGioiTinhKhachH, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel7))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(57, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(27, 27, 27))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel8)
                .addGap(19, 19, 19)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(btnXoaKH))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetKH))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtGioiTinhKhachH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(231, Short.MAX_VALUE))
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
        jPanel2.add(btnLuuSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 140, -1));

        btnXoaSP.setText("Xoa san pham");
        btnXoaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnXoaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, 140, -1));

        btnCapNhatSP.setText("Cap nhat san pham");
        btnCapNhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnCapNhatSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 100, 140, -1));

        btnResetSP.setText("Reset ");
        btnResetSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSPActionPerformed(evt);
            }
        });
        jPanel2.add(btnResetSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 140, 140, -1));

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
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1033, Short.MAX_VALUE)
                        .addGap(13, 13, 13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(319, 319, 319)
                                .addComponent(jLabel2))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(389, 389, 389)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jLabel14.setText("DANH SACH SẢN PHẨM");

        jLabel15.setText("Mã Sản Phẩm");

        jLabel16.setText("Tên Sản Phẩm");

        jLabel17.setText("Số Lượng");

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

        jButton2.setText("Thoát");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane6))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(387, 387, 387)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMinGia, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(27, 27, 27)
                                    .addComponent(txtMinSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel21)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                        .addComponent(btnFindSP, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnResetKho, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(91, 91, 91)
                                .addComponent(jButton2)))))
                .addGap(107, 107, 107))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFindTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(txtFindMaSP)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(342, 342, 342)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(16, 16, 16)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtFindMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(txtMinSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFindSP))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtFindTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(txtMinGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnResetKho)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel21)
                        .addGap(24, 24, 24)
                        .addComponent(jButton2)))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Kho Hàng", jPanel5);

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
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addGap(47, 47, 47)
                .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(303, 303, 303))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(jLabel41))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
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
                    .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(253, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Doanh Thu", jPanel7);

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
        jLabel40.setText("DANH SÁCH KHÁCH HÀNG CÓ HÓA ĐƠN");

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
                .addGap(19, 19, 19)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel31)
                                .addGap(33, 33, 33)
                                .addComponent(btnFindMaHoaDon)
                                .addGap(18, 18, 18)
                                .addComponent(txtFindThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnXoaTTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnThoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNameDsHoaDon)
                                        .addComponent(btnResetBangHh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                                .addComponent(jLabel38)
                                .addGap(388, 388, 388))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                        .addComponent(txtMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                        .addComponent(txtTenHD)
                                        .addComponent(txtGtHD))
                                    .addComponent(txtMaHoaDonAddDs, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(68, 68, 68)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel46)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                                .addComponent(txtDiaChiHD)
                                                .addComponent(txtTongTienHD))))))
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1005, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                        .addGap(19, 19, 19)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33)
                        .addComponent(txtGtHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTongTienHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(txtNameDsHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnResetBangHh)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaTTKH)
                        .addGap(18, 18, 18)
                        .addComponent(btnThoat))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(153, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh Sách Hóa Đơn", jPanel12);

        jLabel48.setText("jLabel48");

        jLabel49.setText("jLabel49");

        jLabel50.setText("jLabel50");

        jLabel51.setText("jLabel51");

        jTextField2.setText("jTextField2");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        jTextField5.setText("jTextField5");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(462, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(231, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(252, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(371, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông Tin Cá Nhân", jPanel13);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1058, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void tblBangKhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangKhMouseClicked
        // TODO add your handling code here:&
        posTableKH = tblBangKh.getSelectedRow();
    }//GEN-LAST:event_tblBangKhMouseClicked

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

    private void btnResetKhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKhoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetKhoActionPerformed

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
//        viewTableKH();
    }//GEN-LAST:event_tblkhachHangsMouseClicked

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

//        viewTableKH();

        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
    }//GEN-LAST:event_btnXoaKHActionPerformed

    private void btnResetKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKHActionPerformed
        txtMaKH.setText("");
        txtHoTenKH.setText("");
        txtDiaChi.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtGioiTinhKhachH.setText("");
//        viewTableKH();
    }//GEN-LAST:event_btnResetKHActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
int choice = JOptionPane.showConfirmDialog(this, "Xác Nhận Thoát Chương Trình","Thông Báo",JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.NO_OPTION)
            return;
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

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
//        DefaultTableModel modelHoaDon = (DefaultTableModel) this.tblDsSP.getModel();

        model1.setRowCount(0);
        modelKho.setRowCount(0);
//        modelHoaDon.setRowCount(0);
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
            if(khachHang.getChucVu().equals("User")) {
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
    private javax.swing.JButton btnXoaKH;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTable tblBangKh;
    private javax.swing.JTable tblDoanhThu;
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
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
