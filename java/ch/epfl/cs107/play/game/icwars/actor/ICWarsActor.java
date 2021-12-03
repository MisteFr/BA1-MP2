package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class ICWarsActor extends MovableAreaEntity {
    private ICWarsFactionType faction;

    public enum ICWarsFactionType{
        NONE(0, "none"),
        ALLY(1, "friendly"),
        ENEMY(2, "enemy"),;

        final int type;
        final String name;

        ICWarsFactionType(int type, String name){
            this.type = type;
            this.name = name;
        }

        public static ICWarsActor.ICWarsFactionType toType(int type){
            for(ICWarsActor.ICWarsFactionType ict : ICWarsActor.ICWarsFactionType.values()){
                if(ict.type == type){
                    return ict;
                }
            }
            return NONE;
        }

        public String getName(){
            return this.name;
        }
    }


    public ICWarsActor(Area owner, DiscreteCoordinates coordinates, ICWarsFactionType faction){
        super(owner, Orientation.UP, coordinates);
        this.faction = faction;
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     *
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
