class OrderAVLTree {
    private OrderNode root;

    // إدراج طلب جديد في النظام
    public void insert(String customerName, int priority) {
        root = insertRecursive(root, new OrderNode(customerName, priority));
    }

    private OrderNode insertRecursive(OrderNode node, OrderNode newOrder) {
        if (node == null) {
            return newOrder;
        }

        if (newOrder.priority > node.priority) {  // الطلبات الأعلى أولوية تكون إلى اليسار
            node.left = insertRecursive(node.left, newOrder);
        } else {
            node.right = insertRecursive(node.right, newOrder);
        }

        node.height = Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1;
        return rebalance(node);
    }

    // البحث عن طلب باستخدام المعرف
    public OrderNode search(int orderId) {
        return searchRecursive(root, orderId);
    }

    private OrderNode searchRecursive(OrderNode node, int orderId) {
        if (node == null || node.id == orderId) {
            return node;
        }
        return searchRecursive(node.left, orderId);
    }

    // استرجاع الطلب ذو الأولوية الأعلى
    public OrderNode getHighestPriorityOrder() {
        return getHighestPriorityRecursive(root);
    }

    private OrderNode getHighestPriorityRecursive(OrderNode node) {
        if (node == null) return null;
        return (node.left == null) ? node : getHighestPriorityRecursive(node.left);
    }

    // تحديث أولوية طلب معين
    public void updatePriority(int orderId, int newPriority) {
        OrderNode node = search(orderId);
        if (node != null) {
            node.priority = newPriority;
            root = insertRecursive(root, node);  // إعادة إدراج الطلب لضمان التوازن
        } else {
            System.out.println("Order with ID " + orderId + " not found.");
        }
    }

    // إعادة توازن الشجرة
    private OrderNode rebalance(OrderNode node) {
        int balance = node.getBalance();

        if (balance > 1 && node.left.getBalance() >= 0) {
            return rotateRight(node);
        }
        if (balance > 1 && node.left.getBalance() < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && node.right.getBalance() <= 0) {
            return rotateLeft(node);
        }
        if (balance < -1 && node.right.getBalance() > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private OrderNode rotateRight(OrderNode y) {
        OrderNode x = y.left;
        OrderNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1;
        x.height = Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1;

        return x;
    }

    private OrderNode rotateLeft(OrderNode x) {
        OrderNode y = x.right;
        OrderNode T2 = y.left;

        y.left = x;
        x.right = T2;

        y.height = Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1;
        x.height = Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1;

        return y;
    }

    private int safeGetHeight(OrderNode node) {
        return (node == null) ? 0 : node.height;
    }
}