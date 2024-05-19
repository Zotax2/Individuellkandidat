package WarehouseCoOp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class InitialSolution {
    public ArrayList<Integer> solution;
    public String name;

    public InitialSolution(ArrayList<Integer> solution, String name) {
        this.solution = solution;
        this.name = name;
    }
}

public class RunComparison {

    public static String MakeFileName(RoutingProblem problem, TSPInterface algorithm, RouteCostCalculator routeCost,
            InitialSolution initalSolution) {
        return "result/kapacitet5_20hyllor/" + problem.GetName() + " " + algorithm.GetName() + " " + routeCost.GetName() + " "
                + initalSolution.name;
    }

    private static String GetResultFileName(){
        return "result/kapacitet5_20hyllor/Resultatfil.txt";
    }
    // Ensure directory exists
    public static void EnsureDirectory(String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();

    }
    public static Random random =new Random(15358);
    public static void main(String[] args) {



        ArrayList<RoutingProblem> problems = new ArrayList<RoutingProblem>();
        System.out.println("Start");

        problems.add(new RoutingProblemLayout("Layouts\\warehouse0_shelf20_10x10.txt", "Lagermodell0"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse1_shelf20_10x10.txt", "Lagermodell1"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse2_shelf20_10x10.txt", "Lagermodell2"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse3_shelf20_10x10.txt", "Lagermodell3"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse4_shelf20_10x10.txt", "Lagermodell4"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse5_shelf20_10x10.txt", "Lagermodell5"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse6_shelf20_10x10.txt", "Lagermodell6"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse7_shelf20_10x10.txt", "Lagermodell7"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse8_shelf20_10x10.txt", "Lagermodell8"));
        problems.add(new RoutingProblemLayout("Layouts\\warehouse9_shelf20_10x10.txt", "Lagermodell9"));

//        problems.add(new RoutingProblemLayout("Layouts\\warehouse1_shelf50_10x10.txt", "Lagermodell1"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse2_shelf50_10x10.txt", "Lagermodell2"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse3_shelf50_10x10.txt", "Lagermodell3"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse4_shelf50_10x10.txt", "Lagermodell4"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse5_shelf50_10x10.txt", "Lagermodell5"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse6_shelf50_10x10.txt", "Lagermodell6"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse7_shelf50_10x10.txt", "Lagermodell7"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse8_shelf50_10x10.txt", "Lagermodell8"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse9_shelf50_10x10.txt", "Lagermodell9"));

        //problems.add(new RoutingProblemLayout("Layouts\\layout.txt", "layout"));
       //problems.add(new RoutingProblemLayout("Layouts\\warehouse_1.txt", "warehouse1"));

//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_3.txt", "warehouse3"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_4.txt", "warehouse4"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_5.txt", "warehouse5"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_6.txt", "warehouse6"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_7.txt", "warehouse7"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_8.txt", "warehouse8"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_9.txt", "warehouse9"));
//        problems.add(new RoutingProblemLayout("Layouts\\warehouse_10.txt", "warehouse10"));



        //problems.add(new RoutingProblemRandom(10));
        //problems.add(new RoutingProblemRandom(100));

        File resultFile = new File(GetResultFileName());
        if (resultFile.exists()) {
            resultFile.delete();
        }
        List<String> columnHeaders = new ArrayList<>();
        columnHeaders.add("Problem");
        columnHeaders.add("Problem storlek");
        columnHeaders.add("Heuristik");
        columnHeaders.add("Ruttlängd");
        columnHeaders.add("Tid");
        columnHeaders.add("Antal iterationer");
        columnHeaders.add("Ruttberäkning");
        columnHeaders.add("Oanvänd kapacitet");
        columnHeaders.add("Antal rutter");
        columnHeaders.add("Initial lösning");

        try {
            FileWriter writer = new FileWriter(GetResultFileName(), true);
            for (String header : columnHeaders) {
                writer.write(header + "\t");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (RoutingProblem problem : problems) {
            System.out.println(problem.GetInfo());
            List<TSPInterface> algoritmList = createAlgoritmList(problem);
            List<RouteCostCalculator> routeCosts = createRouteCostsList(problem);
            ArrayList<InitialSolution> initalSolutions = new ArrayList<InitialSolution>();
            //initalSolutions.add(new InitialSolution(makeNodeOrderList(problem.GetSize()), "I ordning"));
            //initalSolutions.add(new InitialSolution(makeNodeOrderListRandom(problem.GetSize()), "Slumpmässig ordning"));
            TSPInterface nn = new NearestNeighbor(problem.GetDistanceMatrix());
            initalSolutions.add(new InitialSolution(nn.execute(makeNodeOrderList(problem.GetSize()), routeCosts.get(0)),
                    "Närmaste granne"));

           
            for (RouteCostCalculator routeCost : routeCosts) {
                for (TSPInterface algorithm : algoritmList) {
                    for (InitialSolution initalSolution : initalSolutions) {
                        algorithm.Reinit();
                        runAlgorithm(algorithm, problem, routeCost, initalSolution);
                        if (!algorithm.UsesInitalsolution()) {
                            break;
                        }
                    }
                }
            }

        }

        System.out.println("Done");

    }

    public static List<TSPInterface> createAlgoritmList(RoutingProblem problem) {
        List<TSPInterface> algoritmList = new ArrayList<TSPInterface>();
        if (problem.GetSize() < 12) {
            algoritmList.add(new TSPBruteForce());
        }

        // algoritmList.add(new MSTAlgorithm());
        algoritmList.add(new NearestNeighbor(problem.GetDistanceMatrix()));
       // algoritmList.add(new TabuSearch(problem.GetDistanceMatrix(), 1000, 100));
        algoritmList.add(new TabuSearch(problem.GetDistanceMatrix(),25 , 10));
        algoritmList.add(new twoopt(problem.GetDistanceMatrix()));

        return algoritmList;
    }

    public static ArrayList<Integer> makeNodeOrderList(int size) {
        ArrayList<Integer> nodeOrder = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            nodeOrder.add(i);
        }
        return nodeOrder;
    }


    public static ArrayList<Integer> makeNodeOrderListRandom(int size) {
        ArrayList<Integer> nodeOrder = new ArrayList<Integer>(size);
        for (int i = 1; i < size; i++) {
            nodeOrder.add(i);
        }
        java.util.Collections.shuffle(nodeOrder,random);
        nodeOrder.add(0,0);
        return nodeOrder;
    }

    public static List<RouteCostCalculator> createRouteCostsList(RoutingProblem problem) {
        List<RouteCostCalculator> routeCosts = new ArrayList<RouteCostCalculator>();
        routeCosts.add(new RouteCost(problem.GetDistanceMatrix()));
        // Make
        routeCosts.add(new RouteCostCalculatorWeight(problem.GetDistanceMatrix(), 5, problem.GetShelfWeights()));
        //routeCosts.add(new RouteCostCalculatorWeight(problem.GetDistanceMatrix(), 5, problem.GetShelfWeights()));
        //routeCosts.add(new RouteCostCalculatorWeight(problem.GetDistanceMatrix(), 20, problem.GetShelfWeights()));
        return routeCosts;
    }

    public static void runAlgorithm(TSPInterface algoritm, RoutingProblem problem,
            RouteCostCalculator routeCost, InitialSolution initalSolution) {

        long startTime = System.currentTimeMillis(); // Use System.nanoTime(); for more accurate timing
    
        ArrayList<Integer> route = algoritm.execute(initalSolution.solution, routeCost);
        long endTime = System.currentTimeMillis();
        int routeLength = 0;
        if (route != null) {
            routeLength = routeCost.calculateRouteCost(route);
        }
        if (problem instanceof RoutingProblemLayout) {
            problem.Draw(routeCost, route, MakeFileName(problem, algoritm, routeCost, initalSolution)+".png");  // problem is of type RoutingProblemLayout
        }


        
        // System.out.println(algorithm.GetName() + " took " + (endTime - startTime) + "
        // milliseconds");
        String filepath = MakeFileName(problem, algoritm, routeCost, initalSolution);

        EnsureDirectory(filepath);

        // Add info to file
        // Problem
        try {
            FileWriter writer = new FileWriter(GetResultFileName(), true);

           // writer.write("\n");
            writer.write(problem.GetName());
            writer.write("\t" + problem.GetSize());

            // Algorithm
            writer.write("\t" + algoritm.GetName());
            writer.write("\t" + routeLength);
            writer.write("\t" + (endTime - startTime));
            writer.write("\t"+ algoritm.getIteration());

            // RouteCost
            writer.write("\t" + routeCost.GetName());
            if (routeCost.getNoRoutes()<=1) {
                writer.write("\t");
            } else {
                writer.write("\t" + routeCost.getLostCapacity());
            }
            writer.write("\t" + routeCost.getNoRoutes());

            // InitialSolution
            if (algoritm.UsesInitalsolution()) {
                writer.write("\t" + initalSolution.name);
            } else {
                writer.write("\t");
            }
            

            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        algoritm.SaveResult(filepath, endTime - startTime);

    }

}
