package WarehouseCoOp;

import java.util.*;

//test
public class NearestNeighbor implements TSPInterface{


    private final int[][] distMatrix;

    public NearestNeighbor(int[][] distMatrix) {
        this.distMatrix = distMatrix;
        // create a copy of the array so that we can operate on this array

    }
    public ArrayList<Integer> execute(ArrayList<Integer> nodes, RouteCostCalculator Cost){
        ArrayList<Integer> route = new ArrayList<>();
        Boolean[] visited =new Boolean[distMatrix.length];
        Arrays.fill(visited,false);
        int CheapestNode = -1;
        route.add(0);
        do {
            visited[route.getLast()] = true;
            CheapestNode = -1;
            int MinCost = Integer.MAX_VALUE;
            for (int i = 1; i < distMatrix.length; i++) {

                    if (distMatrix[route.getLast()][i] < MinCost && !visited[i]  ) {
                        MinCost = distMatrix[route.getLast()][i];
                        CheapestNode = i;
                    }


            }
            if (CheapestNode != -1) {
                route.add(CheapestNode);
                
            }

        } while (CheapestNode != -1);



        return route;

    }
    @Override
    public String GetName() {
        
        return "Närmaste granne";
    }
    @Override
    public void SaveResult(String path, long executeTime) {
       
        return;
    }


}
