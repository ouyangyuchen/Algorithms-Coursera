/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;
    private int N;
    private WeightedQuickUnionUF uf;
    private int opensites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        grid = new int[n][n];
        N = n;
        opensites = 0;
        uf = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 1; i <= N; i++) {
            uf.union(0, to1d(1, i));
            uf.union(N * N + 1, to1d(N, i));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N)
            throw new IndexOutOfBoundsException();
        if (!isOpen(row, col)) {
            // get indices of neighbor sites
            int left = Math.max(1, col - 1), up = Math.max(1, row - 1);
            int right = Math.min(N, col + 1), down = Math.min(N, row + 1);
            if (isOpen(row, left)) uf.union(to1d(row, left), to1d(row, col));
            if (isOpen(row, right)) uf.union(to1d(row, right), to1d(row, col));
            if (isOpen(up, col)) uf.union(to1d(up, col), to1d(row, col));
            if (isOpen(down, col)) uf.union(to1d(down, col), to1d(row, col));
            // open the current site
            grid[row - 1][col - 1] = 1;
            opensites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N)
            throw new IndexOutOfBoundsException();
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N)
            throw new IndexOutOfBoundsException();
        return isOpen(row, col) && uf.find(to1d(row, col)) == uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(N * N + 1) == uf.find(0);
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
