namespace BeautyShop.Api.Models
{
    public class SellerRegistrationDTO
    {
        public int UserId { get; set; }
        public string ShopName { get; set; } = null!;
        public string ShopAddress { get; set; } = null!;
    }
}
