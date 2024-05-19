package WarehouseCoOp;
//import java.awt.geom.AffineTransform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class DrawGroup implements DrawItem {
 
    Color color = Color.BLACK;
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    List<DrawItem> items = new ArrayList<DrawItem>();

    public DrawGroup() {
        
    }
    public void AddDrawGroup(DrawGroup group) {
        items.add(group);
    }
    public void SetOffset(int offset) {
        for (DrawItem item : items) {
            item.SetOffset(offset);

        }
        setColor(getLineColorForRoute(offset));
    }
    private static Color getLineColorForRoute(int index) {
        Color[] colors = {
                new Color(230, 25, 75),    // Red
                new Color(60, 180, 75),    // Green
                new Color(255, 225, 25),   // Yellow
                new Color(0, 130, 200),    // Blue
                new Color(245, 130, 48),   // Orange
                new Color(145, 30, 180),   // Purple
                new Color(70, 240, 240),   // Cyan
                new Color(240, 50, 230),   // Magenta
                new Color(210, 245, 60),   // Lime
                new Color(250, 190, 212),  // Pink
                new Color(0, 128, 128),    // Teal
                new Color(220, 190, 255),  // Lavender
                new Color(170, 110, 40),   // Brown
                new Color(255, 250, 200),  // Beige
                new Color(128, 0, 0),      // Maroon
                new Color(128, 128, 0),    // Olive
                new Color(0, 0, 128),      // Navy
                new Color(128, 128, 128),  // Grey
                new Color(255, 215, 180),  // Apricot
                new Color(0, 0, 0)         // Black for strong contrast
        };
        return colors[index % colors.length];

    }
    public void AddItem(DrawItem item) {
        items.add(item);
    }
    @Override
    public void Draw(Graphics2D g2d, int imageHeight) {
        g2d.setColor(color);
        for (DrawItem item : items) {
            item.Draw(g2d,imageHeight);
        }
        
    }
}