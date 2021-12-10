package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class Action implements Graphics {
    protected Unit actionUnit;
    protected ICWarsArea owner;

    public Action(Unit unit, ICWarsArea owner){
        this.actionUnit = unit;
        this.owner = owner;
    }

    public abstract String getName();

    public abstract int getKey();

    @Override
    public abstract void draw(Canvas canvas);

    public abstract void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);
}
