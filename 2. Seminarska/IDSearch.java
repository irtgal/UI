import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.*;

public class IDSearch {

    static Counter counter = new Counter();

    static List<Warehouse.Move> search(Warehouse warehouse) {
        for (int depthLimit = 0; depthLimit < Integer.MAX_VALUE; depthLimit++) {
            System.out.println("Searching with depth limit " + depthLimit);

            Set<String> visited = new HashSet<>();
            List<Warehouse.Move> solution = depthLimitedSearch(warehouse, depthLimit, visited);
            if (!solution.isEmpty()) {
                return solution;
            }

            System.out.println("-----------------------------------------------------------");
        }
        return new ArrayList<Warehouse.Move>();
    }

    static List<Warehouse.Move> depthLimitedSearch(Warehouse warehouse, int depthLimit, Set<String> visited) {
        if (warehouse.isSolved()) {
            return warehouse.getMoves();
        }

        if (depthLimit <= 0) {
            return new ArrayList<Warehouse.Move>();
        }

        visited.add(warehouse.toString());

        for (int fromCol = 0; fromCol < warehouse.numCols; fromCol++) {
            for (int toCol = 0; toCol < warehouse.numCols; toCol++) {
                if (warehouse.canMove(fromCol, toCol)) {
                    Warehouse nextState = warehouse.deepClone();
                    nextState.move(fromCol, toCol);
                    if (!visited.contains(nextState.toString())) {
                        List<Warehouse.Move> solution = depthLimitedSearch(nextState, depthLimit - 1, visited);
                        if (!solution.isEmpty()) {
                            return solution;
                        }
                    }
                }
            }
        }
        return new ArrayList<Warehouse.Move>();
    }

    public static void main(String[] args) throws Exception {
        String initialFile = "primer4_zacetna.txt";
        String finalFile = "primer4_koncna.txt";
        char[][] initialState = Warehouse.readStateFromFile(initialFile);
        char[][] finalState = Warehouse.readStateFromFile(finalFile);
        Warehouse w = new Warehouse(initialState, finalState);

        List<Warehouse.Move> moves = search(w);
        Warehouse temp = new Warehouse(initialState, finalState);
        Helper.simulateMoves(temp, moves);
        System.out.print(DFS.counter);
    }
}