using BeautyShop.Api.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using BeautyShop.Api.DTOs;

namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public AuthController(BeautyShopContext context) => _context = context;

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] BeautyShop.Api.DTOs.LoginRequest req)
        {
            var user = await _context.Users
                .FirstOrDefaultAsync(u => u.Username == req.Username && u.Password == req.Password);
            if (user == null) return Unauthorized(new { message = "Sai tài khoản hoặc mật khẩu" });
            return Ok(user);
        }
    }
}
