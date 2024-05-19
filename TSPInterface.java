package WarehouseCoOp;

import java.util.*;

public interface TSPInterface {
    public String GetName();
    public void SaveResult(String path, long executeTime);
    public ArrayList<Integer> execute(ArrayList<Integer> nodes,RouteCostCalculator Cost);
    default public int getIteration(){return 0;}
    default public void Reinit() {
        return;
    }
    default public boolean UsesInitalsolution(){return false;}

}
