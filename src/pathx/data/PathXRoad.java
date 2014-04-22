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
    private int id2;
    
    private int speedLimit;
    
    private boolean oneWay;
    
    public PathXRoad(int firstId, int secondId, int limit, boolean oneWay){
        id1 = firstId;
        id2 = secondId;
        
        speedLimit = limit;
        
        this.oneWay = oneWay;
    }
    
}
