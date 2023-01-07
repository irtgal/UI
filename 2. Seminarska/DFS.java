import java.util.*;

class DFS {

    static String initialFile = "primer2_zacetna.txt";
    static String finalFile = "primer2_koncna.txt";

    static Counter counter = new Counter();

    public static List<Warehouse.Move> search(Warehouse initial, Warehouse finalState) {
        int found = 0;
        counter.startTiming();
        List<Warehouse.Move> best = null;

        Stack<Warehouse> stack = new Stack<>();
        stack.push(initial);

        Set<String> explored = new HashSet<>();

        while (!stack.isEmpty()) {
            Warehouse current = stack.pop();
            explored.add(current.toString());
            counter.incrementExploredNodes();
            counter.checkMaxDepth(current.getNumberOfMoves());

            if (current.isSolved()) {
                if (best == null) {
                    best = current.getMoves();
                } else if (current.getNumberOfMoves() < best.size()) {
                    best = current.getMoves();
                }
                found++;
                continue;
            }

            for (int fromCol = 0; fromCol < current.numCols; fromCol++) {
                for (int toCol = 0; toCol < current.numCols; toCol++) {
                    if (!current.canMove(fromCol, toCol)) {
                        continue;
                    }

                    Warehouse next = current.deepClone();
                    next.move(fromCol, toCol);

                    if (!explored.contains(next.toString())) {
                        stack.push(next);
                        counter.checkMaxMemory(stack.size());
                    }
                }
            }
        }
        counter.stopTiming();
        return best;

    }

    public static void main(String[] args) throws Exception {
        char[][] initialState = Warehouse.readStateFromFile(initialFile);
        char[][] finalState = Warehouse.readStateFromFile(finalFile);

        Warehouse initial = new Warehouse(initialState, finalState);
        Warehouse finalWarehouse = new Warehouse(finalState, finalState);

        List<Warehouse.Move> moves = search(initial, finalWarehouse);
        Warehouse temp = new Warehouse(initialState, finalState);
        Helper.simulateMoves(temp, moves);
        System.out.print(DFS.counter);
    }
}
