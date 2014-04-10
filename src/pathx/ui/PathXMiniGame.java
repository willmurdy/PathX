/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.*;
import pathx.data.PathXDataModel;
import pathx.data.PathXRecord;
import pathx.file.PathXFileManager;

/**
 *
 * @author willmurdy
 */
public class PathXMiniGame extends MiniGame{
    
        // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private PathXRecord record;

    // HANDLES GAME UI EVENTS
    private PathXEventHandler eventHandler;
    
    // HANDLES ERROR CONDITIONS
    private PathXErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private PathXFileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;

    @Override
    public void initAudioContent() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new PathXErrorHandler(window);
        
        // INIT OUR FILE MANAGER
        fileManager = new PathXFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();
        
        // INIT OUR DATA MANAGER
        data = new PathXDataModel(this);
    }
    
    public void switchToHomeScreen(){
        
       guiDecor.get(BACKGROUND_TYPE).setState(HOME_SCREEN_STATE);
       
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(VISIBLE_STATE);
       
       currentScreenState = HOME_SCREEN_STATE;
    }
    
    public void switchToLevelSelectScreen(){
        
        
        
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(INVISIBLE_STATE);
       
       currentScreenState = LEVEL_SELECT_SCREEN_STATE;
        
    }

    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
 
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty("IMG_PATH");        
        String windowIconFile = props.getProperty("WINDOW_ICON");
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = HOME_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(HOME_SCREEN_STATE, img);
//        
//        img = loadImage(imgPath + props.getProperty(PathXPropertyType.LEVEL_SELECT_SCREEN_IMAGE_NAME));
//        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
//        
//        img = loadImage(imgPath + props.getProperty(PathXPropertyType.GAMEPLAY_SCREEN_IMAGE_NAME));
//        sT.addState(GAMEPLAY_SCREEN_STATE, img);
        
        s = new Sprite(sT, 0, 0, 0, 0, HOME_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
      
        
        // ADD A BUTTON FOR EACH LEVEL AVAILABLE
//        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
//        ArrayList<String> levelImageNames = props.getPropertyOptionsList(PathXPropertyType.LEVEL_IMAGE_OPTIONS);
//        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(PathXPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
//        float totalWidth = levels.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN) - LEVEL_BUTTON_MARGIN;
//        Viewport viewport = data.getViewport();
//        x = (viewport.getScreenWidth() - totalWidth)/2.0f;
//        for (int i = 0; i < levels.size(); i++)
//        {
//            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
//            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
//            sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
//            sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
//            s = new Sprite(sT, x, LEVEL_BUTTON_Y, 0, 0, SortingHatTileState.VISIBLE_STATE.toString());
//            guiButtons.put(levels.get(i), s);
//            x += LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN;
//        }
        
        //Add the Buttons for the Home screen
        String playButton = props.getProperty(PathXPropertyType.PLAY_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(PLAY_GAME_BUTTON_TYPE);
	img = loadImage(imgPath + playButton);
        sT.addState(VISIBLE_STATE, img);
        String playMouseOverButton = props.getProperty(PathXPropertyType.PLAY_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(PLAY_GAME_BUTTON_TYPE, s);
        
        //add the reset button to the home screen
        String resetButton = props.getProperty(PathXPropertyType.RESET_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(RESET_GAME_BUTTON_TYPE);
	img = loadImage(imgPath + resetButton);
        sT.addState(VISIBLE_STATE, img);
        String resetMouseOverButton = props.getProperty(PathXPropertyType.RESET_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, RESET_BUTTON_X, RESET_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(RESET_GAME_BUTTON_TYPE, s);
        
        //add the SETTINGS button to the home screen
        String settingButton = props.getProperty(PathXPropertyType.VIEW_SETTINGS_BUTTON_IMAGE_NAME);
        sT = new SpriteType(VIEW_SETTINGS_BUTTON_TYPE);
	img = loadImage(imgPath + settingButton);
        sT.addState(VISIBLE_STATE, img);
        String settingMouseOverButton = props.getProperty(PathXPropertyType.VIEW_SETTINGS_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + settingMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SETTINGS_BUTTON_X, SETTINGS_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(VIEW_SETTINGS_BUTTON_TYPE, s);
        
        //add the HELP button to the home screen
        String helpButton = props.getProperty(PathXPropertyType.VIEW_HELP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(VIEW_HELP_BUTTON_TYPE);
	img = loadImage(imgPath + helpButton);
        sT.addState(VISIBLE_STATE, img);
        String helpMouseOverButton = props.getProperty(PathXPropertyType.VIEW_HELP_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, HELP_BUTTON_X, HELP_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(VIEW_HELP_BUTTON_TYPE, s);
        
       
       
        
        // ADD THE CONTROLS ALONG THE NORTH OF THE GAME SCREEN
                
         // THEN THE NEW BUTTON
//        String playButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_NEW);
//        sT = new SpriteType(NEW_GAME_BUTTON_TYPE);
//	img = loadImage(imgPath + playButton);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        String playMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_NEW_MOUSE_OVER);
//        img = loadImage(imgPath + playMouseOverButton);
//        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
//        s = new Sprite(sT, NEW_BUTTON_X, NEW_BUTTON_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiButtons.put(NEW_GAME_BUTTON_TYPE, s);
        
        //Add a Back Button
//        String backButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_BACK);
//        sT = new SpriteType(BACK_BUTTON_TYPE);
//	img = loadImage(imgPath + backButton);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        String backMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER);
//        img = loadImage(imgPath + backMouseOverButton);
//        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
//        s = new Sprite(sT, BACK_BUTTON_X, BACK_BUTTON_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiButtons.put(BACK_BUTTON_TYPE, s);
        
        // AND THE MISCASTS COUNT
//        String miscastCountContainer = props.getProperty(SortingHatPropertyType.IMAGE_DECOR_MISCASTS);
//        sT = new SpriteType(MISCASTS_COUNT_TYPE);
//        img = loadImage(imgPath + miscastCountContainer);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        s = new Sprite(sT, TILE_COUNT_X, TILE_COUNT_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDecor.put(MISCASTS_COUNT_TYPE, s);
        
        // AND THE TIME DISPLAY
//        String timeContainer = props.getProperty(SortingHatPropertyType.IMAGE_DECOR_TIME);
//        sT = new SpriteType(TIME_TYPE);
//        img = loadImage(imgPath + timeContainer);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        s = new Sprite(sT, TIME_X, TIME_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDecor.put(TIME_TYPE, s);
        
        // AND THE STATS BUTTON
//        String statsButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_STATS);
//        sT = new SpriteType(STATS_BUTTON_TYPE);
//        img = loadImage(imgPath + statsButton);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        String statsMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_STATS_MOUSE_OVER);
//        img = loadImage(imgPath + statsMouseOverButton);
//        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
//        s = new Sprite(sT, STATS_X, STATS_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiButtons.put(STATS_BUTTON_TYPE, s);
        
        //Add the undo Button
//        String undoButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_UNDO);
//        sT = new SpriteType(UNDO_BUTTON_TYPE);
//        img = loadImage(imgPath + undoButton);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        String undoMouseOverButton = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_UNDO_MOUSE_OVER);
//        img = loadImage(imgPath + undoMouseOverButton);
//        sT.addState(SortingHatTileState.MOUSE_OVER_STATE.toString(), img);
//        s = new Sprite(sT, UNDO_X, UNDO_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiButtons.put(UNDO_BUTTON_TYPE, s);

        // AND THE TILE STACK
//        String tileStack = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_TEMP_TILE);
//        sT = new SpriteType(ALGORITHM_TYPE);
//        img = loadImageWithColorKey(imgPath + tileStack, COLOR_KEY);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        s = new Sprite(sT, TEMP_TILE_X, TEMP_TILE_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDecor.put(ALGORITHM_TYPE, s);

        // NOW ADD THE DIALOGS
        
        // AND THE STATS DISPLAY
//        String statsDialog = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_STATS);
//        sT = new SpriteType(STATS_DIALOG_TYPE);
//        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
//        y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
//        s = new Sprite(sT, x, y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDialogs.put(STATS_DIALOG_TYPE, s);
        
        // AND THE WIN CONDITION DISPLAY
//        String winDisplay = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_WIN);
//        sT = new SpriteType(WIN_DIALOG_TYPE);
//        img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
//        y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
//        s = new Sprite(sT, x, y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDialogs.put(WIN_DIALOG_TYPE, s);
//		
        // THEN THE TILES STACKED TO THE TOP LEFT
//        ((SortingHatDataModel)data).initTiles();
     }

    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);
                
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) 
            { eventHandler.respondToExitRequest(); }
        });

//        // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
//        for (String levelFile : levels)
//        {
//            Sprite levelButton = guiButtons.get(levelFile);
//            levelButton.setActionCommand(PATH_DATA + levelFile);
//            levelButton.setActionListener(new ActionListener(){
//                Sprite s;
//                public ActionListener init(Sprite initS) 
//                {   s = initS; 
//                    return this;    }
//                public void actionPerformed(ActionEvent ae)
//                {   eventHandler.respondToSelectLevelRequest(s.getActionCommand());    }
//            }.init(levelButton));
//        }   

        // NEW GAME EVENT HANDLER
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToPlayGameRequest();     }
        });
        
        guiButtons.get(RESET_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {   //ADDED
            public void actionPerformed(ActionEvent ae){
                eventHandler.respondToResetGameRequest();
            }
        });
//
//        // STATS BUTTON EVENT HANDLER
//        guiButtons.get(STATS_BUTTON_TYPE).setActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent ae)
//            {   eventHandler.respondToDisplayStatsRequest();    }
//        });
//        
//        // undo BUTTON EVENT HANDLER
//        guiButtons.get(UNDO_BUTTON_TYPE).setActionListener(new ActionListener(){   //ADDED
//            public void actionPerformed(ActionEvent ae)
//            {   eventHandler.respondToUndoRequest();    }
//        });
//        
//        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
//        this.setKeyListener(new KeyAdapter(){
//            public void keyPressed(KeyEvent ke)
//            {   
//                eventHandler.respondToKeyPress(ke.getKeyCode());    
//            }
//        });    
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGUI() {
         // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(VISIBLE_STATE))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(MOUSE_OVER_STATE);
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(MOUSE_OVER_STATE))
            {
                 if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(VISIBLE_STATE);
                }
            }
        }
    }
    
}
