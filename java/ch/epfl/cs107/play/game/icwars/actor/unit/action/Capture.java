package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.icwars.actor.City;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Capture extends Action {
    private final static String NAME = "(C)apture";
    private final static int KEY = Keyboard.C;
    private final City selectedCity;

    public Capture(Unit unit, ICWarsArea owner, City c) {
        super(unit, owner);
        selectedCity = c;
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
        selectedCity.setFaction(actionUnit.getFaction());
        actionUnit.setAvailable(false);
        player.setCurrentState(ICWarsPlayer.PlayState.NORMAL);
        actionUnit.removeAction(this);
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player) {

    }
}
