package shared;

import java.io.Serializable;

public enum Size implements Serializable {
    // Enumerated values
    S("S"),
    M("M"),
    L("L"),
    XL("XL");

    private final String displayName; // Human-readable name for the size

    // Constructor
    Size(String displayName) {
        this.displayName = displayName;
    }

    // Get the display name
    public String getDisplayName() {
        return displayName;
    }
}