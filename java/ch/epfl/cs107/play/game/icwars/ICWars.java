package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.units.Tank;
import ch.epfl.cs107.play.game.icwars.actor.units.Soldat;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ICWars extends AreaGame {

    private RealPlayer cursor;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};

    private int areaIndex;


    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
    }

    private void createAreas(){
        addArea(new Level0()); //todo to verify if all levels are created at the beginning or one by one
        addArea(new Level1());
    }

    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {
        ICWarsArea area = (ICWarsArea)setCurrentArea(areaKey, true);

        Tank tank = new Tank(area, new DiscreteCoordinates(0, 1), ICWarsActor.ICWarsFactionType.ALLY);
        Soldat soldat = new Soldat(area, new DiscreteCoordinates(1, 0), ICWarsActor.ICWarsFactionType.ALLY);
        cursor = new RealPlayer(area, new DiscreteCoordinates(0, 0), ICWarsActor.ICWarsFactionType.ALLY, soldat, tank);
        cursor.enterArea(area, new DiscreteCoordinates(0, 0));
        cursor.centerCamera();
        //todo verify if we don't nee to write the same ending of the method as in Tuto2.java
    }

    public void end() {
    }

    public String getTitle(){
        return "ICWars";
    }
}
