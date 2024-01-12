package Main;

public class Piece {
    private int x;
    private int y;
    private MapPanel mapPanel;

    public Piece(int x, int y, MapPanel mapPanel) {
        this.x = x;
        this.y = y;
        this.mapPanel = mapPanel;
    }

    // Getters and setters for x and y coordinates
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

