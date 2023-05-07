import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int[][] NEIGHBORS = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
    private boolean[][] grid;
    private int N, opensites;
    private WeightedQuickUnionUF uf, uf2;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        grid = new boolean[n][n];
        N = n;
        opensites = 0;
        uf = new WeightedQuickUnionUF(N * N + 2);
        uf2 = new WeightedQuickUnionUF(N * N + 1);      // neglect the visual bottom
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkValid(row, col);
        if (!isOpen(row, col)) {
            // get indices of neighbor sites
            for (int[] temp : NEIGHBORS) {
                int x = temp[0] + row, y = temp[1] + col;
                try {
                    if (isOpen(x, y)) {
                        uf.union(to1d(row, col), to1d(x, y));
                        uf2.union(to1d(row, col), to1d(x, y));
                    }
                }
                catch (IllegalArgumentException ignored) {
                }
            }
            if (row == 1) {
                uf.union(to1d(row, col), 0);
                uf2.union(to1d(row, col), 0);
            }
            if (row == N) uf.union(N * N + 1, to1d(N, col));
            // open the current site
            grid[row - 1][col - 1] = true;
            opensites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkValid(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkValid(row, col);
        return uf2.find(to1d(row, col)) == uf2.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(N * N + 1) == uf.find(0);
    }

    private void checkValid(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N)
            throw new IllegalArgumentException();
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(20);
        boolean result = percolation.isFull(1, 9);
        StdOut.print(result);
    }

    // convert 1-index 2d coordinates into an integer
    private int to1d(int row, int col) {
        return (row - 1) * N + col;
    }
}
