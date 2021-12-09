package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1 extends ICWarsArea{

    public DiscreteCoordinates getEnemySpawnPosition(){
        return new DiscreteCoordinates(17,5);
    }

    @Override
    public float getCameraScaleFactor() {
        return SCALE_FACTOR;
    }

    @Override
    public String getTitle() {
        return "icwars/Level1";
    }

    @Override
    protected void createArea(){
        registerActor(new Background(this));
    }

    @Override
    public DiscreteCoordinates getDefaultCursorPosition() {
        return new DiscreteCoordinates(2, 5);
    }
}
