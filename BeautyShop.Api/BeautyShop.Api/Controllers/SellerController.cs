using BeautyShop.Api.Data;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using Microsoft.AspNetCore.Mvc;
using BeautyShop.Api.Models;
namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SellerController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public SellerController(BeautyShopContext context) => _context = context;

        // 1. Đăng ký lên người bán (Đổi Role từ Buyer -> Seller)
        [HttpPost("register")]
        public async Task<IActionResult> RegisterAsSeller([FromBody] int userId)
        {
            var user = await _context.Users.FindAsync(userId);
            if (user == null) return NotFound();

            user.Role = "Seller";
            await _context.SaveChangesAsync();
            return Ok(new { message = "Bạn đã trở thành Người bán thành công!" });
        }

        // 2. Thống kê sản phẩm đã bán của Seller này
        [HttpGet("statistics/{sellerId}")]
        public async Task<IActionResult> GetSellerStats(int sellerId)
        {
            // Thống kê: Sản phẩm nào bán được bao nhiêu cái và tổng doanh thu sản phẩm đó
            var stats = await _context.OrderDetails
                .Include(od => od.Product)
                .Where(od => od.Product.SellerId == sellerId)
                .GroupBy(od => new { od.ProductId, od.Product.Name })
                .Select(g => new
                {
                    ProductName = g.Key.Name,
                    TotalQuantity = g.Sum(x => x.Quantity),
                    TotalRevenue = g.Sum(x => x.Quantity * x.UnitPrice)
                })
                .ToListAsync();

            return Ok(stats);
        }

        // 3. Danh mục sản phẩm đang bán của Seller
        [HttpGet("my-products/{sellerId}")]
        public async Task<IActionResult> GetMyProducts(int sellerId)
        {
            var products = await _context.Products
                .Where(p => p.SellerId == sellerId)
                .ToListAsync();
            return Ok(products);
        }
    }
}
