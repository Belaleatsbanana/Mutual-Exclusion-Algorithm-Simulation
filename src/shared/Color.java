package shared;

import java.io.Serializable;

public enum Color implements Serializable {
    // Enumerated values
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    BLACK("Black"),
    WHITE("White"),
    ORANGE("Orange"),
    PURPLE("Purple"),
    PINK("Pink"),
    BROWN("Brown");

    public static Color fromAwtColor(java.awt.Color awtColor) {
        if (awtColor.equals(java.awt.Color.RED)) {
            return RED;
        } else if (awtColor.equals(java.awt.Color.ORANGE)) {
            return ORANGE;
        } else if (awtColor.equals(java.awt.Color.YELLOW)) {
            return YELLOW;
        } else if (awtColor.equals(java.awt.Color.GREEN)) {
            return GREEN;
        } else if (awtColor.equals(java.awt.Color.BLUE)) {
            return BLUE;
        } else if (awtColor.equals(java.awt.Color.WHITE)) {
            return WHITE;
        } else if (awtColor.equals(new java.awt.Color(128, 0, 128))) { // Custom purple
            return PURPLE;
        } else if (awtColor.equals(java.awt.Color.BLACK)) {
            return BLACK;
        } else {
            throw new IllegalArgumentException("Unsupported color: " + awtColor);
        }
    }

    private final String displayName; // Human-readable name for the color

    // Constructor
    Color(String displayName) {
        this.displayName = displayName;
    }

    // Get the display name
    public String getDisplayName() {
        return displayName;
    }
}
