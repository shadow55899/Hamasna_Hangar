class ProductNode {
    int id, quantity, height;
    double price;
    String name;
    ProductNode left, right;
    static final int MAX_CAPACITY = 1000;

    ProductNode(int id, String name, double price, int quantity) {
        if (price < 0 || quantity < 0 || quantity > MAX_CAPACITY)
            throw new IllegalArgumentException("Price/Quantity constraints violated");
        this.id = id; this.name = name;
        this.price = price; this.quantity = quantity;
        this.height = 1;
    }
}
