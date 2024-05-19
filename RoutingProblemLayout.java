package WarehouseCoOp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static WarehouseCoOp.Layout.readPointFile;

public class RoutingProblemLayout implements RoutingProblem {

    int size;
    int[][] distanceMatrix;
    ArrayList<Integer> shelfWeights;
    String name;
    Layout layout;
    OptCreate op;

    RoutingProblemLayout(String filepath, String name) {
        this.name = name;


        layout = readPointFile(filepath);

        // Load the obstacles and shelf locations
        //ds.readLayout();
        //shelfWeights = ds.shelfWeight;
        // gör funktion som plockar ut dist matrix från ds
        op = new OptCreate(layout);

        op.createPlan();
        shelfWeights=op.shelfWeights;
        distanceMatrix = op.getDistmatrix();
        size = distanceMatrix.length;

        // Kan rita ifall den får en lista
        // op.TraceRoute(PathList);

    }

    public void Draw(RouteCostCalculator rc, ArrayList<Integer> Route, String filename) {
        List<DrawGroup> dgList = new ArrayList<>();

        if (rc instanceof RouteCostCalculatorWeight) {
            RouteCostCalculatorWeight rccw = (RouteCostCalculatorWeight) rc;
            ArrayList<ArrayList<Integer>> Subtours = rccw.getSubtour(Route);

            for (ArrayList<Integer> subtour : Subtours) {
                DrawGroup dg = new DrawGroup();


                for (int j = 0; j < subtour.size() - 1; j++) {

                  dg.AddDrawGroup(op.getLink(subtour.get(j), subtour.get(j + 1)));

                }
                dg.SetOffset(dgList.size()*2);
                dgList.add(dg);


            }


            // Save Subtours to file
            try {
                FileWriter writer = new FileWriter(filename + "_subtours.txt");
                int route = 0;
                writer.write("Total optimized route: " + Route.toString() + "\n");
                for (ArrayList<Integer> subtour : Subtours) {
                    writer.write("Route:" + route + ":");
                    writer.write(subtour.toString() + "\n");
                    for (int node : subtour) {
                        writer.write(node + " " + op.getNodeNameTsp(node) + ": weight " + rccw.getWeight(node) + "\n");
                    }
                    route++;
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            layout.createImageFromPoints(filename, dgList);

        }

       
    }

    @Override
    public String GetName() {
        return name;
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
        return "File" + name + " with size " + size;
    }

    @Override
    public ArrayList<Integer> GetShelfWeights() {
        return shelfWeights;
    }
}
