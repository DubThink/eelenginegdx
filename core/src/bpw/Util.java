package bpw;

//import eelengine.Point;
//import eelengine.navigation.Node;
//import org.dyn4j.geometry.Vector2;

import java.awt.Color;


/**
 * Created by benja on 4/12/2017.
 */
public class Util {
    /**
     * returns true if num is between a and b, inclusive.
     * It does not matter if a>b
     * @param num
     * @param a
     * @param b
     * @return
     */
    public static boolean in(float num,float a, float b){
        return Math.min(a,b)<=num&&num<=Math.max(a,b);
    }
    public static boolean in(double num,double a, double b){
        return Math.min(a,b)<=num&&num<=Math.max(a,b);
    }

    public static float min(float a, float b){return a<b?a:b;}
    public static float max(float a, float b){return a>b?a:b;}

    public static int clamp(int num,int min, int max){
        return num<min?min:(num>max?max:num);
    }
    public static double clamp(double num,double min, double max){
        return num<min?min:(num>max?max:num);
    }
    public static float clamp(float num,float min, float max){
        return num<min?min:(num>max?max:num);
    }
    public static void main(String[] args) {
        System.out.println(aabb(0,0,2,2,3,3,2,2));
        System.out.println(aabbInclusive(0,0,2,2,3,3,2,2));

        System.out.println( in(10,0,20));
        System.out.println( in(10,10,20));
        System.out.println( in(15,20,10));
        System.out.println(clamp(10,9,11));
        System.out.println(clamp(8,9,11));

        System.out.println("//  lerp test  //");
        System.out.println(0==lerp(.5,.25,.75,-10,10));
        System.out.println(2==lerp(.55,.25,.75,-10,10));
        System.out.println(10==lerp(.75,.25,.75,-10,10));
        System.out.println(-10==lerp(.75,.75,.25,-10,10));
        System.out.println(20==lerp(1,.25,.75,-10,10));


    }
//    public static double dist(Point a, Point b){
//        return dist(a.x,a.y,b.x,b.y);
//    }
    public static double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
    public static double halfBetween(double a, double b){
        return a+((b-a)/2.0);
    }
        /**
         * Performs aabb collision testing for only exclusive overlap
         * does not include edges touching.
         * @param x1 box 1 x1
         * @param y1 box 1 y1
         * @param x2 box 1 x2
         * @param y2 box 1 y2
         * @param x3 box 2 x1
         * @param y3 box 2 y1
         * @param x4 box 2 x2
         * @param y4 box 2 y2
         * @return true if the boxes overlap
         */
    public static boolean aabb(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        //System.out.printf("Input   ((%f,%f),(%f,%f)) against ((%f,%f),(%f,%f))\n",x1,y1,x2,y2,x3,y3,x4,y4);

        //Make sure larger numbers are second set of coords.
        if(x1>x2){double tmp=x2;x2=x1;x1=tmp;}
        if(y1>y2){double tmp=y2;y2=y1;y1=tmp;}
        if(x3>x4){double tmp=x4;x4=x3;x3=tmp;}
        if(y3>y4){double tmp=y4;y4=y3;y3=tmp;}
        //System.out.printf("Testing ((%f,%f),(%f,%f)) against ((%f,%f),(%f,%f))\n",x1,y1,x2,y2,x3,y3,x4,y4);
        return x1<x4&&
                x2>x3&&
                y1<y4&&
                y2>y3;
    }

