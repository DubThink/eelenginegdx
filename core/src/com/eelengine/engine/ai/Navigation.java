package com.eelengine.engine.ai;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Benjamin on 5/26/2017.
 * Probably Dijkstra- sort of
 */
public class Navigation {
    ArrayList<Cell> cells =new ArrayList<>();
    int nextID=0;
    public Navigation(){}
    public Navigation(FileHandle file){
        HashMap<Integer, Cell> linker=new HashMap<>();
        // Load cells
        try {
            {
                Scanner scanner = new Scanner(Paths.get(file.path()));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("//")) continue; // Ignore comments
                    try {
                        Scanner lineScanner = new Scanner(line);
                        int id=lineScanner.nextInt();
                        int newid=nextID++;
                        Cell cell =new Cell(newid,lineScanner.nextInt(),
                                lineScanner.nextInt(),
                                lineScanner.nextInt(),
                                lineScanner.nextInt());

                        linker.put(id, cell);
                        cells.add(cell);

                    } catch (InputMismatchException e) {
                        System.err.println("Invalid nav line \"" + line + "\" (file \"" + file + "\")");
                    }

                }
                scanner.close();
            }
            {
                Scanner scanner = new Scanner(Paths.get(file.path()));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("//")) continue; // Ignore comments
                    try {
                        Scanner lineScanner = new Scanner(line);
                        Cell cell = linker.get(lineScanner.nextInt());
                        lineScanner.nextInt();
                        lineScanner.nextInt();
                        lineScanner.nextInt();
                        lineScanner.nextInt();

                        while(lineScanner.hasNext()){
                            int id=lineScanner.nextInt();
                            cell.addNeighbor(linker.get(id));
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid nav line \"" + line + "\" (file \"" + file + "\")");
                    }

                }
                scanner.close();
            }
        }catch (IOException e){
            System.err.println("Unable to load nav file \""+file+"\"");
        }
    }
    public void drawNodes(ShapeRenderer renderer){
        for(Cell cell : cells){
            cell.draw(renderer);
        }
        for(Cell cell : cells){
            cell.drawLinks(renderer);
        }
    }
    public void printNodes(){
        for(Cell cell : cells)
            System.out.println(cell.toDataString());
    }
    public void saveNodes(String filename){
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("// EelGame Nav file\n");
            writer.write("// id x1 y1 x2 y2 nbr1 nbr2\n");
            for(Cell cell : cells) {
                writer.write(cell.toDataString()+"\n");
            }
            writer.close();
        }catch (IOException e){
            System.err.println("Unable to save nav data to file \""+filename+"\"");
        }
    }
    public void testFunction(){
        System.out.println(cells.get(0).aabb(0,0,1,1));
    }
    /**
     * returns true if the specified region contains no cells
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public boolean testRegion(int x1, int y1, int x2, int y2){
        for(Cell cell : cells){
            if(cell.aabb(x1, y1, x2, y2))return false;
        }
        return true;
    }
    public void makeNode(int x1, int y1, int x2, int y2){
        int newid=nextID++;
        cells.add(new Cell(newid,x1,y1,x2,y2));
    }

    public Cell getNodeAt(Vector2 pos){
        return getNodeAt(pos.x,pos.y);
    }
    /**
     * If the coords are integer values, it gets the cell in the positive direction (by adding .5 to x and y)
     * @return
     */
    public Cell getNodeAt(double x, double y){
        if(x%1==0)x+=.5;
        if(y%1==0)y+=.5;
        for(Cell cell : cells){
            if(cell.pointIn(x,y))return cell;
        }
        return null;
    }
    public void removeNode(Cell cell){
        cells.remove(cell);
        for(Cell cell2 : cells) {
            cell2.removeNeighbor(cell);
        }
    }

    public void buildNeighbors(){
        for(int i = 0; i< cells.size()-1; i++){
            Cell cellA = cells.get(i);
            for(int j = i+1; j< cells.size(); j++){
                Cell cellB = cells.get(j);
                if(cellA.aabbInclusive(cellB)){
                    cellA.addNeighbor(cellB);
                    cellB.addNeighbor(cellA);
                }
            }
        }
    }
    // PATHFINDING
    public int maxNavSearchLength=1000;

    public NavPath findPath(double x1, double y1, float x2, float y2){
        resetNodes();
        boolean dbg=true;
        // STEP 1
        // find start and end cells
        Cell startCell =getNodeAt(x1,y1);
        Cell endCell =getNodeAt(x2,y2);
        //TODO use getNodeNear so nav doesn't break on border cases
        if(startCell ==null|| endCell ==null)return null;
        if(startCell == endCell){
            //TODO return simple nav
            if(dbg) System.out.println("Same Cell");
        }
        PriorityQueue<Tendril> queue=new PriorityQueue<>(10,new TendrilComparator());
        Tendril tendril=new Tendril(startCell,(float)x1,(float)y1);
        queue.add(tendril);
        startCell.insertTendril(tendril);
        for(int iterNum=0;iterNum<maxNavSearchLength;iterNum++){
            if(queue.size()==0){
                System.err.println("Iternum "+iterNum);
                return null;
            }
            Tendril current=queue.poll();
            for(Cell neighbor:current.cell.neighbors){
                Tendril next=new Tendril(current,neighbor);
                if(neighbor.insertTendril(next)){
                    queue.add(next);
                    if(dbg) System.out.println("Adding tendril to cell "+next.cell.id);
                }
            }
            if(endCell.tendril!=null){
                return new NavPath(this, endCell.tendril,x2,y2);
            }
        }
        return null;
    }
    private void resetNodes(){
        for(Cell cell : cells) cell.tendril=null;
    }

}


