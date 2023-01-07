import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.*;

public class IDSearch {

    public static List<Warehouse.Move> search(Warehouse warehouse) {
        System.out.println(warehouse);
        for (int depthLimit = 0; depthLimit < Integer.MAX_VALUE; depthLimit++) {
            System.out.println("Globina iskanja je " + depthLimit);

            Stack<Warehouse> stack = new Stack<>();
            Set<String> visited = new HashSet<>();

            stack.push(warehouse);
            visited.add(warehouse.toString());

            List<Warehouse.Move> foundMoves = new ArrayList<>();
            while (!stack.isEmpty()) {
                Warehouse currentState = stack.peek();
                // To mi ni cist jasno, zakaj je tole potrebno
                // if (curState.equals(warehouse))
                // mvs.setLength(0);
                if (currentState.isSolved()) {
                    System.out.println("Resitev IDDFS : " + currentState.getMoves().size() + " korakov");
                    foundMoves = currentState.getMoves();
                    System.out.println(foundMoves);
                    return foundMoves;
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

    public static boolean containsState(ArrayList<Warehouse> list, char[][] state) {
        for (Warehouse warehouse : list) {
            if (Arrays.deepEquals(warehouse.state, state)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        String initialFile = "primer4_zacetna.txt";
        String finalFile = "primer4_koncna.txt";
        char[][] initialState = Warehouse.readStateFromFile(initialFile);
        char[][] finalState = Warehouse.readStateFromFile(finalFile);
        Warehouse w = new Warehouse(initialState, finalState);
        search(w);
    }
}