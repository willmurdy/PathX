/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import pathx.data.PathXDataModel;
import pathx.PathXConstants.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.*;
import pathx.data.PathXIntersection;
import properties_manager.PropertiesManager;

/**
 *
 * @author willmurdy
 */
public class PathXPanel extends JPanel {
     // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel data;
    
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
 
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;
    
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;
    
    // THIS IS FOR WHEN THE USE MOUSES OVER A TILE
    private BufferedImage blankTileMouseOverImage;
    
    private JTextArea text;
    
    private Viewport viewport;
    
    /**
     * This constructor stores the game and data references,
     * which we'll need for rendering.
     * 
     * @param initGame The Sorting Hat game that is using
     * this panel for rendering.
     * 
     * @param initData The Sorting Hat game data.
     */
    public PathXPanel(MiniGame initGame, PathXDataModel initData)
    {
        game = initGame;
        data = initData;
        viewport = data.getViewport();
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        text = new JTextArea();
                text.setLineWrap(true);
                text.setFont(FONT_BALANCE);
                text.setWrapStyleWord(true);
                text.setBounds(JTEXTAREA_X, JTEXTAREA_Y, 400, 150);
                text.setLocation(200, 150);
    }
    
    // MUTATOR METHODS
        // -setBlankTileImage
        // -setBlankTileSelectedImage
    
