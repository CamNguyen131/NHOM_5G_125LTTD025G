USE master;
GO
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

/* ===================== 1. Vai trò & Người dùng ===================== */
CREATE TABLE Users (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) UNIQUE NOT NULL,
    Password NVARCHAR(255) NOT NULL,
    FullName NVARCHAR(100),
    Email NVARCHAR(100) UNIQUE,
    Phone NVARCHAR(20),
    Address NVARCHAR(255),
    Avatar NVARCHAR(255),
    Role NVARCHAR(20) DEFAULT 'Buyer', -- Buyer, Seller, Admin
    CreatedAt DATETIME DEFAULT GETDATE()
);

/* ===================== 2. Danh mục mỹ phẩm ===================== */
CREATE TABLE Category (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    [Image] NVARCHAR(255), 
    CONSTRAINT UQ_Category_Name UNIQUE (Name)
);

/* ===================== 3. Sản phẩm mỹ phẩm ===================== */
CREATE TABLE Product (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(150) NOT NULL,
    Brand NVARCHAR(100),            -- Thương hiệu: Laneige, Shiseido...
    [Image] NVARCHAR(255), 
    Price DECIMAL(18,2) NOT NULL,
    DiscountPrice DECIMAL(18,2),    -- Giá sau khi giảm (nếu có)
    Description NVARCHAR(MAX),
    SkinType NVARCHAR(50),           -- Loại da: Da dầu, Da khô, Mọi loại da
    Volume NVARCHAR(50),            -- Dung tích: 30ml, 100g
    Stock INT DEFAULT 0,            -- Số lượng kho
    CategoryId INT NOT NULL,
    IsFeatured BIT DEFAULT 0,       -- Sản phẩm nổi bật
    CONSTRAINT FK_Product_Category FOREIGN KEY (CategoryId) REFERENCES Category(Id)
);

/* ===================== 4. Đơn hàng & Thanh toán ===================== */
CREATE TABLE Billing (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    ReceiverName NVARCHAR(100) NOT NULL,
    Address NVARCHAR(255) NOT NULL,
    Phone NVARCHAR(20) NOT NULL,
    Email NVARCHAR(100),
    PaymentMethod NVARCHAR(50) DEFAULT 'COD' -- COD, Bank Transfer
);

CREATE TABLE Orders (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    BillingId INT NOT NULL,
    OrderTime DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(18,2),
    [Status] INT DEFAULT 0, -- 0: Chờ duyệt, 1: Đang giao, 2: Thành công, 3: Đã hủy
    Note NVARCHAR(500),
    CONSTRAINT FK_Orders_Users FOREIGN KEY (UserId) REFERENCES Users(Id),
    CONSTRAINT FK_Orders_Billing FOREIGN KEY (BillingId) REFERENCES Billing(Id)
);

CREATE TABLE OrderDetail (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    OrderId INT NOT NULL,
    ProductId INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    UnitPrice DECIMAL(18,2) NOT NULL,
    CONSTRAINT FK_OrderDetail_Orders FOREIGN KEY (OrderId) REFERENCES Orders(Id),
    CONSTRAINT FK_OrderDetail_Product FOREIGN KEY (ProductId) REFERENCES Product(Id)
);

/* ===================== 5. Giỏ hàng & Yêu thích ===================== */
CREATE TABLE ShoppingCart (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    ProductId INT NOT NULL,
    Quantity INT DEFAULT 1,
    CONSTRAINT FK_Cart_User FOREIGN KEY (UserId) REFERENCES Users(Id),
    CONSTRAINT FK_Cart_Product FOREIGN KEY (ProductId) REFERENCES Product(Id)
);

CREATE TABLE Wishlist (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    ProductId INT NOT NULL,
    CONSTRAINT FK_Wishlist_User FOREIGN KEY (UserId) REFERENCES Users(Id),
    CONSTRAINT FK_Wishlist_Product FOREIGN KEY (ProductId) REFERENCES Product(Id)
);
GO

USE [BeautyShop];
GO

/* 1. NHẬP DANH MỤC (Cho màn hình Category/Home) */
INSERT INTO Category (Name, [Image]) VALUES 
(N'Chăm sóc da', 'https://cdn-icons-png.flaticon.com/512/3100/3100566.png'),
(N'Trang điểm', 'https://cdn-icons-png.flaticon.com/512/3524/3524752.png'),
(N'Nước hoa', 'https://cdn-icons-png.flaticon.com/512/3024/3024661.png'),
(N'Chăm sóc tóc', 'https://cdn-icons-png.flaticon.com/512/1041/1041314.png');
GO

