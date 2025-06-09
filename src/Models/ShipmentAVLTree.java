import java.time.LocalDate;

class ShipmentAVLTree {
    private ShipmentNode root; // الجذر الرئيسي للشجرة

    // إدراج شحنة جديدة داخل الشجرة وإعادة توازنها بعد الإدراج
    public void insert(String destination, double cost, String deliveryDate) {
        root = insertRecursive(root, new ShipmentNode(destination, cost, deliveryDate));
    }

    private ShipmentNode insertRecursive(ShipmentNode node, ShipmentNode newShipment) {
        if (node == null) {
            return newShipment;
        }

        // ترتيب الشجرة حسب الـ ID
        if (newShipment.id < node.id) {
            node.left = insertRecursive(node.left, newShipment);
        } else if (newShipment.id > node.id) {
            node.right = insertRecursive(node.right, newShipment);
        } else {
            return node; // تجنب التكرار
        }

        node.setHeight(Math.max(safeGetHeight(node.left), safeGetHeight(node.right)) + 1);
        return rebalance(node);
    }

    // البحث عن شحنة باستخدام معرفها
    public ShipmentNode search(int shipmentId) {
        return searchRecursive(root, shipmentId);
    }

    private ShipmentNode searchRecursive(ShipmentNode node, int shipmentId) {
        if (node == null || node.id == shipmentId) {
            return node;
        }
        return (shipmentId < node.id) ? searchRecursive(node.left, shipmentId)
                : searchRecursive(node.right, shipmentId);
    }

    // تحديث تاريخ التسليم لشحنة معينة بعد التأكد من أن التاريخ الجديد مستقبلي
    public void updateDeliveryDate(int shipmentId, String newDate) {
        ShipmentNode node = search(shipmentId);
        if (node == null) {
            System.out.println("Shipment with ID " + shipmentId + " not found.");
            return;
        }

        LocalDate updatedDate = LocalDate.parse(newDate);
        if (updatedDate.isAfter(LocalDate.now())) {
            node.deliveryDate = updatedDate;
            System.out.println("Shipment ID " + shipmentId + " updated: New Delivery Date = " + node.deliveryDate);
        } else {
            System.out.println("Error: Delivery date must be in the future.");
        }
    }

    // إعادة توازن الشجرة بعد الإدراج أو الحذف بناءً على حالات AVL المختلفة
    private ShipmentNode rebalance(ShipmentNode node) {
        int balance = node.getBalance();
        // دوران يمين (LL)
        if (balance > 1 && node.left.getBalance() >= 0) {
            return rotateRight(node);
        }
        // دوران مزدوج: اليسار ثم اليمين (LR)
        if (balance > 1 && node.left.getBalance() < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // دوران يسار (RR)
        if (balance < -1 && node.right.getBalance() <= 0) {
            return rotateLeft(node);
        }
        // دوران مزدوج: اليمين ثم اليسار (RL)
        if (balance < -1 && node.right.getBalance() > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private ShipmentNode rotateRight(ShipmentNode y) {
        ShipmentNode x = y.left;
        ShipmentNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);
        return x;
    }

    private ShipmentNode rotateLeft(ShipmentNode x) {
        ShipmentNode y = x.right;
        ShipmentNode T2 = y.left;

        y.left = x;
        x.right = T2;

        y.setHeight(Math.max(safeGetHeight(y.left), safeGetHeight(y.right)) + 1);
        x.setHeight(Math.max(safeGetHeight(x.left), safeGetHeight(x.right)) + 1);
        return y;
    }

    private int safeGetHeight(ShipmentNode node) {
        return (node == null) ? 0 : node.getHeight();
    }
}