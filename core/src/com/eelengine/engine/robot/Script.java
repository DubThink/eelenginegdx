package com.eelengine.engine.robot;

import java.util.Stack;

/**
 * Represents an instance of an executing script
 */
public class Script {
    private static class Loop{
        int line, ctr;

        public Loop(int line, int ctr) {
            this.line = line;
            this.ctr = ctr;
        }

        @Override
        public String toString() {
            return "Loop{" +
                    "line=" + line +
                    ", ctr=" + ctr +
                    '}';
        }
    }

    private String[] lines;
    private int currentline=-1;
    private int counter=0;
    private Stack<Loop> loops=new Stack<>();

    public Script(String source){
        this.lines=source.split("\n");
        advance();
    }

    /** Get the next line to execute/evaluate */
    public String getNext(){
        if(currentline>=lines.length)return "";
        return lines[currentline].substring(nestLevel());
    }

    public boolean finished(){
        return currentline>=lines.length;
    }

    private int nestLevel(){
        return loops.size();
    }

    /** advance the script, marking the line as executed */
    public void advance(){
        String nextLine;
        currentline++;
        if(currentline>=lines.length)
            nextLine="";
        else
            nextLine=lines[currentline];

        if(nextLine.startsWith("loop ",nestLevel())){
            // new loop
            int iters=Integer.parseInt(nextLine.substring(5+nestLevel(),nextLine.length()-1));
            loops.push(new Loop(currentline,iters));
            System.out.println("starting loop: "+loops.peek());
            advance();
        } else if (indentLevel(nextLine)<nestLevel()) {
            // end of block
            loops.peek().ctr--;
            if(loops.peek().ctr<=0){
                // loop finished, pop out
                System.out.println("finished loop: "+loops.peek());
                loops.pop();
                currentline--;
                advance();
            } else {
                // loop
                currentline=loops.peek().line;
                System.out.println("advanced loop: "+loops.peek());
                advance();
            }
        }

    }
    private int indentLevel(String s){
        int r=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==' ')
                r++;
            else
                return r;
        }
        return r;
    }

     static String test_script=
//             "// mini script for thinking\n"+
             "loop 2:\n" +
                     " move l\n" +
                     "loop 4:\n" +
                     " loop 3:\n" +
                     "  mine\n" +
                     " move l\n";
    public static void main(String[] args) {
        Script script=new Script(test_script);
        while(!script.finished()){
            System.out.println(script.getNext());
            script.advance();
        }

    }
}
