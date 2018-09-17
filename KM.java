package KM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

public class KM {
    int size;           // Number of rows/cols
    int[][] weights;    // Edge weights s.t. weights[i][j] is the
                        //       weight of the edge x=i -> y=j
    int[] xLabel;       // Labels of X/Y nodes s.t. xLabel[i] is the
    int[] yLabel;       //       ith node label in X
    HashSet<Integer> S; // Contains explored X nodes in each iteration
    HashSet<Integer> T; // Contains explored Y nodes in each iteration
    boolean[][] M;      // Matching s.t. M[i][j] == true means that x=i is 
                        //      matched to y=j
    boolean[][] E;      // E[i][j] == true means that:
                        //      xLabel[i] + yLabel[j] == weights[i][j]
 
    public KM(int[][] wts) {
        this.weights = wts;
        this.size = this.weights.length;
        this.M = new boolean[this.size][this.size];
        this.E = new boolean[this.size][this.size];
        
        // Start with basic feasible labels:
        //   all b=0
        //   all g=max(weights[i])
        this.xLabel = new int[size];
        this.yLabel = new int[size];
        for (int i = 0; i < size; i++) {
            this.yLabel[i] = 0;
            this.xLabel[i] = 0;
            int maxY = 0;
            for (int j = 0; j < size; j++) {
                if (weights[i][j] > this.xLabel[i]) {
                    this.xLabel[i] = weights[i][j];
                    maxY = j;
                }
                M[i][j] = false;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                E[i][j] = (xLabel[i] + yLabel[j] == weights[i][j]);
            }
        }
    }
    
    // Parses input file where 1st line is the # of rows/cols
    // The rest of the lines are space separated weights
    static int[][] parseInputFile(String fileName) throws Exception {
        BufferedReader buffer = new BufferedReader(new FileReader(fileName));
        String line;
        int size = Integer.parseInt(buffer.readLine().trim().split("\\s+")[0]);
        
        int[][] graph = new int[size][size];
        int row = 0;
        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split("\\s+");
            for (int col = 0; col < size; col++) {
                graph[row][col] = Integer.parseInt(vals[col]);
            }
            row++;
        }

