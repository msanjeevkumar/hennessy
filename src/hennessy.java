import java.util.Random;
import java.util.Scanner;

public class hennessy {
    public static void main(String[] args) {
        char[][] grid = readGrid();

        while (findGameState(grid) > 2) {
            grid = findMakeStep(grid);
        }

        System.out.print(findGameState(grid) + "\n");
        printGrid(grid);
    }

    private static char[][] deepClone(char[][] arr) {
        char[][] res = arr.clone();
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i].clone();
        }
        return res;
    }

    private static char[][] readGrid() {
        Scanner s = new Scanner(System.in);
        String first = s.nextLine();
        char[][] grid = new char[first.length()][first.length()];

        for (int i = 0; i < grid.length; i++) {
            grid[0][i] = first.charAt(i);
        }

        for (int i = 1; i < grid.length; i++) {
            String temp = s.nextLine();
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = temp.charAt(j);
            }
        }

        return grid;
    }

    private static boolean testEquality(char[][] grid, char symbol, String direction, int i, int j) {

        if (grid[i][j] == symbol) {
            if (direction.equals("right")) {
                if (grid[i][j] == grid[i][j + 1] && grid[i][j] == grid[i][j + 2] && grid[i][j] == grid[i][j + 3] && grid[i][j] == grid[i][j + 4] && grid[i][j] == symbol) {
                    return true;
                }
            } else if (direction.equals("down")) {
                if (grid[i][j] == grid[i + 1][j] && grid[i][j] == grid[i + 2][j] && grid[i][j] == grid[i + 3][j] && grid[i][j] == grid[i + 4][j] && grid[i][j] == symbol) {
                    return true;
                }
            } else if (direction.equals("rightdiagonal")) {
                if (grid[i][j] == grid[i + 1][j + 1] && grid[i][j] == grid[i + 2][j + 2] && grid[i][j] == grid[i + 3][j + 3] && grid[i][j] == grid[i + 4][j + 4] && grid[i][j] == symbol) {
                    return true;
                }
            } else if (direction.equals("leftdiagonal")) {
                if (grid[i][j] == grid[i + 1][j - 1] && grid[i][j] == grid[i + 2][j - 2] && grid[i][j] == grid[i + 3][j - 3] && grid[i][j] == grid[i + 4][j - 4] && grid[i][j] == symbol) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int findGameState(char[][] grid) {
        // returns 0 if draw, 1 if crosses won, 2 if zeroes won, 3 if crosses go, 4 if zeroes go

        int crossesCount = 0;
        int zeroesCount = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 'x') {
                    crossesCount++;
                } else if (grid[i][j] == 'o') {
                    zeroesCount++;
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length - 4; j++) {
                if (testEquality(grid, 'x', "right", i, j)) {
                    return 1;
                } else if (testEquality(grid, 'o', "right", i, j)) {
                    return 2;
                }
            }
        }

        for (int i = 0; i < grid.length - 4; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (testEquality(grid, 'x', "down", i, j)) {
                    return 1;
                } else if (testEquality(grid, 'o', "down", i, j)) {
                    return 2;
                }
            }
        }

        for (int i = 0; i < grid.length - 4; i++) {
            for (int j = 0; j < grid.length - 4; j++) {
                if (testEquality(grid, 'x', "rightdiagonal", i, j)) {
                    return 1;
                } else if (testEquality(grid, 'o', "rightdiagonal", i, j)) {
                    return 2;
                }
            }
        }

        for (int i = 0; i < grid.length - 4; i++) {
            for (int j = 4; j < grid.length; j++) {
                if (testEquality(grid, 'x', "leftdiagonal", i, j)) {
                    return 1;
                } else if (testEquality(grid, 'o', "leftdiagonal", i, j)) {
                    return 2;
                }
            }
        }

        if (crossesCount + zeroesCount == grid.length * grid.length) {
            return 0;
        }

        if (crossesCount == zeroesCount) {
            return 3;
        } else {
            return 4;
        }
    }

    private static int[] naiveFindNextMove(char[][] grid) {
        // returns [-1, -1] if error or cannot find optimal, else returns optimal move

        int state = findGameState(grid);

        int[] crossesWinningMove = {-1, -1};
        int[] zeroesWinningMove = {-1, -1};

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == '.') {
                    char[][] tempGrid = deepClone(grid);
                    tempGrid[i][j] = 'x';
                    if (findGameState(tempGrid) == 1) {
                        crossesWinningMove[0] = i;
                        crossesWinningMove[1] = j;

                        if (state == 3) {
                            return crossesWinningMove;
                        }

                        tempGrid = deepClone(grid);
                        tempGrid[i][j] = 'o';
                        if (findGameState(tempGrid) == 2) {
                            zeroesWinningMove[0] = i;
                            zeroesWinningMove[1] = j;

                            if (state == 4) {
                                return zeroesWinningMove;
                            }
                        }
                    }
                }
            }
        }
        if (state == 3) {
            if (crossesWinningMove[0] != -1) {
                return crossesWinningMove;
            } else if (zeroesWinningMove[0] != -1) {
                return zeroesWinningMove;
            }
        } else if (state == 4) {
            if (zeroesWinningMove[0] != -1) {
                return zeroesWinningMove;
            } else if (crossesWinningMove[0] != -1) {
                return crossesWinningMove;
            }
        }

        return crossesWinningMove;
    }

    private static int horizontalPlayerProfit(char[][] grid, char symbol, int x, int y) {
        int score = 0;
        for (int i = y - 1; i >= 0; i--) {
            if (grid[x][i] == symbol) {
                score++;
            } else {
                break;
            }
        }
        for (int i = y + 1; i < grid.length; i++) {
            if (grid[x][i] == symbol) {
                score++;
            } else {
                break;
            }
        }
        return score;
    }

    private static int verticalPlayerProfit(char[][] grid, char symbol, int x, int y) {
        int score = 0;
        for (int i = x - 1; i >= 0; i--) {
            if (grid[i][y] == symbol) {
                score++;
            } else {
                break;
            }
        }

        for (int i = x + 1; i < grid.length; i++) {
            if (grid[i][y] == symbol) {
                score++;
            } else {
                break;
            }
        }
        return score;
    }

    private static int diagonalPlayerProfit(char[][] grid, char symbol, int x, int y) {
        int score = 0;

        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if (grid[i][j] == symbol) {
                score++;
            } else {
                break;
            }
        }

        for (int i = x + 1, j = y - 1; i < grid.length && j >= 0; i++, j--) {
            if (grid[i][j] == symbol) {
                score++;
            } else {
                break;
            }
        }

        for (int i = x - 1, j = y + 1; i >= 0 && j < grid.length; i--, j++) {
            if (grid[i][j] == symbol) {
                score++;
            } else {
                break;
            }
        }

        for (int i = x + 1, j = y + 1; i < grid.length && j < grid.length; i++, j++) {
            if (grid[i][j] == symbol) {
                score++;
            } else {
                break;
            }
        }
        return score;
    }

    private static int totalPlayerProfit(final char[][] grid, char symbol, int x, int y) {
        return horizontalPlayerProfit(grid, symbol, x, y) +
                +verticalPlayerProfit(grid, symbol, x, y) +
                +diagonalPlayerProfit(grid, symbol, x, y);
    }

    private static int[] smartFindNextMove(char[][] grid) {

        final double AGGRESSIVENESS = 1;
        final double RANDOMNESS = 1;

        //double[][] scores = new double[grid.length][grid.length];
        double score;
        int gameState = findGameState(grid);
        char playerSymbol;
        char enemySymbol;
        Random rand = new Random();

        if (gameState == 3) {
            playerSymbol = 'x';
            enemySymbol = 'o';
        } else {
            playerSymbol = 'o';
            enemySymbol = 'x';
        }

        int maxX = 0;
        int maxY = 0;
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == '.') {
                    score = totalPlayerProfit(grid, playerSymbol, i, j) + (totalPlayerProfit(grid, enemySymbol, i, j) * AGGRESSIVENESS) + (rand.nextDouble() * RANDOMNESS);
                } else {
                    continue;
                }
                if (score > max) {
                    max = score;
                    maxX = i;
                    maxY = j;
                }
            }
        }

        int[] result = new int[2];
        result[0] = maxX;
        result[1] = maxY;

        return result;
    }

    private static char[][] makeStep(char[][] grid, int x, int y) {
        char[][] newGrid = deepClone(grid);
        if (findGameState(grid) == 3) {
            newGrid[x][y] = 'x';
        } else {
            newGrid[x][y] = 'o';
        }
        return newGrid;
    }

    private static char[][] findMakeStep(char[][] grid) {
        int[] step = naiveFindNextMove(grid);
        if (step[0] != -1) {
            return makeStep(grid, step[0], step[1]);
        } else {
            step = smartFindNextMove(grid);
            return makeStep(grid, step[0], step[1]);
        }
    }

    private static void printGrid(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            if (i != grid.length - 1) {
                System.out.print("\n");
            }
        }
    }
}