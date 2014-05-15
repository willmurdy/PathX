/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
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
    
    protected boolean inGame;
    
    private boolean won;
    
    private boolean paused;
    
    private PathXCar player;
    
    private ArrayList<PathXCar> police;
    private ArrayList<PathXCar> zombies;
    private ArrayList<PathXCar> bandits;
    
    private int numPolice;
    private int numZombies;
    private int numBandits;
    
    private boolean unlocked;
    private boolean started;
    private boolean intangable;
    private boolean invincible;
    
    MovePolice move;
    
    private double playerSpeed;
    
    private boolean showEndDialog;

    
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
        zombies  = new ArrayList<>();
        bandits  = new ArrayList<>();
        
        roads = new ArrayList<PathXRoad>();
        
        won = false;
        
        paused = false;
        
        started = false;
        
    }
    
    public PathXLevel(){
        intersections = new ArrayList<>();
        
        roads = new ArrayList<>();
        
        inGame = false;
        
        player = new PathXCar(PathXCarType.PLAYER_TYPE.toString(), 0);
        
        police  = new ArrayList<>();
        zombies  = new ArrayList<>();
        bandits  = new ArrayList<>();

        won = false;
        
        paused = false;
        
        started = false;
        
        showEndDialog = false;
        
        playerSpeed = 1.0;
        
        intangable = false;
        
        invincible = false;
        
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
    
    //Spaecials
    
    public void makeIntangable(){
        class Intangable extends Thread{
            @Override
            public void run(){
                intangable = true;
                try {
                    this.wait(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                }
                intangable = false;
            }
        }
        Intangable in = new Intangable();
        in.start();
    }
    
    public boolean intangable(){
        return intangable;
    }
    
    public void makeInvincible(){
        class Invincible extends Thread{
            public void run(){
                invincible = true;
                try {
                    this.wait(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                }
                invincible = false;
            }
        }
        Invincible in = new Invincible();
        in.start();
    }
    
    public boolean invincible(){
        return invincible;
    }
    
    public void killPolice(int i){
        police.get(i).setRender(false);
    }
    
    public void movePlayerToIntersection(int id){
        int intersectionId;
        PathXIntersection intersection;
        ArrayList<PathXRoad> roads;
        int i = 0;
        PathXRoad roadToTravel = new PathXRoad();
        boolean found = false;
        boolean forward = true;
       // ArrayList<Integer> path = this.calculatePath(player.getIntersection(), destinationId);
        //path.remove(0);
        //for(Integer id : path){
        if(!player.inMotion() && started){
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
                        wait = (long)((110 -road.getSpeedLimit()) * playerSpeed);// * 100;
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
                                if(!inGame)
                                    break;
                                player.setRenderX(road.getXPosition(i) - vp.getViewportX());
                                player.setRenderY(road.getYPosition(i) - vp.getViewportY());
                                player.setX(road.getXPosition(i));
                                player.setY(road.getYPosition(i));
                                wait = (long)((110 -road.getSpeedLimit()) * playerSpeed);
                                try {
                                    Thread.sleep(wait);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            player.setIntersectionId(road.getId2());
                        } else { 
                            for(int i = x.size() - 1; i > 0; i--){
                                if(!inGame)
                                    break;
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
                        if(player.getIntersection() == 1)
                            endGameAsWin();
                    }
                    

                }
                
                MovePlayer p = new MovePlayer(roadToTravel, forward);
                p.start();
            }
        
        }
        //}
        
    }
    
    public void respondToZombieCollision(){
        playerSpeed = playerSpeed - 0.1;
        if(playerSpeed == 0){
            endGameAsLoss();
        }
    }
    
    public void endGameAsWin(){
        System.out.println("YOU WIN!");
        won = true;
        this.setState(PathXLevelState.COMPLETED_STATE.toString());
        inGame = false;
        showEndDialog = true;
    }
    
    public void endGameAsLoss(){
        System.out.println("YOU LOSE");
        won = false;
        inGame = false;
        showEndDialog = true;
        
    }
    
    public void resetLevel(){
        //setLocation(0,0);
        this.initPlayerLocation();
        player.setIntersectionId(0);
        //this.intiZombies();
        this.intiPolice();
        //this.intiBandits();
        //this.intiBandits();
        inGame = false;
        showEndDialog = false;
    }
    
    public boolean showEndDialog(){
        return showEndDialog;
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
    
    public ArrayList<Integer> calculatePath(int id1, int id2){
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
//        PriorityQueue<PathXCar> queue;
//        int distance[] = new int[intersections.size()];
//        int previous[] = new int[intersections.size()];
//        distance[id1] = 0;
//        for(PathXIntersection inter : intersections){
//            if(inter.getId() != id1){
//                distance[inter.getId()] = 9999999;
//                previous[inter.getId()] = -1;
//            }
//        }
        
        LinkedList<PathXIntersection> que = new LinkedList<>();
        ArrayList<Integer> path = new ArrayList<>();
        que.add(intersections.get(id1));
        path.add(id1);
        while(!que.isEmpty()){
            PathXIntersection temp = que.pop();
            if(temp.getId() == id2)
                return path;
            for(PathXRoad rd : temp.getRoads()){
                if(rd.getId1() == temp.getId()){ //id2 is the adjacent node
                    if(!path.contains(rd.getId2())){
                        path.add(rd.getId2());
                        que.add(intersections.get(rd.getId2()));
                    }
                } else {
                    if(!path.contains(rd.getId1())){
                        path.add(rd.getId1());
                        que.add(intersections.get(rd.getId1()));
                    }//end if
                }//end else
            }//end for loop
        }//end while loop
        return path;
    }//end method
    
    public void startGame(){
        started = true;
        this.startPolicePathing();
        this.startZombiePathing();
        Iterator i = this.calculatePath(0, 1).iterator();
        while(i.hasNext()){
            System.out.println(i.next().toString());
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
    public int getNumPolice(){ return numPolice; }
    public void setNumZombies(int zom){ numZombies = zom; }
    public void setNumBandits(int band){ numBandits = band; }
    public int getNumBandits(){ return numBandits; }
    public int getNumZombies(){ return numZombies; }
    public void setUnlocked(boolean un){ unlocked = un; }
    public boolean unlocked(){ return unlocked; }
    public boolean wonLevel(){ return won; }
    
    public void addIntersection(PathXIntersection newIntersection){
        newIntersection.setId(intersections.size());
        intersections.add(newIntersection);
    }
    
    public ArrayList<PathXIntersection> getIntersections(){ return (ArrayList<PathXIntersection>)intersections.clone(); }
    
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
            updatePoliceLocations();
            updateZombieLocations();
            updateBanditLocations();
        }
        //i++;
        
    }
    
    public void updatePlayerLocations(){
        //PathXRoad rd = intersections.get(0).getRoads().get(0);
        player.setRenderX(player.getX() - vp.getViewportX());
        player.setRenderY(player.getY() - vp.getViewportY());
    }
    
    public void updatePoliceLocations(){
        for(PathXCar po : police){
            po.setRenderX(po.getX() - vp.getViewportX());// + 150 - 12);
            po.setRenderY(po.getY() - vp.getViewportY());// + 150 - 12);
        }
    }
    
    public void updateZombieLocations(){
        for(PathXCar po : zombies){
            po.setRenderX(po.getX() - vp.getViewportX());// + 150 - 12);
            po.setRenderY(po.getY() - vp.getViewportY());// + 150 - 12);
        }
    }
    
    public void updateBanditLocations(){
        for(PathXCar po : bandits){
            po.setRenderX(po.getX() - vp.getViewportX() + 150 - 12);
            po.setRenderY(po.getY() - vp.getViewportY() + 150 - 12);
        }
    }
    
    public void initPlayerLocation(){
        player.setRenderX(intersections.get(0).getRoads().get(0).getXPosition(0) + 165 - vp.getViewportX() - 12);
        player.setRenderY(intersections.get(0).getRoads().get(0).getYPosition(0) + 165 - vp.getViewportY() - 12);
        player.setX(intersections.get(0).getRoads().get(0).getXPosition(0) + 165 - vp.getViewportX() - 12);
        player.setY(intersections.get(0).getRoads().get(0).getYPosition(0) + 165 - vp.getViewportY() - 12);
        
    }
    
    public void intiPolice(){
        int temp = 0;
        PathXCar po;
        police = new ArrayList<>();
        for(int i = 0; i < numPolice; i++){
            
            while(temp == 0)
                temp = (int)((Math.random()) * intersections.size()) ;
            po = new PathXCar(PathXCarType.POLICE_TYPE.toString(), temp);
            po.setX(intersections.get(temp).getX());
            po.setY(intersections.get(temp).getY());
            po.setRenderX(intersections.get(temp).getRenderX());//po.getX() + 180 - vp.getViewportX());
            po.setRenderY(intersections.get(temp).getRenderY());//po.getY() + 20 - vp.getViewportY());
            po.setIntersectionId(temp);
            police.add(po);
        }
    }
    
    public void intiZombies(){
        int temp;
        PathXCar zom;
        for(int i = 0; i < numZombies; i++){
            
            temp = (int)((Math.random()) * intersections.size()) ;
            zom = new PathXCar(PathXCarType.ZOMBIE_TYPE.toString(), temp);
            zom.setX(intersections.get(temp).getX());
            zom.setY(intersections.get(temp).getY());
            zom.setRenderX(intersections.get(temp).getRenderX());//po.getX() + 180 - vp.getViewportX());
            zom.setRenderY(intersections.get(temp).getRenderY());//po.getY() + 20 - vp.getViewportY());
            zom.setIntersectionId(temp);
            zombies.add(zom);
        }
    }
    
    public void initZombiePath(){
        ArrayList<Integer> path;
        for(PathXCar zom : zombies){
            path = new ArrayList<>();
            int startingNode = zom.getIntersection();
            int nextNode = 0;
            int currentNode = startingNode;
            int prevNode = -1;
            ArrayList<Integer> adjacentNodes;
            boolean look = true, cont = true;
            path.add(startingNode);
            while(cont){
                look = true;
                adjacentNodes = intersections.get(currentNode).getAdjacentNodes();
                while(look){
                    nextNode = (int)(Math.random() * adjacentNodes.size());
                    nextNode = adjacentNodes.get(nextNode);
                    if(nextNode != prevNode)
                        look = false;
                } // end look while
                path.add(nextNode);
                prevNode = currentNode;
                currentNode = nextNode;
                if(currentNode == startingNode)
                    cont = false;
            }//end cont while
            //the intersection nodes are selected now lets create the x,y coord path
            //that the zombie will take
            
            for(int i = 0; i < path.size() - 1; i++){
                for(PathXRoad rd : roads){
                    if(rd.oneWay()){
                        //intersection check if
                        if(rd.getId1() == (int)path.get(i) && rd.getId2() == (int)path.get(i+1)){ 
                            ArrayList<Integer> tempx = rd.getXList();
                            ArrayList<Integer> tempy = rd.getYList();
                            //add the coords of the road to the zombies path
                            for(int j = 0; j < tempx.size(); j++){         
                                zom.addIntergectionToPath(tempx.get(j));
                                zom.addIntergectionToPath(tempy.get(j));
                            }
                        }// end intersection check if
                    } else {//end oneway if 
                        if(rd.getId1() == (int)path.get(i) && rd.getId2() == (int)path.get(i+1)){ 
                            ArrayList<Integer> tempx = rd.getXList();
                            ArrayList<Integer> tempy = rd.getYList();
                            //add the coords of the road to the zombies path
                            for(int j = 0; j < tempx.size(); j++){         
                                zom.addIntergectionToPath(tempx.get(j));
                                zom.addIntergectionToPath(tempy.get(j));
                            }
                        } else {
                            if(rd.getId2() == (int)path.get(i) && rd.getId1() == (int)path.get(i+1)){ 
                                ArrayList<Integer> tempx = rd.getXList();
                                ArrayList<Integer> tempy = rd.getYList();
                                //add the coords of the road to the zombies path
                                for(int j = tempx.size() - 1; j > 0; j--){         
                                    zom.addIntergectionToPath(tempx.get(j));
                                    zom.addIntergectionToPath(tempy.get(j));
                                }
                            }
                        }
                    }
                }//end roads for loop
            }//end int i for loop
            
        }//end for loop
    }
    
    public void intiBandits(){
        int temp;
        PathXCar ban;
        for(int i = 0; i < numBandits; i++){
            
            temp = (int)((Math.random()) * intersections.size()) ;
            ban = new PathXCar(PathXCarType.BANDIT_TYPE.toString(), temp);
            ban.setX(intersections.get(temp).getX());
            ban.setY(intersections.get(temp).getY());
            ban.setRenderX(intersections.get(temp).getRenderX());//po.getX() + 180 - vp.getViewportX());
            ban.setRenderY(intersections.get(temp).getRenderY());//po.getY() + 20 - vp.getViewportY());
            ban.setIntersectionId(temp);
            bandits.add(ban);
        }
    }
    
    public ArrayList<PathXCar> getPolice(){
        return (ArrayList<PathXCar>)police;
    }
    
    public ArrayList<PathXCar> getZombies(){
        return (ArrayList<PathXCar>)zombies;
    }
    
    public ArrayList<PathXCar> getBandits(){
        return (ArrayList<PathXCar>)bandits;
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
    
    public void startPolicePathing(){
        
        for(PathXCar cp : police){
            move = new MovePolice(cp);
            move.start();
        }
    }
    
    public void startZombiePathing(){
        MoveZombie move;
        this.initZombiePath();
        for(PathXCar zom : zombies){
            move = new MoveZombie(zom);
            move.start();
        }
    }
    
    class MovePolice extends Thread{
        
        PathXCar cop;
        int id;
        long wait;
        
        MovePolice(PathXCar po){
            cop = po;
        }
        
        public void run(){
            int prevNode = -1;
            PathXIntersection inter;
            ArrayList<Integer> adjacent;
            int nextNode = -1;
            ArrayList<Integer> x;
            ArrayList<Integer> y;
            boolean cont = true, look = true;
            while(inGame && cop.render){
                id = cop.getIntersection();
                inter = intersections.get(id);
                adjacent = inter.getAdjacentNodes();
                Iterator<PathXRoad> road;
                look = true;
                while(look){
                    if(adjacent.size() == 1){
                        nextNode = adjacent.get(0);
                        break;
                    }
                    nextNode = (int)(Math.random() * adjacent.size());
                    nextNode = adjacent.get(nextNode);
                    if(!(prevNode == nextNode) && nextNode != 0){
                        look = false;
                    }
                }
                    road = roads.iterator();
                while(road.hasNext() && cont){
                    PathXRoad rd = road.next();
                    if(rd.oneWay()){
                        if(nextNode == rd.getId2() && id == rd.getId1()){
                            wait = 125 -rd.getSpeedLimit();
                            x = rd.getXList();
                            y = rd.getYList();
                            for(int i = 0; i < x.size(); i++){
                                if(paused){
                                    i--;
                                }
                                if(!inGame || !cop.render()){
                                    break;
                                }
                                cop.setRenderX(rd.getXPosition(i) - vp.getViewportX());
                                cop.setRenderY(rd.getYPosition(i) - vp.getViewportY());
                                cop.setX(rd.getXPosition(i));
                                cop.setY(rd.getYPosition(i));
                                if(cop.getX() == player.getX() && cop.getY() == player.getY()){
                                    
                                }
                                try {
                                    Thread.sleep(wait);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            if(!inGame || !cop.render){
                                break;
                            }
                            cont = false;
                            prevNode = id;
                            cop.setIntersectionId(rd.getId2());
                            break;
                        }
                    } else { //if the road it two way
                        if(nextNode == rd.getId1() && id == rd.getId2()){
                            wait = 125 -rd.getSpeedLimit();
                            x = rd.getXList();
                            y = rd.getYList();
                            for(int i = x.size() - 1; i > 0; i--){
                                if(paused){
                                    i++;
                                }
                                if(!inGame || !cop.render){
                                    break;
                                }
                                cop.setRenderX(rd.getXPosition(i) - vp.getViewportX());
                                cop.setRenderY(rd.getYPosition(i) - vp.getViewportY());
                                cop.setX(rd.getXPosition(i));
                                cop.setY(rd.getYPosition(i));
                                try {
                                    Thread.sleep(wait);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            if(!inGame || !cop.render){
                                break;
                            }
                            cont = false;
                            prevNode = id;
                            cop.setIntersectionId(rd.getId1());
                            break;
                        } else {
                            if(nextNode == rd.getId2() && id == rd.getId1()){
                            wait = 125 -rd.getSpeedLimit();
                            x = rd.getXList();
                            y = rd.getYList();
                            for(int i = 0; i < x.size(); i++){
                                if(paused){
                                    i--;
                                }
                                if(!inGame || !cop.render){
                                    break;
                                }
                                cop.setRenderX(rd.getXPosition(i) - vp.getViewportX());
                                cop.setRenderY(rd.getYPosition(i) - vp.getViewportY());
                                cop.setX(rd.getXPosition(i));
                                cop.setY(rd.getYPosition(i));
                                try {
                                    Thread.sleep(wait);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            if(!inGame || !cop.render){
                                
                                break;
                            }
                            cont = false;
                            prevNode = id;
                            cop.setIntersectionId(rd.getId2());
                            break;
                            }
                        }
                    }
                }//end inner while
                cont = true;
            }//end while
        }//end run method
        
    }//end thread class
    
    class MoveZombie extends Thread{
        
        PathXCar zombie;
        int id;
        long wait;
        
        MoveZombie(PathXCar zom){
            zombie = zom;
        }
        
        public void run(){
            ArrayList<Integer> path = zombie.getPath();
            while(inGame){
                int wait = 50;
                for(int i = 0; i < path.size() - 2; i += 2){
                    if(paused)
                        i -= 2;
                    if(!inGame)
                        break;
                    zombie.setRenderX(path.get(i) - vp.getViewportX());
                    zombie.setRenderY(path.get(i+1) - vp.getViewportY());
                    zombie.setX(path.get(i));
                    zombie.setY(path.get(i + 1));
                    try {
                        Thread.sleep(wait);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
                        }       
                }//end for loop
                if(!inGame){
                    break;
                }
            }//end while loop
        }//end run method
        
    }//end thread class
    
}

//class MovePolice extends Thread {
//
//                        PathXRoad road;
//                        boolean forward;
//                        long wait;
//
//                        public MovePolice(PathXRoad rd, boolean forward){ 
//                            road = rd; 
//                            wait = 100 -road.getSpeedLimit();// * 100;
//                            this.forward = forward;
//                        }
//
//                        public void run() {
//                            while(inGame){
//                                ArrayList<Integer> x = road.getXList();
//                                ArrayList<Integer> y = road.getYList();
//                                player.setInMotion(true);
//                                if(forward){
//                                    for(int i = 0; i < x.size(); i++){
//                                        if(paused){
//                                            i--;
//                                        }
//                                        player.setRenderX(road.getXPosition(i) - vp.getViewportX());
//                                        player.setRenderY(road.getYPosition(i) - vp.getViewportY());
//                                        player.setX(road.getXPosition(i));
//                                        player.setY(road.getYPosition(i));
//                                        try {
//                                            Thread.sleep(wait);
//                                        } catch (InterruptedException ex) {
//                                            Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
//                                        }
//
//                                    }
//
//                                    player.setIntersectionId(road.getId2());
//                                } else { 
//                                    for(int i = x.size() - 1; i > 0; i--){
//                                        player.setRenderX(road.getXPosition(i) - vp.getViewportX());
//                                        player.setRenderY(road.getYPosition(i) - vp.getViewportY());
//                                        player.setX(road.getXPosition(i));
//                                        player.setY(road.getYPosition(i));
//                                        try {
//                                            Thread.sleep(wait);
//                                        } catch (InterruptedException ex) {
//                                            Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
//                                        }
//                                        if(paused){
//                                            i++;
//                                        }
//                                    }
//                                    player.setIntersectionId(road.getId1());
//                                }
//                                player.setInMotion(false);
//                                //kill();
//                            }
//                        }
//
//
//}
