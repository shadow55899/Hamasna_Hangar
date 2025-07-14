import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentAVLTree {
    private ShipmentNode root;

    /** Returns true if there are no shipments */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Create and insert a new shipment based on selected orders.
     * Locks each order so it cannot be modified or deleted.
     */
    public void insert(String destination, List<OrderNode> orders) {
        // 1. Lock the orders
        for (OrderNode o : orders) {
            o.locked = true;
        }
        // 2. Build the ShipmentNode (constructor computes cost & deliveryDate)
        ShipmentNode sh = new ShipmentNode(destination, orders);
        // 3. Insert into the AVL tree
        root = insertRecursive(root, sh);
    }

    private ShipmentNode insertRecursive(ShipmentNode node, ShipmentNode sh) {
        if (node == null) {
            return sh;
        }
        if (sh.id < node.id) {
            node.left = insertRecursive(node.left, sh);
        } else if (sh.id > node.id) {
            node.right = insertRecursive(node.right, sh);
        } else {
            // duplicate ID should not happen
            return node;
        }
        node.setHeight(Math.max(height(node.left), height(node.right)) + 1);
        return rebalance(node);
    }

    /** Search shipment by its ID */
    public ShipmentNode search(int shipmentId) {
        return searchRecursive(root, shipmentId);
    }

    private ShipmentNode searchRecursive(ShipmentNode node, int id) {
        if (node == null || node.id == id) {
            return node;
        }
        return (id < node.id)
                ? searchRecursive(node.left, id)
                : searchRecursive(node.right, id);
    }

    /** Delete shipment by ID and unlock its orders */
    public void delete(int shipmentId) {
        root = deleteRecursive(root, shipmentId);
    }

    private ShipmentNode deleteRecursive(ShipmentNode node, int id) {
        if (node == null) {
            return null;
        }

        if (id < node.id) {
            node.left = deleteRecursive(node.left, id);
        } else if (id > node.id) {
            node.right = deleteRecursive(node.right, id);
        } else {
            // found the node to delete
            // unlock all its orders
            for (OrderNode o : node.orders) {
                o.locked = false;
            }

            // perform usual BST deletion
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                // two children: replace with inorder successor
                ShipmentNode succ = minValueNode(node.right);
                node.id           = succ.id;
                node.destination  = succ.destination;
                node.cost         = succ.cost;
                node.creationDate = succ.creationDate;
                node.deliveryDate = succ.deliveryDate;
                node.orders       = new ArrayList<>(succ.orders);
                node.right        = deleteRecursive(node.right, succ.id);
            }
        }

        if (node == null) {
            return null;
        }

        node.setHeight(Math.max(height(node.left), height(node.right)) + 1);
        return rebalance(node);
    }

    private ShipmentNode minValueNode(ShipmentNode node) {
        return (node.left == null) ? node : minValueNode(node.left);
    }

    /** Find the shipment with the highest cost */
    public ShipmentNode findMostExpensiveShipment() {
        return findMostExpensiveRecursive(root);
    }

    private ShipmentNode findMostExpensiveRecursive(ShipmentNode node) {
        if (node == null) {
            return null;
        }
        ShipmentNode leftMax  = findMostExpensiveRecursive(node.left);
        ShipmentNode rightMax = findMostExpensiveRecursive(node.right);

        ShipmentNode maxNode = node;
        if (leftMax  != null && leftMax.cost  > maxNode.cost) maxNode = leftMax;
        if (rightMax != null && rightMax.cost > maxNode.cost) maxNode = rightMax;
        return maxNode;
    }

    /** Sum of all shipment costs */
    public double calculateTotalShipmentCost() {
        return sumCosts(root);
    }

    private double sumCosts(ShipmentNode node) {
        if (node == null) return 0;
        return node.cost
                + sumCosts(node.left)
                + sumCosts(node.right);
    }

    /** In-order traversal printing shipments and their orders */
    public void inorderTraversal() {
        inorder(root);
    }

    private void inorder(ShipmentNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println(
                "Shipment ID="  + node.id +
                        " | Dest="      + node.destination +
                        " | Cost=$"     + node.cost +
                        " | Created="   + node.creationDate +
                        " | Delivery="  + node.deliveryDate
        );
        System.out.println("  Orders in this shipment:");
        for (OrderNode o : node.orders) {
            System.out.println("    - Order#" + o.id +
                    " (" + o.customerName +
                    ", Prio=" + o.priority +
                    ", Cost=$" + o.calculateOrderCost() + ")");
        }
        inorder(node.right);
    }

    // ----- AVL Rotation & Helpers -----
    private int height(ShipmentNode n) {
        return (n == null) ? 0 : n.getHeight();
    }

    private ShipmentNode rebalance(ShipmentNode node) {
        int balance = node.getBalance();

        // LL case
        if (balance > 1 && node.left.getBalance() >= 0) {
            return rotateRight(node);
        }
        // LR case
        if (balance > 1 && node.left.getBalance() < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // RR case
        if (balance < -1 && node.right.getBalance() <= 0) {
            return rotateLeft(node);
        }
        // RL case
        if (balance < -1 && node.right.getBalance() > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private ShipmentNode rotateRight(ShipmentNode y) {
        ShipmentNode x  = y.left;
        ShipmentNode T2 = x.right;

        x.right = y;
        y.left  = T2;

        y.setHeight(Math.max(height(y.left), height(y.right)) + 1);
        x.setHeight(Math.max(height(x.left), height(x.right)) + 1);

        return x;
    }

    private ShipmentNode rotateLeft(ShipmentNode x) {
        ShipmentNode y  = x.right;
        ShipmentNode T2 = y.left;

        y.left  = x;
        x.right = T2;

        x.setHeight(Math.max(height(x.left), height(x.right)) + 1);
        y.setHeight(Math.max(height(y.left), height(y.right)) + 1);

        return y;
    }
}