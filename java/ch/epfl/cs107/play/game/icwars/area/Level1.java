package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.City;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
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

        City city1 = new City(this, new DiscreteCoordinates(5, 2), ICWarsActor.ICWarsFactionType.NEUTRAL);
        City city2 = new City(this, new DiscreteCoordinates(10, 8), ICWarsActor.ICWarsFactionType.NEUTRAL);

        addCity(city1);
        addCity(city2);
    }


    @Override
    public DiscreteCoordinates getDefaultCursorPosition() {
        return new DiscreteCoordinates(2, 5);
    }
}
