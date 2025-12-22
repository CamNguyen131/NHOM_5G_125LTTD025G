namespace BeautyShop.Api.Models
{
    public class Order
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int BillingId { get; set; }
        public DateTime OrderTime { get; set; } = DateTime.Now;
        public decimal TotalAmount { get; set; }
        public int Status { get; set; }
        public virtual Billing? Billing { get; set; }
        public virtual ICollection<OrderDetail> OrderDetails { get; set; } = new List<OrderDetail>();
    }
}
