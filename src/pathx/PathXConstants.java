/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author willmurdy
 */
public class PathXConstants {
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    public static final String DIALOG_TYPE = "DIALOG_TYPE";
    //add menu background type 
    
    //used for switching between the background states
    public static final String HOME_SCREEN_STATE = "HOME_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String GAMEPLAY_SCREEN_STATE = "GAMEPLAY_SCREEN_STATE";  
    
    // THIS REPRESENTS THE BUTTONS ON THE Home SCREEN
    public static final String HOME_SCREEN_BUTTON_TYPE = "HOME_SCREEN_BUTTON_TYPE";
    public static final String PLAY_GAME_BUTTON_TYPE = "PLAY_GAME_BUTTON_TYPE";
  
    //Tile types
    public static final String REGULAR_TILE_TYPE = "REGULAR_TILE_TYPE";
    public static final String STRIPED_TILE_TYPE = "STRIPED_TILE_TYPE";
    public static final String TILE_SPRITE_TYPE_PREFIX = "TILE_";
    
    public static final String LEVEL_PATH = "LEVEL_PATH";

    // IN-GAME UI CONTROL TYPES
    public static final String PLAY_BUTTON_TYPE = "./zombies/UIZombiesPlay.png";
    public static final String QUIT_BUTTON_TYPE = "./zombies/UIZombiesQuit.png";
    public static final String EXIT_GAME_BUTTON_TYPE = "./zombies/UIZombiesExit.png";
    public static final String EXIT_LEVEL_BUTTON_TYPE = "./zombies/UIZombiesExitLevel.png";
    public static final String LEFT_BUTTON_TYPE = "LEFT_BUTTON_TYPE"; 
    public static final String RIGHT_BUTTON_TYPE = "RIGHT_BUTTON_TYPE"; 
    public static final String STATS_BUTTON_TYPE = "STATS_BUTTON_TYPE";

    // DIALOG TYPES
    public static final String STATS_DIALOG_TYPE = "STATS_DIALOG_TYPE";
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String LOSS_DIALOG_TYPE = "LOSS_DIALOG_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String SPLASH_SCREEN_STATE = "SPLASH_SCREEN_STATE";
    public static final String SAGA_SCREEN_STATE = "SAGA_SCREEN_STATE";
    public static final String LEVEL_SCREEN_STATE = "LEVEL_SCREEN_STATE"; 
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String FIRST_LEVEL_SCREEN_STATE = "FIRST_LEVEL_SCREEN_STATE";
    public static final String SECOND_LEVEL_SCREEN_STATE = "SECOND_LEVEL_SCREEN_STATE";
    public static final String STATS_SCREEN_STATE = "STATS_SCREEN_STATE"; 
    
        // ANIMATION SPEED
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 750;
    public static final int WINDOW_HEIGHT = 650;

    // THE TILES MAY HAVE 4 STATES:
        // - INVISIBLE_STATE: USED WHEN ON THE SPLASH SCREEN, MEANS A TILE
            // IS NOT DRAWN AND CANNOT BE CLICKED
        // - VISIBLE_STATE: USED WHEN ON THE GAME SCREEN, MEANS A TILE
            // IS VISIBLE AND CAN BE CLICKED (TO SELECT IT), BUT IS NOT CURRENTLY SELECTED
        // - SELECTED_STATE: USED WHEN ON THE GAME SCREEN, MEANS A TILE
            // IS VISIBLE AND CAN BE CLICKED (TO UNSELECT IT), AND IS CURRENTLY SELECTED     
        // - NOT_AVAILABLE_STATE: USED FOR A TILE THE USER HAS CLICKED ON THAT
            // IS NOT FREE. THIS LET'S US GIVE THE USER SOME FEEDBACK
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String VISIBLE_STATE = "VISIBLE_STATE";
    public static final String SELECTED_STATE = "SELECTED_STATE";
    public static final String INCORRECTLY_SELECTED_STATE = "NOT_AVAILABLE_STATE";
    public static final String MOUSE_OVER_STATE = "MOUSE_OVER_STATE";

