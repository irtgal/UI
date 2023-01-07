import java.io.IOException;
import java.util.Random;

public class SimulatedAnnealing {
    private static final double INITIAL_TEMPERATURE = 10000;
    private static final double MIN_TEMPERATURE = 0;
    private static final double COOLING_RATE = 0.99;

    public static void main(String[] args) throws IOException {
        String initialFile = "primer2_zacetna.txt";
        String finalFile = "primer2_koncna.txt";
        char[][] initialState = Warehouse.readStateFromFile(initialFile);
        char[][] finalState = Warehouse.readStateFromFile(finalFile);
        Warehouse w = new Warehouse(initialState,finalState);
        search(w);
    }
    private static double getAcceptanceProbability(double energy, double newEnergy, double temperature) {
        // if the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // if the new solution is worse, calculate the acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    public static void search(Warehouse warehouse) {
        // create a random number generator
        Random rng = new Random();

        // set the initial temperature
        double temperature = INITIAL_TEMPERATURE;

        // initialize the best solution to the current solution
        Warehouse bestSolution = warehouse.deepClone();
        double bestEnergy = bestSolution.calculateStateDistance();

        // initialize the current solution to the initial solution
        Warehouse currentSolution = warehouse.deepClone();
        double currentEnergy = currentSolution.calculateStateDistance();

        while (temperature > MIN_TEMPERATURE) {
            // make a random move
            int fromCol = rng.nextInt(warehouse.numCols);
            int toCol = rng.nextInt(warehouse.numCols);
            if (!currentSolution.canMove(fromCol, toCol)) {
                try {
                    currentSolution.move(fromCol, toCol);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                // calculate the energy of the new solution
                double newEnergy = currentSolution.calculateStateDistance();

                // decide whether to accept the new solution
                double acceptanceProbability = getAcceptanceProbability(currentEnergy, newEnergy, temperature);
                if (rng.nextDouble() < acceptanceProbability) {
                    // accept the new solution
                    currentEnergy = newEnergy;
                    if (newEnergy < bestEnergy) {
                        bestEnergy = newEnergy;
                        bestSolution = currentSolution.deepClone();
                    }
                } else {
                    // revert to the previous solution
                    currentSolution = bestSolution.deepClone();
                    currentEnergy = bestEnergy;
                }

                // cool the temperature
                temperature *= COOLING_RATE;
            }

            System.out.println("Best solution found: " + bestSolution);
        }
    }
}