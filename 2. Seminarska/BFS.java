import java.util.*;

class BFS {

    static String initialFile = "2. Seminarska/primer5_zacetna.txt";
    static String finalFile = "2. Seminarska/primer5_koncna.txt";

    public static Counter counter = new Counter();

    public static List<Warehouse.Move> search(Warehouse initial) {
        counter.startTiming();
        Queue<Warehouse> queue = new LinkedList<>();
        queue.add(initial);

        Set<String> explored = new HashSet<>();

        while (!queue.isEmpty()) {
            Warehouse current = queue.poll();
            counter.incrementExploredNodes();
            counter.checkMaxDepth(current.getNumberOfMoves());

            if (current.isSolved()) {
                counter.stopTiming();
                return current.getMoves();
            }

            explored.add(current.toString());

            for (int fromCol = 0; fromCol < current.numCols; fromCol++) {
                for (int toCol = 0; toCol < current.numCols; toCol++) {
                    if (!current.canMove(fromCol, toCol)) {
                        continue;
                    }
                    Warehouse next = current.deepClone();
                    Warehouse.Move move = next.move(fromCol, toCol);
                    if (!explored.contains(next.toString())) {
                        queue.add(next);
                        counter.checkMaxMemory(queue.size());
                    }
                }
            }
        }
        counter.stopTiming();
        return new ArrayList<>();
    }

    public static void main(String[] args) throws Exception {
        char[][] initialState = Warehouse.readStateFromFile(initialFile);
        char[][] finalState = Warehouse.readStateFromFile(finalFile);

        Warehouse initial = new Warehouse(initialState, finalState);

        List<Warehouse.Move> moves = search(initial);
        Warehouse temp = new Warehouse(initialState, finalState);
        Helper.simulateMoves(temp, moves);
        System.out.print(BFS.counter);
    }

}
