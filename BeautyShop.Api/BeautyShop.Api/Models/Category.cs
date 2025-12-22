using BeautyShop.Api.DTOs;

namespace BeautyShop.Api.Models
{
    public class Category
    {
        public int Id { get; set; }
        public string Name { get; set; } = null!;
        public string? Image { get; set; }
        public virtual ICollection<Product> Products { get; set; } = new List<Product>();
    }
}
