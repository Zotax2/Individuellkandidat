package WarehouseCoOp;

import java.awt.*;


public class Shelf extends Point {
    public char direction;
    public boolean isShelf;
    public int weight;

    public void SetInUse(boolean inUse) {
        this.isShelf = inUse;
    }

    public Shelf(int x, int y, char direction) {
        super(x, y);
        this.direction = direction;
        isShelf = true;
        weight = 1;
    }
    public void SetRandomWeight(){
        weight = (int)(Math.random() * 5 + 1);
    }

    public Shelf makeNewShelfWithOffset(Point other) {
        int newX = this.x + other.x;
        int newY = this.y + other.y;
        Shelf s = new Shelf(newX, newY, direction);
        System.out.println("Shelf: " + s);
        return s;
    }

    public char getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        if (isShelf) {
            //return String.format("%d,  %d, %s,", x, y, direction);
            return String.format("%d,%d,%s,%d,", x, y, direction,weight);
        } else {
            return String.format("%d,%d,", x, y);
        }

        // return String.format("PointDirection(x: %d, y: %d, direction: %s)", x, y,
        // direction);
    }

    public int flip(int value, int max) {
        return max - value;
    }
    public void Draw(Graphics2D g2d, int imageHeight, int size) {

        int sizeOffset = size / 2;
        int left = this.x - sizeOffset;
        
        int right = this.x + sizeOffset-1;
        int bottom = flip( (this.y - sizeOffset+1),imageHeight);
        int top =  flip((this.y + sizeOffset),imageHeight);
        g2d.setColor(Color.BLACK);
        // Draw all four sides one by one
        if (isShelf) {
            // Clear the area
            g2d.setColor(new Color(230, 230, 230));
            //g2d.fillRect(left, bottom, size, size); // if tranformed g2d
            g2d.fillRect(left, top, size, size);
            g2d.setColor(Color.GRAY);
            if (direction != 'S') {
                g2d.drawLine(left, bottom, right, bottom); // Bottom
            }
            if (direction != 'E') {
                g2d.drawLine(left, bottom, left, top); // Left
            }
            if (direction != 'N') {
                g2d.drawLine(left, top, right, top);// Top
            }
            if (direction != 'W') {
                g2d.drawLine(right, bottom, right, top); // Right
            }
            g2d.setColor(Color.BLACK);
            String weightText = String.valueOf(weight);
            int weightTextWidth = g2d.getFontMetrics().stringWidth(weightText);
            int weightTextHeight = g2d.getFontMetrics().getHeight();
            int weightTextX = this.x - weightTextWidth / 2;
            int weightTextY = flip((this.y - weightTextHeight / 2),imageHeight);
            g2d.drawString(weightText, weightTextX, weightTextY);
        } else {
            g2d.setColor(Color.GRAY);
            //g2d.fillRect(left, bottom, size, size); // if tranformed g2d
            g2d.fillRect(left, top, size, size);
        }

    }
}
