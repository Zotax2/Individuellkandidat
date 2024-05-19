package WarehouseCoOp;

import java.util.ArrayList;

public interface RoutingProblem {
    public String GetName();
    public int GetSize();
    public int[][] GetDistanceMatrix();
    public ArrayList<Integer>  GetShelfWeights();

    public String GetInfo();

    default void Draw(RouteCostCalculator routeCost, ArrayList<Integer> route, String s){};

}
