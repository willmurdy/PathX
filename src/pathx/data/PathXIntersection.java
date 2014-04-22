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
public class PathXIntersection {
    
    private boolean open;
    
    private int x;
    private int y;
    
    private int id;
    
    public PathXIntersection(int x, int y, boolean open){
        this.x = x;
        this.y = y;
        this.open = open;
    }
    
    protected void setId(int id){
        this.id = id;
    }
    
}
