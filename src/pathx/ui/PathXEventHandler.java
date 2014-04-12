/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.KeyEvent;
import static pathx.PathXConstants.VIEWPORT_INC;
import pathx.data.PathXDataModel;

/**
 *
 * @author willmurdy
 */
public class PathXEventHandler {
    
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public PathXEventHandler(PathXMiniGame initGame)
    {
        game = initGame;
    }

    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }

    /**
     * Called when the user clicks the New button.
     */
    public void respondToPlayGameRequest()
    {
       game.switchToLevelSelectScreen();
    }
    
    public void respondToResetGameRequest()
    {
        game.getDataModel().reset(game);
    }
    
    public void respondToHomeRequest()
    {
      game.switchToHomeScreen();
    }
    
        public void respondToHelpRequest()
    {
      game.switchToHelpScreen();
    }

    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToSelectLevelRequest(String levelFile)
    {
       
    }

    /**
     * Called when the user clicks the Stats button.
     */
    public void respondToDisplayStatsRequest()
    {
          
    }

        /**
     * Called when the user clicks the scroll button.
     */
    public void respondToScrollRequest(int incX, int incY)
    {
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getViewport().scroll(incX, incY);
    }
    
            /**
     * Called when the user clicks the scroll button.
     */
    public void respondToSettingsRequest()
    {
        game.switchToSettingsScreen();
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    { 
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        if(keyCode == KeyEvent.VK_DOWN){
            data.getViewport().scroll(0, VIEWPORT_INC);
        }
        if(keyCode == KeyEvent.VK_UP){
            data.getViewport().scroll(0, -VIEWPORT_INC);
        }
        if(keyCode == KeyEvent.VK_LEFT){
            data.getViewport().scroll(-VIEWPORT_INC, 0);
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            data.getViewport().scroll(VIEWPORT_INC, 0);
        }

    }
}
