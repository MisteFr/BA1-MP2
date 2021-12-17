package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class ICWarsActor extends MovableAreaEntity {
    private final ICWarsFactionType faction;

    public enum ICWarsFactionType {
        NONE(0),
        ALLY(1),
        ENEMY(2),
        ;

        final int type;

        ICWarsFactionType(int type) {
            this.type = type;
        }
    }


    public ICWarsActor(Area owner, DiscreteCoordinates coordinates, ICWarsFactionType faction) {
        super(owner, Orientation.UP, coordinates);
        this.faction = faction;
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * @param area (Area): initial area, not null
     */
    public void enterArea(Area area) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        resetMotion();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public ICWarsFactionType getFaction() {
        return faction;
    }
}
