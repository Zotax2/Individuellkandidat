package WarehouseCoOp;

public class Point {
    public int x;
    
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addOffset(Point other) {
        this.x += other.x;
         this.y += other.y;
    }
  
    public void scale(Point other) {
        this.x = this.x * other.x;
        this.y = this.y * other.y;
  
    }
    @Override
    public String toString() {
        return String.format("%d, %d", x, y);
        //return String.format("Point(x: %d, y: %d)", x, y);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
 