/* 2. NHẬP NGƯỜI DÙNG (Để test Login/Profile) */
-- Password để là '123456' cho dễ test
INSERT INTO Users (Username, Password, FullName, Email, Phone, Address, Role, Avatar) VALUES 
('uyen_admin', '123456', N'Nguyễn Uyên', 'uyenadmin@gmail.com', '0911223344', N'Đà Nẵng', 'Admin', 'https://i.pravatar.cc/150?u=admin'),
('seller_minhhai', '123456', N'Mỹ Phẩm Minh Hải', 'minhhai@gmail.com', '0988776655', N'TP. Hồ Chí Minh', 'Seller', 'https://i.pravatar.cc/150?u=seller'),
('khachhang01', '123456', N'Trần Mỹ Linh', 'mylinh@gmail.com', '0333444555', N'Hà Nội', 'Buyer', 'https://i.pravatar.cc/150?u=buyer');
GO

/* 3. NHẬP SẢN PHẨM (Mỹ phẩm chi tiết) */
INSERT INTO Product (Name, Brand, Price, DiscountPrice, Description, SkinType, Volume, Stock, CategoryId, IsFeatured, [Image]) VALUES 
(N'Serum B5 La Roche-Posay', 'La Roche-Posay', 950000, 850000, N'Phục hồi da hư tổn, cấp ẩm sâu.', N'Da nhạy cảm', '30ml', 50, 1, 1, 'https://u6v8d4u3.rocketcdn.me/wp-content/uploads/2021/05/HUA60212-1.jpg'),
(N'Kem chống nắng Anessa', 'Anessa', 680000, 550000, N'Chống nắng tối ưu, kiềm dầu.', N'Da dầu', '60ml', 100, 1, 1, 'https://anessa.vn/media/catalog/product/k/c/kcn-anessa-perfect-uv-sunscreen-mild-milk-60ml-1.jpg'),
(N'Son Dior Rouge 999', 'Dior', 1100000, 950000, N'Màu đỏ thuần huyền thoại, satin mịn.', N'Mọi loại môi', '3.5g', 20, 2, 1, 'https://thegioisonmoi.com/cdn/shop/products/son-dior-rouge-999-matte-do-tuoi-phien-ban-moi-nhat-2021.jpg'),
(N'Nước hoa Chanel No.5', 'Chanel', 4500000, 3900000, N'Mùi hương quyến rũ vượt thời gian.', N'N/A', '100ml', 10, 3, 0, 'https://thegioinuochoa.com.vn/uploads/products/2021/11/chanel-no-5-edp-1.jpg'),
(N'Dầu gội bưởi Milaganics', 'Milaganics', 250000, 190000, N'Kích thích mọc tóc, giảm gãy rụng.', N'Tóc yếu', '250ml', 30, 4, 0, 'https://milaganics.com/wp-content/uploads/2019/06/dau-goi-buoi.jpg');
GO

/* 4. NHẬP GIỎ HÀNG (Dành cho khachhang01 - Id là 3) */
INSERT INTO ShoppingCart (UserId, ProductId, Quantity) VALUES 
(3, 1, 1), -- 1 Serum B5
(3, 2, 2); -- 2 Kem chống nắng
GO

/* 5. NHẬP ĐƠN HÀNG MẪU (Lịch sử đơn hàng) */
-- Bước 5.1: Tạo thông tin thanh toán
INSERT INTO Billing (ReceiverName, Address, Phone, Email, PaymentMethod) VALUES 
(N'Trần Mỹ Linh', N'99 Tô Hiến Thành, Hà Nội', '0333444555', 'mylinh@gmail.com', 'COD');

-- Bước 5.2: Tạo đơn hàng (Giả định UserId=3, BillingId=1)
INSERT INTO Orders (UserId, BillingId, TotalAmount, Status, Note) VALUES 
(3, 1, 1100000, 2, N'Giao hàng giờ hành chính'); -- Trạng thái 2: Thành công

-- Bước 5.3: Chi tiết đơn hàng (Mua son Dior Id=3)
INSERT INTO OrderDetail (OrderId, ProductId, Quantity, UnitPrice) VALUES 
(1, 3, 1, 1100000);
GO

/* 6. NHẬP DANH SÁCH YÊU THÍCH */
INSERT INTO Wishlist (UserId, ProductId) VALUES 
(3, 3), (3, 4);
GO

-- KIỂM TRA DỮ LIỆU VỪA NHẬP
SELECT 'Product Count' as TableName, COUNT(*) as Total FROM Product
UNION ALL
SELECT 'User Count', COUNT(*) FROM Users
UNION ALL
SELECT 'Order Count', COUNT(*) FROM Orders;