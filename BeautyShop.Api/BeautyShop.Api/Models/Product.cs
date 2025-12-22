namespace BeautyShop.Api.Models
{
    public class Product
    {
        public int Id { get; set; }
        public string Name { get; set; } = null!;
        public string? Brand { get; set; }
        public decimal Price { get; set; }
        public decimal? DiscountPrice { get; set; }
        public string? Image { get; set; }
        public int Stock { get; set; }
        public int CategoryId { get; set; }
        public int? SellerId { get; set; } 
        public bool IsFeatured { get; set; }
        public virtual Category? Category { get; set; }
    }
}