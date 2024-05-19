package WarehouseCoOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GridImage {
    public static void main(String[] args) {
        // Image dimensions and tile size
        int tileSize = 30;
        int width = 22*tileSize;
        int height = 10*tileSize;
        

        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create a graphics contents on the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();

        // Draw the grid
        g2d.setColor(Color.BLACK); // Grid color
        for (int x = 0; x <= width; x += tileSize) {
            g2d.drawLine(x, 0, x, height); // Vertical lines
        }
        for (int y = 0; y <= height; y += tileSize) {
            g2d.drawLine(0, y, width, y); // Horizontal lines
        }

        // Draw a dot at the center of each tile
        g2d.setColor(Color.RED); // Dot color
        int radius = 3; // Dot radius
        for (int x = tileSize / 2; x < width; x += tileSize) {
            for (int y = tileSize / 2; y < height; y += tileSize) {
                g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2); // Draw dot
            }
        }

        // Graphics context no longer needed so dispose it
        g2d.dispose();

        // Write the image to a file
        File file = new File("grid_image.png");
        try {
            ImageIO.write(bufferedImage, "PNG", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
