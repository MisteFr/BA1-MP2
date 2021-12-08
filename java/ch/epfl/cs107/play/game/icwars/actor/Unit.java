package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Queue;

public abstract class Unit extends ICWarsActor implements ICWarsInteractionVisitor {
    protected boolean isAvailable;
    protected boolean isAlive;
    protected int hp;
    protected int moveRadius;

    private Sprite sprite;
    protected ICWarsRange range;

    public Unit(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, int mvRadius, int dmg, int hp){
        super(owner, coordinates, factionType);
        isAvailable = true;
        isAlive = true;
        if(factionType == ICWarsFactionType.ALLY){
            sprite = new Sprite("icwars/friendly" + getName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }else if (factionType == ICWarsFactionType.ENEMY){
            sprite = new Sprite("icwars/enemy" + getName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }

        moveRadius = mvRadius;
        setRange(moveRadius, coordinates);
        owner.registerActor(this);
    }

    public abstract String getName();

    public abstract void dealDamage(Unit enemy);

    public abstract int getDamage();

    public abstract void isDealtDamage(int amount);


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
        hp = Math.max(amount, 0);
    }

    /**
     * @param available (boolean): availability state to set to
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * @param amount (int): amount of health points to add
     */
    public void heal(int amount){
        if (amount > 0){
            this.hp += amount;
        }
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if (!range.nodeExists(newPosition) || !super.changePosition(newPosition)){
            return false;
        }

        //update the range.
        setRange(moveRadius, getCurrentMainCellCoordinates());

        return true;
    }

    /**
     * Draw the unit's range and a path from the unit position to
     destination
     * @param destination path destination * @param canvas canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination, Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path = range.shortestPath(getCurrentMainCellCoordinates(), destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null) {
            new Path(getCurrentMainCellCoordinates().toVector(), path).draw(canvas);
        }
    }

    /**
        Function to initialise the node network according to a position and a radius
     */
    private void setRange(int moveRadius, DiscreteCoordinates coordinates){
        range = new ICWarsRange();
        for(int x = (-moveRadius + coordinates.x); x <= (moveRadius + coordinates.x); x++){
            for(int y = (-moveRadius + coordinates.y); y <= (moveRadius + coordinates.y); y++){
                if(x >= 0 && y >= 0 && x <  getOwnerArea().getWidth() && y < getOwnerArea().getHeight()){

                    boolean hasLeftEdge = (x - coordinates.x) > -moveRadius && x > 0;
                    boolean hasRightEdge = (x - coordinates.x) < moveRadius && x < (getOwnerArea().getWidth() - 1);
                    boolean hasUpEdge = (y - coordinates.y) < moveRadius && y < (getOwnerArea().getHeight() - 1);
                    boolean hasDownEdge = (y - coordinates.y) > -moveRadius && y > 0;

                    range.addNode(new DiscreteCoordinates(x, y), hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
                }
            }
        }
    }

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
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }
}
