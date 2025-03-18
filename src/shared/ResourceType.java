package shared;
import java.io.Serializable;


public enum ResourceType implements Serializable {
    // Enumerated values
    TSHIRTS("T-shirts"),
    BOTTOMS("Bottoms"),
    SHOES("Shoes");

    private final String displayName; // Human-readable name for the resource

    // Constructor
    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    // Get the display name
    public String getDisplayName() {
        return displayName;
    }
}
