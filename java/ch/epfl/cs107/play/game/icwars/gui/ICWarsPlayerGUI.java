package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class ICWarsPlayerGUI implements Graphics {

    private ICWarsPlayer player;
    private Unit selectedUnit;
    private ICWarsBehavior.ICWarsCellType cellType;
    private ICWarsActionsPanel actionPanel;
    private ICWarsInfoPanel infoPanel;

    protected static final float FONT_SIZE = 20.f;

    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player){
        this.player = player;
        this.actionPanel = new ICWarsActionsPanel(cameraScaleFactor);
        this.infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
    }

    @Override
    public void draw(Canvas canvas) {
        if(selectedUnit != null){
            if(player.getCurrentState() == ICWarsPlayer.PlayState.MOVE_UNIT){
                selectedUnit.drawRangeAndPathTo(new DiscreteCoordinates(player.getPosition()), canvas);
            }
            if(player.getCurrentState() == ICWarsPlayer.PlayState.ACTION_SELECTION) {
                actionPanel.setActions(selectedUnit.getActionsList());
                actionPanel.draw(canvas);
            }
        }
        if(player.getCurrentState() == ICWarsPlayer.PlayState.NORMAL || player.getCurrentState() == ICWarsPlayer.PlayState.SELECT_CELL) {
            infoPanel.draw(canvas);
        }
    }

    /**
     * Set the unit selected by the player
     * @param selectedUnit
     */
    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    /**
     * Set the hovered unit the player is currently on for the info panel
     * @param hoveredUnit
     */
    public void setHoveredUnit(Unit hoveredUnit) {
        infoPanel.setUnit(hoveredUnit);
    }

    /**
     * Set the cell the player is currently on
     * @param cellType
     */
    public void setCell(ICWarsBehavior.ICWarsCellType cellType) {
        infoPanel.setCurrentCell(cellType);
    }
}
