import java.util.*;

public class TicTacToeBot {

    public static void main(final String[] args) {
        final Scanner user = new Scanner(System.in);
        boolean gameOver = false;
        int[][] board = new int[3][3];
        int[] row = new int[3];
        int[] col = new int[3];
        int[] dig = new int[2];
        while (!gameOver) {
            printCurrentBoard(board);
            int spot = promptUser(user, board);
            gameOver = updateUserBoard(board, spot, row, col, dig);
            if (gameOver) { 
                System.out.println();
                System.out.println("Congrats, you won!");
                printCurrentBoard(board);
                break;
            }
            String outcome = botMove(board, row, col, dig);
            if (outcome.equals("win")) {
                System.out.println();
                System.out.println("Sorry, you lost. :(");
                printCurrentBoard(board);
                break;
            } else if (outcome.equals("draw")) {
                System.out.println();
                System.out.println("Tie Game");
                printCurrentBoard(board);
                break;
            }
        }

    }

    public static int promptUser(Scanner user, int[][] board) {
        int space = 0;
        while (true) {
            System.out.print("Which space do you want? ");
            space = user.nextInt();

            // check that user selected space is valid
            int row = (space-1) / 3;
            int column = (space-1) % 3;
            if (space < 0 || space > 9) {
                System.out.println("That number is not in range of the board");
            } else if (board[row][column] < 0) {
                System.out.println("That space is not avaliable, pick another one.");
            } else {
                break;
            }
        }
        return space;
    }

    public static void printCurrentBoard(final int[][] board) {
        int count = 1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == -1) {
                    System.out.print("O");
                } else if (board[i][j] == 1) {
                    System.out.print("X");
                } else {
                    System.out.print(count);
                }
                if (j < board[i].length - 1) {
                    System.out.print(" | ");
                }
                count++;
            }
            System.out.println();
            if (i < board.length - 1) {
                System.out.println("---------");
            }
        }
        System.out.println();
    }

    public static boolean updateUserBoard(int[][] board, int space, int[] row, int[] col, int[] dig){
        // update users space
        board[(space-1)/3][(space-1)%3] = -1;
        row[(space-1)/3] -= 1;
        col[(space-1)%3] -= 1;
        if ((space-1)/3 == (space-1)%3) {
            dig[0] -=1;
        }
        if ((space-1)/3 + (space-1)%3 == 2) {
            dig[1] -= 1;
        }
        if(row[(space-1)/3] == -3 || col[(space-1)%3] == -3||
           dig[1] == -3 || dig[0] == -3) {
             return true;
        }
        return false;
    }

    public static boolean updateBotBoard(int[][] board, int[] move, int[] row, int[] col, int[] dig){
        // update users space
        int x = move[0];
        int y = move[1];
        board[x][y] = 1;
        row[x] += 1;
        col[y] += 1;
        if (x == y) {
            dig[0] +=1;
        }
        if (x + y == 2) {
            dig[1] += 1;
        }
        if(row[x] == 3 || col[y] == 3||
           dig[1] == -3 || dig[0] == 3) {
             return true;
        }
        return false;
    }

    public static String botMove(int[][] board, int[] row, int[] col, int[] dig) {
        List<int[]> block = new ArrayList<>();
        List<int[]> oppositeCorner = new ArrayList<>();
        List<int[]> emptyCorner = new ArrayList<>();
        List<int[]> emptySide = new ArrayList<>();
        boolean center = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1 || board[i][j] == -1) {
                    continue;
                }
                int[] position = { i, j };
                if (row[i] == 2 || col[j] == 2) {
                    board[i][j] = 1;
                    return "win";
                } else if (i == j && dig[0] == 2) {
                    board[i][j] = 1;
                    return "win";
                } else if (i + j == 2 && dig[1] == 2) {
                    board[i][j] = 1;
                    return "win";
                } else if (row[i] == -2 || col[j] == -2) {
                    block.add(position);
                    continue;
                } else if (i == j && dig[0] == 2) {
                    block.add(position);
                    continue;
                } else if (i + j == 2 && dig[1] == 2) {
                    block.add(position);
                    continue;
                } else if (i == 1 && j == 1) {
                    center = true;
                } else if (i == 0 && j == 0) {
                    if (board[2][2] == -1) {
                        oppositeCorner.add(position);
                    } else {
                        emptyCorner.add(position);
                    }
                } else if (i == 2 && j == 0) {
                    if (board[0][2] == -1) {
                        oppositeCorner.add(position);
                    } else {
                        emptyCorner.add(position);
                    }
                } else if (i == 2 && j == 2) {
                    if (board[0][0] == -1) {
                        oppositeCorner.add(position);
                    } else {
                        emptyCorner.add(position);
                    }
                } else if (i == 0 && j == 2) {
                    if (board[2][0] == -1) {
                        oppositeCorner.add(position);
                    } else {
                        emptyCorner.add(position);
                    }
                } else {
                    emptySide.add(position);
                }
            }
        }
        int[] move;
        if (block.size() != 0) {
            move = block.get(0);
        } else if (center) {
            int[] spot = {1, 1};
            move = spot;
        } else if (oppositeCorner.size() != 0) {
            move = oppositeCorner.get(0);
        } else if (emptyCorner.size() != 0) {
            move = emptyCorner.get(0);
        } else if (emptySide.size() != 0) {
            move = emptySide.get(0);
        } else {
            return "draw";
        }
        if (updateBotBoard(board, move, row, col, dig)) {
            return "win";
        } else {
            return "continue";
        }
    }
}