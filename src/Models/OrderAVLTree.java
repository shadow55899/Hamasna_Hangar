import java.util.ArrayList;
import java.util.List;

public class OrderAVLTree {
    private OrderNode root;
    public boolean isEmpty() {
        return root == null;
    }

    // إدراج طلب جديد حسب الأولوية
    public void insert(String customerName, Priority priority) {
        root = insertRecursive(root, new OrderNode(customerName, priority));
    }

    private OrderNode insertRecursive(OrderNode node, OrderNode newOrder) {
        if (node == null) return newOrder;

        int cmp = newOrder.priority.compare(node.priority);
        if (cmp > 0) {
            node.left = insertRecursive(node.left, newOrder);
        } else if (cmp < 0) {
            node.right = insertRecursive(node.right, newOrder);
        } else {
            if (newOrder.id < node.id) {
                node.left = insertRecursive(node.left, newOrder);
            } else {
                node.right = insertRecursive(node.right, newOrder);
            }
        }

        node.setHeight(Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1);
        return rebalance(node);
    }

    // البحث عن طلب باستخدام المعرف
    public OrderNode search(int orderId) {
        return searchRecursive(root, orderId);
    }

    private OrderNode searchRecursive(OrderNode node, int orderId) {
        if (node == null || node.id == orderId) return node;
        OrderNode foundLeft = searchRecursive(node.left, orderId);
        return (foundLeft != null) ? foundLeft : searchRecursive(node.right, orderId);
    }

    // الطلب ذو الأولوية الأعلى
    public OrderNode getHighestPriorityOrder() {
        return getHighestPriorityRecursive(root);
    }

    private OrderNode getHighestPriorityRecursive(OrderNode node) {
        if (node == null) return null;
        return (node.left == null) ? node : getHighestPriorityRecursive(node.left);
    }

    // تحديث أولوية الطلب
    // داخل OrderAVLTree.java
    public void updatePriority(int orderId, Priority newPriority) {
        // 1) ابحث عن الكائن
        OrderNode node = search(orderId);
        if (node == null) {
            System.out.println("Order ID " + orderId + " not found.");
            return;
        }

        // 2) حدّث الأولوية
        node.updatePriority(newPriority);

        // 3) احذفه من الشجرة القديمة
        root = deleteRecursive(root, orderId);

        // 4) افصّل العقدة عن أي فروع قديمة
        node.left = node.right = null;
        node.setHeight(1);

        // 5) أعد إدراجه “عقدة جديدة”
        root = insertRecursive(root, node);

        System.out.println("Order ID " + orderId +
                " priority updated to " + newPriority);
    }

    // حذف طلب
    public void delete(int orderId) {
        root = deleteRecursive(root, orderId);
    }

    private OrderNode deleteRecursive(OrderNode node, int orderId) {
        if (node == null) return null;

        if (orderId < node.id) {
            node.left = deleteRecursive(node.left, orderId);
        } else if (orderId > node.id) {
            node.right = deleteRecursive(node.right, orderId);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                OrderNode temp = minValueNode(node.right);
                node.id           = temp.id;
                node.customerName = temp.customerName;
                node.priority     = temp.priority;
                node.items        = new ArrayList<>(temp.items);
                node.right        = deleteRecursive(node.right, temp.id);
            }
        }

        if (node == null) return null;

        node.setHeight(Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1);
        return rebalance(node);
    }

    private OrderNode minValueNode(OrderNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // إعادة التوازن حسب AVL
    private OrderNode rebalance(OrderNode node) {
        int balance = node.getBalance();
        if (balance > 1 && node.left.getBalance() >= 0)
            return rotateRight(node);
        if (balance > 1 && node.left.getBalance() < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && node.right.getBalance() <= 0)
            return rotateLeft(node);
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
        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);
        return x;
    }

    private OrderNode rotateLeft(OrderNode x) {
        OrderNode y = x.right;
        OrderNode T2 = y.left;
        y.left = x;
        x.right = T2;
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);
        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        return y;
    }

    private int safeGetHeight(OrderNode node) {
        return (node == null) ? 0 : node.getHeight();
    }

    // عرض جميع الطلبات
    public void inorderTraversal() {
        inorderRecursive(root);
    }

    private void inorderRecursive(OrderNode node) {
        if (node != null) {
            inorderRecursive(node.left);
            System.out.println("Order ID: " + node.id +
                    " | Customer: " + node.customerName +
                    " | Priority: " + node.priority +
                    " | Total Cost: $" + node.calculateOrderCost());
            if (!node.items.isEmpty()) {
                System.out.println("  Items:");
                for (OrderNode.OrderItem item : node.items) {
                    System.out.println("   - " + item.product.name +
                            " x " + item.quantity +
                            " @ $" + item.product.price);
                }
            }
            inorderRecursive(node.right);
        }
    }

    // حساب إجمالي تكلفة جميع الطلبات
    public double calculateTotalOrderCost() {
        return calculateTotalOrderCostRecursive(root);
    }

    private double calculateTotalOrderCostRecursive(OrderNode node) {
        if (node == null) return 0;
        double thisCost = node.calculateOrderCost();
        return thisCost +
                calculateTotalOrderCostRecursive(node.left) +
                calculateTotalOrderCostRecursive(node.right);
    }

    public void inorderUnshipped() {
        inorderUnshippedRecursive(root);
    }

    private void inorderUnshippedRecursive(OrderNode node) {
        if (node == null) return;
        inorderUnshippedRecursive(node.left);

        if (!node.locked) {
            System.out.println(
                    "Order ID: " + node.id +
                            " | Customer: " + node.customerName +
                            " | Priority: " + node.priority +
                            " | Total Cost: $" + node.calculateOrderCost()
            );
        }

        inorderUnshippedRecursive(node.right);
    }

}