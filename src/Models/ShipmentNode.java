import java.time.LocalDate;

class ShipmentNode {
    static int shipment_num = 0;  // عداد لتوليد معرف فريد لكل شحنة
    int id, height;
    String destination;
    double cost;
    LocalDate deliveryDate;
    ShipmentNode left, right;

    // المُنشئ: يتحقق من صحة التكلفة وصحة تاريخ التسليم (يجب أن يكون مستقبليًا)
    ShipmentNode(String destination, double cost, String deliveryDate) {
        if (!isValidCost(cost) || !isValidDeliveryDate(deliveryDate)) {
            throw new IllegalArgumentException("Invalid cost or delivery date. " +
                    "Delivery date must be in the future and cost >= 0.");
        }
        this.id = ++shipment_num;  // توليد معرف فريد لكل شحنة جديدة
        this.destination = destination;
        this.cost = cost;
        this.deliveryDate = LocalDate.parse(deliveryDate);
        this.height = 1;
    }

    // التحقق من صحة التكلفة (يجب أن تكون غير سالبة)
    private boolean isValidCost(double cost) {
        return cost >= 0;
    }

    // التحقق من صحة تاريخ التسليم (يجب أن يكون مستقبليًا)
    private boolean isValidDeliveryDate(String date) {
        LocalDate delivery = LocalDate.parse(date);
        return delivery.isAfter(LocalDate.now());
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // حساب عامل التوازن لشجرة AVL: الفرق بين ارتفاع الفرع الأيسر واليمين
    public int getBalance() {
        return safeGetHeight(left) - safeGetHeight(right);
    }

    // دالة مساعدة لإرجاع ارتفاع العقدة بأمان
    private int safeGetHeight(ShipmentNode node) {
        return (node == null) ? 0 : node.getHeight();
    }
}