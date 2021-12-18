package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Unit extends ICWarsActor implements ICWarsInteractionVisitor, Interactor {

    private int hp;
    private Sprite sprite;
    private int defenseStars;
    private boolean isAvailable;

    private final int moveRadius;
    private final ICWarsInteractionHandler handler = new ICWarsInteractionHandler();

    protected LinkedList<Action> actionsList = new LinkedList<>();
    protected ICWarsRange range;
    protected boolean soundNeedToBePlayed;

    public Unit(ICWarsArea owner, DiscreteCoordinates coordinates, ICWarsFactionType factionType, int mvRadius, int dmg, int hp) {
        super(owner, coordinates, factionType);
        if (factionType == ICWarsFactionType.ALLY) {
            sprite = new Sprite("icwars/friendly" + getName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        } else if (factionType == ICWarsFactionType.ENEMY) {
            sprite = new Sprite("icwars/enemy" + getName(), 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }

        moveRadius = mvRadius;
        setRange(moveRadius, coordinates);
    }

    public abstract String getName();

    public abstract void dealDamage(Unit enemy);

    public abstract int getDamage();

    public abstract void isDealtDamage(int amount);


    /**
     * @return (int): the number of health points of the unit.
     */
    public int getHp() {
        return hp;
    }

    /**
     * @param amount (int): new amount of hp to set to
     */
    public void setHp(int amount) {
        hp = Math.max(amount, 0);
    }

    /**
     * @return (int): the move radius of the unit.
     */
    public int getMoveRadius() {
        return moveRadius;
    }

    /**
     * @return (int): the defense stars of the cell of the unit.
     */
    public int getUnitCellDefenseStars() {
        return defenseStars;
    }

    /**
     * @return (LinkedList < Action >): the action list of the unit.
     */
    public LinkedList<Action> getActionsList() {
        return actionsList;
    }

    /**
     * @param available (boolean): availability state to set to
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
        if (available) {
            sprite.setAlpha(1.f);
        } else {
            sprite.setAlpha(0.5f);
        }
    }

    /**
     * @return isAvailable (boolean): unit's availability (true if it can be moved)
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * @param amount (int): amount of health points to add
     */
    public void heal(int amount) {
        if (amount > 0) {
            this.hp += amount;
        }
    }

    /**
     * Change the unit position to the one specified
     *
     * @param newPosition new unit's position
     * @return true if the move was successful, false otherwise
     */
    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if (!range.nodeExists(newPosition) || !super.changePosition(newPosition)) {
            return false;
        }

        //update the range.
        setRange(moveRadius, getCurrentMainCellCoordinates());

        return true;
    }

    /**
     * Draw the unit's range and a path from the unit position to
     * destination
     *
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
     * Function to initialise the node network according to a position and a radius
     */
    private void setRange(int moveRadius, DiscreteCoordinates coordinates) {
        range = new ICWarsRange();
        for (int x = (-moveRadius + coordinates.x); x <= (moveRadius + coordinates.x); x++) {
            for (int y = (-moveRadius + coordinates.y); y <= (moveRadius + coordinates.y); y++) {
                if (x >= 0 && y >= 0 && x < getOwnerArea().getWidth() && y < getOwnerArea().getHeight()) {

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
        return true;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    private class ICWarsInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            defenseStars = cell.getType().getDefenseStar();
        }
    }
}
