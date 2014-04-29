/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

/**
 *
 * @author willmurdy
 */
public class PathXRoad {
    
    private int id1;
    private int id1x;
    private int id1y;
    private int id2;
    private int id2x;
    private int id2y;
    
    private int speedLimit;
    
    private double length;
    
    private double cost;
    
    private boolean oneWay;
    
    public PathXRoad(int firstId, int secondId, int limit, boolean oneWay){
        id1 = firstId;
        id2 = secondId;
        
        speedLimit = limit;
        
        this.oneWay = oneWay;
    }
    
    public double calculateLength(){
        double tempX = id1x - id2x;
        tempX = tempX * tempX;
        
        double tempY = id1y - id2y;
        tempY = tempY * tempY;
        
        double temp = tempX + tempY;
        length = Math.sqrt(temp);
        
        cost = length / speedLimit;
        
        return length;
    }
    
    public double getCost(){
        return cost;
    }
    
    public boolean oneWay(){
        return oneWay;
    }
    
    public int getId1(){
        return id1;
    }
   
    public int getId2(){
        return id2;
    }
    
    public void setId1Coords(int x, int y){
        id1x = x;
        id1y = y;
    }
    
    public void setId2Coords(int x, int y){
        id2x = x;
        id2y = y;
    }
    
    public int getId1x(){
        return id1x;
    }
    
    public int getId1y(){ return id1y; }
    
    public int getid2x(){ return id2x; }

    public int getid2y(){ return id2y; }
    
    public int getSpeedLimit(){ return speedLimit; };
}
