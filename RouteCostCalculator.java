package WarehouseCoOp;

import java.util.ArrayList;


public interface RouteCostCalculator {
    String GetName();
    int calculateRouteCost(ArrayList<Integer> route);
   default public int getLostCapacity(){
        return Integer.MAX_VALUE;
    }
    default public int getNoRoutes(){
        return 1;
    }
}
