using BeautyShop.Api.Data;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using BeautyShop.Api.Models;
namespace BeautyShop.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductController : ControllerBase
    {
        private readonly BeautyShopContext _context;
        public ProductController(BeautyShopContext context) => _context = context;

        [HttpGet("categories")]
        public async Task<IActionResult> GetCategories() => Ok(await _context.Categories.ToListAsync());

        [HttpGet]
        public async Task<IActionResult> GetProducts(int? catId)
        {
            var query = _context.Products.AsQueryable();
            if (catId.HasValue) query = query.Where(p => p.CategoryId == catId);
            return Ok(await query.ToListAsync());
        }

        [HttpGet("featured")]
        public async Task<IActionResult> GetFeatured() =>
            Ok(await _context.Products.Where(p => p.IsFeatured).ToListAsync());
    }
}
