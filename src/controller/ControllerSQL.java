/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import database.DBConnection;
import java.sql.*;
import javax.swing.JOptionPane;
import model.HoaDon;
import model.SanPham;

public class ControllerSQL {
    private Connection conn = DBConnection.connect();
    boolean check= false;
    
    // lưu thông tin khách hàng vào database
    public void luuKhachHang(String maKh, String matkhau, String tenKh, String chucVu, String gioiTinh, String sdt, String diaChi){
        // kiểm tra mã xem trùng khóa chính nào ở trong dsKhachHang đã lưu không
        if(checkMaKh(maKh)) {
            JOptionPane.showMessageDialog(null, "Mã Khách Hàng Đã Tồn Tại","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "INSERT INTO khach_hang (ma_khach_hang,mat_khau,ho_ten,chuc_vu,gioi_tinh,so_dien_thoai,dia_chi) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)){// hàm chuyên dùng để thực hiện select, update, delete
            pst.setString(1, maKh);
            pst.setString(2, matkhau);
            pst.setString(3, tenKh);
            pst.setString(4, chucVu);
            pst.setString(5, gioiTinh);
            pst.setString(6, sdt);
            pst.setString(7, diaChi);
            pst.executeUpdate(); // lệnh thực thi câu lệnh
            JOptionPane.showMessageDialog(null, "Khách Hàng Đã Được Thêm!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng!");
        }
    }
    
