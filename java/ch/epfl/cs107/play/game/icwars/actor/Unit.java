package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Queue;

public abstract class Unit extends ICWarsActor {
    protected boolean isAvailable;
    protected boolean isAlive;
    protected int hp;
    private Sprite sprite;
    protected ICWarsRange range = new ICWarsRange();

    public Unit(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, int moveRadius, int dmg, int hp){
        super(owner, coordinates, factionType);
        isAvailable = true; //todo : is this necessary ?
        isAlive = true;
        if(factionType == ICWarsFactionType.ALLY){
            sprite = new Sprite("icwars/friendly" + getName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }else if (factionType == ICWarsFactionType.ENEMY){
            sprite = new Sprite("icwars/enemy" + getName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }

        initRange(moveRadius, coordinates);
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
        if(amount < 0){
            hp = 0;
        } else {
            hp = amount;
        }
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
        Function to initialize the ICWarsRange
     */
    private void initRange(int moveRadius, DiscreteCoordinates coordinates){
        for(int x = (-moveRadius + coordinates.x); x <= (moveRadius + coordinates.x); x++){
            for(int y = (-moveRadius + coordinates.y); y <= (moveRadius + coordinates.y); y++){
                if(x >= 0 && y >= 0 && x <= getOwnerArea().getWidth() && y <= getOwnerArea().getHeight()){

                    boolean hasLeftEdge = false;
                    if((x - coordinates.x) > -moveRadius && x > 0){
                        hasLeftEdge = true;
                    }

                    boolean hasRightEdge = false;
                    if((x- coordinates.x) < moveRadius && x < getOwnerArea().getWidth()){
                        hasRightEdge = true;
                    }

                    boolean hasUpEdge = false;
                    if((y - coordinates.y) < moveRadius && y < getOwnerArea().getHeight()){
                        hasUpEdge = true;
                    }

                    boolean hasDownEdge = false;
                    if((y - coordinates.y) > -moveRadius && y > 0){
                        hasDownEdge = true;
                    }

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
        //TODO: check?
    }
}
