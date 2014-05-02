/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import mini_game.Viewport;
import static pathx.PathXConstants.NORTH_PANEL_HEIGHT;
import static pathx.PathXConstants.VIEWPORT_MARGIN_LEFT;
import static pathx.PathXConstants.VIEWPORT_MARGIN_TOP;

/**
 *
 * @author willmurdy
 */
public class PathXLevel {
    
    private String currentState;
    
    private String levelName;
    
    private String levelDescription;
    
    private int money;
    
    private BufferedImage startingImage;
    
    private BufferedImage destinationImage;
    
    private BufferedImage backgroundImage;
    
    private int spriteID;
    
    //the x and y position on the gameworld
    private int x;
    private int y;
    
    private int renderx;
    private int rendery;
    
    private Viewport vp;
    
    private ArrayList<PathXIntersection> intersections;
    
    private ArrayList<PathXRoad> roads;
    
    private boolean inGame;
    
    private boolean won;
    
    private boolean paused;
    
    private PathXCar player;
    
    private ArrayList<PathXCar> police;
    
    public int numPolice;

    
    public PathXLevel(int xPos, int yPos, Viewport gameViewport, String state, String name,
                            String description, int reward){
        
        vp = gameViewport;
        x = xPos + 20 - vp.getViewportX();
        y = yPos + 120 - vp.getViewportY(); 
        
        renderx = x + 20 - vp.getViewportX();
        rendery = y + 120 - vp.getViewportY();
        
        levelName = name;
        
        levelDescription = description;
        
        currentState = state;
        
        money = reward;
        
        player = new PathXCar(PathXCarType.PLAYER_TYPE.toString(), 0);
        
        intersections = new ArrayList<PathXIntersection>();
  
        police = new ArrayList<>();        
        
        roads = new ArrayList<PathXRoad>();
        
        won = false;
        
        paused = false;
        
    }
    
    public PathXLevel(){
        intersections = new ArrayList<>();
        
        roads = new ArrayList<>();
        
        inGame = false;
        
        player = new PathXCar(PathXCarType.PLAYER_TYPE.toString(), 0);
        
        police  = new ArrayList<>();

        won = false;
        
        paused = false;
    }
    
    public void setLocation(int xPos, int yPos){
        x = xPos + 20 - vp.getViewportX();
        y = yPos + 120 - vp.getViewportY(); 
        
        renderx = x + 20 - vp.getViewportX();
        rendery = y + 120 - vp.getViewportY();
        
        //for(int i = 0; i < intersections.size(); i++){
          //  intersections.get(i).setRenderX(x);
        //}
        
        //initPlayerLocation();
    }
    
