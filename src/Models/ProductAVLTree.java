class ProductAVLTree {
    private ProductNode root; // الجذر الرئيسي للشجرة
    public boolean isEmpty() {
        return root == null;
    }


    // عملية الإدراج: تُنشئ منتجًا جديدًا وتُدرجه بالشجرة مع إعادة التوازن
    public void insert(String name, double price, int quantity) {
        root = insertRecursive(root, new ProductNode(name, price, quantity));
    }

    private ProductNode insertRecursive(ProductNode node, ProductNode newProduct) {
        if (node == null) {
            return newProduct; // حالة إدراج العقدة الجديدة
        }

        // ترتيب الشجرة استنادًا إلى معرف المنتج (يُستخدم للتمييز)
        if (newProduct.id < node.id) {
            node.left = insertRecursive(node.left, newProduct);
        } else if (newProduct.id > node.id) {
            node.right = insertRecursive(node.right, newProduct);
        } else {
            // في حالة حدوث تكرار (نادرًا بسبب التوليد التلقائي للمعرفات)
            return node;
        }

        // تحديث ارتفاع العقدة الحالية
        node.setHeight(Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1);

        // إعادة توازن الشجرة قبل العودة
        return rebalance(node);
    }

    // البحث عن منتج باستخدام معرّف المنتج
    public ProductNode search(int productId) {
        return searchRecursive(root, productId);
    }

    private ProductNode searchRecursive(ProductNode node, int productId) {
        if (node == null || node.id == productId) {
            return node;
        }
        return (productId < node.id) ? searchRecursive(node.left, productId)
                : searchRecursive(node.right, productId);
    }

    // تحديث بيانات المنتج: السعر والكمية مع التحقق من القيود
    public void updateProduct(int productId, Double newPrice, Integer newQty) {
        ProductNode node = search(productId);
        if (node == null) {
            System.out.println("Product not found.");
            return;
        }
        if (newPrice != null) {
            if (newPrice < 0) {
                System.out.println("Error: Price must be >= 0.");
            } else {
                node.price = newPrice;
            }
        }
        if (newQty != null) {
            if (newQty < 0 || newQty > ProductNode.MAX_CAPACITY) {
                System.out.println("Error: Quantity must be between 0 and " + ProductNode.MAX_CAPACITY);
            } else {
                node.quantity = newQty;
            }
        }
    }

    // حذف منتج من الشجرة باستخدام معرّف المنتج
    public void delete(int productId) {
        root = deleteRecursive(root, productId);
    }

    private ProductNode deleteRecursive(ProductNode node, int productId) {
        if (node == null) {
            return null; // المنتج غير موجود
        }

        if (productId < node.id) {
            node.left = deleteRecursive(node.left, productId);
        } else if (productId > node.id) {
            node.right = deleteRecursive(node.right, productId);
        } else {
            // تم العثور على المنتج المراد حذفه
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                // استبدال العقدة المُحذوفة بالعقدة التي تملك أقل قيمة من الفرع الأيمن
                ProductNode temp = minValueNode(node.right);
                // يمكن استبدال محتويات العقدة دون تغيير المعرف لمنع خطأ ترتيب الشجرة
                node.id = temp.id;
                node.name = temp.name;
                node.price = temp.price;
                node.quantity = temp.quantity;
                node.right = deleteRecursive(node.right, temp.id);
            }
        }

        if (node == null) {
            return null;
        }

        // تحديث الارتفاع وإعادة التوازن
        node.setHeight(Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1);
        return rebalance(node);
    }

    // دالة مساعدة لإيجاد العقدة التي تحتوي على أقل قيمة (عنصر الفرع الأيمن)
    private ProductNode minValueNode(ProductNode node) {
        ProductNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // إعادة توازن الشجرة وفقًا لحالات دوران AVL المختلفة
    private ProductNode rebalance(ProductNode node) {
        int balance = node.getBalance();

        // حالة الدوران اليماني (LL)
        if (balance > 1 && node.left.getBalance() >= 0) {
            return rotateRight(node);
        }
        // حالة الدوران المزدوج (LR)
        if (balance > 1 && node.left.getBalance() < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // حالة الدوران اليساري (RR)
        if (balance < -1 && node.right.getBalance() <= 0) {
            return rotateLeft(node);
        }
        // حالة الدوران المزدوج (RL)
        if (balance < -1 && node.right.getBalance() > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node; // إذا كان التوازن سليمًا
    }

    // دالة مساعدة لإرجاع ارتفاع العقدة بأمان
    private int safeGetHeight(ProductNode node) {
        return (node == null) ? 0 : node.getHeight();
    }

    // دوران يمين لعقدة معينة
    private ProductNode rotateRight(ProductNode y) {
        ProductNode x = y.left;
        ProductNode T2 = x.right;

        // إجراء عملية الدوران
        x.right = y;
        y.left = T2;

        // تحديث الارتفاعات
        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);

        return x;
    }

    // دوران يسار لعقدة معينة
    private ProductNode rotateLeft(ProductNode x) {
        ProductNode y = x.right;
        ProductNode T2 = y.left;

        // إجراء عملية الدوران
        y.left = x;
        x.right = T2;

        // تحديث الارتفاعات
        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);

        return y;
    }

    // الطباعة باستخدام الترتيب التصاعدي (inorder traversal)
    public void inorderTraversal() {
        inorderTraversalRecursive(root);
    }

    private void inorderTraversalRecursive(ProductNode node) {
        if (node != null) {
            inorderTraversalRecursive(node.left);
            System.out.println("Product ID: " + node.id + " | Name: " + node.name +
                    " | Price: $" + node.price + " | Quantity: " + node.quantity);
            inorderTraversalRecursive(node.right);
        }
    }

    // تقرير تحليلي: حساب قيمة المخزون الكلية (مجموع السعر * الكمية لكل المنتجات)
    public double calculateTotalStockValue() {
        return calculateTotalStockValueRecursive(root);
    }

    private double calculateTotalStockValueRecursive(ProductNode node) {
        if (node == null)
            return 0;
        return (node.price * node.quantity)
                + calculateTotalStockValueRecursive(node.left)
                + calculateTotalStockValueRecursive(node.right);
    }
}