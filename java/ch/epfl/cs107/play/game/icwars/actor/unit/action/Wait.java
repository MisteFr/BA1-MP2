package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Wait extends Action {
    private final static String NAME = "(W)ait";
    private final static int KEY = Keyboard.W;

    public Wait(Unit unit, ICWarsArea owner) {
        super(unit, owner);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getKey() {
        return KEY;
    }

    @Override
    public void draw(Canvas canvas) {
        //does nothing
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        actionUnit.setAvailable(false);
        player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player) {

    }
}
