package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Unit extends ICWarsActor {
    protected int hp;
    private Sprite sprite;

    public Unit(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, int moveRadius, int dmg, int hp){
        super(owner, coordinates, factionType);
        String spriteName = "icwars/" + factionType.getName() + "" + getName();
        sprite = new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));

        owner.registerActor(this);
        owner.registerUnit(this);
    }


    /**
     * @return (int): the number of health points of the actor.
     */
    public int getHp() {
        return hp;
    }

    /**
     * @param amount (int): new amount of hp to set to
     */
    public void setHp(int amount){
        if(amount < 0){
            hp = 0;
        } else {
            hp = amount;
        }
    }

    /**
     * @param amount (int): amount to add to health points
     */
    public void heal(int amount){
        if (amount > 0){
            this.hp += amount;
        }
    }

    public abstract String getName();

    public abstract void dealDamage(Unit enemy);

    public abstract int getDamage();

    public abstract void isDealtDamage(int amount);

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        //TODO: check?
    }
}
