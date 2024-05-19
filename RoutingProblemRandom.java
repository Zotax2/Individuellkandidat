package WarehouseCoOp;

import java.util.ArrayList;
import java.util.Random;

public class RoutingProblemRandom implements RoutingProblem{
    private int size;
    private int[][] distanceMatrix;
    private ArrayList<Integer> shelfWeights;
    public RoutingProblemRandom(int size) {
        this.size = size;
        Random random = new Random();
        distanceMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0; // Distance from a node to itself is zero
                } else {
                    int distance = 1 + random.nextInt(100); // Random distance between 1 and 100
                    distanceMatrix[i][j] = distance;
                    distanceMatrix[j][i] = distance; // Ensure symmetry
                }
            }
        }

        // Random shelf weights
        shelfWeights = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            shelfWeights.add(i, 1 + random.nextInt(10)); // Random weight between 1 and 10
        }
    }

    @Override
    public String GetName() {
        return "Random";
    }

    @Override
    public int GetSize() {
        return size;
    }

    @Override
    public int[][] GetDistanceMatrix() {
        return distanceMatrix;
    }

    @Override
    public String GetInfo() {
        return "Random problem with size " + size;
    }

    @Override
    public ArrayList<Integer>  GetShelfWeights() {
        return shelfWeights;
    }
    

    
    
}
