CREATE DATABASE quanlycuahang;
go
USE quanlycuahang;

CREATE TABLE khach_hang (
	ma_khach_hang Nvarchar(50) primary key,
	mat_khau varchar(50),
	ho_ten Nvarchar(100),
	chuc_vu varchar(10),
	gioi_tinh Nvarchar(5),
	so_dien_thoai Nvarchar(15),
	dia_chi Nvarchar(200)
);

CREATE TABLE san_pham(
	ma_san_pham Nvarchar(50) primary key,
	ten_san_pham Nvarchar(100),
	so_luong int,
	gia_ca decimal(15,2)
);

CREATE TABLE Manager(
	Id_quan_ly Nvarchar(50)primary key,
	ho_ten_quan_ly Nvarchar(50),
	manager_username Nvarchar(50),
	manager_password Nvarchar(100)
);

CREATE TABLE hoa_don(
	ma_hoa_don Nvarchar(50) primary key,
	ma_khach_hang Nvarchar(50),              
    ngay_lap Nvarchar(15),
	tong_tien float ,
    FOREIGN KEY (ma_khach_hang) REFERENCES khach_hang(ma_khach_hang),
);

CREATE TABLE chi_tiet_hoa_don (
    ma_hoa_don Nvarchar(50),                
    ma_san_pham Nvarchar(50),                
    so_luong INT,                           
    gia FLOAT, 
	thanh_tien FLOAT,
    PRIMARY KEY (ma_hoa_don, ma_san_pham),
    FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
);




	select * from khach_hang;
	select * from san_pham;

	select * from hoa_don;
	select * from chi_tiet_hoa_don;



select h.ma_hoa_don,h.ngay_lap,h.tong_tien,
		kh.ho_ten, kh.so_dien_thoai , kh.dia_chi,
       ct.ma_san_pham, sp.ten_san_pham, ct.so_luong, ct.gia, ct.thanh_tien
from hoa_don h 
	inner join khach_hang kh on h.ma_khach_hang = kh.ma_khach_hang
	inner join chi_tiet_hoa_don ct on ct.ma_hoa_don = h.ma_hoa_don
	inner join san_pham sp on sp.ma_san_pham = ct.ma_san_pham
	order by h.ma_hoa_don


	delete from chi_tiet_hoa_don;
	delete FROm hoa_don;
	delete from san_pham;
	delete from khach_hang;

drop table hoa_don;
drop table chi_tiet_hoa_don;
drop table san_pham;
drop table khach_hang;