package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class ICWarsPlayerGUI implements Graphics {

    private final ICWarsPlayer PLAYER;
    private Unit selectedUnit;
    private final ICWarsActionsPanel ACTION_PANEL;
    private final ICWarsInfoPanel INFO_PANEL;

    protected static final float FONT_SIZE = 20.f;

    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player){
        this.PLAYER = player;
        this.ACTION_PANEL = new ICWarsActionsPanel(cameraScaleFactor);
        this.INFO_PANEL = new ICWarsInfoPanel(cameraScaleFactor);
    }

    @Override
    public void draw(Canvas canvas) {
        if(selectedUnit != null){
            if(PLAYER.getCurrentState() == ICWarsPlayer.PlayState.MOVE_UNIT){
                selectedUnit.drawRangeAndPathTo(new DiscreteCoordinates(PLAYER.getPosition()), canvas);
            }
            if(PLAYER.getCurrentState() == ICWarsPlayer.PlayState.ACTION_SELECTION) {
                ACTION_PANEL.setActions(selectedUnit.getActionsList());
                ACTION_PANEL.draw(canvas);
            }
        }
        if(PLAYER.getCurrentState() == ICWarsPlayer.PlayState.NORMAL || PLAYER.getCurrentState() == ICWarsPlayer.PlayState.SELECT_CELL) {
            INFO_PANEL.draw(canvas);
        }
    }

    /**
     * Set the unit selected by the player
     * @param selectedUnit selected unit by the player
     */
    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    /**
     * Set the hovered unit the player is currently on for the info panel
     * @param hoveredUnit hovered unit
     */
    public void setHoveredUnit(Unit hoveredUnit) {
        INFO_PANEL.setUnit(hoveredUnit);
    }

    /**
     * Set the cell the player is currently on
     * @param cellType type of the cell (ICWarsCellType)
     */
    public void setCell(ICWarsBehavior.ICWarsCellType cellType) {
        INFO_PANEL.setCurrentCell(cellType);
    }
}
