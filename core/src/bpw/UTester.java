package bpw;

import java.util.Objects;

public class UTester {
    private int testCount,testPassed;
    private boolean started=false;
    private String name;

    public void start(String name){
        this.name=name;
        if(started){
            System.out.println("Cannot start test: test '"+name+"' already started.");
            return;
        }
        testCount=0;
        testPassed=0;
        started=true;
        System.out.println("==== STARTING TEST '"+name+"' ====");
    }

    public void end(){
        if(!started){
            System.out.println("Cannot end test: test '"+name+"' not started.");
            return;
        }
        System.out.println("==== ENDING TEST '"+name+"' ====");
        System.out.println("==== "+testPassed+"/"+testCount+" passed.");
        if(testCount==testPassed)
            System.out.println("==== ALL PASSED ====");
        else
            System.out.println("==== !!FAILED!! ====");

    }

    public void testBool(boolean test){
        testCount++;
        if(test){
            testPassed++;
            System.out.println("+ Test "+testCount+" passed.");
        } else {
            System.out.println("+ Test "+testCount+" failed.");
        }
    }

    public void testInt(int testVal, int correctVal){
        testCount++;
        if(testVal==correctVal){
            testPassed++;
            System.out.println("+ Test "+testCount+" passed.");
        } else {
            System.out.println("+ Test " + testCount + " failed. Expected <" + correctVal + "> got <" + testVal + ">");
        }
    }

//    public void testFloatEpsilon(float testVal,float correctVal,float epsilon){
//        testBool(Util.abs(testVal-correctVal)<epsilon);
//    }
//
//    public void testDoubleEpsilon(double testVal,double correctVal,double epsilon){
//        testBool(Util.abs(testVal-correctVal)<epsilon);
//    }
//
//    public void testFloat(float testVal, float correctVal){
//        testBool(testVal==correctVal);
//    }
//
//    public void testDouble(double testVal, double correctVal){
//        testBool(testVal==correctVal);
//    }
//
//    public void testString(String testVal, String correctVal){
//        testBool(Objects.equals(testVal, correctVal));
//    }
}
