/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author willmurdy
 */
public class PathXIntersection {
    
    private boolean open;
    
    private int x;
    private int y;
    
    private int renderx;
    private int rendery;
    
    private int id;
    
    private ArrayList<Integer> adjacentNodesIds;
    private ArrayList<Double> adjacentNodesCost;
    

    private ArrayList<PathXRoad> roads;
    
    public PathXIntersection(int x, int y, boolean open){
        this.x = x;
        this.y = y;
        this.open = open;
        adjacentNodesIds = new ArrayList<Integer>();
        adjacentNodesCost = new ArrayList<Double>();
        roads = new ArrayList<PathXRoad>();
    }
    
    public int getId(){
        return id;
    }
    
    public PathXIntersection(){
        adjacentNodesIds = new ArrayList<Integer>();
        adjacentNodesCost = new ArrayList<Double>(); 
        roads = new ArrayList<PathXRoad>();
    }
    
    public ArrayList<PathXRoad> getRoads(){
        return roads;
    }
    
    public void addConnection(int id, double cost){
        adjacentNodesIds.add(id);
        adjacentNodesCost.add(cost);
    }
    
    public ArrayList<Integer> getAdjacentNodes(){
        return adjacentNodesIds;
    }
    
    protected void setId(int id){
        this.id = id;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setRenderY(int y){
        rendery = y;
    }
    
    public void setRenderX(int x){
        renderx = x;
    }
    
    public int getRenderX(){
        return renderx;
    }
    
    public int getRenderY(){
        return rendery;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setOpen(boolean open){
        this.open = open;
    }
    
    public boolean open(){
        return open;
    }
    
    public void addRoad(PathXRoad newRoad){
        roads.add(newRoad);
    }
    
}
