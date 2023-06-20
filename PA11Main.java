/*
    File: PA11Main.java
    Author: Camila Grubb
    Purpose: The main file where the DGraph class is called and command line arguments
    are interpreted.
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;

public class

PA11Main {
    public static void main(String[] args) {
        DGraph dg = null;
        try {
            dg = readFile(args[0]);
        } catch (IOException e) {
            System.out.println("Error: File not found");
        }

        approach(args[1], dg);
    }

    public static DGraph readFile(String file) throws IOException {
        File newFile = new File(file);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(newFile));
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
        }

        boolean isComment = true;
        String line = br.readLine();

        while (isComment) {
            line = br.readLine();
            isComment = line.startsWith("%");
        }
        String[] str = line.split(" ");

        int rows = Integer.parseInt(str[0]);
        DGraph dg = new DGraph(rows);
        while (true) {
            line = br.readLine();
            if (line == null)
                break;
            str = line.split(" ");
            Double[] numLine = new Double[3];

            int i = 0;
            for (String s : str) {
                if (!s.equals("")) {
                    numLine[i] = Double.valueOf(s);
                    i++;
                }
            }
            dg.addEdge((numLine[0].intValue())-1, (numLine[1].intValue()),
                    (numLine[2]));

        }
        //System.out.println(dg);
        return dg;
    }

    public static void approach(String method, DGraph dg) {
        List<Integer> tspCycle = new ArrayList<>();
        if (method.equals("HEURISTIC")) {
            List<Object> heu = dg.tspHeuristic(0, tspCycle);
            System.out.println("cost = " + dg.round((double)heu.get(0)) + ", visitOrder = " + heu.get(1));
        }
        else if (method.equals("BACKTRACK")) {
            ArrayList<Integer> bktk = dg.tspBacktracking();
            System.out.println("cost = " + dg.round(dg.getTsp()) + ", visitOrder = " + bktk);
        }
        else if (method.equals("MINE")){
           //System.out.println(dg.mine());
        }
        else if (method.equals("TIME")) {
            long startTime = System.nanoTime();
            List<Object> h = dg.tspHeuristic(0, tspCycle);
            long endTime = System.nanoTime();
            float duration = (endTime - startTime)/1000000.0f;

            System.out.println("heuristic: cost = " + h.get(0) + ", " + duration + " milliseconds");

            startTime = System.nanoTime();
            ArrayList<Integer> b = dg.tspBacktracking();
            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000.0f;
            System.out.println("backtrack: cost = " + dg.getTsp() + ", " + duration + " milliseconds");
        }
    }
}