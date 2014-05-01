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
    
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    public static final String MAP_TYPE = "MAP_TYPE";
    public static final String LEVEL_TYPE = "LEVEL_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    
    //used for switching between the background states
    public static final String HOME_SCREEN_STATE = "HOME_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    public static final String GAMEPLAY_SCREEN_STATE = "GAMEPLAY_SCREEN_STATE"; 
    
    //used for switching viewport states
    public static final String LEVEL_SCREEN_STATE = "LEVEL_SCREEN_STATE";
    
    //Entity Types
    public static final String PLAYER_TYPE = "PLAYER_TYPE";
    
    // this represents the buttons on the home screen
    public static final String HOME_SCREEN_BUTTON_TYPE = "HOME_SCREEN_BUTTON_TYPE";
    public static final String PLAY_GAME_BUTTON_TYPE = "PLAY_GAME_BUTTON_TYPE";
    public static final String RESET_GAME_BUTTON_TYPE = "RESET_GAME_BUTTON_TYPE";
    public static final String VIEW_SETTINGS_BUTTON_TYPE = "VIEW_SETTINGS_BUTTON_TYPE";
    public static final String VIEW_HELP_BUTTON_TYPE = "VIEW_HELP_BUTTON_TYPE";
    public static final String QUIT_GAME_BUTTON_TYPE = "QUIT_GAME_BUTTON_TYPE";
    public static final String CLOSE_BUTTON_TYPE = "CLOSE_BUTTON_TYPE";
    
    public static final String HOME_BUTTON_TYPE = "HOME_BUTTON_TYPE";
    public static final String RIGHT_BUTTON_TYPE = "RIGHT_BUTTON_TYPE";
    public static final String LEFT_BUTTON_TYPE = "LEFT_BUTTON_TYPE";
    public static final String UP_BUTTON_TYPE = "UP_BUTTON_TYPE";
    public static final String DOWN_BUTTON_TYPE = "DOWN_BUTTON_TYPE";
    public static final String PAUSE_BUTTON_TYPE = "PAUSE_BUTTON_TYPE";

    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE";
    
    public static final String SPECIALS_REDLIGHT_TYPE = "SPECIALS_REDLIGHT_TYPE";
    public static final String SPECIALS_REDLIGHT_TYPE1 = "SPECIALS_REDLIGHT_TYPE1";
    public static final String SPECIALS_REDLIGHT_TYPE2 = "SPECIALS_REDLIGHT_TYPE2";
    public static final String SPECIALS_REDLIGHT_TYPE3 = "SPECIALS_REDLIGHT_TYPE3";

    public static final String INTERSECTION_TYPE = "INTERSECTION_TYPE";
    public static final String OPEN_STATE = "OPEN_STATE";
    public static final String OPEN_MOUSE_OVER_STATE = "OPEN_MOUSE_OVER_STATE";
    public static final String CLOSED_STATE = "CLOSED_STATE";
    public static final String CLOSED_MOUSE_OVER_STATE = "CLOSED_MOUSE_OVER_STATE";
    public static final String STARTING_STATE = "STARTING_STATE";
    public static final String ENDING_STATE = "ENDING_STATE";

    // DIALOG TYPES
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String STATS_DIALOG_TYPE = "STATS_DIALOG_TYPE";
    public static final String LEVEL_DIALOG_TYPE = "LEVEL_DIALOG_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";    

    // ANIMATION SPEED
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 750;
    public static final int WINDOW_HEIGHT = 650;
    
    public static final int VIEWPORT_MARGIN_LEFT = 20;
    public static final int VIEWPORT_MARGIN_RIGHT = 20;
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 30;
    
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;
    public static final int VIEWPORT_INC = 10;
    
    public static final int MAP_WIDTH = 1499;
    public static final int MAP_HEIGHT = 1129;
        
    // FOR TILE RENDERING
    public static final int NUM_TILES = 30;
    public static final int TILE_WIDTH = 135;
    public static final int TILE_HEIGHT = 126;
    public static final int TILE_IMAGE_OFFSET_X = 45;
    public static final int TILE_IMAGE_OFFSET_Y = 30;
    public static final String TILE_SPRITE_TYPE_PREFIX = "TILE_";
    public static final int COLOR_INC = 25;
    
    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 20;
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int NORTH_PANEL_HEIGHT = 100;
    public static final int CONTROLS_MARGIN = 0;
    
    public static final int PLAY_BUTTON_X = 75;          //changed
    public static final int PLAY_BUTTON_Y = 500;          //changed
    public static final int RESET_BUTTON_X = PLAY_BUTTON_X + 150;          //changed
    public static final int RESET_BUTTON_Y = 500;          //changed
    public static final int SETTINGS_BUTTON_X = RESET_BUTTON_X + 150;          //changed
    public static final int SETTINGS_BUTTON_Y = 500; 
    public static final int HELP_BUTTON_X = SETTINGS_BUTTON_X + 150;          //changed
    public static final int HELP_BUTTON_Y = 500;  
    public static final int QUIT_BUTTON_X = 700;          //changed
    public static final int QUIT_BUTTON_Y = 0;  
    public static final int HOME_BUTTON_X = 650;          //changed
    public static final int HOME_BUTTON_Y = 0;  
    public static final int BACK_BUTTON_X = 20;          //changed
    public static final int BACK_BUTTON_Y = 100; 
    public static final int GAMEPLAY_QUIT_BUTTON_X = 100;          //changed
    public static final int GAMEPLAY_QUIT_BUTTON_Y = 100;
    public static final int BALANCE_X = 300;          //changed
    public static final int BALANCE_Y = 60; 
    public static final int GOAL_X = 300;          //changed
    public static final int GOAL_Y = 90; 
    public static final int RIGHT_BUTTON_X = 95;          //changed
    public static final int RIGHT_BUTTON_Y = 540;
    public static final int LEFT_BUTTON_X = 20;          //changed
    public static final int LEFT_BUTTON_Y = 540;
    public static final int UP_BUTTON_X = 50;          //changed
    public static final int UP_BUTTON_Y = 510;
    public static final int DOWN_BUTTON_X = 50;          //changed
    public static final int DOWN_BUTTON_Y = 585;
    
    public static final int SPECIALS_X = 20;          //changed
    public static final int SPECIALS_Y = 340;
    
    public static final int RIGHT_BUTTON_GAME_X = 95 + 25;          //changed
    public static final int RIGHT_BUTTON_GAME_Y = 540 - 10;
    public static final int LEFT_BUTTON_GAME_X = 20 + 25;          //changed
    public static final int LEFT_BUTTON_GAME_Y = 540 - 10;
    public static final int UP_BUTTON_GAME_X = 50 + 25;          //changed
    public static final int UP_BUTTON_GAME_Y = 510 - 10;
    public static final int DOWN_BUTTON_GAME_X = 50 + 25;          //changed
    public static final int DOWN_BUTTON_GAME_Y = 585 - 10;
    public static final int PAUSE_BUTTON_GAME_X = LEFT_BUTTON_GAME_X + 36;          //changed
    public static final int PAUSE_BUTTON_GAME_Y = LEFT_BUTTON_GAME_Y + 6;
    
    
    public static final int START_BUTTON_X = 20;          //changed
    public static final int START_BUTTON_Y = 200 + 65;
    
    public static final int LEVEL_DIALOG_X = 150;          //changed
    public static final int LEVEL_DIALOG_Y = 40;
    public static final int LEVEL_NAME_DIALOG_X = LEVEL_DIALOG_X + 50;          //changed
    public static final int LEVEL_NAME_DIALOG_Y = LEVEL_DIALOG_Y + 50; 
    public static final int LEVEL_DESCRIPTION_X = LEVEL_DIALOG_X + 50;          //changed
    public static final int LEVEL_DESCRIPTION_Y = LEVEL_NAME_DIALOG_Y + 50;    
    public static final int CLOSE_BUTTON_X = LEVEL_DIALOG_X + 150;          //changed
    public static final int CLOSE_BUTTON_Y = LEVEL_DIALOG_Y + 375;
    public static final int JTEXTAREA_X = LEVEL_NAME_DIALOG_X;          //changed
    public static final int JTEXTAREA_Y = LEVEL_NAME_DIALOG_Y + 150;
    
    public static final int LEVEL_NAME_X = 600;          //changed
    public static final int LEVEL_NAME_Y = 600;

    public static final int TILE_COUNT_X = 260 + CONTROLS_MARGIN;  //Miscasts
    public static final int TILE_COUNT_Y = 0;                                               //Miscasts
    public static final int TILE_COUNT_OFFSET = 145;
    public static final int TILE_TEXT_OFFSET = 60;
    public static final int TIME_X = TILE_COUNT_X + 232 + CONTROLS_MARGIN;
    public static final int TIME_Y = 0;
    public static final int TIME_OFFSET = 130;
    public static final int TIME_TEXT_OFFSET = 55;
    public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
    public static final int STATS_Y = 0;
    public static final int UNDO_X = STATS_X + 160 + CONTROLS_MARGIN;           //Added for undo
    public static final int UNDO_Y = 0;                                         //Added for undo
    public static final int TEMP_TILE_X = UNDO_X + 130 + CONTROLS_MARGIN;
    public static final int TEMP_TILE_Y = 0;
    public static final int TEMP_TILE_OFFSET_X = 20;
    public static final int TEMP_TILE_OFFSET_Y = 12;
    public static final int TEMP_TILE_OFFSET2 = 105;
    public static final int TEMP_TILE_TEXT_OFFSET_Y = 50;
    
    public static final int ONE_WAY_TRIANGLE_WIDTH = 30;
    
   
    
    // STATS DIALOG COORDINATES
    public static final int STATS_LEVEL_INC_Y = 30;
    public static final int STATS_LEVEL_X = 460;
    public static final int STATS_LEVEL_Y = 300;
    public static final int STATS_ALGORITHM_Y = STATS_LEVEL_Y + (STATS_LEVEL_INC_Y * 2);
    public static final int STATS_GAMES_Y = STATS_ALGORITHM_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_WINS_Y = STATS_GAMES_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_PERFECT_WINS_Y = STATS_WINS_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_FASTEST_PERFECT_WIN_Y = STATS_PERFECT_WINS_Y + STATS_LEVEL_INC_Y;
    
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
    public static final int WIN_PATH_NODES = 5;
    public static final int WIN_PATH_TOLERANCE = 50;
    public static final int WIN_PATH_COORD = 100;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    public static final Color COLOR_STATS = new Color(0, 60, 0);
    public static final Color COLOR_ALGORITHM_HEADER = Color.WHITE;

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 40);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
    public static final Font FONT_BALANCE = new Font(Font.MONOSPACED, Font.BOLD, 30);
    
    //added
    public static final String VISIBLE_STATE = "VISIBLE_STATE";
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String MOUSE_OVER_STATE = "MOUSE_OVER_STATE";
}