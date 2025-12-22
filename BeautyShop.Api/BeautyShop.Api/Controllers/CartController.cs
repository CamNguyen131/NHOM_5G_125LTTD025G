using BeautyShop.Api.Data;
using BeautyShop.Api.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CartController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public CartController(BeautyShopContext context) => _context = context;

        [HttpGet("{userId}")]
        public async Task<IActionResult> GetCart(int userId)
        {
            // Sử dụng .AsNoTracking() để tăng hiệu năng khi chỉ đọc dữ liệu
            var cartItems = await _context.ShoppingCarts
                .Include(c => c.Product)
                .Where(c => c.UserId == userId)
                .ToListAsync();
            return Ok(cartItems);
        }

        [HttpPost("add")]
        public async Task<IActionResult> AddToCart(ShoppingCart item)
        {
            // 1. Kiểm tra sản phẩm có tồn tại không trước khi thêm
            var productExists = await _context.Products.AnyAsync(p => p.Id == item.ProductId);
            if (!productExists) return NotFound("Sản phẩm không tồn tại");

            // 2. Tìm sản phẩm đã có trong giỏ hàng chưa
            var exist = await _context.ShoppingCarts
                .FirstOrDefaultAsync(c => c.UserId == item.UserId && c.ProductId == item.ProductId);

            if (exist != null)
            {
                exist.Quantity += item.Quantity;
            }
            else
            {
                _context.ShoppingCarts.Add(item);
            }

            await _context.SaveChangesAsync();
            return Ok(new { message = "Đã thêm vào giỏ hàng" });
        }

        // Bổ sung thêm hàm Xóa sản phẩm khỏi giỏ
        [HttpDelete("remove/{id}")]
        public async Task<IActionResult> RemoveFromCart(int id)
        {
            var item = await _context.ShoppingCarts.FindAsync(id);
            if (item == null) return NotFound();

            _context.ShoppingCarts.Remove(item);
            await _context.SaveChangesAsync();
            return Ok();
        }
    }
}