package life;

import java.util.Random;

public class GameEngine {
    private boolean[][] currentGeneration;
    private boolean[][] newGeneration;
    private int numberOfGenerations;
    private int size;

    public GameEngine(int size, int seed) {
        Random random = new Random(seed);
        this.size = size;
        currentGeneration = new boolean[size][size];
        for (boolean[] b1 : currentGeneration) {
            for (int i = 0; i < size; i++) {
                b1[i] = random.nextBoolean();
            }
        }
        numberOfGenerations = 0;
        newGeneration = new boolean[size][size];
    }

    public boolean[][] getCurrentGeneration() {
        return currentGeneration;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }

    public int getSize() {
        return size;
    }

    void mutate() {
        newGeneration = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int count = countAliveNeighbours(i, j);

                if (count == 3) {
                    newGeneration[j][i] = true;
                } else if (count > 3 || count < 2) {
                    newGeneration[j][i] = false;
                } else {
                    newGeneration[j][i] = currentGeneration[j][i];
                }
            }
        }
        currentGeneration = newGeneration;
        numberOfGenerations++;
    }

    private int countAliveNeighbours(int i, int j) {
        int count = 0;
        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (k == 0 && l == 0) {
                    continue;
                }
                int x = j + k;
                if (x < 0) {
                    x = size - 1;
                }
                if (x > size - 1) {
                    x = 0;
                }
                int y = i + l;
                if (y < 0) {
                    y = size - 1;
                }
                if (y > size - 1) {
                    y = 0;
                }
                if (currentGeneration[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }


    long getAliveCells() {
        int count = 0;
        for (boolean[] cellRow : currentGeneration) {
            for (boolean cell : cellRow) {
                if (cell) {
                    count++;
                }
            }
        }
        return count;
    }
}
