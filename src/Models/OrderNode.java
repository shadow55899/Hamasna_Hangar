import java.util.ArrayList;
import java.util.List;

public class OrderNode {
    public static int orderCounter = 0;
    public int id, height;
    public String customerName;
    public Priority priority;
    public OrderNode left, right;
    public boolean locked = false;       // ← يمنع التعديل/حذف الطلب بعد ربطه بشحنة

    public static class OrderItem {
        public ProductNode product;
        public int quantity;
        public OrderItem(ProductNode p, int q) { product = p; quantity = q; }
    }
    public List<OrderItem> items = new ArrayList<>();

    public OrderNode(String customerName, Priority priority) {
        if (priority == null)
            throw new IllegalArgumentException("Priority must be specified");
        this.id = ++orderCounter;
        this.customerName = customerName;
        this.priority = priority;
        this.height = 1;
    }

    public void addProduct(ProductNode product, int qty) {
        if (locked) {
            System.out.println("❌ Order ID " + id + " is locked (in a shipment).");
            return;
        }
        if (product == null || qty <= 0) return;
        if (qty > product.quantity) {
            System.out.println("❌ Cannot add \"" + product.name +
                    "\" — requested (" + qty +
                    ") exceeds stock (" + product.quantity + ").");
            return;
        }
        product.quantity -= qty;
        for (OrderItem it : items) {
            if (it.product.id == product.id) { it.quantity += qty; return; }
        }
        items.add(new OrderItem(product, qty));
    }

    public boolean removeProductById(int productId) {
        if (locked) {
            System.out.println("❌ Order ID " + id + " is locked (in a shipment).");
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            OrderItem it = items.get(i);
            if (it.product.id == productId) {
                it.product.quantity += it.quantity;
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public void updatePriority(Priority newPriority) {
        if (locked) {
            System.out.println("❌ Order ID " + id + " is locked (in a shipment).");
            return;
        }
        if (newPriority != null) this.priority = newPriority;
    }

    public double calculateOrderCost() {
        double sum = 0;
        for (OrderItem it : items)
            sum += it.product.price * it.quantity;
        return sum;
    }

    public int getHeight() { return height; }
    public void setHeight(int h) { height = h; }
    public int getBalance() {
        return safeHeight(left) - safeHeight(right);
    }
    private int safeHeight(OrderNode n) { return (n == null)?0:n.height; }
}