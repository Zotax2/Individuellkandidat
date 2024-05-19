package WarehouseCoOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Layout {
    List<Shelf> shelfs;
    Point scaledTopBottomMargin;
    final static int pointSize = 30;
    final static int imageMagin = 40; // add 4 pixels on all sides of the image
    // Grid margins
    final static int marginXright = 1;
    final static int marginXleft = 1;
    final static int marginYtop = 1;
    final static int marginYbottom = 1;
    final static int marginXBetweenBlocks = 1;
    final static int marginYBetweenBlocks = 1;

    final static Color obstacleColor = Color.RED;
    final static Color northColor = Color.GREEN;
    final static Color eastColor = Color.BLACK;
    final static Color southColor = Color.BLUE;
    final static Color westColor = Color.YELLOW;


    Layout() {
        shelfs = new ArrayList<>();
    }

    void Add(Layout layout, Point offset) {
        System.out.println("Add Shelfs with Offset: " + offset);
        for (Shelf point : layout.shelfs) {
            shelfs.add(point.makeNewShelfWithOffset(offset));
        }
    }

    void Scale(Point scale) {
        for (Shelf point : shelfs) {
            point.scale(scale);
        }
    }


    Point getFigureSize() {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Shelf point : shelfs) {
            int x = point.getX();
            int y = point.getY();
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }


        return new Point(maxX, maxY);
    }
    Point getExtent() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        boolean zeroX = true;
        boolean zeroY = true;
        for (Shelf point : shelfs) {
            int x = point.getX();
            int y = point.getY();
            if (x == 0)
                zeroX = true;
            if (y == 0)
                zeroY = true;
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        int width = maxX - minX;
        int height = maxY - minY;
        /*
         * if (zeroX)
         * width++;
         * if (zeroY)
         * height++;
         */
        return new Point(width, height);
    }

    public static Layout makeOneRow(int sub_x_size) {
        Layout layout = new Layout();
        for (int i = 0; i < sub_x_size; i++) {
            layout.shelfs.add(new Shelf(i, 0, 'S'));
            layout.shelfs.add(new Shelf(i, 1, 'N'));
        }
        return layout;
    }

    public static Layout makeXDirection(int x_size, int sub_x_size) {
        Layout layout = new Layout();
        Layout pattern = makeOneRow(sub_x_size);
        Point extent = pattern.getExtent();
        System.out.println("Create in X direction: " + extent);
        for (int i = 0; i < x_size; i++) {
            layout.Add(pattern, new Point((extent.getX() + marginXBetweenBlocks + 1) * i, 0));

        }
        return layout;
    }

    public static Layout makeYDirection(int x_size, int y_size, int sub_x_size) {
        Layout layout = new Layout();
        Layout pattern = makeXDirection(x_size, sub_x_size);

        Point extent = pattern.getExtent();
        System.out.println("Create in Y direction: " + extent);
        for (int i = 0; i < y_size; i++) {
            layout.Add(pattern, new Point(marginXleft, (extent.getY() + 1 + marginYBetweenBlocks) * i + marginYbottom));

        }
        return layout;
    }

    public static Layout makeWarehouse(int x_size, int y_size, int sub_x_size, Point grid) {
        Layout layout = makeYDirection(x_size, y_size, sub_x_size);
        System.out.println("Layout: " + layout.shelfs.size());
        System.out.println("Extent: " + layout.getExtent());
        System.out.println("Grid: " + grid);
        layout.Scale(grid);

        System.out.println("Extent: " + layout.getExtent());

        return layout;
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
    private static Color getColorForDirection(char direction) {
        switch (direction) {
            case 'N':
                return northColor;
            case 'E':
                return eastColor;
            case 'S':
                return southColor;
            case 'W':
                return westColor;
            default:
                return Color.BLACK; // Default color
        }
    }

    public int flip(int value, int max) {
        return max - value;
    }

    public void createImageFromPoints(String filePath, List<DrawGroup> routes) {
        //Point extent = getExtent();
        //int figureHeight = extent.getY() + pointSize * (marginYtop+marginYbottom)  ;
        //int figureWidth = extent.getX() + pointSize * (marginXleft+ marginXright) ;
        Point figurSize=getFigureSize();
        int figureHeight = figurSize.getY() + pointSize * (marginYtop)  ;
        int figureWidth = figurSize.getX() + pointSize * ( marginXright) ;
        int imageHeight = figureHeight + 2 * imageMagin;
        int imageWidth = figureWidth + 2 * imageMagin;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();

        // Adjust the drawing area to allow for the image margin
        AffineTransform transformSaved = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        // transform.scale(1, -1); // Flips the y-axis.
        transform.translate(imageMagin, imageMagin); // offset
        // bottom of the component (e.g., JPanel).
        g2d.setTransform(transform);

        // Clear the image
        g2d.setColor(Color.WHITE);
        g2d.fillRect(-imageMagin, -imageMagin,imageWidth, imageHeight);

        // Draw a frame around the image
//        g2d.setColor(Color.GREEN);
//        g2d.drawRect(0, 0, figureWidth, figureHeight);

        // Draw grid dots
        for (int x = 0; x <= figureWidth; x += pointSize) {
            for (int y = 0; y <= figureHeight; y += pointSize) {
                g2d.setColor(Color.GRAY);
                g2d.fillRect(x, flip((y), figureHeight), 2, 2);
            }
        }
        // Draw shelfs and obstacles
        for (Shelf shelf : shelfs) {

            shelf.Draw(g2d, figureHeight, pointSize);
        }
        // Draw routes routes input argument?
        if (routes != null) {
            int lineNumber=0;
            for (DrawGroup route: routes){
                route.setColor(getLineColorForRoute(lineNumber++));
                route.Draw(g2d, figureHeight);
            }
        }
        g2d.setTransform(transformSaved);
        g2d.dispose();

        try {
            ImageIO.write(image, "PNG", new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writePointsToFile(String filePath) {
        // separate point and pointDirection objects into two lists
        List<Shelf> pointList = new ArrayList<>();
        List<Shelf> pointDirectionList = new ArrayList<>();
        for (Shelf shelf : shelfs) {
            if (shelf.isShelf) {
                pointDirectionList.add(shelf);
            } else {
                pointList.add(shelf);
            }
        }
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(pointList.size() + ",");
            for (Point point : pointList) {
                writer.println(point.toString());
            }
            writer.println(pointDirectionList.size() + ",");
            for (Point point : pointDirectionList) {
                writer.println(point.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // (\d+)[,;\s]+(\d+)[,;\s]*(([NSEW])[,;\s]+(\d+))?
    public static Layout readPointFile(String fileName) {
        Layout layout = new Layout();
        layout.shelfs.clear();
        // Patterns to match obstacle and shelf lines with various separators
        //------------------------------------------"(\\d+)[,;\\s]+(\\d+)[,;\\s]*(([NSEW])[,;\\s]+(\\d+))?";
        Pattern linePattern = Pattern.compile("(\\d+)[,;\\s]+(\\d+)[,;\\s]*(([NSEW])[,;\\s]+(\\d+))?", Pattern.MULTILINE);
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Ignore empty lines and comments
                if (line.isEmpty() || line.startsWith("//"))
                    continue;

                Matcher shelfMatcher = linePattern.matcher(line);
                while (shelfMatcher.find()) {
                    System.out.println("Full match: " + shelfMatcher.group(0));

                    for (int i = 1; i <= shelfMatcher.groupCount(); i++) {
                        System.out.println("Group " + i + ": " + shelfMatcher.group(i));
                    }
                }
                shelfMatcher = linePattern.matcher(line);
                if (shelfMatcher.find()){
                    // Create and add a Shelf object
                    int x = Integer.parseInt(shelfMatcher.group(1));
                    int y = Integer.parseInt(shelfMatcher.group(2));
                    Shelf shelf = null;
                    if (shelfMatcher.group(3) == null) {
                        shelf = new Shelf(x, y, 'S');
                        shelf.SetInUse(false);


                    } else {
                        String direction = shelfMatcher.group(4);
                        shelf = new Shelf(x, y, direction.charAt(0));
                        shelf.SetInUse(true);


                        int weight = 1; // Default weight
                        if (shelfMatcher.group(5) != null) {
                            weight = Integer.parseInt(shelfMatcher.group(5));
                            shelf.weight = weight;
                        }
                    }
                    layout.shelfs.add(shelf);
                } else {
                    System.out.println("No match found");
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return layout;
    }



    public void MakeShelvsToObstacles(int activeShelvs) {
        // Make copy of the shelf list avoid shuffling the original list
        List<Shelf> shuffelList = new ArrayList<>(this.shelfs);
        // Shuffle the list
        Collections.shuffle(shuffelList);
        // Set the first n elements to obstacles keeping the rest as shelves
        int obstacles = (shuffelList.size() - activeShelvs);
        for (Shelf shelf : shuffelList) {
            if (shelf.isShelf && obstacles-- > 0) {
                shelf.SetInUse(false);
            } else {
                shelf.SetRandomWeight();
            }
        }
    }

    public static void main(String[] args) {
        int x_size =10;
        int y_size =10;
        int sub_x_size=4;
        int activeshelvs=20;
        int noWarehouse=10;
        for(int i=0;i<noWarehouse;i++){
            Layout layout = makeWarehouse(x_size, y_size, sub_x_size, new Point(pointSize, pointSize));
            layout.MakeShelvsToObstacles(activeshelvs);
            layout.createImageFromPoints("warehouse"+i+"_shelf"+activeshelvs+"_"+x_size+"x"+y_size+".png",null);
            layout.writePointsToFile("warehouse"+i+"_shelf"+activeshelvs+"_"+x_size+"x"+y_size+".txt");
        }
//        Layout layout = makeWarehouse(10, 10, 4, new Point(pointSize, pointSize));
//        layout.MakeShelvsToObstacles(20);
//        layout.createImageFromPoints("warehouse.png",null);
//        layout.writePointsToFile("warehouse.txt");

        Layout testReadback = readPointFile("warehouse.txt");
        testReadback.createImageFromPoints("warehouse_readback.png",null);
        testReadback.writePointsToFile("warehouse_readback.txt");
    }

}
