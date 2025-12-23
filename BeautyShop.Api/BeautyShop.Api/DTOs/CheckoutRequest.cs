namespace BeautyShop.Api.DTOs
{
    public class CheckoutRequest
    {
        public int UserId { get; set; }
        public string ReceiverName { get; set; } = null!;
        public string Address { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public string? Email { get; set; }
        public string PaymentMethod { get; set; } = "COD";
    }
}