    // xóa khách hàng ra khỏi database khach_hang
    public void xoaKhachHang(String maKh){
        if(maKh.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã khách Hàng Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "DELETE FROM khach_hang WHERE ma_khach_hang = ?"; 
        try (PreparedStatement pst = conn.prepareStatement(sql)){ // PreparedStatement 1 lớp trong java có tác dụng thực thi câu lênh vs các tham số ? kia kìa
            pst.setString(1, maKh); // đều bắt đâuù từ 1 , 0 là lỗi
            int rowAffected = pst.executeUpdate(); // chạy lệnh, rowAffected = dòng bị ảnh hưởng
            if(rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "Khách Hàng Đã Được Xóa Khỏi Database!","Thành Công",JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Khách Hàng Trong Danh Sách Bộ Nhớ!","Lỗi",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Xóa Khách Hàng!","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //sửa thông tin khách hàng trong database
    public void updateKhachHangToDB(String maKh, String matkhau, String tenKh, String gioiTinh, String sdt, String diaChi){
        if(maKh.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã khách Hàng Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "UPDATE khach_hang SET ho_ten = ?,gioi_tinh = ?, so_dien_thoai = ?, dia_chi = ?,mat_khau = ? WHERE ma_khach_hang = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            
            pst.setString(1, tenKh);
            pst.setString(2, gioiTinh);
            pst.setString(3, sdt);
            pst.setString(4, diaChi);
            pst.setString(5, matkhau);
            pst.setString(6, maKh);
            int row = pst.executeUpdate();// leệnh thực thi câu lệnh sql
            if(row > 0) {
                JOptionPane.showMessageDialog(null, "Khách Hàng Đã Được Sửa!","Thành Công",JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Khách Hàng Trong Danh Sách Bộ Nhớ!","Lỗi",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Sửa Khách Hàng!","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void suaMK(String maKh, String matkhau, String tenKh){
        if(maKh.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã khách Hàng Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "UPDATE khach_hang SET mat_khau = ? WHERE ma_khach_hang = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            
            pst.setString(5, matkhau);
            pst.setString(6, maKh);
            int row = pst.executeUpdate();// leệnh thực thi câu lệnh sql
            if(row > 0) {
                JOptionPane.showMessageDialog(null, "Khách Hàng Đã Được Sửa!","Thành Công",JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Khách Hàng Trong Danh Sách Bộ Nhớ!","Lỗi",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Sửa Khách Hàng!","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //kiểm tra xem có bị trùng mã khách hàng(khóa chính k)
    public boolean checkMaKh(String makh){
        String sql = "SELECT COUNT(*) FROM khach_hang WHERE ma_khach_hang = ?";// viết câu lệnh thực thi
        try (PreparedStatement pst = conn.prepareStatement(sql)){// hàm chuyên dùng để thực hiện select, update, delete
            pst.setString(1, makh);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return rs.getInt(1) > 0;// Nếu có bản ghi tồn tại, trả về true = 0
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTaiKhoan(String makh,String matkhau){
        String sql = "SELECT COUNT(*) FROM khach_hang WHERE ma_khach_hang = ? AND mat_khau = ?";// viết câu lệnh thực thi
        try (PreparedStatement pst = conn.prepareStatement(sql)){// hàm chuyên dùng để thực hiện select, update, delete
            pst.setString(1, makh);
            pst.setString(2, matkhau);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return rs.getInt(1) > 0;// Nếu có bản ghi tồn tại, trả về true = 0
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTonTai(String makh,String role){
        String sql = "SELECT COUNT(*) FROM khach_hang WHERE ma_khach_hang = ? AND chuc_vu = ?";// viết câu lệnh thực thi
        try (PreparedStatement pst = conn.prepareStatement(sql)){// hàm chuyên dùng để thực hiện select, update, delete
            pst.setString(1, makh);
            pst.setString(2, role);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                return rs.getInt(1) > 0;// Nếu có bản ghi tồn tại, trả về true = 0
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
        public void upadteMk(String makh,String mk){
        String sql = "UPDATE khach_hang Set mat_khau = ? Where ma_khach_hang = ?";
           try (PreparedStatement pst = conn.prepareStatement(sql)){
                pst.setString(1, mk);
                pst.setString(2, makh);
                
                int rowAffectd = pst.executeUpdate();
                if(rowAffectd > 0)
                    JOptionPane.showMessageDialog(null, "Thành Công");
                else 
                    JOptionPane.showMessageDialog(null, "Không Thay Đổi Được");
              } catch (SQLException e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(null, "Lỗi Khi thay đổi Mật Khẩu","Lỗi",JOptionPane.ERROR_MESSAGE);
         }
    }
    
    //--------------------------------------------------------------------------------------------------------------------------------------
    
    //Lưu thông tin sản phẩm vào database
    public void luuSanPham(String maSp, String tenSp, int soluong, double gia){
        if(maSp.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã Sản Phẩm Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(checkMaSP(maSp)){
            JOptionPane.showMessageDialog(null, "Mã Sản Phẩm Đã Tồn Tại","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = "INSERT INTO san_pham (ma_san_pham,ten_san_pham,so_luong,gia_ca)VALUES (?,?,?,?)";// viết câu lệnh thực thi
        try (PreparedStatement pst = conn.prepareStatement(sql)){// hàm chuyên dùng để thực hiện select, update, delete
            pst.setString(1, maSp);
            pst.setString(2, tenSp);
            pst.setInt(3, soluong);
            pst.setDouble(4, gia);
            // lệnh thực thi câu lệnh
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sản phẩm Đã Được Thêm!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Lưu Sản Phẩm","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void xoaSanPham(String maSp){
        // check xem mã sản phấm có trông không
        if(maSp.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã Sản Phẩm Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "DELETE FROM san_pham WHERE ma_san_pham = ?"; // viết câu lệnh thực thi
        try (PreparedStatement pst = conn.prepareStatement(sql)){ // hàm chuyên dùng để thực hiện select, update, delete
            pst.setString(1, maSp);
            
            int rowAffected = pst.executeUpdate(); // lấy dòng thay đổi true = 1
            if(rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "Đã Xóa Sản Phẩm Khỏi database!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Sản Phẩm Trong Danh Sách Bộ Nhớ!","Lỗi",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Xóa Sản Phẩm!","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void suaSanPham(String maSp, String tenSp, int soluong, double gia) {
        // check xem mã sản phấm có trông không
        if(maSp.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã Sản Phẩm Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "UPDATE san_pham SET ten_san_pham = ?, so_luong = ?, gia_ca = ? WHERE ma_san_pham = ?";
        try (PreparedStatement pst = conn.prepareCall(sql)){
            pst.setString(1, tenSp);
            pst.setInt(2, soluong);
            pst.setDouble(3, gia);
            pst.setString(4, maSp);
            // lệnh thực thi câu lệnh
            int rowAffected = pst.executeUpdate(); // lấy dòng bị thay đổi
            if(rowAffected > 0) // kiểm tra = 1 => đã thay đổi dữ liệu
                JOptionPane.showMessageDialog(null, "Sản Phẩm Đã Được Sửa!","Thành Công",JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Sản Phẩm Trong Danh Sách Bộ Nhớ!", "Lỗi" ,JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Sửa Sản Phẩm","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean checkMaSP(String masp){
        String sql = "SELECT COUNT(*) FROM san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, masp);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;// Nếu có bản ghi tồn tại, trả về true = 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //-----------------------------------------------------------------------------
    
    public boolean kiemTraMaHoaDon(String maHoaDon) {
        String sql = "SELECT COUNT(*) FROM hoa_don WHERE ma_hoa_don = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, maHoaDon);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Mã hóa đơn đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mã hóa đơn chưa tồn tại
    }

    public void luuHoaDon(HoaDon hoadon){
        if(kiemTraMaHoaDon(hoadon.getMaHoaDon())){
            JOptionPane.showMessageDialog(null, "Mã Hóa Đơn Trùng Lặp");
            return;
        }
        
        String sql = "INSERT INTO hoa_don(ma_hoa_don, ma_khach_hang, ngay_lap, tong_tien) VALUES (?,?,?,?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, hoadon.getMaHoaDon().trim());
            pst.setString(2, hoadon.getTTKhachHang().getMaKhachHang().trim());
            pst.setString(3, hoadon.getNgayLap());
            pst.setDouble(4, hoadon.getTongTien());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu hóa đơn!");
        }
    }

    public void luuChiTietHoaDon(HoaDon hoadon){
        if (!kiemTraMaHoaDon(hoadon.getMaHoaDon())) { 
            JOptionPane.showMessageDialog(null, "Mã Hóa Đơn Không Tồn Tại");
            return; 
        }
        
        String sql = "INSERT INTO chi_tiet_hoa_don(ma_hoa_don, ma_san_pham, so_luong, gia, thanh_tien) VALUES (?,?,?,?,?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            for (SanPham sanPham : hoadon.getDsMuaSanPham()) {
                pst.setString(1, hoadon.getMaHoaDon());
                pst.setString(2, sanPham.getMaSanPham());
                pst.setInt(3, sanPham.getSoLuong());
                pst.setDouble(4, sanPham.getGia());
                pst.setDouble(5, sanPham.getGia()*sanPham.getSoLuong());
                pst.addBatch();// lưu vào batch để giảm số lần truy cập thằng vào database
            }
            pst.executeBatch();// thực thi các lệnh ở trong batch 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu chi tiết hóa đơn!");        
        }
    }
  
    public void xoaHoaDon(String mahoadon){
        if(mahoadon.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã hóa đơn Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "DELETE FROM hoa_don WHERE ma_hoa_don = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, mahoadon);
            int rowAffected = pst.executeUpdate();
            if(rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "Đã Xóa hóa đơn Khỏi database!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy hóa đơn Trong Danh Sách Bộ Nhớ!","Lỗi",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Xóa Hóa Đơn !","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void xoaChiTietHoaDon(String mahoadon){
        if(mahoadon.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã hóa đơn Trống","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "DELETE FROM chi_tiet_hoa_don WHERE ma_hoa_don = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, mahoadon);
            int rowAffected = pst.executeUpdate();
            if(rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "Đã Xóa Thông Tin hóa đơn Khỏi database!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                check = true;
            } else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy hóa đơn Trong Danh Sách Bộ Nhớ!","Lỗi",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi Xóa Thông Tin Hóa Đơn !","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public void deleteHoaDon(String maHoaDon) {
        xoaChiTietHoaDon(maHoaDon);
        if(check) {
            xoaHoaDon(maHoaDon);
        }
    }

    //-------------------------------------------------------------------------
    public void capNhatSPkhiLuuHD(String maSP, int old, int newSlg){
        String sql = "UPDATE san_pham SET so_luong = ? WHERE ma_san_pham = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, old - newSlg);
            pst.setString(2, maSP);
            
           int rowAffected = pst.executeUpdate(); // lấy dòng bị thay đổi
            if(rowAffected > 0) // kiểm tra = 1 => đã thay đổi dữ liệu
                JOptionPane.showMessageDialog(null, "Thay đổi số lượng thành công!","Thành Công",JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Sản Phẩm Trong Danh Sách Bộ Nhớ!", "Lỗi" ,JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi thay đổi số lượng","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void capNhatSPkhiXoaHD(String maSP, int old, int newSlg){
        String sql = "UPDATE san_pham SET so_luong = ? WHERE ma_san_pham = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, old + newSlg);
            pst.setString(2, maSP);
            
           int rowAffected = pst.executeUpdate(); // lấy dòng bị thay đổi
            if(rowAffected > 0) // kiểm tra = 1 => đã thay đổi dữ liệu
                JOptionPane.showMessageDialog(null, "Thay đổi số lượng thành công!","Thành Công",JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Không Tìm Thấy Sản Phẩm Trong Danh Sách Bộ Nhớ!", "Lỗi" ,JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi Khi thay đổi số lượng","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    //--------------------------------------------------------------------------------

    
}
