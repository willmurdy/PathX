/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.BasicStroke;
import pathx.data.PathXDataModel;
import pathx.PathXConstants.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.*;
import pathx.data.PathXCar;
import pathx.data.PathXIntersection;
import pathx.data.PathXRoad;
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
    
    HashMap<Integer, BasicStroke> recyclableStrokes;
    
    private JTextArea text;
    
    private Viewport viewport;
    
        int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH/2,  -ONE_WAY_TRIANGLE_WIDTH/2,  ONE_WAY_TRIANGLE_WIDTH/2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH/2, -ONE_WAY_TRIANGLE_WIDTH/2, 0};
    GeneralPath recyclableTriangle;
    
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
                
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++)
        {
            recyclableStrokes.put(i, new BasicStroke(i*2));
        }
        
        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle =  new GeneralPath(   GeneralPath.WIND_EVEN_ODD,
                                                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) 
        {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
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
        
            
            if(((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ||
                    ((PathXMiniGame)game).isCurrentScreenState(GAMEPLAY_SCREEN_STATE))
                    renderViewport(g);
        
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            
            
            
            
//            if(!data.notStarted())
//                renderRoads(g);

            if(((PathXMiniGame)game).isCurrentScreenState(HELP_SCREEN_STATE))
                    renderHelp(g);
            
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
            renderRoads(g);
            renderIntersections(g);
            renderPlayer(g);
            renderPolice(g);
            renderZombies(g);
            renderBandits(g);
        }
        
    }
    
    public void renderPlayer(Graphics g){
        
        int x = data.getLevel(data.getCurrentLevelInt()).getPlayerX();
        int y = data.getLevel(data.getCurrentLevelInt()).getPlayerY();
        
        Sprite s = ((PathXMiniGame)game).getGuiEntities().get(PLAYER_TYPE);
        
        s.setX(x);
        s.setY(y);
        
        renderSprite(g, s);
    }
    
    public void renderPolice(Graphics g){
        ArrayList<PathXCar> police = data.getLevel(data.getCurrentLevelInt()).getPolice();
        int i = 0;
        for(PathXCar p : police){
            p.lockData();
            int x = p.getRenderX();
            int y = p.getRenderY();
            int zx = p.getX();
            int zy = p.getY();
            int px = data.getLevel(data.getCurrentLevelInt()).getPlayerX();
            int py = data.getLevel(data.getCurrentLevelInt()).getPlayerY();
            if((x == px && y == py) || (zx == px && zy == py)){
                data.policeCollision(i);
//                data.endGameAsLoss();
                
            }
            //System.out.println("(" + x + ", " + y + ")");
            i++;
        
            Sprite s = ((PathXMiniGame)game).getGuiEntities().get(POLICE_TYPE);
        
            s.setX(x);
            s.setY(y);
            
            s.setState(VISIBLE_STATE);
        
            renderSprite(g, s);
            p.unlockData();
        }
    }
    
    public void renderZombies(Graphics g){
        ArrayList<PathXCar> zombies = data.getLevel(data.getCurrentLevelInt()).getZombies();
        for(PathXCar p : zombies){
            p.lockData();
            int x = p.getRenderX();
            int y = p.getRenderY();
            int zx = p.getX();
            int zy = p.getY();
            int px = data.getLevel(data.getCurrentLevelInt()).getPlayerX();
            int py = data.getLevel(data.getCurrentLevelInt()).getPlayerY();
            if((x == px && y == py) || (zx == px && zy == py)){
                data.zombieCollision();
                
            }
            //System.out.println("(" + x + ", " + y + ")");
        
            Sprite s = ((PathXMiniGame)game).getGuiEntities().get(ZOMBIE_TYPE);
        
            s.setX(x);
            s.setY(y);
            
            s.setState(VISIBLE_STATE);
        
            renderSprite(g, s);
            p.unlockData();
        }
    }
    
    public void renderBandits(Graphics g){
        ArrayList<PathXCar> bandits = data.getLevel(data.getCurrentLevelInt()).getBandits();
        for(PathXCar p : bandits){
            int x = p.getRenderX();
            int y = p.getRenderY();
            //System.out.println("(" + x + ", " + y + ")");
        
            Sprite s = ((PathXMiniGame)game).getGuiEntities().get(BANDIT_TYPE);
        
            s.setX(x);
            s.setY(y);
            
            s.setState(VISIBLE_STATE);
        
            renderSprite(g, s);
        }
    }
    
    public void renderLevels(Sprite s, Graphics g){
        
        int x = data.getLevelSprite(s.getID()).getX();
        int y = data.getLevelSprite(s.getID()).getY();

      //  if(data.getLevelSprite(s.getID()).wonLevel())
      //      s.setState(PATH_DATA);
        
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
//        SpriteType sprite = new SpriteType("START");
//        sprite.addState("START", data.getLevel(data.getCurrentLevelInt()).getStartingImage());
//        s1 = new Sprite(sprite, 0, 0, 0, 0, "START");
        Sprite s;
        
        for(int i = 0; i < data.getLevel(data.getCurrentLevelInt()).getNumIntersections(); i++){
            s = ((PathXMiniGame)game).getGuiIntersections().get(i);
//            if(data.getLevel(data.getCurrentLevelInt()).getIntersection(i).open())
//                s.setState(OPEN_STATE);
//            else
//                s.setState(CLOSED_STATE);
//        }
//        for(int i = 0; i < intersections.size(); i++){
            int x = intersections.get(i).getRenderX();
            int y = intersections.get(i).getRenderY();
//            if((x > 180 - 5 && x < 730 - 20) && (y > 20) && y < 610-5){
                s.setX(x);
                s.setY(y);

                //if(intersections.get(i).open()){
                    if(s.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                        s.setState(MOUSE_OVER_STATE);
                    else
                        s.setState(VISIBLE_STATE);
//                } else {
//                    if(s.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
//                         s.setState(CLOSED_MOUSE_OVER_STATE);
//                    else
//                         s.setState(CLOSED_STATE);       
//                }
                
                if(i == 0){
                    SpriteType sprite = new SpriteType("START");
                    sprite.addState("START", data.getLevel(data.getCurrentLevelInt()).getStartingImage());
                    s = new Sprite(sprite, x - 12, y - 12, 0, 0, "START");
                    renderSprite(g, s);
                } else
                    if(i == 1){
                        SpriteType sprite = new SpriteType("START");
                        sprite.addState("START", data.getLevel(data.getCurrentLevelInt()).getDestinationImage());
                        s = new Sprite(sprite, x - 12, y - 12, 0, 0, "START");
                        renderSprite(g, s);
                    }
                
                renderSprite(g, s);
            
        }
                
        
    }
    
    public void renderRoads(Graphics g){
        ArrayList<PathXRoad> roads = data.getLevel(data.getCurrentLevelInt()).getRoads();
        for(int i = 0; i < roads.size(); i++){
            renderRoad(g, roads.get(i));
        }
    }
    
    public void renderRoad(Graphics g, PathXRoad road){
                
        int strokeId = road.getSpeedLimit()/10;
        
        Graphics2D g2 = (Graphics2D)g;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) strokeId = 1;
        if (strokeId > 10) strokeId = 10;
        g2.setStroke(recyclableStrokes.get(strokeId));

        Line2D.Double recyclableLine = new Line2D.Double(0,0,0,0);
        
        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getId1x()-viewport.getViewportX() + 12;
        recyclableLine.y1 = road.getId1y()-viewport.getViewportY() + 12;
        recyclableLine.x2 = road.getid2x()-viewport.getViewportX() + 12;
        recyclableLine.y2 = road.getid2y()-viewport.getViewportY() + 12;
        
        

        // AND DRAW IT
        g2.draw(recyclableLine);
        
        
        
        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.oneWay())
        {
            this.renderOneWaySignalsOnRecyclableLine(g2, recyclableLine);
        }
    }
    
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2, Line2D.Double recyclableLine)
    {
        
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();        
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);
        
        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);
        
        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
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
     
    }
    
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
            
            if(((PathXDataModel)data).getLevel(((PathXDataModel)data).getCurrentLevelInt()).showEndDialog()){
//                game.getGUIDialogs().get(LEVEL_DIALOG_TYPE).setState(VISIBLE_STATE);
//                
//                
//                game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(VISIBLE_STATE);
//                game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);
//                renderSprite(g, game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE));
//                
//                
//                game.getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
//                game.getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(true);
//                renderSprite(g, game.getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE));
                if(((PathXDataModel)data).getLevel(((PathXDataModel)data).getCurrentLevelInt()).wonLevel()){
                    text.setText(((PathXDataModel)game.getDataModel()).getCurrentLevelDescription() + "\nYou Win! You got away with $" + data.getLevel(data.getCurrentLevelInt()).getReward());
                    this.add(text);
                } else {
                    text.setText("Bad News!\nYou you have been caught. The legal Bills will cost you $" + data.getAmountLost());
                    this.add(text);
                }
                text.setLocation(200, 150);
                ((PathXMiniGame)game).showWinDialog();
            } else {
            if(game.getGUIDialogs().get(LEVEL_DIALOG_TYPE).getState().equals(VISIBLE_STATE)){
                text.setText(((PathXDataModel)game.getDataModel()).getCurrentLevelDescription() + "\nPolice: " + data.getLevel(data.getCurrentLevelInt()).getNumPolice());
                this.add(text);
                text.setLocation(200, 150);
                g.setFont(FONT_BALANCE);
                g.drawString(((PathXDataModel)game.getDataModel()).getCurrentLevel(), LEVEL_NAME_DIALOG_X, LEVEL_NAME_DIALOG_Y);
                
            }
            }
            
            
            //if(((PathXMiniGame)game).renderDescription())
                //renderLevelDescription(((PathXMiniGame)game).renderDescription(), text);
        }
        renderLevelDescription(((PathXMiniGame)game).renderDescription(), text);
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

//                text.setText(((PathXDataModel)game.getDataModel()).getCurrentLevelDescription() + "\nPolice: " + data.getLevel(data.getCurrentLevelInt()).getNumPolice());
//                this.add(text);
//                text.setLocation(200, 150);
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
