package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;

public interface ICWarsInteractionVisitor extends AreaInteractionVisitor {

    default void interactWith(ICWarsPlayer player){
        //Interaction is not doing anything
    }

    default void interactWith(Unit unit){
        //Interaction is not doing anything
    }

    default void interactWith(ICWarsBehavior.ICWarsCell cell){
        //Interaction is not doing anything
    }
}
