USE master;
IF EXISTS (SELECT * FROM sys.databases WHERE name = N'BeautyShop')
BEGIN
    ALTER DATABASE [BeautyShop] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [BeautyShop];
END;
GO
CREATE DATABASE [BeautyShop];
GO
USE [BeautyShop];
GO

-- 1. Bảng Người dùng
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullname NVARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    avatarurl VARCHAR(255),
    role VARCHAR(20) CHECK (role IN ('buyer', 'seller', 'admin')) DEFAULT 'buyer',
    createdat DATETIME DEFAULT GETDATE()
);

-- 2. Bảng Cửa hàng (Người bán)
CREATE TABLE Shops (
    id INT PRIMARY KEY IDENTITY(1,1),
    userid INT,
    shopname NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    logourl VARCHAR(255),
    rating DECIMAL(2,1) DEFAULT 5.0,
    FOREIGN KEY (userid) REFERENCES Users(id)
);

-- 3. Bảng Danh mục
CREATE TABLE Categories (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    imageurl VARCHAR(255)
);

-- 4. Bảng Sản phẩm
CREATE TABLE Products (
    id INT PRIMARY KEY IDENTITY(1,1),
    shopid INT,
    categoryid INT,
    name NVARCHAR(255) NOT NULL,
    brand NVARCHAR(100),
    price DECIMAL(12, 2),
    description NVARCHAR(MAX),
    imageurl VARCHAR(255),
    stock INT DEFAULT 0,
    isflashsale BIT DEFAULT 0, -- 0: False, 1: True
    FOREIGN KEY (shopid) REFERENCES Shops(id),
    FOREIGN KEY (categoryid) REFERENCES Categories(id)
);

-- 5. Bảng Đơn hàng
CREATE TABLE Orders (
    id INT PRIMARY KEY IDENTITY(1,1),
    buyerid INT,
    ordercode VARCHAR(20) UNIQUE,
    totalamount DECIMAL(12, 2),
    status NVARCHAR(50) CHECK (status IN (N'Chờ xác nhận', N'Đang giao', N'Đã giao', N'Đã hủy')) DEFAULT N'Chờ xác nhận',
    orderdate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (buyerid) REFERENCES Users(id)
);

-- 6. Chi tiết đơn hàng
CREATE TABLE OrderItems (
    id INT PRIMARY KEY IDENTITY(1,1),
    orderid INT,
    productid INT,
    quantity INT,
    priceatpurchase DECIMAL(12, 2),
    FOREIGN KEY (orderid) REFERENCES Orders(id),
    FOREIGN KEY (productid) REFERENCES Products(id)
);

-- 7. Giỏ hàng
CREATE TABLE Cart (
    id INT PRIMARY KEY IDENTITY(1,1),
    userid INT,
    productid INT,
    quantity INT DEFAULT 1,
    FOREIGN KEY (userid) REFERENCES Users(id),
    FOREIGN KEY (productid) REFERENCES Products(id)
);
GO