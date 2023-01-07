import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.*;

public class IDSearch {

    static Counter counter = new Counter();

    public static List<Warehouse.Move> search(Warehouse warehouse) {
        counter.startTiming();

        for (int depthLimit = 0; depthLimit < Integer.MAX_VALUE; depthLimit++) {
            System.out.println("Globina iskanja je " + depthLimit);

            Stack<Warehouse> stack = new Stack<>();
            Set<String> visited = new HashSet<>();

            stack.push(warehouse);
            visited.add(warehouse.toString());

            while (!stack.isEmpty()) {
                Warehouse currentState = stack.peek();
                // To mi ni cist jasno, zakaj je tole potrebno
                // if (curState.equals(warehouse))
                // mvs.setLength(0);
                if (currentState.isSolved()) {
                    counter.stopTiming();
                    return currentState.getMoves();
                }

                boolean found = false;
                if (currentState.getMoves().size() <= depthLimit) {
                    // najdi neobiskano naslednje stanje
                    for (int fromCol = 0; fromCol < currentState.numCols; fromCol++) {
                        for (int toCol = 0; toCol < currentState.numCols; toCol++) {
                            if (currentState.canMove(fromCol, toCol)) {
                                Warehouse nextState = currentState.deepClone();
                                String mv = nextState.move(fromCol, toCol).toString();
                                if (!visited.contains(nextState.toString())) {
                                    stack.push(nextState);
                                    visited.add(nextState.toString());
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                }

                if (!found) {
                    stack.pop();
                }
            }

            System.out.println("-----------------------------------------------------------");
        }
        return new ArrayList<Warehouse.Move>();
    }

    public static void main(String[] args) throws Exception {
        String initialFile = "primer5_zacetna.txt";
        String finalFile = "primer5_koncna.txt";
        char[][] initialState = Warehouse.readStateFromFile(initialFile);
        char[][] finalState = Warehouse.readStateFromFile(finalFile);
        Warehouse w = new Warehouse(initialState, finalState);

        List<Warehouse.Move> moves = search(w);
        Warehouse temp = new Warehouse(initialState, finalState);
        Helper.simulateMoves(temp, moves);
        System.out.print(DFS.counter);
    }
}