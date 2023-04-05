import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> pset;

    public PointSET() {
        pset = new SET<>();
    }

    public boolean isEmpty() {
        return pset.isEmpty();
    }

    public int size() {
        return pset.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!pset.contains(p))
            pset.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pset.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : pset)
            p.draw();
        StdDraw.show();
    }

    // all points inside the rectangle or on the boundary
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> res = new ArrayList<>();
        for (Point2D p : pset) {
            if (rect.contains(p))
                res.add(p);
        }
        return res;
    }

    // a nearest neighbor in the set to point p
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D res = null;
        double mindistance = Double.POSITIVE_INFINITY;
        for (Point2D point : pset) {
            double curr = point.distanceTo(p);
            if (curr < mindistance) {
                mindistance = curr;
                res = point;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        int num = 10;
        for (int i = 0; i < num; i++) {
            Point2D p = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
            ps.insert(p);
        }
        ps.draw();
    }
}
