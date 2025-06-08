class ProductAVLTree {
    private ProductNode root; // الجذر الرئيسي للشجرة

    // إدراج منتج جديد داخل الشجرة دون الحاجة إلى إعادة تعيين `root`
    public void insert(String name, double price, int quantity) {
        root = insertRecursive(root, new ProductNode(name, price, quantity)); // 🔥 يتم إنشاء المنتج بمعرف فريد
    }

    private ProductNode insertRecursive(ProductNode node, ProductNode newProduct) {
        if (node == null) {
            return newProduct; // يتم إدراج المنتج الجديد
        }

        if (newProduct.id < node.id) { // ترتيب الشجرة حسب `id`
            node.left = insertRecursive(node.left, newProduct);
        } else if (newProduct.id > node.id) {
            node.right = insertRecursive(node.right, newProduct);
        } else {
            return node; // تجنب التكرار
        }

        node.setHeight(Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1);
        int balance = node.getBalance();

        // حالات إعادة التوازن لشجرة AVL
        if (balance > 1 && newProduct.id < node.left.id) {
            return rotateRight(node);
        }
        if (balance < -1 && newProduct.id > node.right.id) {
            return rotateLeft(node);
        }
        if (balance > 1 && newProduct.id > node.left.id) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && newProduct.id < node.right.id) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // البحث عن منتج عبر `id`
    public ProductNode search(int productId) {
        return searchRecursive(root, productId);
    }

    private ProductNode searchRecursive(ProductNode node, int productId) {
        if (node == null || node.id == productId) {
            return node;
        }
        return (productId < node.id) ? searchRecursive(node.left, productId) : searchRecursive(node.right, productId);
    }

    // تحديث سعر أو كمية المنتج
    public void updateProduct(int productId, Double newPrice, Integer newQuantity) {
        ProductNode node = search(productId); // البحث عن المنتج

        if (node == null) {
            System.out.println("Product with ID " + productId + " not found.");
            return;
        }

        if (newPrice != null && newPrice >= 0) {
            node.price = newPrice;
        } else if (newPrice != null) {
            System.out.println("Error: Price cannot be negative.");
        }

        if (newQuantity != null && newQuantity >= 0 && newQuantity <= ProductNode.MAX_CAPACITY) {
            node.quantity = newQuantity;
        } else if (newQuantity != null) {
            System.out.println("Error: Quantity out of valid range.");
        }

        System.out.println("Product ID " + productId + " updated: Price = $" + node.price + ", Quantity = " + node.quantity);
    }

    // طباعة جميع المنتجات بترتيب تصاعدي
    public void inorderTraversal() {
        inorderTraversalRecursive(root);
    }

    private void inorderTraversalRecursive(ProductNode node) {
        if (node != null) {
            inorderTraversalRecursive(node.left);
            System.out.println("Product ID: " + node.id + " | Name: " + node.name + " | Price: $" + node.price + " | Quantity: " + node.quantity);
            inorderTraversalRecursive(node.right);
        }
    }

    private int safeGetHeight(ProductNode node) {
        return (node == null) ? 0 : node.getHeight();
    }

    private ProductNode rotateRight(ProductNode y) {
        ProductNode x = y.left;
        ProductNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);

        return x;
    }

    private ProductNode rotateLeft(ProductNode x) {
        ProductNode y = x.right;
        ProductNode T2 = y.left;

        y.left = x;
        x.right = T2;

        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);

        return y;
    }
}