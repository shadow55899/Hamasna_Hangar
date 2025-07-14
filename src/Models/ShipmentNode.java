import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentNode {
    public static int shipmentCounter = 0;
    public int id, height;
    public String destination;
    public double cost;
    public LocalDate creationDate, deliveryDate;
    public ShipmentNode left, right;
    public List<OrderNode> orders = new ArrayList<>();

    public ShipmentNode(String destination, List<OrderNode> orders) {
        this.id = ++shipmentCounter;
        this.creationDate = LocalDate.now();
        this.destination = destination;
        this.orders.addAll(orders);

        // 1) تحسب التكلفة
        this.cost = orders.stream()
                .mapToDouble(OrderNode::calculateOrderCost)
                .sum();

        // 2) تحدد موعد التوصيل
        boolean hasUrgent = orders.stream()
                .anyMatch(o -> o.priority == Priority.URGENT);
        boolean hasVip    = orders.stream()
                .anyMatch(o -> o.priority == Priority.VIP);

        if (hasUrgent) {
            deliveryDate = creationDate.plusWeeks(1);
        } else if (hasVip) {
            deliveryDate = creationDate.plusWeeks(2);
        } else {
            deliveryDate = creationDate.plusMonths(1);
        }

        this.height = 1;
    }

    public int getHeight() { return height; }
    public void setHeight(int h) { height = h; }
    public int getBalance() {
        return safeHeight(left) - safeHeight(right);
    }
    private int safeHeight(ShipmentNode n) { return (n == null)?0:n.height; }
}