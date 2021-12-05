package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class ICWarsPlayerGUI implements Graphics {

    private ICWarsPlayer player;
    private Unit selectedUnit;

    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player){
        this.player = player;
    }

    @Override
    public void draw(Canvas canvas) {
        if(selectedUnit != null && player.getCurrentState() == ICWarsPlayer.PlayState.MOVE_UNIT){
            selectedUnit.drawRangeAndPathTo(new DiscreteCoordinates(player.getPosition()), canvas);
        }
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
}
