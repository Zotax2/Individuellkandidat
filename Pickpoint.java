package WarehouseCoOp;
//import java.awt.geom.AffineTransform;

import java.awt.*;

public class Pickpoint implements DrawItem {
    private Point start;
    private int size =6;
    public int offset = 0;
    public Pickpoint(Point start, int offset)  {
        this.start = start;
        this.offset=offset;
    }
    public void SetOffset(int offset) {

            this.offset=offset-5;

    }

    public void Draw(Graphics2D g2d, int imageHeight) {

        //g2d.drawOval(start.getX()+offset-size/2, flip(start.getY()+offset-size/2,imageHeight), size, size);
        g2d.fillOval(start.getX()+offset-size/2, flip(start.getY()+offset+size/2,imageHeight), size, size);

    }
}