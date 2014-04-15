/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Stack;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import static pathx.PathXConstants.*;

/**
 *
 * @author willmurdy
 */
public class PathXDataModel extends MiniGameDataModel{
    
        // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;

    // THIS STORES THE TILES ON THE GRID DURING THE GAME
//    private ArrayList<SortingHatTile> tilesToSort;

    // THE LEGAL TILES IN ORDER FROM LOW SORT INDEX TO HIGH
//    private ArrayList<SnakeCell> snake;

    // GAME GRID AND TILE DATA
    private int gameTileWidth;
    private int gameTileHeight;
    private int numGameGridColumns;
    private int numGameGridRows;

    // THESE ARE THE TILES STACKED AT THE START OF THE GAME
//    private ArrayList<SortingHatTile> stackTiles;
//    private int stackTilesX;
//    private int stackTilesY;

    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
//    private ArrayList<SortingHatTile> movingTiles;

    // THIS IS THE TILE THE USER IS DRAGGING
//    private SortingHatTile selectedTile;
//    private int selectedTileIndex;

    // THIS IS THE TEMP TILE
//    private SortingHatTile tempTile;
    
    //Levels
    private ArrayList<PathXLevel> levels;

    // KEEPS TRACK OF HOW MANY BAD SPELLS WERE CAST
    private int badSpellsCounter;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // LEVEL
    private String currentLevel;

    // THE SORTING ALGORITHM WHICH GENERATES THE PROPER TRANSACTIONS
//    private SortingHatAlgorithm sortingAlgorithm;

    // THE PROPER TRANSACTIONS TO USE FOR COMPARISION AGAINST PLAYER MOVES
//    private ArrayList<SortTransaction> properTransactionOrder;
//    private int transactionCounter;
    
    private Stack prevMoves;
    
    private int balance;
    
    public PathXDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
//        stackTiles = new ArrayList();
//        movingTiles = new ArrayList();
//        tilesToSort = new ArrayList();

        // NOTHING IS BEING DRAGGED YET
//        selectedTile = null;
//        selectedTileIndex = -1;
//        tempTile = null;
        prevMoves = new Stack();
        levels = new ArrayList<PathXLevel>();
        balance = 1000;     
    }
    
    public boolean notStarted(){
        return true;
    }
    
    public void initViewport(){
        
        viewport.setNorthPanelHeight(NORTH_PANEL_HEIGHT);
        viewport.setViewportSize(WINDOW_WIDTH -VIEWPORT_MARGIN_LEFT - VIEWPORT_MARGIN_RIGHT, WINDOW_HEIGHT - VIEWPORT_MARGIN_BOTTOM - NORTH_PANEL_HEIGHT);
        viewport.setGameWorldSize(MAP_WIDTH, MAP_HEIGHT);
        viewport.setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        //viewport.initViewportMargins();
        viewport.updateViewportBoundaries();
        viewport.scroll(150, 150);
        
    }
    
    public void initLevels(){
                
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        ArrayList<String> levels = props.getPropertyOptionsList(PathX.PathXPropertyType.LEVEL_OPTIONS);
        
        
        PathXLevel level = new PathXLevel(208, 532, viewport, PathXLevelState.AVAILABLE_STATE.toString(), "San Fransisco");
        levels.add(level);
        level = new PathXLevel(457, 478, viewport, PathXLevelState.LOCKED_STATE.toString(), "Salt lake city");
        levels.add(level);
    }
    
    public PathXLevel getLevel(int levelNum){
        return levels.get(levelNum);
    }
    
    public PathXLevel getLevelSprite(int spriteID){
        for(int i = 0; i < levels.size(); i++)
            if(levels.get(i).getSpriteID() == spriteID)
                return levels.get(i);
        return null;
    }
    
    public void updateLevelLocations(int x, int y){
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).updateLocation(x, y);
        }
    }
    
    public int getNumLevels(){
        return levels.size();
    }
    
    public int getBalance(){
        return balance;
    }

    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset(MiniGame game) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateAll(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDebugText(MiniGame game) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