        return graph;
    }
    
    private boolean isFreeInX(int i){
        boolean free = true;
        for (int j=0; j<size; j++) {
            free = free && !M[i][j];
        }
        return free;
    }
    
    private boolean isFreeInY(int i){
        boolean free = true;
        for (int j=0; j<size; j++) {
            free = free && !M[j][i];
        }
        return free;
    }
    
    private HashSet<Integer> neighbors(HashSet<Integer> nodes) {
        HashSet<Integer> n = new HashSet<>();
        for (int u : nodes) {
            for (int v=0; v<size; v++) {
                if (E[u][v]) {
                    n.add(v);
                }
            }
        }
        return n;
    }
    
    // Calculates delta & updates labels
    private void updateLabels() {
        int delta = Integer.MAX_VALUE;
        int newDelta;
        for (int x: S) {
            for (int y = 0; y < size; y++) {
                if (!T.contains(y)) {
                    newDelta = xLabel[x] + yLabel[y] - weights[x][y];
                    if (newDelta < delta) {
                        delta = newDelta;
                    }
                }
            }
        }
        for (int i: S) {
            xLabel[i] = xLabel[i] - delta;
        }
        for (int j: T) {
            yLabel[j] = yLabel[j] + delta;
        }
        // update E
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                E[i][j] = (xLabel[i] + yLabel[j] == weights[i][j]);
            }
        }
        
    }
    
    // Finds & flips alternating path from x=u -> y=v
    private void augment(int u, int v) {
        // Alt tree rooted at u, extends to v, travels through all T & S
        HashSet<Integer> localS = new HashSet<>();
        HashSet<Integer> localT = new HashSet<>();
        boolean[][] explored = new boolean[size][size];
        Stack<Integer> path = new Stack<>();
        for (int s: this.S) {
            localS.add(s);
        }
        for (int t: this.T) {
            localT.add(t);
        }
        localT.add(v);
        int xpt = u;
        int ypt = 0;
        path.add(xpt);
        localS.remove(xpt);
        boolean xTurn = true;
        boolean found;
        while (!localS.isEmpty() || !localT.isEmpty()) {
            if (xTurn) {
                // find ypt st xpt -> ypt exists in E && ypt in T
                found = false;
                for (int y: localT) {
                    if (E[xpt][y] && !explored[xpt][y] && !M[xpt][y]) {
                        found = true;
                        ypt = y;
                        path.push(ypt);
                        localT.remove(ypt);
                        explored[xpt][ypt] = true;
                        xTurn = false;
                        break;
                    }
                }
                if (!found) {
                    // No path found -- backtrack
                    xpt = path.pop();
                    localS.add(xpt);
                    ypt = path.peek();
                    xTurn = false;
                } else if (ypt == v) {
                    // We found alternating path!
                    break;
                }
            } else {
                // find xpt st ypt -> xpt exists in E && xpt in S
                found = false;
                for (int x: localS) {
                    if (E[x][ypt] && !explored[x][ypt] && M[x][ypt]) {
                        found = true;
                        xpt = x;
                        path.push(xpt);
                        localS.remove(xpt);
                        explored[xpt][ypt] = true;
                        xTurn = true;
                        break;
                    }
                }
                if (!found) {
                    // No path found -- backtrack
                    ypt = path.pop();
                    localT.add(ypt);
                    xpt = path.peek();
                    xTurn = true;
                }
            }
        }
        // now path contains all hops from x=u to y=ypt.
        // We need to extend ypt->v, then trace back through path & flip
        xTurn = false;
        while (path.size() > 1) {
            if (xTurn) {
                xpt = path.pop();
                ypt = path.peek();
            } else {
                ypt = path.pop();
                xpt = path.peek();
            }
            M[xpt][ypt] = !M[xpt][ypt];
            xTurn = !xTurn;
        }
    }
    
    // Calculates size of matching
    public int matchingSize() {
        int count = 0;
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (this.M[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }
    
    // Prints formatted solution
    public void printSolution() {
        int totalWt = 0;
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < size; y++) {
                if (M[x][y]) {
                    totalWt = totalWt + weights[x][y];
                }
            }
        }
        System.out.println(totalWt + "   // weight of the matching");
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < size; y++) {
                if (M[x][y]) {
                    // Add 1 for 1 indexing instead of 0 indexing
                    System.out.println("(" + (x+1) + "," + (y+1) + ")");
                }
            }
        }
    }
    
    // Prints out the value for all algorithm vars for Debug
    public void printDebugInfo() {
        int cnt = 0;
        System.out.println("E=");
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < size; y++) {
                if (E[x][y]) {
                    System.out.print("(" + x + "," + y + "),");
                    cnt++;
                }
                if (cnt > 7) {
                    System.out.println();
                    cnt = 0;
                }
            }
        }
        System.out.println("");
        System.out.println("M=");
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < size; y++) {
                if (M[x][y]) {
                    System.out.println("(" + x + "," + y + ")");
                }
            }
        }
        System.out.println("xLabels=" + Arrays.toString(xLabel));
        System.out.println("yLabels=" + Arrays.toString(yLabel));
        System.out.println("S=" + S.toString());
        System.out.println("T=" + T.toString());
        System.out.println("\n");
    }
    
    public void findMaxMatching() {
        S = new HashSet<>();
        T = new HashSet<>();
        boolean nextFree = true;
        int u = 0;
        while (matchingSize() < size) {
            if (nextFree) {
                // find free vertex in g
                u = 0;
                while (u < size) {
                    if (isFreeInX(u)) {
                        break;
                    }
                    u++;
                }
                // u is free
                S.clear();
                S.add(u);
                T.clear();
                nextFree = false;
            }
            // If Nl(S) = T : Update Labels
            if (neighbors(S).equals(T)) {
                updateLabels();
            } else {
                // pick y in (neighbors(S) - T)
                HashSet<Integer> pickY = neighbors(S);
                for (int t: T) {
                    pickY.remove(t);
                }
                int y = pickY.iterator().next();
                //If y is free, Augment M
                if (isFreeInY(y)) {
                    // x=u −> y=y is augmenting path
                    augment(u, y);
                    nextFree = true;
                } else {
                    //Else (y matched to z)
                    int z = 0;
                    while (z < size) {
                        if (!M[z][y]) {
                            z++;
                        } else {
                            break;
                        }
                    }
                    S.add(z);
                    T.add(y);
                }
            }
        }
    }

    /**
     * Input File Name argument
     */
    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();
        int[][] weights = parseInputFile(args[0]);
        KM km = new KM(weights);
        km.findMaxMatching();
        km.printSolution();
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total time taken for KM is " + totalTime);
    }
    
}
