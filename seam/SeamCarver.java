import edu.princeton.cs.algs4.Picture;

import java.util.NoSuchElementException;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        resetEnergy();
    }

    private void resetEnergy() {
        this.energy = new double[height()][width()];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++)
                energy[j][i] = -1.0;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateHorizontal(x);
        validateVertical(y);
        if (energy[y][x] >= 0) return energy[y][x];
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            energy[y][x] = 1000.0;
        else {
            double res = colorDiffSquare(picture.getRGB(x - 1, y), picture.getRGB(x + 1, y))
                    + colorDiffSquare(picture.getRGB(x, y - 1), picture.getRGB(x, y + 1));
            res = Math.sqrt(res);
            energy[y][x] = res;
        }
        return energy[y][x];
    }

    private double colorDiffSquare(int rgb1, int rgb2) {
        double res = 0.0;
        for (int i = 0; i < 24; i += 8) {
            int temp = ((rgb1 >> i) & 0xFF) - ((rgb2 >> i) & 0xFF);
            res += Math.pow(temp, 2);
        }
        return res;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] res = findVerticalSeam();
        transpose();
        return res;
    }

    private void transpose() {
        Picture temp = new Picture(height(), width());
        double[][] etemp = new double[width()][height()];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++) {
                temp.setRGB(j, i, picture.getRGB(i, j));
                etemp[i][j] = energy[j][i];
            }
        picture = temp;
        energy = etemp;
    }

    private int[] traceUp(int[][] edgeTo, int index) {
        int[] res = new int[height()];
        for (int i = height() - 1; i >= 0; i--) {
            res[i] = index;
            index = edgeTo[i][index];
        }
        return res;
    }

    private int minIndexOfLastRow(double[][] arr) {
        if (height() == 0) throw new NoSuchElementException();
        int index = 0;
        for (int i = 1; i < width(); i++)
            if (arr[height() - 1][i] < arr[height() - 1][index])
                index = i;
        return index;
    }

    private void relaxVertex(int x, int y, double[][] distTo, int[][] edgeTo) {
        // (x, y) -> (x - 1, y + 1), (x, y + 1), (x + 1, y + 1)
        for (int dx = -1; dx < 2; dx++) {
            int nx = x + dx;
            if (nx >= 0 && nx < width() && distTo[y][x] + energy(x, y) < distTo[y + 1][nx]) {
                edgeTo[y + 1][nx] = x;
                distTo[y + 1][nx] = distTo[y][x] + energy(x, y);
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // topological order: (0->width, 0->height)
        double[][] distTo = new double[height()][width()];
        for (int i = 1; i < height(); i++)
            for (int j = 0; j < width(); j++)
                distTo[i][j] = Double.POSITIVE_INFINITY;
        int[][] edgeTo = new int[height()][width()];

        for (int j = 0; j < height() - 1; j++)
            for (int i = 0; i < width(); i++) {
                relaxVertex(i, j, distTo, edgeTo);
            }
        // find the shortest distance in the last row
        return traceUp(edgeTo, minIndexOfLastRow(distTo));
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkArray(seam);
        if (width() <= 1) throw new IllegalArgumentException();
        // remove all columns in seam
        Picture temp = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < seam[j]; i++)
                temp.setRGB(i, j, picture.getRGB(i, j));
            for (int i = seam[j]; i < width() - 1; i++)
                temp.setRGB(i, j, picture.getRGB(i + 1, j));
        }
        this.picture = temp;
        resetEnergy();
    }

    private void checkArray(int[] seam) {
        if (seam == null || seam.length != height()) throw new IllegalArgumentException();
        for (int i = 0; i < height(); i++) {
            validateHorizontal(seam[i]);
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
    }

    private void validateHorizontal(int col) {
        if (col < 0 || col >= width()) throw new IllegalArgumentException();
    }

    private void validateVertical(int row) {
        if (row < 0 || row >= height()) throw new IllegalArgumentException();
    }
}
