package ch.epfl.cs107.play.game.icwars.actor.units.action;

import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class WaitC extends ActionC {
    private final static String NAME = "Wait";
    private final static int KEY = Keyboard.W;

    public WaitC(Unit unit, ICWarsArea owner) {
        super(unit, owner);
    }

    @Override
    public void draw(Canvas canvas) {
        //does nothing
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        unit.setAvailable(false);
        player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);
    }
}
