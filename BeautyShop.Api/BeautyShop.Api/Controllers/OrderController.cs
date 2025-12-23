using BeautyShop.Api.Data;
using BeautyShop.Api.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrderController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public OrderController(BeautyShopContext context) => _context = context;

        [HttpPost("checkout/{userId}")]
        public async Task<IActionResult> CheckOut(int userId, [FromBody] Billing billingInfo)
        {
            // 1. Kiểm tra giỏ hàng của người dùng
            var cartItems = await _context.ShoppingCarts
                .Include(c => c.Product)
                .Where(c => c.UserId == userId)
                .ToListAsync();

            if (cartItems == null || !cartItems.Any())
                return BadRequest("Giỏ hàng của bạn đang trống!");

            // 2. Lưu thông tin Billing (Thông tin giao hàng)
            _context.Billings.Add(billingInfo);
            await _context.SaveChangesAsync();

            // 3. Tạo đơn hàng mới (Order)
            var order = new Order
            {
                UserId = userId,
                BillingId = billingInfo.Id,
                OrderTime = DateTime.Now,
                Status = 1, // 1: Chờ xác nhận
                TotalAmount = 0
            };
            _context.Orders.Add(order);
            await _context.SaveChangesAsync();

            decimal runningTotal = 0;

            // 4. Duyệt qua từng sản phẩm trong giỏ hàng
            foreach (var item in cartItems)
            {
                var product = item.Product;
                if (product != null)
                {
                    // KIỂM TRA TỒN KHO
                    if (product.Stock < item.Quantity)
                    {
                        return BadRequest($"Sản phẩm '{product.Name}' không đủ số lượng trong kho!");
                    }

                    // XỬ LÝ LỖI DECIMAL? (Lấy DiscountPrice nếu có, không thì lấy Price)
                    decimal finalPrice = product.DiscountPrice ?? product.Price;

                    // TẠO CHI TIẾT ĐƠN HÀNG
                    var detail = new OrderDetail
                    {
                        OrderId = order.Id,
                        ProductId = item.ProductId,
                        Quantity = item.Quantity,
                        UnitPrice = finalPrice
                    };
                    _context.OrderDetails.Add(detail);

                    // --- LOGIC TRỪ KHO ---
                    product.Stock -= item.Quantity;

                    // Cộng dồn tổng tiền
                    runningTotal += (item.Quantity * finalPrice);
                }
            }

            // 5. Cập nhật lại tổng tiền thực tế vào đơn hàng
            order.TotalAmount = runningTotal;

            // 6. XÓA GIỎ HÀNG SAU KHI ĐẶT HÀNG THÀNH CÔNG
            _context.ShoppingCarts.RemoveRange(cartItems);

            // Lưu tất cả thay đổi (Trừ kho, Thêm chi tiết đơn hàng, Xóa giỏ hàng)
            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Đặt hàng thành công!",
                orderId = order.Id,
                total = runningTotal
            });
        }

        // API Xem lịch sử đơn hàng
        [HttpGet("history/{userId}")]
        public async Task<IActionResult> GetHistory(int userId)
        {
            var history = await _context.Orders
                .Include(o => o.Billing)
                .Where(o => o.UserId == userId)
                .OrderByDescending(o => o.OrderTime)
                .ToListAsync();
            return Ok(history);
        }
    }
}