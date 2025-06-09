import java.time.LocalDate;

public class main {
    public static void main(String[] args) {
        // ✅ إنشاء شجرة المنتجات
        ProductAVLTree productTree = new ProductAVLTree();

        // ✅ إدراج بعض المنتجات
        productTree.insert("Laptop", 1200.0, 5);
        productTree.insert("Phone", 800.0, 10);
        productTree.insert("Tablet", 500.0, 7);

        // ✅ البحث عن منتج معين
        ProductNode foundProduct = productTree.search(2);
        System.out.println(foundProduct != null ? "Found Product: " + foundProduct.name : "Product not found.");

        // ✅ تحديث بيانات المنتج
        productTree.updateProduct(2, 750.0, 12);

        // ✅ حذف منتج معين
        productTree.delete(3);

        // ✅ طباعة المنتجات المتبقية
        System.out.println("\nProducts in Inventory:");
        productTree.inorderTraversal();

        // ✅ حساب إجمالي قيمة المخزون
        System.out.println("\nTotal Stock Value: $" + productTree.calculateTotalStockValue());

        // -----------------------------

        // ✅ إنشاء شجرة الطلبات
        OrderAVLTree orderTree = new OrderAVLTree();

        // ✅ إدراج طلبات جديدة
        orderTree.insert("Alice", 5);
        orderTree.insert("Bob", 8);
        orderTree.insert("Charlie", 3);

        // ✅ البحث عن الطلب ذو الأولوية الأعلى
        OrderNode highestPriorityOrder = orderTree.getHighestPriorityOrder();
        System.out.println("\nHighest Priority Order: " + highestPriorityOrder.customerName);

        // ✅ تحديث أولوية طلب معين
        orderTree.updatePriority(2, 10);

        // -----------------------------

        // ✅ إنشاء شجرة الشحنات
        ShipmentAVLTree shipmentTree = new ShipmentAVLTree();

        // ✅ إدراج شحنات جديدة
        shipmentTree.insert("New York", 500.0, LocalDate.now().plusDays(5).toString());
        shipmentTree.insert("London", 800.0, LocalDate.now().plusDays(10).toString());
        shipmentTree.insert("Tokyo", 1200.0, LocalDate.now().plusDays(15).toString());

        // ✅ تحديث تاريخ تسليم شحنة
        shipmentTree.updateDeliveryDate(2, LocalDate.now().plusDays(20).toString());
    }
}