package WarehouseCoOp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class OptCreate {

    private List<Vertex> nodes;
    private List<Edge> edges;
    private List<Vertex> nodesTSP;
    private List<Edge> edgesTSP;

    int GridSizeX;
    int GridSizeY;
    private  ArrayList<Integer> obsNodes  =new ArrayList<>();
    private ArrayList<Integer> shelfNodes  =new ArrayList<>();
    private ArrayList<String> Directions = new ArrayList<>();
    private ArrayList<Integer> linkStarts = new ArrayList<>();
    private ArrayList<Integer> linkEnds = new ArrayList<>();
    public ArrayList<Integer> shelfWeights = new ArrayList<>();


    private Layout layout;
    private HashMap<String, Vertex> all_vertex = new HashMap<String, Vertex>();
    private int[][] Distmatrix;
    private DijkstraAlgorithm dijkstra;

    public OptCreate(Layout layout) {
        this.layout=layout;
        getShelfs();

    }
    public void getShelfs(){
        GridSizeX=layout.getFigureSize().getX()/layout.pointSize+4;
        //
        GridSizeY=layout.getFigureSize().getY()/layout.pointSize+4;

        for(int i=0;i<layout.shelfs.size();i++){

            if(layout.shelfs.get(i).isShelf){
                shelfNodes.add((layout.shelfs.get(i).x/layout.pointSize+(layout.shelfs.get(i).y/layout.pointSize)*GridSizeX) +1);
                Directions.add(""+layout.shelfs.get(i).direction);
                shelfWeights.add(layout.shelfs.get(i).weight);

            }
            else{
                obsNodes.add(layout.shelfs.get(i).x/layout.pointSize+(layout.shelfs.get(i).y/layout.pointSize)*GridSizeX +1);;
            }

        }

    }

    public boolean contains(ArrayList<Integer> array, int target) {
        for (int element : array) {
            if (element == target) {
                return true;
            }
        }
        return false;
    }

    public int getNodeIndex(int x, int y) {
        return x / 30 + (y / 30) * GridSizeX;

    }

    public int getNodeIndexFromGrid(int x, int y) {
        int node = GridSizeX * y + x;
        return node;

    }
    public int getNodeNumber(int x, int y) {
        return x + y * GridSizeX +1;

    }

    public int getX(int node) {
        return (node-1) % GridSizeX;

    }

    public int getY(int node) {
        return (node-1) / GridSizeX;

    }

    public String getNodeId(int x, int y, String dir) {
        return ((getNodeIndexFromGrid(x, y) + 1) + dir);
    }

    public boolean checkXY(int x, int y) {
        if (x < 0 || x >= GridSizeX) {
            return false;
        }
        if (y < 0 || y >= GridSizeY) {
            return false;
        }
        return true;
    }

    public int[][] getDistmatrix() {
        return Distmatrix;
    }

    public void createPlan() {
        this.nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        // Set up network
        String[] Dir = { "N", "E", "S", "W" };

        for (int i = 1; i <= GridSizeX * GridSizeY; i++) {
            // System.out.println(" Adding node: " + i);
            for (String D : Dir) {
                Vertex location = new Vertex("" + i + D, "Nod #" + i + D);
                all_vertex.put("" + i + D, location);

                this.nodes.add(location);

                // System.out.println("Adding node: " + location );
            }
        }
        // Obstacle nodes





        // Plocknings noder
        ArrayList<String> loadNodes = new ArrayList<>();
        for (int n = 0; n < shelfNodes.size(); n++) {

            int x = getX(shelfNodes.get(n));
            int y = getY(shelfNodes.get(n));
            String loaddir = "";
            switch (Directions.get(n)) {
                case "N":
                    y = y + 1;
                    loaddir = "S";
                    break;
                case "S":
                    y = y - 1;
                    loaddir = "N";
                    break;
                case "E":
                    x = x + 1;
                    loaddir = "W";
                    break;
                case "W":
                    x = x - 1;
                    loaddir = "E";
                    break;

            }
            if (checkXY(x, y)) {
                loadNodes.add(getNodeId(x, y, loaddir));
                //System.out.println("load node: " + loadNodes[n]);
            } else {
                System.out.println("Node for loadning not allowed" + (shelfNodes.get(n) + 1));
            }

        }
        int arcmove_cost = 1; // Kostnad att åka till nästa nod
        int arcturn_cost = 1; // Kostnad att byta riktning
        int arc = 0;
        for (int y = 0; y < GridSizeY; y++) {
            for (int x = 0; x < GridSizeX; x++) {
                arc += 3;

                int source = getNodeNumber(x, y);
                if (contains(obsNodes, source) || contains(shelfNodes, source)) {
                    continue;
                }

                String dest_right = "";
                String dest_left = "";
                for (String D : Dir) {
                    int dest_x = x;
                    int dest_y = y;
                    switch (D) {
                        case "N":
                            dest_y = y + 1;
                            dest_right = "E";
                            dest_left = "W";
                            break;
                        case "S":
                            dest_y = y - 1;
                            dest_right = "W";
                            dest_left = "E";
                            break;
                        case "E":
                            dest_x = x + 1;

                            dest_right = "N";
                            dest_left = "S";
                            break;
                        case "W":
                            dest_x = x - 1;
                            dest_right = "S";
                            dest_left = "N";
                            break;
                        default:
                            break;
                    }

                    if (checkXY(dest_x, dest_y)) {
                        int destination = getNodeNumber(dest_x, dest_y);
                        if (!contains(obsNodes, destination) && !contains(shelfNodes, destination)) {

                            Edge arcmove = new Edge(arc + D, all_vertex.get(source + D),
                                    all_vertex.get(getNodeId(dest_x, dest_y, D)), arcmove_cost, Robot_Command.MOVE);
                            // System.out.println("Added arc: "+arcmove);
                            edges.add(arcmove);
                        }
                    }
                    Edge arcleft = new Edge((arc + 1) + D, all_vertex.get(source + D),
                            all_vertex.get(getNodeId(x, y, dest_left)), arcturn_cost, Robot_Command.TURN_LEFT);
                    Edge arcright = new Edge((arc + 2) + D, all_vertex.get(source + D),
                            all_vertex.get(getNodeId(x, y, dest_right)), arcturn_cost, Robot_Command.TURN_RIGHT);

                    edges.add(arcleft);
                    edges.add(arcright);
                }
            }
        }

        Graph graph = new Graph(this.nodes, edges);
        dijkstra = new DijkstraAlgorithm(graph);

        int startNode = GridSizeX/2;

        LinkedList<Vertex> path = null;
        // Samla alla noder och bågar som skall användas för Heruistikerna, alltså de
        // noder och bågar som är de kortaste vägarna mellan de noder som ska besökas.
        nodesTSP = new ArrayList<>();
        edgesTSP = new ArrayList<>();
        nodesTSP.add(new Vertex("" + startNode + "E", "Nod #" + startNode + "E"));
        for (int n = 0; n < loadNodes.size(); n++) {

            Vertex node = new Vertex(loadNodes.get(n), "Nod #" + (loadNodes.get(n)));
            nodesTSP.add(node);
            System.out.println("Adding node: " + node);

        }


        Distmatrix = new int[nodesTSP.size()][nodesTSP.size()];
        // for(int i=0;i<nodesTSP.size();i++){}
        // System.out.println("nod nummer: "+nodesTSP.get(i).getNum());}

        for (int SourceNode = 0; SourceNode < nodesTSP.size(); SourceNode++) {
            for (int DestinationNode = 0; DestinationNode < nodesTSP.size(); DestinationNode++) {
                if (SourceNode == DestinationNode) {
                    continue;
                }
                dijkstra.execute(nodesTSP.get(SourceNode));
                path = dijkstra.getPath(nodesTSP.get(DestinationNode));
                // Edge laneforward = new Edge("", nodesTSP.get(SourceNode),
                // nodesTSP.get(DestinationNode), path.size()-1);
                Distmatrix[SourceNode][DestinationNode] = path.size() - 1;
                // System.out.println("utskrift av path"+path);
                // System.out.println("Startnode: " + nodesTSP.get(SourceNode)+ "Slutnode: "+
                // nodesTSP.get(DestinationNode));

                // edgesTSP.add(laneforward);
                // System.out.println("Added arc from: " + nodesTSP.get(SourceNode) + " to: " +
                // nodesTSP.get(DestinationNode) + " Cost: " + (path.size()-1));

            }
        }
    }
    public String getNodeNameTsp(int SoureNodeTsp){
        return nodesTSP.get(SoureNodeTsp).getName();
    }
    public int getNodeFromId(String id) {
        return Integer.parseInt(id.replaceAll("\\D+", ""));
    }

    public DrawGroup getLink(int SourceNode, int DestinationNode) {

        DrawGroup dg = new DrawGroup();
        if (SourceNode == DestinationNode) {
            return dg;
        }
        LinkedList<Vertex> path;
        //System.err.println("Start:" + nodesTSP.get(SourceNode));
        //System.err.println("Stop:" + nodesTSP.get(DestinationNode));

        dijkstra.execute(nodesTSP.get(SourceNode));
        path = dijkstra.getPath(nodesTSP.get(DestinationNode));
        int firstNode = getNodeFromId(path.get(0).getId());

        //System.out.println("Start:" + firstNode +" at "+ getX(firstNode) + " " + getY(firstNode) + " ");
        Point Previouspoint =new Point(getX(firstNode) * 30, getY(firstNode) * 30);
        Point Newpoint =null;
        for (int i = 1; i < path.size(); i++) {
            if (dijkstra.getEdge(path.get(i - 1), path.get(i)).getCommand().equals(Robot_Command.MOVE)) {
                int nextNode = getNodeFromId(path.get(i).getId());
                int x = getX(nextNode);
                int y = getY(nextNode);

                Newpoint =new Point(x * 30, (y) * 30);
                if(Previouspoint!=null){
                    dg.AddItem(new Line(Previouspoint, Newpoint, 0));
                }
                Previouspoint = Newpoint;




            }



        }
        dg.AddItem(new Pickpoint(Previouspoint, 0));


        return dg;

    }

    // public ArrayList<Point> getLinkEnds(int SourceNode, int DestinationNode) {
    // LinkedList<Vertex> path;
    // dijkstra.execute(nodesTSP.get(SourceNode));
    // path = dijkstra.getPath(nodesTSP.get(DestinationNode));
    //
    // }

    public void TraceRoute(ArrayList<Integer> PathList) {
        LinkedList<Vertex> path = null;
        System.out.println("Här börjar listan");
        PathList.forEach(System.out::println);
        // RouteList.forEach(System.out::println);
        System.out.println("Här slutar listan");

        int pathIndex = 0;


        int LengthofPath = 0;

        for (int k = 1; k < PathList.size(); k++) {

            dijkstra.execute(all_vertex.get(PathList.get(k - 1))); // Förra hyllan
            path = dijkstra.getPath(all_vertex.get(PathList.get(k))); // Nästa hylla

            // Get shortest path (node ids);
            System.out.println("Shortest path:" + path);
            LengthofPath += path.size() - 1;

            for (int t = 0; t < path.size() - 1; t++) {
                linkStarts.set(t + pathIndex,Integer.parseInt(path.get(t).getId().replaceAll("\\D+", "")));
                linkEnds.set(t + pathIndex,Integer.parseInt(path.get(t + 1).getId().replaceAll("\\D+", "")));
                System.out.println(linkStarts.get(t + pathIndex) + " " + linkEnds.get(t + pathIndex));

            }
            pathIndex = pathIndex + path.size();



        }
        System.out.println("length of the path " + LengthofPath);

    }

}
