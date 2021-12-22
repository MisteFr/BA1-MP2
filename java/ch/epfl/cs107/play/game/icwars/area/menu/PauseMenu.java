package ch.epfl.cs107.play.game.icwars.area.menu;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class PauseMenu extends ICWarsArea {

    @Override
    public float getCameraScaleFactor() {
        return 1260f;
    }

    @Override
    public String getTitle() {
        return "icwars/PauseMenu";
    }

    @Override
    protected void createArea(){
        registerActor(new Background(this));
    }

    @Override
    public DiscreteCoordinates getDefaultCursorPosition() {
        return new DiscreteCoordinates(0, 0);
    }

    @Override
    public DiscreteCoordinates getEnemySpawnPosition() {
        return new DiscreteCoordinates(0, 0);
    }
}
