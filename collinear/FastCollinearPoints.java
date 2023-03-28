/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> res;
    private int n;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // check whether input points are valid.
        if (points == null) throw new IllegalArgumentException();
        n = points.length;
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();        // no null point
        Arrays.sort(points);
        for (int i = 1; i < n; i++)
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException();       // no repeated points
        if (n < 4) return;

        // take any point[i] as origin, determine whether point is in the 4-point line
        res = new ArrayList<>(0);
        for (int i = 0; i < n - 1; i++) {
            // copy points in the slope order
            Point[] temp = new Point[n];
            for (int j = 0; j < n; j++)
                temp[j] = points[j];
            Arrays.sort(temp, points[i].slopeOrder());
            getLine(temp);
        }
    }

    private void getLine(Point[] points) {
        Point origin = points[0];
        int cnt = 0;
        double prev = Double.NEGATIVE_INFINITY, curr;
        boolean visited = false;
        for (int i = 1; i < n; i++) {
            curr = origin.slopeTo(points[i]);
            if (points[i].compareTo(origin) < 0) {
                addLineToRes(origin, points[i - 1], cnt);
                visited = true;
                cnt = 0;
            }
            else {
                if (curr == prev) {
                    if (!visited) {
                        cnt++;
                    }
                    if (i == n - 1)
                        addLineToRes(origin, points[i], cnt);
                }
                else {
                    addLineToRes(origin, points[i - 1], cnt);
                    visited = false;
                    cnt = 1;
                }
            }
            prev = curr;
        }
    }

    private void addLineToRes(Point origin, Point end, int cnt) {
        if (cnt >= 3) {
            // StdOut.printf(origin + "->" + end + ", cnt: " + cnt + '\n');
            res.add(new LineSegment(origin, end));
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return res.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return res.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
