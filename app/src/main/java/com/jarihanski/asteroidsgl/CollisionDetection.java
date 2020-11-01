package com.jarihanski.asteroidsgl;

import android.graphics.PointF;

//CollisionDetection.java
public class CollisionDetection {

    public static boolean triangleVsPoint(PointF[] triVerts, float px, float py){
        Utils.expect(triVerts.length == 3, "triangleVsPoints expects 3 vertices. For more complex shapes, use polygonVsPoint!");
        final PointF p1 = triVerts[0];
        final PointF p2 = triVerts[1];
        final PointF p3 = triVerts[2];

        //calculate the area of the original triangle using Cramers Rule
        // https://web.archive.org/web/20070912110121/http://mcraefamily.com:80/MathHelp/GeometryTriangleAreaDeterminant.htm
        final float triangleArea = Math.abs( (p2.x-p1.x)*(p3.y-p1.y) - (p3.x-p1.x)*(p2.y-p1.y) );

        // get the area of 3 triangles made between the point, and each corner of the triangle
        final float area1 = Math.abs((p1.x-px)*(p2.y-py) - (p2.x-px)*(p1.y-py));
        final float area2 = Math.abs((p2.x-px)*(p3.y-py) - (p3.x-px)*(p2.y-py));
        final float area3 = Math.abs((p3.x-px)*(p1.y-py) - (p1.x-px)*(p3.y-py));

        // if the sum of the three areas equals the original we're inside the triangle.
        // we avoid equality comparisons on float by checking "larger than".
        return !((area1 + area2 + area3) > triangleArea);
    }

    public static boolean polygonVsPolygon(PointF[] polyA, PointF[] polyB) {
        final int count = polyA.length;
        int next = 0;
        for (int current = 0; current < count; current++) {
            next = current+1;
            if (next == count){ next = 0; }
            final PointF segmentStart = polyA[current]; //get a line segment from polyA
            final PointF segmentEnd = polyA[next];
            if (polygonVsSegment(polyB, segmentStart, segmentEnd)){ //compare the segment to all segments in polyB
                return true;
            }
        }
        return false;
    }

    public static boolean polygonVsSegment(PointF[] vertices, final PointF segmentStart, final PointF segmentEnd) {
        final int count = vertices.length;
        int next = 0;
        for (int current = 0; current < count; current++) {
            next = current+1;
            if (next == count) {next = 0;}
            final PointF lineBStart = vertices[current];
            final PointF lineBEnd = vertices[next];
            if(segmentVsSegment(segmentStart, segmentEnd, lineBStart, lineBEnd)){
                return true;
            }
        }
        return false;
    }

    // takes 4 vertices, the begin and end for two line segments.
    public static boolean segmentVsSegment(final PointF lineAStart, final PointF lineAEnd, final PointF lineBStart, final PointF lineBEnd) {
        final float x1 = lineAStart.x; final float y1 = lineAStart.y; //create some local names to make the typing easier further down
        final float x2 = lineAEnd.x; final float y2 = lineAEnd.y;
        final float x3 = lineBStart.x; final float y3 = lineBStart.y;
        final float x4 = lineBEnd.x; final float y4 = lineBEnd.y;
        //pre-calculate any value that's needed twice or more
        final float dx1 = x2-x1;
        final float dy1 = y2-y1;
        final float dx2 = x4-x3;
        final float dy2 = y4-y3;
        final float cInv = 1f / (dy2*dx1 - dx2*dy1);
        // calculate the direction of the lines
        float uA = (dx2*(y1-y3) - dy2*(x1-x3)) * cInv;
        float uB = (dx1*(y1-y3) - dy1*(x1-x3)) * cInv;
        // if uA and uB are between 0-1, lines are colliding
        return (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1);
    }

    // used to check if a point is inside a polygon, using the Jordan curve theorem.
    // See:
    // https://web.archive.org/web/20161108113341/https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
    public static boolean polygonVsPoint(PointF[] vertices, float px, float py) {
        final int count = vertices.length;
        boolean collision = false;
        int next = 0;
        for (int current = 0; current < count; current++) {
            next = current+1;
            if (next == count) next = 0;
            final PointF segmentStart = vertices[current];
            final PointF segmentEnd = vertices[next];
            // compare position, flip 'collision' variable back and forth
            // Look up "Crossing Number Algorithm" for details.
            // If our variable is odd after testing the vertex against every line we have a hit. If it is even, no collision has occurred.

            if (((segmentStart.y > py && segmentEnd.y < py) || (segmentStart.y < py && segmentEnd.y > py)) && //Is the point's Y coordinate within the lines' Y-range?
                    (px < (segmentEnd.x-segmentStart.x)*(py-segmentStart.y) / (segmentEnd.y-segmentStart.y)+segmentStart.x)) { //look up the "jordan curve theorem"
                collision = !collision;
            }
        }
        return collision;
    }
}