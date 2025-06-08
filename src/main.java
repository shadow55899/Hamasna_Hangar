public class main {
    public static void main(String[] args) {
        ProductAVLTree tree = new ProductAVLTree();

        // إدراج المنتجات بدون الحاجة إلى إعادة تعيين `root`
        tree.insert("Product A", 50.0, 100);
        tree.insert("Product B", 30.0, 150);
        tree.insert("Product C", 70.0, 80);
        tree.insert("Product D", 90.0, 50);

        // طباعة المنتجات للتحقق من ID الصحيح
        System.out.println("Product list:");
        tree.inorderTraversal();

        // البحث عن المنتجات
        System.out.println("\nSearching for products:");
        System.out.println(tree.search(2) != null ? "Product with ID 2 found!" : "Product with ID 2 not found.");
        System.out.println(tree.search(3) != null ? "Product with ID 3 found!" : "Product with ID 3 not found.");
    }
}
