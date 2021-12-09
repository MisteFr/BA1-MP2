package ch.epfl.cs107.play.game.icwars.actor.units.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class ActionC implements Graphics {
    protected Unit unit;
    private ICWarsArea owner;
    private String name; // how do we code that all actions have
    private int key;     // these two parameters ?

    public ActionC(Unit unit, ICWarsArea owner){
        this.unit = unit;
        this.owner = owner;
    }

    @Override
    public abstract void draw(Canvas canvas);

    public abstract void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);
}
