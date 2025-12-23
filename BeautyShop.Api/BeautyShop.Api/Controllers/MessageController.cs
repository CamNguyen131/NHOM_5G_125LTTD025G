using BeautyShop.Api.Data;
using Microsoft.EntityFrameworkCore;
using BeautyShop.Api.Models;
using Microsoft.AspNetCore.Mvc;
namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MessageController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public MessageController(BeautyShopContext context) => _context = context;

        // Lấy tin nhắn giữa 2 người
        [HttpGet("history")]
        public async Task<IActionResult> GetChatHistory(int user1, int user2)
        {
            var msgs = await _context.Messages
                .Where(m => (m.SenderId == user1 && m.ReceiverId == user2) ||
                            (m.SenderId == user2 && m.ReceiverId == user1))
                .OrderBy(m => m.SentAt)
                .ToListAsync();
            return Ok(msgs);
        }

        [HttpPost("send")]
        public async Task<IActionResult> SendMessage(Message msg)
        {
            _context.Messages.Add(msg);
            await _context.SaveChangesAsync();
            return Ok(msg);
        }
    }
}