    // THE BUTTONS MAY HAVE 2 STATES:
        // - INVISIBLE_STATE: MEANS A BUTTON IS NOT DRAWN AND CAN'T BE CLICKED
        // - VISIBLE_STATE: MEANS A BUTTON IS DRAWN AND CAN BE CLICKED
        // - MOUSE_OVER_STATE: MEANS A BUTTON IS DRAWN WITH SOME HIGHLIGHTING
            // BECAUSE THE MOUSE IS HOVERING OVER THE BUTTON

    // UI CONTROL SIZE AND POSITION SETTINGS
    
    // OR POSITIONING THE LEVEL SELECT BUTTONS
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;

    // FOR STACKING TILES ON THE GRID
    public static final int NUM_TILES = 144;
    public static final int TILE_IMAGE_OFFSET = 1;
    public static final int TILE_IMAGE_WIDTH = 55;
    public static final int TILE_IMAGE_HEIGHT = 55;
    public static final int Z_TILE_OFFSET = 5;

    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 70;
    
    //UI Controls on the Home screen
    public static final int PLAY_BUTTON_X = 100;
    public static final int PLAY_BUTTON_Y = 400;
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int CONTROLS_MARGIN = 0;
    public static final int EXIT_BUTTON_X = 1200;
    public static final int EXIT_BUTTON_Y = 0;
    public static final int LEFT_BUTTON_X = 0;
    public static final int LEFT_BUTTON_Y = 720 - 120;
    public static final int RIGHT_BUTTON_X = 1200;
    public static final int RIGHT_BUTTON_Y = 720 - 120;
    public static final int TILE_COUNT_X =  130 + CONTROLS_MARGIN;
    public static final int TILE_COUNT_Y = 0;
    public static final int TILE_COUNT_OFFSET = 150;
    public static final int TILE_TEXT_OFFSET = 57;
    public static final int TIME_X = TILE_COUNT_X + 232 + CONTROLS_MARGIN;
    public static final int TIME_Y = 0;
    public static final int TIME_OFFSET = 130;
    public static final int TIME_TEXT_OFFSET = 55;
    public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
    public static final int STATS_Y = 0;
    public static final int UNDO_BUTTON_X = STATS_X + 160 + CONTROLS_MARGIN;
    public static final int UNDO_BUTTON_Y = 0;
    public static final int TILE_STACK_X = UNDO_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int TILE_STACK_Y = 0;
    public static final int TILE_STACK_OFFSET_X = 30;
    public static final int TILE_STACK_OFFSET_Y = 12;
    public static final int TILE_STACK_2_OFFSET_X = 105;
       
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
    public static final int WIN_PATH_NODES = 8;
    public static final int WIN_PATH_TOLERANCE = 100;
    public static final int WIN_PATH_X_OFFSET = 175;
    public static final int WIN_PATH_Y_OFFSET = 225;
    public static final int WIN_PATH_COORD = 100;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    public static final Color TEXT_DISPLAY_COLOR = new Color (10, 160, 10);
    public static final Color SELECTED_TILE_COLOR = new Color(255,255,0,100);
    public static final Color INCORRECTLY_SELECTED_TILE_COLOR = new Color(255, 50, 50, 100);
    public static final Color STATS_COLOR = new Color(0, 60, 0);

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font TEXT_DISPLAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font DEBUG_TEXT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font STATS_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font TIME_FONT = new Font(Font.SERIF, Font.BOLD, 45);
    
    // AND AUDIO STUFF
    public static final String SUCCESS_AUDIO_TYPE = "SUCCESS_AUDIO_TYPE";
    public static final String FAILURE_AUDIO_TYPE = "FAILURE_AUDIO_TYPE";
    public static final String THEME_SONG_TYPE = "THEME_SONG_TYPE";
    
}
