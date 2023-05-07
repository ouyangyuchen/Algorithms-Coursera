import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] board;
    private final int n;
    private int hamming, manhattan;       // not computed yet

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++)
            board[i] = tiles[i].clone();        // O(N^2)
        // compute hamming, manhattan distance, save into cache
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int d = distance(board[i][j], i, j);
                manhattan += d;
                if (d > 0) hamming++;
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(Integer.toString(n) + '\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res.append(" " + Integer.toString(board[i][j]));
            }
            res.append('\n');
        }
        return res.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // Manhatten distance of (row, col) with the goal position of num
    private int distance(int num, int row, int col) {
        if (num == 0) return 0;
        int grow = (num - 1) / n, gcol = (num - 1) % n;
        return Math.abs(row - grow) + Math.abs(col - gcol);
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board other = (Board) y;
        return Arrays.deepEquals(board, other.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // find 0 position
        int row = -1, col = -1;
        for (int i = 0; i < n * n; i++) {
            int r = i / n, c = i % n;
            if (board[r][c] == 0) {
                row = r;
                col = c;
                break;
            }
        }
        // check whether adjacent coordinates are out of bound
        // no -> swap zero with the coordinate, add to the result
        ArrayList<Board> res = new ArrayList<>();
        if (row > 0) res.add(swap(row, col, row - 1, col));
        if (col > 0) res.add(swap(row, col, row, col - 1));
        if (row < n - 1) res.add(swap(row, col, row + 1, col));
        if (col < n - 1) res.add(swap(row, col, row, col + 1));
        return res;
    }

    // swap (row1, col1) with (row2, col2), return the new Board
    // Time complexity: O(N^2)
    private Board swap(int row1, int col1, int row2, int col2) {
        int[][] boardtemp = new int[n][];
        for (int i = 0; i < n; i++)
            boardtemp[i] = board[i].clone();
        int temp = boardtemp[row1][col1];
        boardtemp[row1][col1] = boardtemp[row2][col2];
        boardtemp[row2][col2] = temp;
        return new Board(boardtemp);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int j = 0; j < n; j++) {
            if (board[1][j] != 0 && board[0][j] != 0)
                return swap(0, j, 1, j);
        }
        return null;
    }
}
