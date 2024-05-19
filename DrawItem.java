package WarehouseCoOp;

import java.awt.*;


public interface DrawItem {


    void Draw(Graphics2D g2d,int imageHeight);
    default int flip(int value, int max) {
        return max - value;
    }
    void SetOffset(int offset);
}