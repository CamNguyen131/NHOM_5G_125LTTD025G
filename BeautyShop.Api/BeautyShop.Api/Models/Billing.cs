namespace BeautyShop.Api.Models
{
    public class Billing
    {
        public int Id { get; set; }
        public string ReceiverName { get; set; } = null!;
        public string Address { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public string? Email { get; set; }
        public string PaymentMethod { get; set; } = "COD";
    }
}
