import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductAVLTree   pt = new ProductAVLTree();
        OrderAVLTree     ot = new OrderAVLTree();
        ShipmentAVLTree  st = new ShipmentAVLTree();

        while (true) {
            System.out.println("\n=== Warehouse Management System ===");
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Orders");
            System.out.println("3. Manage Shipments");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.print("Select: ");
            int choice = readInt(sc);

            switch (choice) {
                case 1 -> manageProducts(sc, pt);
                case 2 -> manageOrders(sc, pt, ot);
                case 3 -> manageShipments(sc, ot, st);
                case 4 -> showReports(pt, st, ot);
                case 5 -> {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- PRODUCTS ----------------
    private static void manageProducts(Scanner sc, ProductAVLTree tree) {
        while (true) {
            System.out.println("\n-- Product Management --");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Search Product");
            System.out.println("5. List Products");
            System.out.println("6. Back");
            System.out.print("Select: ");
            int cmd = readInt(sc);

            switch (cmd) {
                case 1 -> {
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Price: ");
                    double price = readDouble(sc);
                    System.out.print("Quantity: ");
                    int qty = readInt(sc);
                    tree.insert(name, price, qty);
                    System.out.println("✅ Product created (ID=" + ProductNode.productCounter + ")");
                }
                case 2 -> {
                    System.out.print("Product ID: ");
                    int pid = readInt(sc);
                    System.out.print("New Price (-1 to skip): ");
                    double rp = readDouble(sc);
                    Double newPrice = (rp >= 0) ? rp : null;
                    System.out.print("New Quantity (-1 to skip): ");
                    int rq = readInt(sc);
                    Integer newQty = (rq >= 0) ? rq : null;
                    tree.updateProduct(pid, newPrice, newQty);
                }
                case 3 -> {
                    System.out.print("Product ID to delete: ");
                    int pidDel = readInt(sc);
                    if (tree.search(pidDel) != null) {
                        tree.delete(pidDel);
                        System.out.println("🗑 Product ID " + pidDel + " deleted successfully.");
                    } else {
                        System.out.println("❌ Product ID " + pidDel + " not found.");
                    }
                }
                case 4 -> {
                    System.out.print("Product ID to search: ");
                    int pidSr = readInt(sc);
                    ProductNode p = tree.search(pidSr);
                    if (p != null) {
                        System.out.println("→ ID=" + p.id +
                                ", Name=" + p.name +
                                ", Price=$" + p.price +
                                ", Stock=" + p.quantity);
                    } else {
                        System.out.println("❌ Product not found.");
                    }
                }
                case 5 -> {
                    if (tree.isEmpty()) {
                        System.out.println("⚠️ No products available.");
                    } else {
                        tree.inorderTraversal();
                    }
                }
                case 6 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- ORDERS ----------------
    private static void manageOrders(Scanner sc,
                                     ProductAVLTree pt,
                                     OrderAVLTree ot) {
        while (true) {
            System.out.println("\n-- Order Management --");
            System.out.println("1. Create Order");
            System.out.println("2. Add Product to Order");
            System.out.println("3. Remove Product from Order");
            System.out.println("4. Change Order Priority");
            System.out.println("5. View Order Details");
            System.out.println("6. Delete Order");
            System.out.println("7. List Orders");
            System.out.println("8. Back");
            System.out.print("Select: ");
            int cmd = readInt(sc);

            switch (cmd) {
                case 1 -> {
                    // create
                    System.out.print("Customer name: ");
                    String cust = sc.next();
                    Priority pr = readPriority(sc);
                    ot.insert(cust, pr);
                    int oid = OrderNode.orderCounter;
                    System.out.println("✅ Order created (ID=" + oid + ", Priority=" + pr + ")");

                    // list products
                    System.out.println("\n📦 Available Products:");
                    if (pt.isEmpty()) {
                        System.out.println("⚠️ No products available.");
                    } else {
                        pt.inorderTraversal();
                    }

                    // add items
                    OrderNode order = ot.search(oid);
                    while (true) {
                        System.out.print("Product ID to add (0 to finish): ");
                        int pid = readInt(sc);
                        if (pid == 0) break;
                        ProductNode prod = pt.search(pid);
                        if (prod != null) {
                            System.out.print("Quantity: ");
                            int q = readInt(sc);
                            order.addProduct(prod, q);
                        } else {
                            System.out.println("❌ Invalid Product ID.");
                        }
                    }
                }
                case 2 -> {
                    // add to existing
                    if (ot.isEmpty()) {
                        System.out.println("⚠️ No orders available.");
                        break;
                    }
                    System.out.println("\n📋 Current Orders:");
                    ot.inorderTraversal();

                    System.out.print("Order ID: ");
                    int oid2 = readInt(sc);
                    OrderNode o2 = ot.search(oid2);
                    if (o2 == null) {
                        System.out.println("❌ Order not found.");
                        break;
                    }
                    System.out.println("\n📦 Available Products:");
                    if (pt.isEmpty()) {
                        System.out.println("⚠️ No products available.");
                        break;
                    }
                    pt.inorderTraversal();

                    System.out.print("Product ID to add: ");
                    int pid2 = readInt(sc);
                    ProductNode p2 = pt.search(pid2);
                    if (p2 == null) {
                        System.out.println("❌ Product not found.");
                        break;
                    }
                    System.out.print("Quantity: ");
                    int qty2 = readInt(sc);
                    o2.addProduct(p2, qty2);
                }
                case 3 -> {
                    // remove item
                    if (ot.isEmpty()) {
                        System.out.println("⚠️ No orders available.");
                        break;
                    }
                    System.out.println("\n📋 Current Orders:");
                    ot.inorderTraversal();

                    System.out.print("Order ID: ");
                    int oid3 = readInt(sc);
                    OrderNode o3 = ot.search(oid3);
                    if (o3 == null) {
                        System.out.println("❌ Order not found.");
                        break;
                    }
                    System.out.print("Product ID to remove: ");
                    int pid3 = readInt(sc);
                    if (o3.removeProductById(pid3)) {
                        System.out.println("✔ Product removed and stock restored.");
                    } else {
                        System.out.println("❌ Product not in order.");
                    }
                }
                case 4 -> {
                    // change priority
                    if (ot.isEmpty()) {
                        System.out.println("⚠️ No orders available.");
                        break;
                    }
                    System.out.println("\n📋 Current Orders:");
                    ot.inorderTraversal();

                    System.out.print("Order ID: ");
                    int oid4 = readInt(sc);
                    Priority newPr = readPriority(sc);
                    ot.updatePriority(oid4, newPr);
                }
                case 5 -> {
                    // view details
                    System.out.print("Order ID: ");
                    int oid5 = readInt(sc);
                    OrderNode o5 = ot.search(oid5);
                    if (o5 != null) {
                        System.out.println("Order #" + o5.id +
                                ", Customer=" + o5.customerName +
                                ", Priority=" + o5.priority);
                        System.out.println("Total cost = $" + o5.calculateOrderCost());
                        if (o5.items.isEmpty()) {
                            System.out.println("  (No products)");
                        } else {
                            System.out.println("  Items:");
                            for (OrderNode.OrderItem it : o5.items) {
                                System.out.println("   - " + it.product.name +
                                        " x" + it.quantity +
                                        " @ $" + it.product.price);
                            }
                        }
                    } else {
                        System.out.println("❌ Order not found.");
                    }
                }
                case 6 -> {
                    // delete order
                    if (ot.isEmpty()) {
                        System.out.println("⚠️ No orders available.");
                        break;
                    }
                    System.out.println("\n📋 Current Orders:");
                    ot.inorderTraversal();

                    System.out.print("Enter Order ID to delete: ");
                    int did = readInt(sc);
                    OrderNode td = ot.search(did);
                    if (td != null) {
                        ot.delete(did);
                        System.out.println("🗑 Order ID " + did + " deleted successfully.");
                    } else {
                        System.out.println("❌ Order ID " + did + " not found.");
                    }
                }
                case 7 -> {
                    // list orders
                    if (ot.isEmpty()) {
                        System.out.println("⚠️ No orders available.");
                    } else {
                        ot.inorderTraversal();
                    }
                }
                case 8 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- SHIPMENTS ----------------
    private static void manageShipments(Scanner sc,
                                        OrderAVLTree ot,
                                        ShipmentAVLTree st) {
        while (true) {
            System.out.println("\n-- Shipment Management --");
            System.out.println("1. Create Shipment");
            System.out.println("2. List Shipments");
            System.out.println("3. Back");
            System.out.print("Select: ");
            int cmd = readInt(sc);

            switch (cmd) {
                case 1 -> {
                    // Create a new shipment
                    if (ot.isEmpty()) {
                        System.out.println("⚠️ No orders to ship.");
                        break;
                    }
                    System.out.print("Destination: ");
                    String dest = sc.next();

                    // Show only unshipped orders
                    System.out.println("\nAvailable Orders (unshipped):");
                    ot.inorderUnshipped();

                    // Collect selected orders
                    List<OrderNode> selected = new ArrayList<>();
                    while (true) {
                        System.out.print("Enter Order ID to add (0 to finish): ");
                        int oid = readInt(sc);
                        if (oid == 0) break;
                        OrderNode o = ot.search(oid);
                        if (o == null) {
                            System.out.println("❌ Order not found.");
                        } else if (o.locked) {
                            System.out.println("❌ Order already shipped.");
                        } else {
                            selected.add(o);
                        }
                    }

                    if (selected.isEmpty()) {
                        System.out.println("⚠️ No orders selected. Aborting.");
                        break;
                    }

                    // Insert shipment (locks orders automatically)
                    st.insert(dest, selected);
                    ShipmentNode sh = st.search(ShipmentNode.shipmentCounter);
                    System.out.println("🚚 Shipment created (ID=" + sh.id +
                            ", Cost=$" + sh.cost +
                            ", Delivery=" + sh.deliveryDate + ")");
                }

                case 2 -> {
                    // List all shipments
                    if (st.isEmpty()) {
                        System.out.println("⚠️ No shipments available.");
                    } else {
                        System.out.println("\nAll Shipments:");
                        st.inorderTraversal();
                    }
                }

                case 3 -> {
                    // Back to main menu
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
    // --------------- REPORTS ---------------
    private static void showReports(ProductAVLTree pt,
                                    ShipmentAVLTree st,
                                    OrderAVLTree ot) {
        System.out.println("\n-- Reports & Analytics --");
        System.out.println("Total Stock Value        = $" + pt.calculateTotalStockValue());
        ShipmentNode maxS = st.findMostExpensiveShipment();
        if (maxS != null) {
            System.out.println("Most Expensive Shipment  = ID " + maxS.id + ", Cost=$" + maxS.cost);
        }
        System.out.println("Total Shipment Cost      = $" + st.calculateTotalShipmentCost());
        System.out.println("Total Orders Cost        = $" + ot.calculateTotalOrderCost());
    }

    // --------------- HELPERS ---------------
    private static int readInt(Scanner sc) {
        try { return Integer.parseInt(sc.next()); }
        catch (Exception e) { return -1; }
    }

    private static double readDouble(Scanner sc) {
        try { return Double.parseDouble(sc.next()); }
        catch (Exception e) { return -1.0; }
    }

    private static Priority readPriority(Scanner sc) {
        while (true) {
            System.out.println("Select priority: [1] URGENT  [2] VIP  [3] NORMAL");
            int p = readInt(sc);
            if (p == 1) return Priority.URGENT;
            if (p == 2) return Priority.VIP;
            if (p == 3) return Priority.NORMAL;
            System.out.println("Invalid input.");
        }
    }
}