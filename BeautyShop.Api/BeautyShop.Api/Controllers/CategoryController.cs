using BeautyShop.Api.Data;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using BeautyShop.Api.Models;
namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CategoryController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public CategoryController(BeautyShopContext context) => _context = context;

        [HttpGet]
        public async Task<IActionResult> GetCategories()
            => Ok(await _context.Categories.ToListAsync());
    }
}
