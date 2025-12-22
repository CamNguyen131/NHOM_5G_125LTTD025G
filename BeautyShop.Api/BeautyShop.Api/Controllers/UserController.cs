using BeautyShop.Api.Data;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using Microsoft.AspNetCore.Mvc;
using BeautyShop.Api.Models;
namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public UserController(BeautyShopContext context) => _context = context;

        // 1. Xem thông tin cá nhân
        [HttpGet("profile/{id}")]
        public async Task<IActionResult> GetProfile(int id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null) return NotFound();
            return Ok(user);
        }

        // 2. Xem lịch sử đơn hàng
        [HttpGet("history/{userId}")]
        public async Task<IActionResult> GetOrderHistory(int userId)
        {
            var orders = await _context.Orders
                .Include(o => o.Billing) // Lấy thông tin thanh toán
                .Where(o => o.UserId == userId)
                .OrderByDescending(o => o.OrderTime)
                .ToListAsync();
            return Ok(orders);
        }

        // 3. Xem chi tiết một đơn hàng trong lịch sử
        [HttpGet("order-detail/{orderId}")]
        public async Task<IActionResult> GetOrderDetail(int orderId)
        {
            var details = await _context.OrderDetails
                .Include(d => d.Product)
                .Where(d => d.OrderId == orderId)
                .ToListAsync();
            return Ok(details);
        }
    }
}
