/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> res;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();        // no null point
        int n = points.length;
        Arrays.sort(points);
        for (int i = 1; i < n; i++)
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException();       // no repeated point

        res = new ArrayList<>(0);
        for (int i = 0; i < n - 3; i++)
            for (int j = i + 1; j < n - 2; j++)
                for (int k = j + 1; k < n - 1; k++)
                    for (int m = k + 1; m < n; m++) {
                        double s1 = points[i].slopeTo(points[j]);
                        double s2 = points[j].slopeTo(points[k]);
                        double s3 = points[k].slopeTo(points[m]);
                        if (s1 == s2 && s2 == s3) res.add(new LineSegment(points[i], points[m]));
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
}
