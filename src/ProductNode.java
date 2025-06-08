class ProductNode {
    static int product_num = 0;  // عداد لتوليد معرف فريد لكل منتج
    int id, quantity, height;
    double price;
    String name;
    ProductNode left, right;
    static final int MAX_CAPACITY = 1000;

    ProductNode(String name, double price, int quantity) {
        if (price < 0 || quantity < 0 || quantity > MAX_CAPACITY) {
            throw new IllegalArgumentException("Price/Quantity constraints violated");
        }
        this.id = ++product_num;  // 🔥 يتم توليد معرف فريد لكل منتج جديد
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.height = 1;
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

    private int safeGetHeight(ProductNode node) {
        return (node == null) ? 0 : node.getHeight();
    }
}