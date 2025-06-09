class ProductNode {
    static int product_num = 0;  // عداد لتوليد معرف فريد لكل منتج
    int id, quantity, height;
    double price;
    String name;
    ProductNode left, right;
    static final int MAX_CAPACITY = 1000;

    // المُنشئ مع التحقق من صحة السعر والكمية
    ProductNode(String name, double price, int quantity) {
        if (!isValidPrice(price) || !isValidQuantity(quantity)) {
            throw new IllegalArgumentException("Price must be >= 0 and Quantity must be between 0 and " + MAX_CAPACITY);
        }
        this.id = ++product_num;  // توليد معرف فريد لكل منتج جديد
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.height = 1;
    }

    // التحقق من صحة السعر
    private boolean isValidPrice(double price) {
        return price >= 0;
    }

    // التحقق من صحة الكمية
    private boolean isValidQuantity(int quantity) {
        return quantity >= 0 && quantity <= MAX_CAPACITY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // يحسب عامل التوازن (الفرق بين ارتفاع الفروع اليسرى واليمنى)
    public int getBalance() {
        return safeGetHeight(left) - safeGetHeight(right);
    }

    // دالة مساعدة لإرجاع الارتفاع بأمان
    private int safeGetHeight(ProductNode node) {
        return (node == null) ? 0 : node.getHeight();
    }
}