class Tendril{
    Tendril previous;
    Cell cell;
    //double weight;
    float x,y;
    boolean isEnd=false;
    public Tendril(Cell cell, float x, float y) {
        this.previous = null;
        this.cell = cell;
        this.x=x;
        this.y=y;
        isEnd=true;
        //weight=0;
    }
    public Tendril(Tendril previous, Cell cell) {
        this.previous = previous;
        this.cell = cell;
    }
    public void draw(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        if(previous==null){
            //renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0,1,0,1);
            renderer.ellipse(getX(),getY(),20,20);
            //renderer.end();
            return;
        }
//        shapeRenderer.beginShape();
//        shapeRenderer.stroke(255,0,0);
//        shapeRenderer.vertex(eelengine.Trans.x(previous.getX()),eelengine.Trans.y(previous.getY()));
//        shapeRenderer.stroke(0,255,0);
//        shapeRenderer.vertex(eelengine.Trans.x(cell.centerX()),eelengine.Trans.y(cell.centerY()));
//        shapeRenderer.endShape();
        renderer.setColor(0,1,0,1);
        renderer.line(previous.getX(),previous.getY(), cell.centerX(), cell.centerY());
        renderer.end();
        previous.draw(renderer);
    }
    public float getX(){
        if(isEnd)return x;
        else return cell.centerX();
    }
    public float getY(){
        if(isEnd)return y;
        else return cell.centerY();
    }
    public float getLength(){
        if(isEnd)return 0;
        return cell.distTo(previous.getX(),previous.getY());
    }
    public float getChainLength(){
        if(isEnd)return 0;
        else return previous.getChainLength()+getLength();
    }
    public void print(){
        System.out.println("Tendril links to cell "+ cell.id+" with length "+getLength()+". Total chain length: "+getChainLength());
        if(previous!=null)previous.print();
    }
    public void buildNodeChain(ArrayList<Cell> chain){
        if(previous!=null)previous.buildNodeChain(chain);
        if(cell !=null)chain.add(cell);
    }
}
class TendrilComparator implements Comparator<Tendril>{
    @Override
    public int compare(Tendril o1, Tendril o2) {
        return Double.compare(o1.getChainLength(),o2.getChainLength());
    }
}
