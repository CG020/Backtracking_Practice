/*
    File: DGraph.java
    Author: Camila Grubb
    Purpose: DGraph constructs the directed graph from the collected matrix
    data and its traversal algorithms
 */

import java.util.*;

public class DGraph {
    private class edgeCompare implements Comparator<Edge> {
        public int compare(Edge e1, Edge e2) {
            if(e1.label > e2.label){
                return 1;
            } else {
                return -1;
            }
        }
    }

    private class Edge {
        int label;
        double weight;

        Edge(int v, double w) {
            label = v;
            weight = w;
        }

        @Override
        public String toString() {
            return String.valueOf(weight) + ", " + String.valueOf(label);
        }

    }

    private ArrayList<LinkedList<Edge>> adjList = new ArrayList<>();
    private int numVertices;

    DGraph(int numVertices) {
        this.numVertices = numVertices;
        for (int i = 0; i < numVertices; i++) {
            adjList.add(new LinkedList<Edge>());
        }
    }


    void addEdge(int u, int v, double w) {
        adjList.get(u).add(new Edge(v, w));
        for (LinkedList<Edge> li : adjList) {
            Collections.sort(li, new edgeCompare());
        }
    }

    double round(double num) {
        return (int)(num * 10 + 0.5) / 10.0;
    }


    public List<Object> tspHeuristic(int start, List<Integer> path) {
        boolean[] visited = new boolean[numVertices];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;
        double tspCost = 0.0;
        int cur = 0;

        while (queue.size() != 0) {
            int u = queue.pollFirst();
            path.add(u+1);

            double minEdgeWeight = Double.MAX_VALUE;
            int minVertex = -1;
            for (Edge e : adjList.get(u)) {
                if (!visited[e.label-1]) {
                    if (e.weight < minEdgeWeight) {
                        minEdgeWeight = e.weight;
                        minVertex = e.label;
                    }
                }
            }
            if (minVertex != -1) {
                visited[minVertex-1] = true;
                tspCost += minEdgeWeight;
                queue.add(minVertex-1);
            }
            else {
                tspCost += adjList.get(u).get(0).weight;
            }
        }
        List<Object> retVal = new ArrayList<>();
        retVal.add(tspCost);
        retVal.add(path);
        return retVal;
    }

    public List<Object> tspHeuristic(ArrayList<LinkedList<Edge>> temp, int start, List<Integer> path) {
        boolean[] visited = new boolean[numVertices];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;
        double tspCost = 0.0;
        int cur = 0;

        while (queue.size() != 0) {
            int u = queue.pollFirst();
            path.add(u+1);

            double minEdgeWeight = Double.MAX_VALUE;
            int minVertex = -1;
            for (Edge e : temp.get(u-1)) {
                if (!visited[e.label-1]) {
                    if (e.weight < minEdgeWeight) {
                        minEdgeWeight = e.weight;
                        minVertex = e.label;
                    }
                }
            }
            if (minVertex <= 0) {
                visited[minVertex-1] = true;
                tspCost += minEdgeWeight;
                queue.add(minVertex-1);
            }
            else if (temp.get(u).size() > 0) {
                tspCost += temp.get(u).get(0).weight;
            } else {
                return null;
            }
        }
        List<Object> retVal = new ArrayList<>();
        retVal.add(tspCost);
        retVal.add(path);
        if (path.size() != numVertices) {
            return null;
        }
        return retVal;
    }


    public ArrayList<Integer> tspBacktracking() {
        tspCost = Double.MAX_VALUE;
        return tspBacktracking(1, 0.0, new ArrayList<>(), new ArrayList<>());
    }

    private static double tspCost = Double.MAX_VALUE;
    public double getTsp () {
        return tspCost;
    }


    private ArrayList<Integer> tspBacktracking(int u, double currCost, List<Integer> path, ArrayList<Integer> minPath) {
        if (path.size() == numVertices) {
            if (u == 1) {
                if (currCost < tspCost) {
                    tspCost = currCost;
                    minPath = new ArrayList<>(path);
                    return minPath;
                }
                return minPath;
            }
            return null;
        }
        else {
            for (Edge edge: adjList.get(u-1)) {
                int label = edge.label;
                double weight = edge.weight;
                if (!path.contains(label) || (path.size() == numVertices-1 && label == 1)) {
                    if (currCost+weight <= tspCost) {
                        path.add(u);
                        minPath = tspBacktracking(label, currCost + weight, path, minPath);
                        path.remove((Integer) (u));
                    }
                }
            }
        }
        return minPath;
    }

    public Object mine() {
        ArrayList<LinkedList<Edge>> temp = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            temp.add(new LinkedList<>());
        }
        ArrayList<LinkedList<Edge>> notAdded = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            notAdded.add(new LinkedList<>(adjList.get(i)));
        }

        return mine(1, notAdded, temp);
    }

    private Object mine(int u, ArrayList<LinkedList<Edge>> notAdded, ArrayList<LinkedList<Edge>> temp) {
        List<Object> cycle = tspHeuristic(temp, 1, new ArrayList<>());
        if (cycle != null) {
            return cycle.get(1);
        }
        Edge minEdge = findMin(notAdded, u);
        temp.get(u-1).add(minEdge);
        int nextNode = (u % numVertices) + 1;
        return mine(nextNode, notAdded, temp);
    }

    private Edge findMin(ArrayList<LinkedList<Edge>> notAdded, int u) {
        LinkedList<Edge> cur = notAdded.get(u-1);
        Edge e = new Edge(Integer.MAX_VALUE, Double.MAX_VALUE);
        for (Edge edge : cur) {
            if (edge.weight < e.weight) {
                e = edge;
            }
        }
        if (e.label != Integer.MAX_VALUE) {
            notAdded.remove(e);
            return e;
        }
        return null;
    }


    public String toString() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.print(i + 1 + "->");
            for (Edge x : adjList.get(i)) {
                System.out.print(x.label + " ");
            }
            System.out.println();
        }
        return "";
    }


}