    /**
     * Performs aabb collision testing for inclusive overlap
     * includes edges touching.
     * @param x1 box 1 x1
     * @param y1 box 1 y1
     * @param x2 box 1 x2
     * @param y2 box 1 y2
     * @param x3 box 2 x1
     * @param y3 box 2 y1
     * @param x4 box 2 x2
     * @param y4 box 2 y2
     * @return true if the boxes overlap
     */
    public static boolean aabbInclusive(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        //System.out.printf("Input   ((%f,%f),(%f,%f)) against ((%f,%f),(%f,%f))\n",x1,y1,x2,y2,x3,y3,x4,y4);

        //Make sure larger numbers are second set of coords.
        if(x1>x2){double tmp=x2;x2=x1;x1=tmp;}
        if(y1>y2){double tmp=y2;y2=y1;y1=tmp;}
        if(x3>x4){double tmp=x4;x4=x3;x3=tmp;}
        if(y3>y4){double tmp=y4;y4=y3;y3=tmp;}
        //System.out.printf("Testing ((%f,%f),(%f,%f)) against ((%f,%f),(%f,%f))\n",x1,y1,x2,y2,x3,y3,x4,y4);
        return x1<=x4&&
                x2>=x3&&
                y1<=y4&&
                y2>=y3;
    }
    /**
     * Performs aabb collision testing for inclusive overlap
     * includes edges touching.
     * @param x1 box 1 x1
     * @param y1 box 1 y1
     * @param x2 box 1 x2
     * @param y2 box 1 y2
     * @param x3 box 2 x1
     * @param y3 box 2 y1
     * @param x4 box 2 x2
     * @param y4 box 2 y2
     * @return true if the boxes overlap
     */
    public static boolean aabbInclusiveNoCorners(float x1,float y1,float x2,float y2,float x3,float y3,float x4,float y4){
        //System.out.printf("Input   ((%f,%f),(%f,%f)) against ((%f,%f),(%f,%f))\n",x1,y1,x2,y2,x3,y3,x4,y4);

        //Make sure larger numbers are second set of coords.
        if(x1>x2){float tmp=x2;x2=x1;x1=tmp;}
        if(y1>y2){float tmp=y2;y2=y1;y1=tmp;}
        if(x3>x4){float tmp=x4;x4=x3;x3=tmp;}
        if(y3>y4){float tmp=y4;y4=y3;y3=tmp;}
        //System.out.printf("Testing ((%f,%f),(%f,%f)) against ((%f,%f),(%f,%f))\n",x1,y1,x2,y2,x3,y3,x4,y4);
        return x1<=x4&&
                x2>=x3&&
                y1<=y4&&
                y2>=y3&&!(
                (x1==x4&&y1==y4)||
                        (x2==x3&&y2==y3)||
                        (x1==x4&&y2==y3)||
                        (x2==x3&&y1==y4)
                );
    }
    /**
     * True if line (a,b) intersects (c,d) exclusive;
     * If one line is length 0 and on the other line, returns true;
     * two lines of length 0 cannot intersect.
     * @return
     */
//    public static boolean intersect(Point a, Point b, Point c, Point d){
//        double denom=(d.y-c.y)*(b.x-a.x)-(d.x-c.x)*(b.y-a.y);
//        double ua=(d.x-c.x)*(a.y-c.y)-(d.y-c.y)*(a.x-c.x);
//        double ub=(b.x-a.x)*(a.y-c.y)-(b.y-a.y)*(a.x-c.x);
//        if(denom==0){
//            if(ua==0&&ub==0){
//                return Util.aabb(a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);
//            }
//            // Parallel
//            //println("FF",ua, ub);
//        }
//        ua/=denom;
//        ub/=denom;
//        return (0<ua&&ua<1&&0<ub&&ub<1);
//    }
//    /**
//     * True if line (a,b) intersects (c,d) inclusive;
//     * If one line is length 0 and on the other line, returns true;
//     * two lines of length 0 can intersect.
//     * @return
//     */
//    public static boolean intersectInclusive(Point a, Point b, Point c, Point d){
//        double denom=(d.y-c.y)*(b.x-a.x)-(d.x-c.x)*(b.y-a.y);
//        double ua=(d.x-c.x)*(a.y-c.y)-(d.y-c.y)*(a.x-c.x);
//        double ub=(b.x-a.x)*(a.y-c.y)-(b.y-a.y)*(a.x-c.x);
//        if(denom==0){
//            if(ua==0&&ub==0){
//                return Util.aabbInclusive(a.x,a.y,b.x,b.y,c.x,c.y,d.x,d.y);
//            }
//            // Parallel
//            //println("FF",ua, ub);
//        }
//        ua/=denom;
//        ub/=denom;
//        return (0<=ua&&ua<=1&&0<=ub&&ub<=1);
//    }
//
//    public static Point intersectPoint(Point a, Point b, Point c, Point d) {
//        double denom=(d.y-c.y)*(b.x-a.x)-(d.x-c.x)*(b.y-a.y);
//        double ua=(d.x-c.x)*(a.y-c.y)-(d.y-c.y)*(a.x-c.x);
//        if(denom==0){
//            return null;
//        }
//        ua/=denom;
//        double ix=a.x+ua*(b.x-a.x);
//        double iy=a.y+ua*(b.y-a.y);
//        return new Point(ix, iy);
//    }
//
//    public static Point intersectPointFromAmount(Point a, Point b,double amt) {
//        double ix=a.x+amt*(b.x-a.x);
//        double iy=a.y+amt*(b.y-a.y);
//        return new Point(ix, iy);
//    }
//
//    public static double intersectAmount(Point a, Point b, Point c, Point d) {
//        double denom=(d.y-c.y)*(b.x-a.x)-(d.x-c.x)*(b.y-a.y);
//        double ua=(d.x-c.x)*(a.y-c.y)-(d.y-c.y)*(a.x-c.x);
//        if(denom==0){
//            return Double.NaN;
//        }
//        ua/=denom;
//        return ua;
//    }
//    public static double direction(Point start, Point end){
//        return Math.atan2(start.x - end.x, start.y - end.y);
//    }
//    public static Point lineInDirection(Point start, float length, double theta){
//        return new Point(start.x-length*Math.sin(theta),start.y-length*Math.cos(theta));
//    }
//
//    public static Vector2 toPoint(double x1, double y1, double x2, double y2){
//        return new Vector2(x2-x1,y2-y1);
//    }
//    public static Vector2 toPoint(double x1, double y1, Node node){
//        return toPoint(x1,y1,node.centerX(),node.centerY());
//    }
    public static float round(float val, float roundTo){
        return (float)round((double)val,roundTo);
    }
    public static double round(double val, double roundTo){
        if(roundTo==0)return val;
        return Math.round(val/roundTo)*roundTo;

    }
    public static int pColor(Color c){
        float v1=c.getRed();
        float v2=c.getGreen();
        float v3=c.getBlue();
        float alpha=c.getAlpha();
        //Code from PApplet source code. Copyright Processing Foundation
        if(alpha > 255.0F) {
            alpha = 255.0F;
        } else if(alpha < 0.0F) {
            alpha = 0.0F;
        }

        if(v1 > 255.0F) {
            v1 = 255.0F;
        } else if(v1 < 0.0F) {
            v1 = 0.0F;
        }

        if(v2 > 255.0F) {
            v2 = 255.0F;
        } else if(v2 < 0.0F) {
            v2 = 0.0F;
        }

        if(v3 > 255.0F) {
            v3 = 255.0F;
        } else if(v3 < 0.0F) {
            v3 = 0.0F;
        }

        return (int)alpha << 24 | (int)v1 << 16 | (int)v2 << 8 | (int)v3;
    }

