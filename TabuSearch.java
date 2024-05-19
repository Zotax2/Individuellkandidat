package WarehouseCoOp;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TabuSearch implements TSPInterface {
    private int[][] distanceMatrix;
    private int numIterations;
    private int tabuTenure;
    private int bestGlobalCost;
    private static RouteCostCalculator costCalculator;
    public static ArrayList<LogItem> logList;
    private int Iterations;

    public TabuSearch(int[][] distanceMatrix, int numIterations, int tabuTenure) {
        this.distanceMatrix = distanceMatrix;
        this.numIterations = numIterations;
        this.tabuTenure = tabuTenure;
        logList = new ArrayList<LogItem>(numIterations + 2);

    }
    public boolean UsesInitalsolution(){return true;}

    @Override
    public void Reinit() {
        logList = new ArrayList<LogItem>(numIterations + 2);
        Iterations=0;
    }
    @Override
    public ArrayList<Integer> execute(ArrayList<Integer> routeNodes, RouteCostCalculator costCalc) {
        costCalculator = costCalc;
        System.out.println("Test ");

        ArrayList<Integer> bestSolution;
        // copy the routeNodes

        bestSolution = new ArrayList<>(routeNodes);
        bestGlobalCost = costCalculator.calculateRouteCost(bestSolution);
        System.out.println("Inital solution BestCost: " + bestGlobalCost);
        // Log inital solution
        Log(0,bestGlobalCost, 0, bestGlobalCost);

        int[][] tabuList = new int[routeNodes.size()][routeNodes.size()];
        for (int i = 0; i < numIterations; i++) {
            ArrayList<Integer> currentSolution = new ArrayList<>(bestSolution);
            ArrayList<Integer> candidate = getBestNeighbor(currentSolution, tabuList);
            if (candidate != null) {
                int candidateCost = costCalculator.calculateRouteCost(candidate);

                // Aspiration criterion: accept if better than the best known solution
                if (candidateCost < bestGlobalCost) {
                    bestSolution = new ArrayList<>(candidate);
                    bestGlobalCost = candidateCost;
                    System.out.println("new bestCost: " + bestGlobalCost + "  at iteration: " + i);

                }

            }
            // Update tabu list
            updateTabuList(tabuList);
        }

        // Log final
        Log(Iterations,bestGlobalCost, 0, bestGlobalCost);
        return bestSolution;
    }

    private void updateTabuList(int[][] tabuList) {
        for (int i = 0; i < tabuList.length; i++) {
            for (int j = 0; j < tabuList[i].length; j++) {
                if (tabuList[i][j] > 0) {
                    tabuList[i][j]--;
                }
            }
        }
    }

    public static List<Integer> generateRandomSolution(int numberOfCities) {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < numberOfCities; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);
        return solution;
    }

    private ArrayList<Integer> getBestNeighbor(ArrayList<Integer> currentSolution, int[][] tabuList) {

        ArrayList<Integer> bestNeighbor = null;
        int bestCost = Integer.MAX_VALUE;
        int neighbors_searched = 0;
        int bestFirstSwap = 0;
        int bestSecondSwap = 0;
        for (int i = 1; i < currentSolution.size() - 1; i++) {
            for (int j = i + 1; j < currentSolution.size(); j++) {

                    ArrayList<Integer> newSolution = new ArrayList<>(currentSolution);

                    // Perform 2-opt swap
                    Collections.reverse(newSolution.subList(i, j+1));
                    neighbors_searched += 1;
                    Iterations +=1;

                    int newCost = costCalculator.calculateRouteCost(newSolution);
                    if (((tabuList[i][j] == 0)&&(tabuList[j][i] == 0)) || (newCost < bestGlobalCost) ) {
                    //if ((tabuList[currentSolution.get(i - 1)][currentSolution.get(j)] == 0) || (newCost < bestGlobalCost) ) {

//                    if ((tabuList[i][j] == 0 || newCost < bestGlobalCost) && (tabuList[j][i] == 0 || newCost < bestGlobalCost)) {
                        // Perform 2-opt swap
 //                       Collections.reverse(newSolution.subList(i, j + 1)); // Make sure to include j in the swap
 //                       int newCost = costCalculator.calculateRouteCost(newSolution);
                    
                    // Check if this neighbor is better, not tabu, or meets the aspiration criterion
                    if (newCost < bestCost) {
                        bestNeighbor = newSolution;
                        bestCost = newCost;
                        bestFirstSwap = i;
                        bestSecondSwap = j;
                    }
                }

            }
        }

        // Update the tabu list to include the move made
        if (bestNeighbor != null) {

            tabuList[bestFirstSwap][bestSecondSwap] = tabuTenure; // tabuTenure is the duration for which the move is
                                                                  // considered tabu
            tabuList[bestSecondSwap][bestFirstSwap] = tabuTenure; // Symmetric update if the list is symmetric
        } else {
            System.out.println("No best neighbor found");

        }
        Log(Iterations,bestCost, neighbors_searched, bestGlobalCost);

        return bestNeighbor;
    }

    public static void Log(int iteration,int bestCost, int neighbors_searched, int bestGlobalCost) {
        logList.add(new LogItem(iteration,bestCost, neighbors_searched, bestGlobalCost));

    }

    @Override
    public String GetName() {
        return "Tabusökning";
    }

    public int getIteration(){return Iterations;}
    @Override
    public void SaveResult(String path, long executeTime) {
        // Save log to excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Log");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Iteration");
        headerRow.createCell(1).setCellValue("Best Cost");
        headerRow.createCell(2).setCellValue("Neighbors Searched");
        //headerRow.createCell(3).setCellValue("Best ");


        // Populate data rows
        for (int i = 0; i < logList.size(); i++) {
            LogItem logItem = logList.get(i);
            Row dataRow = sheet.createRow(i + 1);
            dataRow.createCell(0).setCellValue(logItem.iteration);
            dataRow.createCell(1).setCellValue(logItem.bestCost);
            dataRow.createCell(2).setCellValue(logItem.neighbors_searched);

        }

        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(path + ".xlsx")) {
            workbook.write(fileOut);
            System.out.println("Log saved to " + path + ".xlsx");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Failed to save the file: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.out.println("Error while closing the workbook: " + e.getMessage());
            }
        }

        System.out.println("Log saved to " + path);
    }
}
