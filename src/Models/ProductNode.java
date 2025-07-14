class ProductNode {
    public static final int MAX_CAPACITY = 1000;  // الحد الأقصى المسموح به للكمية

    public static int productCounter = 0;
    public int      id, height, quantity;
    public String   name;
    public double   price;
    public ProductNode left, right;

    public ProductNode(String name, double price, int quantity) {
        if (price < 0)
            throw new IllegalArgumentException("Price must be >= 0");
        if (quantity < 0 || quantity > MAX_CAPACITY)
            throw new IllegalArgumentException("Quantity must be between 0 and " + MAX_CAPACITY);

        this.id       = ++productCounter;
        this.name     = name;
        this.price    = price;
        this.quantity = quantity;
        this.height   = 1;
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