    /**
     * Lerps between a and b
     */
    public static float lerp(double val, double a, double b){
        val=clamp(val,0f,1f);
        return (float)((1-val)*a+val*b);
    }

    /**
     * Lerps between a and b
     */
    public static float lerp(double val, double minVal, double maxVal, double a, double b){
        double c=(val-minVal)/(maxVal-minVal);
        return (float)((1-c)*a+c*b);
    }

    /**
     * lerps between (ar,ag,ab) and (br,bg,bb)
     * @param val 0 to 1 (clamped inside)
     */
    public static Color lerpColor(double val, float ar, float ag, float ab, float br, float bg, float bb){
//        System.out.println(lerp(val,ar,br));
//        System.out.println(lerp(val,ag,bg));
//        System.out.println(lerp(val,ab,bb));
        return new Color(lerp(val,ar,br),lerp(val,ag,bg),lerp(val,ab,bb));
    }

    /**
     * Tests if points are in the bounds
     */
    public static class BTester{
        public float x1, y1, x2, y2;

        public BTester(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
//        public boolean test(Point point){
//            return in(point.x,x1,x2)&&in(point.y,y1,y2);
//        }
        public boolean test(double x, double y){
            return in(x,x1,x2)&&in(y,y1,y2);
        }
    }
//    static Point lineToBounds(Point a,Point b,float x1,float y1,float x2,float y2){
//        // return the original line if not in bounds
//        if(!in(a.x,x1,x2)||!in(a.y,y1,y2))return b;
//
//    }
//    public static Card pickFirst(Card ... cards){
//        for(Card card:cards)if(card!=null)return;
//    }
}
