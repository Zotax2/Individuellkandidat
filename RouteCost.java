package WarehouseCoOp;

import java.util.ArrayList;


public class RouteCost implements RouteCostCalculator{


    private int[][] distMatrix;
    
    public RouteCost(int[][] distMatrix) {
        this.distMatrix = distMatrix;
    }

    @Override
    public int calculateRouteCost(ArrayList<Integer> cities) {
        int droppOff = 0; //distMatrix.length-1
        int length = 0;
        int k = 0;
        for (int i = 1; i < cities.size(); i++) {
            length += distMatrix[k][cities.get(i)];
            k = cities.get(i);
        }
        length += distMatrix[k][droppOff];
        return length;    }

    @Override
    public String GetName() {
        return "Utan kapacitet";
    }

}