    public void movePlayerToIntersection(int id){
        int intersectionId;
        PathXIntersection intersection;
        ArrayList<PathXRoad> roads;
        int i = 0;
        PathXRoad roadToTravel = new PathXRoad();
        boolean found = false;
        boolean forward = true;
        
        if(!player.inMotion()){
            intersectionId = player.getIntersection();
            intersection = intersections.get(intersectionId);
            roads = intersection.getRoads();
            for(PathXRoad rd : roads){
                if(rd.oneWay()){
                    if(rd.getId2() == id){
                        roadToTravel = rd;
                        found = true;
                        forward = true;
                        break;
                    }
                } else {
                    if(rd.getId1() == id && rd.getId1() != intersectionId){
                        roadToTravel = rd;
                        found = true;
                        forward = false;
                        break;
                    }
                    if(rd.getId2() == id && rd.getId2() != intersectionId){
                        roadToTravel = rd;
                        found = true;
                        forward = true;
                        break;
                    }
                }
            }
            if(found){
                class MovePlayer extends Thread {
                
                    PathXRoad road;
                    boolean forward;
                    long wait;
                
                    public MovePlayer(PathXRoad rd, boolean forward){ 
                        road = rd; 
                        wait = 100 -road.getSpeedLimit();// * 100;
                        this.forward = forward;
                    }
                
                    public void run() {
                        ArrayList<Integer> x = road.getXList();
                        ArrayList<Integer> y = road.getYList();
                        player.setInMotion(true);
                        if(forward){
                            for(int i = 0; i < x.size(); i++){
                                if(paused){
                                    i--;
                                }
                                player.setRenderX(road.getXPosition(i) - vp.getViewportX());
                                player.setRenderY(road.getYPosition(i) - vp.getViewportY());
                                player.setX(road.getXPosition(i));
                                player.setY(road.getYPosition(i));
                                try {
                                    Thread.sleep(wait);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            
                            player.setIntersectionId(road.getId2());
                        } else { 
                            for(int i = x.size() - 1; i > 0; i--){
                                player.setRenderX(road.getXPosition(i) - vp.getViewportX());
                                player.setRenderY(road.getYPosition(i) - vp.getViewportY());
                                player.setX(road.getXPosition(i));
                                player.setY(road.getYPosition(i));
                                try {
                                    Thread.sleep(wait);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if(paused){
                                    i++;
                                }
                            }
                            player.setIntersectionId(road.getId1());
                        }
                        player.setInMotion(false);
                        //kill();
                    }
                    

                }
                
                MovePlayer p = new MovePlayer(roadToTravel, forward);
                p.start();
            }
        
        
        
        
        }
        
        if(player.getIntersection() == 1){
            won = true;
        }
    }
    
    
    
    public void updateIntersectionLocations(){
        for(int i = 0; i < intersections.size(); i++){
            intersections.get(i).setRenderX(intersections.get(i).getX() + 150 - vp.getViewportX() - 12);
            intersections.get(i).setRenderY(intersections.get(i).getY() + 150 - vp.getViewportY() - 12);
        }
    }
    
    public int getPlayerX(){
        return player.getRenderX();
    }
    
    public int getPlayerY(){
        return player.getRenderY();
    }
    
    
    
    public void addIntersection(int x, int y, boolean open){
        PathXIntersection newInter = new PathXIntersection();
        newInter.setX(x + 180 - vp.getViewportX());
        newInter.setY(y + 20 - vp.getViewportY()); 
        
        newInter.setRenderX(x + 180 - vp.getViewportX());
        newInter.setRenderY(y + 20 - vp.getViewportY());
        
        newInter.setOpen(open);
        
        newInter.setId(intersections.size());
        
        intersections.add(newInter);
        
    }
    
    public void pause(){
        if(paused){
            paused = false;
        } else {
            paused = true;
        }
    }
    
    public boolean paused(){
        return paused;
    }
    
    public void calculatePath(int id1, int id2){
//1  function Dijkstra(Graph, source):
//2      dist[source] := 0                     // Initializations
//3      for each vertex v in Graph:           
//4          if v ≠ source
//5              dist[v] := infinity           // Unknown distance from source to v
//6              previous[v] := undefined      // Predecessor of v
//7          end if
//8          PQ.add_with_priority(v,dist[v])
//9      end for 
//10
//11
//12     while PQ is not empty:                // The main loop
//13         u := PQ.extract_min()             // Remove and return best vertex
//14         for each neighbor v of u:         // where v has not yet been removed from PQ.
//15             alt = dist[u] + length(u, v) 
//16             if alt < dist[v]              // Relax the edge (u,v) 
//17                 dist[v] := alt 
//18                 previous[v] := u
//19                 PQ.decrease_priority(v,alt)
//20             end if
//21         end for
//22     end while
//23     return previous[]
        PriorityQueue<PathXCar> queue;
        int distance[] = new int[intersections.size()];
        int previous[] = new int[intersections.size()];
        distance[id1] = 0;
        for(PathXIntersection inter : intersections){
            if(inter.getId() != id1){
                distance[inter.getId()] = 9999999;
                previous[inter.getId()] = -1;
            }
        }
    }
        
    public void setStartingImage(BufferedImage img){ startingImage = img; }  
    public BufferedImage getStartingImage(){ return startingImage; }
    public void setDestinationImage(BufferedImage img){ destinationImage = img; }
    public BufferedImage getDestinationImage(){ return destinationImage; }
    public void setIngame(boolean in){ inGame = in; }
    public boolean inGame(){ return inGame; }
    public void setViewport(Viewport view){ vp = view; }  
    public void setState(String state){ currentState = state; }
    public int getBackgroundWidth(){ return backgroundImage.getWidth(); }
    public int getBackgroundHeight(){ return backgroundImage.getHeight(); }
    public Viewport getViewport(){ return vp; }
    public void setSpriteID(int id){ spriteID = id; }
    public void setLevelDescription(String desc){ levelDescription = desc; }
    public int getReward(){ return money; }
    public void setReward(int amount){ money = amount; }
    public String getLevelName(){ return levelName; }
    public void setLevelName(String name){ levelName = name; }
    public String getLevelDescription(){ return levelDescription; }    
    public int getSpriteID(){ return spriteID; }
    public int getX(){ return renderx; }
    public int getY(){ return rendery; }
    public String getState(){ return currentState; }
    public void setX(int x){ this.x = x;}
    public void setY(int y){this.y = y;}
    public int getNumIntersections(){ return intersections.size(); }
    public void setNumPolice(int police){ numPolice = police; }
    
    public void addIntersection(PathXIntersection newIntersection){
        newIntersection.setId(intersections.size());
        intersections.add(newIntersection);
    }
    
    public ArrayList<PathXIntersection> getIntersections(){
        return (ArrayList<PathXIntersection>)intersections.clone();
    }
    
    public void addRoad(PathXRoad road){
        roads.add(road);
        intersections.get(road.getId1()).addRoad(road);
        if(!road.oneWay())
            intersections.get(road.getId2()).addRoad(road);
        
    }
    
    public void initConnections(){
        for(PathXRoad road : roads){
            if(road.oneWay()){
                intersections.get(road.getId1()).addConnection(road.getId2(), road.getCost());
            } else {
                intersections.get(road.getId1()).addConnection(road.getId2(), road.getCost());
                intersections.get(road.getId2()).addConnection(road.getId1(), road.getCost());
            }
        }
    }
    
    public ArrayList<PathXRoad> getRoads(){
        return (ArrayList<PathXRoad>)roads.clone();
    }
    
    public void setBackgroundImage(BufferedImage img){
        backgroundImage = img;
    }
    
    public BufferedImage getBackgroundImage(){
        return backgroundImage;
    }
    
    public void updateLocation(int xInc, int yInc){
        
        if(!inGame){
            renderx = x + VIEWPORT_MARGIN_LEFT - vp.getViewportX();
            rendery = y + VIEWPORT_MARGIN_TOP + NORTH_PANEL_HEIGHT - vp.getViewportY();
        } else{
            updateIntersectionLocations();
            updateRoadLocations();
            updatePlayerLocations();
        }
        //i++;
        
    }
    
    
    
    public void updatePlayerLocations(){
        //PathXRoad rd = intersections.get(0).getRoads().get(0);
        player.setRenderX(player.getX() - vp.getViewportX());
        player.setRenderY(player.getY() - vp.getViewportY());
    }
    
    public void initPlayerLocation(){
        player.setRenderX(intersections.get(0).getRoads().get(0).getXPosition(0) + 165 - vp.getViewportX() - 12);
        player.setRenderY(intersections.get(0).getRoads().get(0).getYPosition(0) + 165 - vp.getViewportY() - 12);
        player.setX(intersections.get(0).getRoads().get(0).getXPosition(0) + 165 - vp.getViewportX() - 12);
        player.setY(intersections.get(0).getRoads().get(0).getYPosition(0) + 165 - vp.getViewportY() - 12);
        
        
    }
    
    public void intiPolice(){
        int temp;
        for(int i = 0; i < numPolice; i++){
            temp = ((int)Math.random()) + (intersections.size() - 2) + 1;
            
            police.add(new PathXCar(PathXCarType.POLICE_TYPE.toString(), temp));
            police.get(i).setRenderX(intersections.get(police.get(i).getIntersection()).getRoads().get(0).getXPosition(0) + 165 - vp.getViewportX() - 12);
            police.get(i).setRenderX(intersections.get(police.get(i).getIntersection()).getRoads().get(0).getYPosition(0) + 165 - vp.getViewportY() - 12);
        }
    }
    
    public ArrayList<PathXCar> getPolice(){
        return (ArrayList<PathXCar>)police;
    }
    
    public PathXIntersection getIntersection(int id){
        return intersections.get(id);
    }
    
    public void changeState(String newState){
        currentState = newState;
    }
    
    public void updateRoadLocations(){
        for(PathXRoad rd : roads){
            PathXIntersection inter = getIntersection(rd.getId1());
            rd.setId1Coords(inter.getRenderX(), inter.getRenderY());
            inter = getIntersection(rd.getId2());
            rd.setId2Coords(inter.getRenderX(), inter.getRenderY());
        }
    }        
    
    
}
