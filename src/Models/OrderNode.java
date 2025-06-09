import java.util.List;
import java.util.ArrayList;

class OrderNode {
    static int orderCounter = 0;  // عداد لتوليد معرف فريد لكل طلب
    int id, priority, height;
    String customerName;
    OrderNode left, right;
    List<ProductNode> products;  // قائمة المنتجات داخل الطلب

    OrderNode(String customerName, int priority) {
        this.id = ++orderCounter;
        this.customerName = customerName;
        this.priority = priority;
        this.height = 1;
        this.products = new ArrayList<>();
    }

    public void addProduct(ProductNode product) {
        products.add(product);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBalance() {
        return safeGetHeight(left) - safeGetHeight(right);
    }

    private int safeGetHeight(OrderNode node) {
        return (node == null) ? 0 : node.getHeight();
    }

    // تحديث أولوية الطلب
    public void updatePriority(int newPriority) {
        this.priority = newPriority;
    }
}