package WarehouseCoOp;

class LogItem {
    public int iteration;
    public int bestCost;
    public int neighbors_searched;
    public int bestGlobalCost;

    public LogItem(int iteration,int bestCost, int neighbors_searched, int bestGlobalCost) {
        this.iteration = iteration;
        this.bestCost = bestCost;
        this.neighbors_searched = neighbors_searched;
        this.bestGlobalCost = bestGlobalCost;
    }
}
