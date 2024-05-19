package WarehouseCoOp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;


public class RouteCostCalculatorWeight implements RouteCostCalculator{

    // Distance matrix between nodes
    // distMatrix[i][j] is the distance between node i and node j
    // distMatrix[i][i] is always 0
    // assume node 0 is the start and node n-1 is the dropoff
    private int[][] distMatrix;
    private int capacity;
    private ArrayList<Integer> shelfWeight;
    private int lostCapacity;
    private ArrayList<Integer> routeStarts;

    public RouteCostCalculatorWeight(int[][] distMatrix, int capacity,ArrayList<Integer> shelfWeights) {
        this.distMatrix = distMatrix;
        this.capacity= capacity;
        shelfWeight=shelfWeights;

    }
    public int getLostCapacity(){
        return lostCapacity;
    }
    public int getNoRoutes(){
        if(routeStarts==null){
            return -1;
        }
        return routeStarts.size();
    }

    public ArrayList<ArrayList<Integer>> getSubtour(ArrayList<Integer> cities) {
        ArrayList<ArrayList<Integer>> routes = new ArrayList<>();
        if (cities.size()!= distMatrix.length){
            System.err.println("cities.size() must be equal to distMatrix.length");
        }
        int length = 0;
        int weight = getWeight(cities.get(0));

        ArrayList<Integer> route = new ArrayList<>();
        route.add(cities.get(0));
        // start from start1
        //int k = 0;
        for (int i = 1; i < cities.size(); i++) {

            if(getWeight(i)>capacity){
                System.out.println("Weight is larger than capacity");
                exit(-1);
            }

            if(weight+getWeight(cities.get(i)) > capacity){
                lostCapacity+=capacity-weight;
                weight=getWeight(cities.get(i));

                // distance from shelf where capacity is full to drop off
                route.add(cities.get(0));
                
                routes.add(route);
                route= new ArrayList<>();
                route.add(cities.get(0));
                route.add(cities.get(i));
                // distance from start to next shelf


            } else {

                weight+=getWeight(cities.get(i));
                route.add(cities.get(i));
            }
            //k = cities.get(i);
        }
        if(route.size() >0){
            route.add(cities.get(0));
            routes.add(route);
        }





        // distance from the last node to dropoff

        return routes;
    }

    @Override
    public int calculateRouteCost(ArrayList<Integer> cities) {
        //ArrayList<Integer> testweight =new ArrayList<>(Collections.nCopies(60,1));
        if (cities.size()!= distMatrix.length){
            System.err.println("cities.size() must be equal to distMatrix.length");
        }
        int length = 0;
        int weight = getWeight(cities.get(0));
        int droppOff =0; //distMatrix.length-1
        lostCapacity=0;
        routeStarts=new ArrayList<>();
        routeStarts.add(1);


        // start from start1
        int k = 0;
        for (int i = 1; i < cities.size(); i++) {
            if(getWeight(i)>capacity){
                System.out.println("Weight is larger than capacity");
                exit(-1);
            }

            if(weight+getWeight(cities.get(i)) > capacity){
                lostCapacity+=capacity-weight;
                weight=getWeight(cities.get(i));
                
                // distance from shelf where capacity is full to drop off
                length += distMatrix[k][0];
                routeStarts.add(i);
                // distance from start to next shelf
                length += distMatrix[0][cities.get(i)];
             
            } else {
            length += distMatrix[k][cities.get(i)];
            weight+=getWeight(cities.get(i));
            }
            k = cities.get(i);
        }
        lostCapacity+=capacity-weight;


        // distance from the last node to dropoff
        length += distMatrix[k][droppOff];
        return length;
    }
    public int getWeight(int node){
        if(node==0){
            return 0;
        }
        return shelfWeight.get(node-1);
    }

    @Override
    public String GetName() {
       return "Kapacitet ="+capacity;
    }
}
