import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private Node left, right;
        private Point2D point;
        private int layer;
        private RectHV range;

        public Node(Point2D point, int layer, RectHV ran) {
            this.point = new Point2D(point.x(), point.y());
            this.layer = layer;
            this.left = null;
            this.right = null;
            this.range = ran;
        }

        public int compare(Point2D other, int axis) {
            assert axis == 0 || axis == 1;
            if (axis == 0) return Double.compare(point.x(), other.x());
            else return Double.compare(point.y(), other.y());
        }

        public String toString() {
            return point.toString() + "layer " + layer;
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) {
            root = new Node(p, 0, new RectHV(0.0, 0.0, 1.0, 1.0));
            size = 1;
        }
        else {
            Node temp = helper(p);
            if (p.equals(temp.point)) return;       // has existed
            int compare = temp.compare(p, temp.layer % 2);
            if (compare > 0)
                temp.left = new Node(p, temp.layer + 1, subRange(temp, true));
            else
                temp.right = new Node(p, temp.layer + 1, subRange(temp, false));
            size++;
        }
    }

    // find the range of x.left or x.right
    private RectHV subRange(Node node, boolean left) {
        assert node != null;
        RectHV res;
        RectHV currange = node.range;
        double x = node.point.x(), y = node.point.y();
        if (node.layer % 2 == 0) {
            if (left)
                res = new RectHV(currange.xmin(), currange.ymin(), x, currange.ymax());
            else
                res = new RectHV(x, currange.ymin(), currange.xmax(), currange.ymax());
        }
        else {
            if (left)
                res = new RectHV(currange.xmin(), currange.ymin(), currange.xmax(), y);
            else
                res = new RectHV(currange.xmin(), y, currange.xmax(), currange.ymax());
        }
        return res;
    }

    // if contains p, return p, else return the 'parent node' of p
    // KdTree has to be non-empty
    private Node helper(Point2D p) {
        assert !isEmpty();
        Node ptr = root, parent = null;
        while (ptr != null) {
            parent = ptr;
            int compare = ptr.compare(p, ptr.layer % 2);
            if (compare > 0) ptr = ptr.left;            // p.xi is smaller than current node
            else if (compare < 0) ptr = ptr.right;      // p.xi is larger than current node
            else {
                if (p.equals(ptr.point)) return ptr;    // if p == current node, return node
                else ptr = ptr.right;                   // else go to right
            }
        }
        return parent;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return false;
        Node temp = helper(p);
        return temp.point.equals(p);
    }

    // all points inside the rectangle or on the boundary
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> res = new ArrayList<>();
        range(rect, root, res);
        return res;
    }

    private void range(RectHV rect, Node node, ArrayList<Point2D> res) {
        if (node == null) return;
        if (!node.range.intersects(rect)) return;
        if (rect.contains(node.point)) res.add(node.point);

        range(rect, node.left, res);
        range(rect, node.right, res);
    }

    // a nearest neighbor in the set to point p
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearest(p, root, null);
    }

    private Point2D nearest(Point2D p, Node node, Point2D currmin) {
        if (node == null) return currmin;
        if (currmin != null && currmin.distanceSquaredTo(p) < node.range.distanceSquaredTo(p))
            return currmin;
        if (currmin == null || node.point.distanceSquaredTo(p) < currmin.distanceSquaredTo(p))
            currmin = node.point;

        if (node.compare(p, node.layer % 2) > 0) {
            currmin = nearest(p, node.left, currmin);
            currmin = nearest(p, node.right, currmin);
        }
        else {
            currmin = nearest(p, node.right, currmin);
            currmin = nearest(p, node.left, currmin);
        }
        return currmin;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        double x = node.point.x(), y = node.point.y();
        if (node.layer % 2 == 0) {
            // vertical red line
            StdDraw.setPenColor(Color.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(x, node.range.ymin(), x, node.range.ymax());
        }
        else {
            // horizontal blue line
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(node.range.xmin(), y, node.range.xmax(), y);
        }
        draw(node.left);
        draw(node.right);
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        for (int i = 0; i < 10; i++) {
            Point2D p = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
            kdTree.insert(p);
            StdOut.println("size: " + kdTree.size());
        }
    }
}
