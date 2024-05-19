package WarehouseCoOp;
//import java.awt.geom.AffineTransform;

import java.awt.*;

public class Line implements DrawItem {
    private Point start;
    private Point stop;
    public int offset = 0;
    public Line(Point start, Point stop, int offset)  {
        this.start = start;
        this.stop = stop;
        this.offset=offset;
    }
    public void SetOffset(int offset) {

        this.offset=offset-5;

    }

    public void Draw(Graphics2D g2d, int imageHeight) {
        
        g2d.drawLine(start.getX()+offset, flip(start.getY()+offset,imageHeight), stop.getX()+offset, flip(stop.getY()+offset,imageHeight));
    }
}