    /**
     * This mutator method sets the base image to use for rendering tiles.
     * 
     * @param initBlankTileImage The image to use as the base for rendering tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage)
    {
        blankTileImage = initBlankTileImage;
    }
    
    /**
     * This mutator method sets the base image to use for rendering selected tiles.
     * 
     * @param initBlankTileSelectedImage The image to use as the base for rendering
     * selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage)
    {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }
    
    public void setBlankTileMouseOverImage(BufferedImage initBlankTileMouseOverImage)
    {
        blankTileMouseOverImage = initBlankTileMouseOverImage;
    }

    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
        
            // CLEAR THE PANEL
            super.paintComponent(g);
        
            
            
        
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            
            if(((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ||
                    ((PathXMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE))
                    renderViewport(g);

            if(((PathXMiniGame)game).isCurrentScreenState(HELP_SCREEN_STATE))
                    renderHelp(g);
            // ONLY RENDER THIS STUFF IF WE'RE ACTUALLY IN-GAME
            if (!data.notStarted())
            {
                // RENDER THE SNAKE
//                if (!data.won())
//                    renderSnake(g);
                
                // AND THE TILES
                renderTiles(g);
                
                // AND THE DIALOGS, IF THERE ARE ANY
                
                                
                // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
                // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
                renderGrid(g);
                
                // RENDER THE ALGORITHM NAME
                renderHeader(g);
               
            }
            
            
            
            renderStats(g);
            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);

            // AND THE TIME AND TILES STATS
            
            renderDialogs(g);

            

        
            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);

        }
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }
    
    // RENDERING HELPER METHODS
        // - renderBackground
        // - renderGUIControls
        // - renderSnake
        // - renderTiles
        // - renderDialogs
        // - renderGrid
        // - renderDebuggingText
    
    /**`
     * Renders the background image, which is different depending on the screen. 
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
                
    }
    
        public void renderHelp(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
//        JEditorPane hp = new JEditorPane();
//        hp.setBounds(100, 150, 550, 400);
//        
        PropertiesManager props = PropertiesManager.getPropertiesManager();            
        String helpText = props.getProperty(PathXPropertyType.TEXT_HELP);
//            
//        hp.setText(helpText);
//        
//        hp.setEditable(false);
//        hp.setEnabled(true);

                
    }
    
    public void renderViewport(Graphics g){
        
        Viewport vp = data.getViewport();
        Sprite bg = game.getGUIDecor().get(MAP_TYPE);
        Image img;
        if(!bg.getState().equals(GAMEPLAY_SCREEN_STATE.toString())){     
            SpriteType bgST = bg.getSpriteType();
            img = bgST.getStateImage(bg.getState());
        } else{
             img = data.getLevel(data.getCurrentLevelInt()).getBackgroundImage();
        }
        
        
        g.drawImage(img, vp.getViewportMarginLeft(), vp.getViewportMarginTop(),
                vp.getViewportMarginLeft() + vp.getViewportWidth(), vp.getViewportMarginTop() + vp.getViewportHeight(), 
                vp.getViewportX(), vp.getViewportY(), vp.getViewportWidth() + vp.getViewportX(), vp.getViewportHeight() + vp.getViewportY(), this);
        //g.setColor(Color.BLACK);
       if(((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
            g.drawRect(VIEWPORT_MARGIN_LEFT, VIEWPORT_MARGIN_TOP + NORTH_PANEL_HEIGHT,
                    710, 500);
        if(bg.getState().equals(GAMEPLAY_SCREEN_STATE.toString())){
            renderIntersections(g);
        }
        
    }
    
    public void renderLevels(Sprite s, Graphics g){
        
        int x = data.getLevelSprite(s.getID()).getX();
        int y = data.getLevelSprite(s.getID()).getY();

        
        s.setX(x);
        s.setY(y);  
//        !(s.getX() < viewport.getViewportX() || s.getX() > (viewport.getViewportX() + viewport.getViewportWidth())) 
//                && !(s.getY() < viewport.getViewportY() || s.getY() > (viewport.getViewportY() + viewport.getViewportHeight()))
        if((x > VIEWPORT_MARGIN_LEFT - 5 && x < VIEWPORT_MARGIN_LEFT + 710 - 20) && 
                (y > NORTH_PANEL_HEIGHT + 10) && y < VIEWPORT_MARGIN_TOP + NORTH_PANEL_HEIGHT + 475){
            renderSprite(g, s);
            if(s.getState().equals(MOUSE_OVER_STATE)){
                 String levelInfo = data.getLevelSprite(s.getID()).getLevelName() + " - $" + data.getLevelSprite(s.getID()).getReward();
                 x = 750 - levelInfo.length() * 20;
                 y = LEVEL_NAME_Y;
                 g.setFont(FONT_BALANCE);
                 g.drawString(levelInfo, x, y);
            }
        }
           
        
    }
    
    public void renderIntersections(Graphics g){
        ArrayList<PathXIntersection> intersections = data.getLevel(data.getCurrentLevelInt()).getIntersections();
        Sprite s = game.getGUIDecor().get(INTERSECTION_TYPE);
        for(int i = 0; i < intersections.size(); i++){
            int x = intersections.get(i).getRenderX();
            int y = intersections.get(i).getRenderY();
            if((x > 180 - 5 && x < 730 - 20) && (y > 20) && y < 610-5){
                s.setX(x);
                s.setY(y);
                if(intersections.get(i).open())
                    s.setState(OPEN_STATE);
                else
                    s.setState(CLOSED_STATE);

                renderSprite(g, s);
            } else {
                s.setState(INVISIBLE_STATE);
            }
        }
    }

    /**
     * Renders all the GUI decor and buttons.
     * 
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE 
                    && s.getSpriteType().getSpriteTypeID() != MAP_TYPE)
                renderSprite(g, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            if(s.getSpriteType().getSpriteTypeID().equals(LEVEL_SELECT_BUTTON_TYPE))
                renderLevels(s, g);
            else 
                renderSprite(g, s);
        }
    }
    
    public void renderHeader(Graphics g)
    {
        //g.setColor(COLOR_ALGORITHM_HEADER);
        
    }
    

    /**
     * This method renders the on-screen stats that change as
     * the game progresses. This means things like the game time
     * and the number of tiles remaining.
     * 
     * @param g the Graphics context for this panel
     */
    public void renderStats(Graphics g)
    {
//        // RENDER THE GAME TIME AND THE TILES LEFT FOR IN-GAME
//        if (((SortingHatMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE) 
//                && data.inProgress() || data.isPaused())
//        {
//            // RENDER THE TILES LEFT
//            g.setFont(FONT_TEXT_DISPLAY);
//            g.setColor(Color.BLACK);
        
        if(((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
            String balance = "Balance: $" + data.getBalance();
            int x = BALANCE_X;
            int y = BALANCE_Y;
            g.setFont(FONT_BALANCE);
            g.drawString(balance, x, y);
            
            String goal = "Goal: $100,000";
            x = GOAL_X;
            y = GOAL_Y;
            g.setFont(FONT_BALANCE);
            g.drawString(goal, x, y);
        }
        
        if(((PathXMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)){
            g.drawRect(180, 20, 550, 610-20);
            g.drawRect(20, 500, 160, 130-20);
            g.setFont(FONT_BALANCE);
            if(((PathXDataModel)data).getCurrentLevelInt() >= 0){
                g.drawString(((PathXDataModel)data).getCurrentLevel(), 190, 50);
                g.drawString("$" + Integer.toString(((PathXDataModel)data).getCurrentLevelReward()), 190, 80);
            }
            //g.drawRect(20, 200, 160, 200);
            
        }
        
//            
//            //Render miscasts
//            String miscasts = Integer.toString(data.getBadSpellsCounter());
//            int mX = TILE_COUNT_X + TILE_COUNT_OFFSET;
//            int mY = TILE_COUNT_Y + TILE_TEXT_OFFSET;
//            g.drawString(miscasts, mX, mY);
//            
//            //render Sorting algorithm
//            String algorithm = data.getCurrentLevel().substring(21);
//            if(algorithm.startsWith("B"))
//                algorithm = "Bubble Sort";
//            else
//                algorithm = "Selection sort";
//            int aX = TEMP_TILE_X + TEMP_TILE_OFFSET_X;
//            int aY = TEMP_TILE_TEXT_OFFSET_Y ;
//            g.setFont(FONT_ALGORITHM);
//            g.drawString(algorithm, aX, aY);
//        }        
//        
//        // IF THE STATS DIALOG IS VISIBLE, ADD THE TEXTUAL STATS
//        if (game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(SortingHatTileState.VISIBLE_STATE.toString()))
//        {
//            g.setFont(FONT_STATS);
//            g.setColor(COLOR_STATS);
//            String currentLevel = data.getCurrentLevel();
//            int lastSlash = currentLevel.lastIndexOf("/");
//            String levelName = currentLevel.substring(lastSlash+1);
//            SortingHatRecord record = ((SortingHatMiniGame)game).getPlayerRecord();
//
//            // GET ALL THE STATS
//            String algorithm = record.getAlgorithm(currentLevel);
//            int games = record.getGamesPlayed(currentLevel);
//            int wins = record.getWins(currentLevel);
//            int perfectWins = record.getPerfectWins(currentLevel);
//            long fastestPerfectWin = record.getFastestPerfectWin(currentLevel);
//            
//
//            // GET ALL THE STATS PROMPTS
//            PropertiesManager props = PropertiesManager.getPropertiesManager();            
//            String algorithmPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_ALGORITHM);
//            String gamesPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_GAMES);
//            String winsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_WINS);
//            String perfectWinsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_PERFECT_WINS);
//            String fastestPerfectWinsPrompt = props.getProperty(SortingHatPropertyType.TEXT_LABEL_STATS_FASTEST_PERFECT_WIN);
//
//            // NOW DRAW ALL THE STATS WITH THEIR LABELS
//            int dot = levelName.indexOf(".");
//            levelName = levelName.substring(0, dot);
//            g.drawString(levelName,                                     STATS_LEVEL_X, STATS_LEVEL_Y);
//            g.drawString(algorithmPrompt + algorithm,                   STATS_LEVEL_X, STATS_ALGORITHM_Y);
//            g.drawString(gamesPrompt + games,                           STATS_LEVEL_X, STATS_GAMES_Y);
//            g.drawString(winsPrompt + wins,                             STATS_LEVEL_X, STATS_WINS_Y);
//            g.drawString(perfectWinsPrompt + perfectWins,               STATS_LEVEL_X, STATS_PERFECT_WINS_Y);
//            if(fastestPerfectWin == 0)
//                g.drawString(fastestPerfectWinsPrompt + "--:--:--",  STATS_LEVEL_X, STATS_FASTEST_PERFECT_WIN_Y);
//            else
//                g.drawString(fastestPerfectWinsPrompt + data.timeToText(fastestPerfectWin),  STATS_LEVEL_X, STATS_FASTEST_PERFECT_WIN_Y);
//        }
    }
        
    /**
     * Renders all the game tiles, doing so carefully such
     * that they are rendered in the proper order.
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderTiles(Graphics g)
    {
//        // DRAW THE GRID
//        ArrayList<SortingHatTile> tilesToSort = data.getTilesToSort();
//        for (int i = 0; i < tilesToSort.size(); i++)
//        {
//            SortingHatTile tile = tilesToSort.get(i);
//            if (tile != null)
//                renderTile(g, tile);
//        }
//        
//        // THEN DRAW ALL THE MOVING TILES
//        Iterator<SortingHatTile> movingTiles = data.getMovingTiles();
//        while (movingTiles.hasNext())
//        {
//            SortingHatTile tile = movingTiles.next();
//            renderTile(g, tile);
//        }
//        
//        // AND THE SELECTED TILE, IF THERE IS ONE
//        SortingHatTile selectedTile = data.getSelectedTile();
//        if (selectedTile != null)
//            renderTile(g, selectedTile);
    }

    /**
     * Helper method for rendering the tiles that are currently moving.
     * 
     * @param g Rendering context for this panel.
     * 
     * @param tileToRender Tile to render to this panel.
     */
//    public void renderTile(Graphics g, SortingHatTile tileToRender)
//    {
//        // ONLY RENDER VISIBLE TILES
//        if (!tileToRender.getState().equals(SortingHatTileState.INVISIBLE_STATE.toString()))
//        {
//            Viewport viewport = data.getViewport();
//            int correctedTileX = (int)(tileToRender.getX());
//            int correctedTileY = (int)(tileToRender.getY());
//
//            // THEN THE TILE IMAGE
//            if(tileToRender.equals(data.getSelectedTile())){     //Added to get the 
//                tileToRender.setState(SortingHatTileState.SELECTED_STATE.toString());     //mouse over working
//            } else {
//                tileToRender.setState(SortingHatTileState.VISIBLE_STATE.toString());
//            }
//            SpriteType bgST = tileToRender.getSpriteType();
//            Image img = bgST.getStateImage(tileToRender.getState());
//            g.drawImage(img,    correctedTileX, 
//                                correctedTileY, 
//                                bgST.getWidth(), bgST.getHeight(), null); 
//            
//        }
//    }
    
    /**
     * Renders the game dialog boxes.
     * 
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
        
        //special case where the close button needs to be rendered ontop of the leveldiscription dialog
        renderSprite(g, game.getGUIButtons().get(CLOSE_BUTTON_TYPE));
        

        //used for rendering the level description dialog
        //renderLevelDescription(false, text);
        if(((PathXMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE)){
            if(game.getGUIDialogs().get(LEVEL_DIALOG_TYPE).getState().equals(VISIBLE_STATE)){
                g.setFont(FONT_BALANCE);
                g.drawString(((PathXDataModel)game.getDataModel()).getCurrentLevel(), LEVEL_NAME_DIALOG_X, LEVEL_NAME_DIALOG_Y);
                renderLevelDescription(true, text);
            } else {
                renderLevelDescription(false, text);
            }
        }
        
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     * 
     * @param g the Graphics context of this panel
     * 
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(INVISIBLE_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        }
    }
    
        public void renderLevelDescription(boolean enable, JTextArea text)
    {
        // ONLY RENDER THE VISIBLE ONES

                text.setText(((PathXDataModel)game.getDataModel()).getCurrentLevelDescription());
                this.add(text);
                text.setLocation(200, 150);
                if(enable && !text.isVisible())
                    text.setVisible(true);
                else 
                    if(!enable && text.isVisible())
                        text.setVisible(false);
    }

    /**
     * This method renders grid lines in the game tile grid to help
     * during debugging.
     * 
     * @param g Graphics context for this panel.
     */
    public void renderGrid(Graphics g)
    {
//        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
//        if (data.isDebugTextRenderingActive())
//        {
//            for (int i = 0; i < data.getNumGameGridColumns(); i++)
//            {
//                for (int j = 0; j < data.getNumGameGridRows(); j++)
//                {
//                    int x = data.calculateGridTileX(i);
//                    int y = data.calculateGridTileY(j);
//                    g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
//                }
//            }
//        }
    }
    
    /**
     * Renders the debugging text to the panel. Note
     * that the rendering will only actually be done
     * if data has activated debug text rendering.
     * 
     * @param g the Graphics context for this panel
     */
    public void renderDebuggingText(Graphics g)
    {
//        // IF IT'S ACTIVATED
//        if (data.isDebugTextRenderingActive())
//        {
//            // ENABLE PROPER RENDER SETTINGS
//            g.setFont(FONT_DEBUG_TEXT);
//            g.setColor(COLOR_DEBUG_TEXT);
//            
//            // GO THROUGH ALL THE DEBUG TEXT
//            Iterator<String> it = data.getDebugText().iterator();
//            int x = data.getDebugTextX();
//            int y = data.getDebugTextY();
//            while (it.hasNext())
//            {
//                // RENDER THE TEXT
//                String text = it.next();
//                g.drawString(text, x, y);
//                y += 20;
//            }   
//        } 
    }
}
