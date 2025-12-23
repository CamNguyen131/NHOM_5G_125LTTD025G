using Microsoft.EntityFrameworkCore;
using BeautyShop.Api.Models;

namespace BeautyShop.Api.Data;

public class BeautyShopContext : DbContext
{
    public BeautyShopContext(DbContextOptions<BeautyShopContext> options) : base(options) { }

    // Thực thể cơ bản
    public DbSet<User> Users { get; set; }
    public DbSet<Category> Categories { get; set; }
    public DbSet<Product> Products { get; set; }

    // Giao dịch & Giỏ hàng
    public DbSet<ShoppingCart> ShoppingCarts { get; set; }
    public DbSet<Wishlist> Wishlists { get; set; } // Bổ sung Yêu thích
    public DbSet<Billing> Billings { get; set; }
    public DbSet<Order> Orders { get; set; }
    public DbSet<OrderDetail> OrderDetails { get; set; }
    public DbSet<Message> Messages { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        // Cấu hình tên bảng chính xác theo SQL
        modelBuilder.Entity<Category>().ToTable("Category");
        modelBuilder.Entity<Product>().ToTable("Product");
        modelBuilder.Entity<User>().ToTable("Users");
        modelBuilder.Entity<Billing>().ToTable("Billing");
        modelBuilder.Entity<Order>().ToTable("Orders");
        modelBuilder.Entity<OrderDetail>().ToTable("OrderDetail");
        modelBuilder.Entity<ShoppingCart>().ToTable("ShoppingCart");
        modelBuilder.Entity<Wishlist>().ToTable("Wishlist");
        modelBuilder.Entity<Message>().ToTable("Messages");

        // Giữ nguyên các cấu hình Precision cho tiền tệ
        modelBuilder.Entity<Product>().Property(p => p.Price).HasPrecision(18, 2);
        modelBuilder.Entity<Product>().Property(p => p.DiscountPrice).HasPrecision(18, 2);
        modelBuilder.Entity<Order>().Property(o => o.TotalAmount).HasPrecision(18, 2);
        modelBuilder.Entity<OrderDetail>().Property(od => od.UnitPrice).HasPrecision(18, 2);
    }
}