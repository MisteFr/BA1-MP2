package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.units.Tank;
import ch.epfl.cs107.play.game.icwars.actor.units.Soldat;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICWars extends AreaGame {

    private ICWarsPlayer player;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};

    private int areaIndex;


    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        Keyboard keyboard = getCurrentArea().getKeyboard();

        if(keyboard.get(Keyboard.N).isReleased()){
            if((areaIndex + 1) < areas.length){
                ++areaIndex;
                initArea(areas[areaIndex]);
            }else{
                end();
            }
        }

        if(keyboard.get(Keyboard.R).isReleased()){
            for(String areaName: areas){
                //allow us to force restart every area
                setCurrentArea(areaName, true);
            }

            //We switch to level 0 and re init the game in this level0.
            areaIndex = 0;
            initArea(areas[areaIndex]);
        }

        /* //commented based on the instructions from the assignment
        if(keyboard.get(Keyboard.U).isDown()){
            ((RealPlayer)player).selectUnit(1);
        }
         */
    }

    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
    }

    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            player.startTurn();
            return true;
                    }
        return false;
    }

    private void initArea(String areaKey) {
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates defaultCursorPosition = area.getDefaultCursorPosition();

        Tank tank = new Tank(area, new DiscreteCoordinates(2, 5), ICWarsActor.ICWarsFactionType.ALLY);
        Soldat soldat = new Soldat(area, new DiscreteCoordinates(3, 5), ICWarsActor.ICWarsFactionType.ALLY);
        player = new RealPlayer(area, defaultCursorPosition, ICWarsActor.ICWarsFactionType.ALLY, soldat, tank);
        ((RealPlayer)player).enterArea(area);
    }

    public void end() {
        System.out.println("Game Over");
    }

    public String getTitle(){
        return "ICWars";
    }
}
