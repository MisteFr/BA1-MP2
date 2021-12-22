package ch.epfl.cs107.play.game.icwars.area.menu;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class GameOver extends ICWarsArea {

    @Override
    public float getCameraScaleFactor() {
        return 1260f;
    }

    @Override
    public String getTitle() {
        return "icwars/GameOver";
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
        return null;
    }
}
