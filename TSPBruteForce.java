/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WarehouseCoOp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author gummi
 */
public class TSPBruteForce  implements TSPInterface{


    private List<Vertex> nodes;
    private static RouteCostCalculator costCalculator;

    public TSPBruteForce() {


        

    }

    public ArrayList<Integer> execute(ArrayList<Integer> nodes,RouteCostCalculator costCalc) {
        costCalculator=costCalc;

        int shortestTourLength = Integer.MAX_VALUE;
        ArrayList<Integer> shortestTour = new ArrayList<>(nodes);
        
        shortestTourLength=permuteAndCalculate(nodes,0,shortestTour,shortestTourLength);
        
        System.out.println("Shortest tour length: "+ shortestTourLength);
        System.out.println("Shortest tour path: "+ shortestTour );
        List<Integer> IntegerList = new ArrayList<Integer>();
       // IntegerList.add(nodes.get(0).getNum());// Add start node
////       for(int i = 1; i< shortestTour.size();i++){
//           //nodes.get(shortestTour.get(i)).getNum()
//           IntegerList.add(shortestTour.get(i));
//       
//       }
        return shortestTour;

    }

    private static int permuteAndCalculate(ArrayList<Integer> Nodes, int startIndex, ArrayList<Integer> shortestTour, int shortestTourLength) {

        if (startIndex == Nodes.size() - 1) {
            int tourLength = calculateTourLength(Nodes);
            if (tourLength < shortestTourLength) {
                shortestTourLength = tourLength;
                shortestTour.clear();
                // TODO: Handle the case where the start and end nodes are not the same
                //shortestTour.add(0);
                shortestTour.addAll(Nodes);
                //shortestTour.add(Nodes.size()-1);
            }
        } else {
            for (int i = startIndex; i < Nodes.size(); i++) {
                Collections.swap(Nodes, startIndex, i);
                shortestTourLength=permuteAndCalculate(Nodes, startIndex + 1, shortestTour, shortestTourLength);
                Collections.swap(Nodes, startIndex, i);
            }
        }
        return shortestTourLength;
    }

    private static int calculateTourLength(ArrayList<Integer> Nodes ){

        return costCalculator.calculateRouteCost(Nodes);
    }

    @Override
    public String GetName() {
        return "Brute Force";
    }

    @Override
    public void SaveResult(String path, long executeTime) {
        // TODO Auto-generated method stub
        return ;
    }

}

