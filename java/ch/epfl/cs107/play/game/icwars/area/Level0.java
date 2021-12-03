package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;

public class Level0 extends ICWarsArea{

    @Override
    public float getCameraScaleFactor() {
        return SCALE_FACTOR;
    }

    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    @Override
    protected void createArea(){
        registerActor(new Background(this));
    }
}
