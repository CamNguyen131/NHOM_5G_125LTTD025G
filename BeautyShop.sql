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

-- 1. Bảng Người dùng
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

-- 2. Bảng Danh mục (Table name: Category)
CREATE TABLE Category (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    [Image] NVARCHAR(255)
);

-- 3. Bảng Sản phẩm (Table name: Product)
CREATE TABLE Product (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(150) NOT NULL,
    Brand NVARCHAR(100),
    [Image] NVARCHAR(255),
    Price DECIMAL(18,2) NOT NULL,
    DiscountPrice DECIMAL(18,2),
    Description NVARCHAR(MAX),
    Stock INT DEFAULT 0,
    CategoryId INT NOT NULL,
    SellerId INT NULL, -- Thêm để làm tính năng thống kê Seller
    IsFeatured BIT DEFAULT 0,
    CONSTRAINT FK_Product_Category FOREIGN KEY (CategoryId) REFERENCES Category(Id),
    CONSTRAINT FK_Product_Seller FOREIGN KEY (SellerId) REFERENCES Users(Id)
);

-- 4. Bảng Thông tin thanh toán
CREATE TABLE Billing (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    ReceiverName NVARCHAR(100) NOT NULL,
    Address NVARCHAR(255) NOT NULL,
    Phone NVARCHAR(20) NOT NULL,
    Email NVARCHAR(100),
    PaymentMethod NVARCHAR(50) DEFAULT 'COD'
);

-- 5. Bảng Đơn hàng
CREATE TABLE Orders (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    BillingId INT NOT NULL,
    OrderTime DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(18,2),
    [Status] INT DEFAULT 0, -- 0: Chờ duyệt, 1: Đang giao, 2: Thành công, 3: Đã hủy
    CONSTRAINT FK_Orders_Users FOREIGN KEY (UserId) REFERENCES Users(Id),
    CONSTRAINT FK_Orders_Billing FOREIGN KEY (BillingId) REFERENCES Billing(Id)
);

-- 6. Bảng Chi tiết đơn hàng
CREATE TABLE OrderDetail (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    OrderId INT NOT NULL,
    ProductId INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    UnitPrice DECIMAL(18,2) NOT NULL,
    CONSTRAINT FK_OrderDetail_Orders FOREIGN KEY (OrderId) REFERENCES Orders(Id),
    CONSTRAINT FK_OrderDetail_Product FOREIGN KEY (ProductId) REFERENCES Product(Id)
);

-- 7. Bảng Giỏ hàng
CREATE TABLE ShoppingCart (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    ProductId INT NOT NULL,
    Quantity INT DEFAULT 1,
    CONSTRAINT FK_Cart_User FOREIGN KEY (UserId) REFERENCES Users(Id),
    CONSTRAINT FK_Cart_Product FOREIGN KEY (ProductId) REFERENCES Product(Id)
);

-- 8. Bảng Yêu thích
CREATE TABLE Wishlist (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    ProductId INT NOT NULL,
    CONSTRAINT FK_Wishlist_User FOREIGN KEY (UserId) REFERENCES Users(Id),
    CONSTRAINT FK_Wishlist_Product FOREIGN KEY (ProductId) REFERENCES Product(Id)
);

-- 9. Bảng Tin nhắn
CREATE TABLE Messages (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    SenderId INT NOT NULL,
    ReceiverId INT NOT NULL,
    Content NVARCHAR(MAX) NOT NULL,
    SentAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Msg_Sender FOREIGN KEY (SenderId) REFERENCES Users(Id),
    CONSTRAINT FK_Msg_Receiver FOREIGN KEY (ReceiverId) REFERENCES Users(Id)
);
GO


/* 1. NHẬP NGƯỜI DÙNG (Dùng để test Login và Phân quyền) */
INSERT INTO Users (Username, Password, FullName, Email, Phone, Address, Role) VALUES 
('admin', '123456', N'Quản Trị Viên', 'admin@beautyshop.com', '0123456789', N'Đà Nẵng', 'Admin'),
('hanashop', '123456', N'Hana Beauty Store', 'contact@hana.vn', '0905111222', N'TP. Hồ Chí Minh', 'Seller'),
('korealook', '123456', N'Korea Look Official', 'korea@look.vn', '0905333444', N'Hà Nội', 'Seller'),
('khachhang01', '123456', N'Nguyễn Thu Thảo', 'thao@gmail.com', '0705555666', N'Cần Thơ', 'Buyer'),
('khachhang02', '123456', N'Trần Mỹ Linh', 'linh@gmail.com', '0705777888', N'Hải Phòng', 'Buyer');

