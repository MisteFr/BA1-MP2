package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Capture;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class City extends ICWarsActor implements Interactor {

    private int hp = 10;
    private Sprite sprite;
    private ICWarsFactionType factionType;
    private final ICWarsCityInteractionHandler HANDLER;

    public City(Area owner, DiscreteCoordinates coordinates, ICWarsFactionType faction) {
        super(owner, coordinates, faction);
        HANDLER = new ICWarsCityInteractionHandler();
        factionType = faction;
        updateSprite();
    }

    public void updateSprite(){
        if (factionType == ICWarsFactionType.ALLY) {
            sprite = new Sprite("icwars/friendlyBuilding", 1f, 1f, this, null, new Vector(0f, 0f));
        } else if (factionType == ICWarsFactionType.ENEMY) {
            sprite = new Sprite("icwars/enemyBuilding", 1f, 1f, this, null, new Vector(0f, 0f));
        }else if (factionType == ICWarsFactionType.NEUTRAL){
            sprite = new Sprite("icwars/neutralBuilding", 1f, 1f, this, null, new Vector(0f, 0f));
        }
    }

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

    public void setFaction(ICWarsFactionType factionType){
        this.factionType = factionType;
        this.updateSprite();
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
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
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(HANDLER);
    }

    private class ICWarsCityInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit u) {
            if(u.getFaction() != City.this.getFaction()){
                if(!(u.getActionsList().getLast() instanceof Capture)){
                    u.addAction(new Capture(u, (ICWarsArea) getOwnerArea(), City.this));
                }
            }
        }
    }
}
