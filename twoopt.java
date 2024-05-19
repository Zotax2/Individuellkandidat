package WarehouseCoOp;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class twoopt implements TSPInterface {

    private int[][] distMatrix;
    private int bestTourLength;
    public static ArrayList<LogItem> logList;
    private int Iterations;

    public twoopt(int[][] distMatrix) {
        this.distMatrix = distMatrix;
        logList = new ArrayList<LogItem>();
    }

    public boolean UsesInitalsolution() {
        return true;
    }

    public void Reinit() {
        logList = new ArrayList<LogItem>(+2);
        Iterations = 0;
    }

    public ArrayList<Integer> execute(ArrayList<Integer> routeNodes, RouteCostCalculator routeCost) {
        int n = routeNodes.size();

        ArrayList<Integer> bestTour = new ArrayList<>(routeNodes);
        bestTourLength = routeCost.calculateRouteCost(bestTour);
        Log(0, bestTourLength, 0);

        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 1; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    ArrayList<Integer> newTour = twoOptSwap(bestTour, i, j);
                    int newTourLength = routeCost.calculateRouteCost(newTour);
                    if (newTourLength < bestTourLength) {
                        bestTourLength = newTourLength;
                        bestTour = newTour;
                        improvement = true;
                    }
                    Iterations += 1;
                    Log(Iterations,bestTourLength, 0);
                    
                }
            }
        }
        return bestTour;
    }
    // 2-opt byte
    private ArrayList<Integer> twoOptSwap(ArrayList<Integer> tour, int i, int j) {
        ArrayList<Integer> tempTour = new ArrayList<Integer>(tour);
        while (i < j) {
            int temp = tempTour.get(i);
            tempTour.set(i, tempTour.get(j));
            tempTour.set(j, temp);
            i++;
            j--;
        }
        return tempTour;
    }



    @Override
    public String GetName() {
        return "2-Opt";
    }

    public static void Log(int iterations, int bestCost, int neighbors_searched) {
        logList.add(new LogItem(iterations, bestCost, neighbors_searched, 0));

    }

    @Override
    public int getIteration() {
        return Iterations;
    }

    public void SaveResult(String path, long executeTime) {
        // Save log to excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Log");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Iteration");
        headerRow.createCell(1).setCellValue("Best Cost");
        headerRow.createCell(2).setCellValue("Neighbors Searched");

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