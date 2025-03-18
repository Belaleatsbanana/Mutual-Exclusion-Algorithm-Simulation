package resource;

import java.io.Serializable;
import shared.Color;
import shared.Size;
import shared.ResourceType;


public class Resource implements Serializable {

    private final String code; // Unique code for the resource
    private final String name; // Name of the resource
    private final ResourceType type; // Type of the resource (e.g., T-shirts, bottoms, shoes)
    private final Color color; // Color of the resource
    private final Size size; // Size of the resource
    private int quantity; // Quantity of the resource

    // Constructor
    public Resource(String code, String name, ResourceType type, Color color, Size size, int quantity) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
    }

    // Constructor
    public Resource(String code, String name, ResourceType type, java.awt.Color color, Size size, int quantity) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.color = Color.fromAwtColor(color);
        this.size = size;
        this.quantity = quantity;
    }

    // Get the code
    public String getCode() {
        return code;
    }

    // Get the name
    public String getName() {
        return name;
    }

    // Get the type
    public ResourceType getType() {
        return type;
    }

    // Get the color
    public Color getColor() {
        return color;
    }

    // Get the size
    public Size getSize() {
        return size;
    }

    // Get the quantity
    public int getQuantity() {
        return quantity;
    }

    // Set the quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Resource{" + "code=" + code + ", name=" + name + ", type=" + type + ", color=" + color + ", size="
                + size + ", quantity=" + quantity + '}';
    }
}