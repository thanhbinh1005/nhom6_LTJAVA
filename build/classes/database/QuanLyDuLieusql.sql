CREATE DATABASE quanlycuahang;
go
USE quanlycuahang;

CREATE TABLE khach_hang (
	ma_khach_hang Nvarchar(50) primary key,
	ho_ten Nvarchar(100),
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
	manager_username Nvarchar(50) primary key,
	manager_password Nvarchar(100)
);

CREATE TABLE hoa_don(
	ma_hoa_don Nvarchar(50) primary key,
	ma_khach_hang Nvarchar(50),              
    ngay_lap DATE,                          
    FOREIGN KEY (ma_khach_hang) REFERENCES khach_hang(ma_khach_hang)
);

CREATE TABLE chi_tiet_hoa_don (
    ma_hoa_don Nvarchar(50),                
    ma_san_pham Nvarchar(50),                
    so_luong INT,                           
    gia DECIMAL(15, 2),                     
    PRIMARY KEY (ma_hoa_don, ma_san_pham),
    FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
);