/* 2. NHẬP DANH MỤC (Cho màn hình Home/Category) */
INSERT INTO Category (Name, [Image]) VALUES 
(N'Chăm sóc da', 'https://cdn-icons-png.flaticon.com/512/3100/3100566.png'),
(N'Trang điểm', 'https://cdn-icons-png.flaticon.com/512/3524/3524752.png'),
(N'Nước hoa', 'https://cdn-icons-png.flaticon.com/512/3024/3024661.png'),
(N'Chăm sóc tóc', 'https://cdn-icons-png.flaticon.com/512/1041/1041314.png'),
(N'Dụng cụ làm đẹp', 'https://cdn-icons-png.flaticon.com/512/2800/2800414.png');

/* 3. NHẬP SẢN PHẨM (Gắn với SellerId để test Thống kê) */
-- Hana Shop (SellerId = 2)
INSERT INTO Product (Name, Brand, Price, DiscountPrice, Stock, CategoryId, SellerId, IsFeatured, [Image], Description) VALUES 
(N'Serum B5 La Roche-Posay', 'La Roche-Posay', 950000, 850000, 50, 1, 2, 1, 'https://u6v8d4u3.rocketcdn.me/wp-content/uploads/2021/05/HUA60212-1.jpg', N'Phục hồi da chuyên sâu.'),
(N'Kem chống nắng Anessa', 'Anessa', 680000, 550000, 100, 1, 2, 1, 'https://anessa.vn/media/catalog/product/k/c/kcn-anessa-perfect-uv-sunscreen-mild-milk-60ml-1.jpg', N'Bảo vệ da tối ưu dưới ánh nắng.'),
(N'Son Dior Rouge 999', 'Dior', 1100000, 990000, 20, 2, 2, 1, 'https://thegioisonmoi.com/cdn/shop/products/son-dior-rouge-999-matte-do-tuoi-phien-ban-moi-nhat-2021.jpg', N'Màu đỏ huyền thoại.');

-- Korea Look (SellerId = 3)
INSERT INTO Product (Name, Brand, Price, DiscountPrice, Stock, CategoryId, SellerId, IsFeatured, [Image], Description) VALUES 
(N'Nước hoa Chanel No.5', 'Chanel', 4500000, 3950000, 10, 3, 3, 0, 'https://thegioinuochoa.com.vn/uploads/products/2021/11/chanel-no-5-edp-1.jpg', N'Hương thơm quyến rũ.'),
(N'Dầu gội bưởi Milaganics', 'Milaganics', 250000, 195000, 30, 4, 3, 0, 'https://milaganics.com/wp-content/uploads/2019/06/dau-goi-buoi.jpg', N'Kích thích mọc tóc tự nhiên.');

/* 4. GIẢ LẬP ĐƠN HÀNG (Để test Lịch sử và Doanh thu) */
-- Bước 4.1: Tạo thông tin Billing
INSERT INTO Billing (ReceiverName, Address, Phone, Email) VALUES 
(N'Nguyễn Thu Thảo', N'123 Nguyễn Lương Bằng, Đà Nẵng', '0705555666', 'thao@gmail.com');

-- Bước 4.2: Tạo Đơn hàng (Khách Id=4 mua sản phẩm của Hana Shop Id=2)
INSERT INTO Orders (UserId, BillingId, TotalAmount, [Status]) VALUES 
(4, 1, 1400000, 2); -- Trạng thái 2: Thành công

-- Bước 4.3: Chi tiết đơn hàng (1 Serum + 1 Kem chống nắng)
INSERT INTO OrderDetail (OrderId, ProductId, Quantity, UnitPrice) VALUES 
(1, 1, 1, 850000),
(1, 2, 1, 550000);

/* 5. TIN NHẮN MẪU */
INSERT INTO Messages (SenderId, ReceiverId, Content) VALUES 
(4, 2, N'Shop ơi, Serum B5 có sẵn hàng không ạ?'),
(2, 4, N'Chào bạn, shop còn sẵn hàng bạn nhé. Bạn đặt hôm nay sẽ có quà tặng kèm ạ!');

/* 6. DANH SÁCH YÊU THÍCH MẪU */
INSERT INTO Wishlist (UserId, ProductId) VALUES (4, 3);
GO

SELECT p.Name, SUM(od.Quantity) as TotalSold, SUM(od.Quantity * od.UnitPrice) as Revenue
FROM OrderDetail od
JOIN Product p ON od.ProductId = p.Id
WHERE p.SellerId = 2
GROUP BY p.Name;

SELECT o.Id as OrderID, b.ReceiverName, o.TotalAmount, o.OrderTime, o.[Status]
FROM Orders o
JOIN Billing b ON o.BillingId = b.Id
WHERE o.UserId = 4;