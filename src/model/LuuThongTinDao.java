
package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import view.QuanLyCuaHang;
//import java.util.List;

public class LuuThongTinDao{
    private static final String FILE = "hoadon.dat";
    
    public static void ghiDanhSachKhachHang(ArrayList<KhachHang> danhSach) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("dskhachhang.dat"))) {
            oos.writeObject(danhSach);
            System.out.println("Da ghi danh sach khach hang vao file.");
        } catch (IOException e) {
            System.out.println("Loi khi ghi file: " + e.getMessage());
        }
    }

    // Ham doc danh sach khach hang tu file
    public static ArrayList<KhachHang> docDanhSachKhachHang() {
        ArrayList<KhachHang> danhSach = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("dskhachhang.dat"))) {
            danhSach = (ArrayList<KhachHang>) ois.readObject();
            System.out.println("Da doc danh sach khach hang tu file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Loi khi doc file: " + e.getMessage());
        }
        return danhSach;
    }
    
    public static void ghiDanhSachSanPham(ArrayList<SanPham> sanPhams) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("dsSanPham.dat"))) {
            oos.writeObject(sanPhams);
            System.out.println("Da ghi danh sach San Pham vao file.");
        } catch (IOException e) {
            System.out.println("Loi khi ghi file: " + e.getMessage());
        }
    }

    // Ham doc danh sach khach hang tu file
    public static ArrayList<SanPham> docDanhSachSanPham() {
        ArrayList<SanPham> sanPhams = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("dsSanPham.dat"))) {
            sanPhams = (ArrayList<SanPham>) ois.readObject();
            System.out.println("Da doc danh sach San Pham tu file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Loi khi doc file: " + e.getMessage());
        }
        return sanPhams;
    }
     
//------------------------------------

    // Ghi danh sách khách hàng vào file
    public static void ghiHoaDon( ArrayList<HoaDon> dsHoaDon) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(dsHoaDon);
            System.out.println("Dữ liệu khách hàng đã được ghi vào file: " + FILE);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Loi khi doc file: " + e.getMessage());
        }
    }

    // Đọc danh sách khách hàng từ file
    @SuppressWarnings("unchecked")
    public static ArrayList<HoaDon> docDanhSachHoaDon() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            return (ArrayList<HoaDon>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Loi khi doc file: " + e.getMessage());
            return null;
        }
    }
    
    public static void ghiDanhSachUser(ArrayList<User> danhSach) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("dsUser.dat"))) {
            oos.writeObject(danhSach);
            System.out.println("Da ghi danh sach khach hang vao file.");
        } catch (IOException e) {
            System.out.println("Loi khi ghi file: " + e.getMessage());
        }
    }
    
    public static ArrayList<User> docDanhSachUser() {
        ArrayList<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("dsUser.dat"))) {
            users = (ArrayList<User>) ois.readObject();
            System.out.println("Da doc danh sach San Pham tu file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Loi khi doc file: " + e.getMessage());
        }
        return users;
    }
     
}




