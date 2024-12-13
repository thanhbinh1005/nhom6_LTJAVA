package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HoaDon implements Serializable{
    private String maHoaDon;
    private KhachHang TTKhachHang;
    private String ngayLap;
    private List<SanPham> dsMuaSanPham;
    private double tongTien;

    public HoaDon(String maHoaDon, KhachHang TTKhachHang,String ngayLap) {
        this.maHoaDon = maHoaDon;
        this.TTKhachHang = TTKhachHang;
        this.dsMuaSanPham = new ArrayList<>();
        this.ngayLap = ngayLap;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }



    public double getTongTien() {
        return tongTien ;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
    
    
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
    
    public HoaDon() {
        this.dsMuaSanPham = new ArrayList<>();
    }
    
    // Thêm sản phẩm vào hóa đơn
    public void themSanPham(SanPham sanPham) {
        this.dsMuaSanPham.add(sanPham);
        tongTien += sanPham.getGia() * sanPham.getSoLuong(); // tính lại tổng mỗi khi thêm sản phẩm

    }

    public KhachHang getTTKhachHang() {
        return TTKhachHang;
    }

    public void setTTKhachHang(KhachHang TTKhachHang) {
        this.TTKhachHang = TTKhachHang;
    }

    public List<SanPham> getDsMuaSanPham() {
        return dsMuaSanPham;
    }

    public void setDsMuaSanPham(List<SanPham> dsMuaSanPham) {
        this.dsMuaSanPham = dsMuaSanPham;
    }
    
    public double tinhTongTien(){
        for (SanPham sanPham : dsMuaSanPham) {
            tongTien+=sanPham.getGia()*sanPham.getSoLuong();
        }
        return tongTien;
    }
    


    // Tính tổng tiền (nếu cần cập nhật lại)
    public void capNhatTongTien() {
        tongTien = dsMuaSanPham.stream()
                .mapToDouble(sp -> sp.getGia() * sp.getSoLuong()) // chuyển đổi thành tổng giá 1 sp rồi cộng = hàm sum
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mã Hóa Đơn: ").append(maHoaDon).append("\tNgay Lap: ").append(ngayLap).append("\n");
        sb.append("Thông tin khách hàng:\n").append(TTKhachHang).append("\n");
        sb.append("Danh sach san pham:\n");
        for (SanPham sanPham : dsMuaSanPham) {
            sb.append(sanPham);
        }
        sb.append("Tong tien: ").append(tongTien).append("\n");
        return sb.toString();
    }
}
