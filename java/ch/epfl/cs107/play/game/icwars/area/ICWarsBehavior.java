package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class ICWarsBehavior extends AreaBehavior {
    public enum ICWarsCellType{
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NONE(0, 0),
        ROAD(-16777216, 0),
        PLAIN(-14112955,1),
        WOOD(-65536, 3),
        RIVER(-16776961, 0),
        MOUNTAIN(-256, 4),
        CITY(-1, 2),;

        final int type;
        final int numberDefenseStars;

        ICWarsCellType(int type, int numberDefenseStars){
            this.type = type;
            this.numberDefenseStars = numberDefenseStars;
        }

        public static ICWarsBehavior.ICWarsCellType toType(int type){
            for(ICWarsBehavior.ICWarsCellType ict : ICWarsBehavior.ICWarsCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            return NONE;
        }

        public int getDefenseStar() {
            return numberDefenseStars;
        }

        public String typeToString(){
            switch (this){
                case ROAD:
                    return "ROAD";
                case PLAIN:
                    return "PLAIN";
                case WOOD:
                    return "WOOD";
                case RIVER:
                    return "RIVER";
                case MOUNTAIN:
                    return "MOUTAIN";
                case CITY:
                    return "CITY";
            }
            return "NONE";
        }
    }

    /**
     * Default ICWarsBehavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public ICWarsBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICWarsBehavior.ICWarsCellType color = ICWarsBehavior.ICWarsCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICWarsBehavior.ICWarsCell(x,y, color));
            }
        }
    }

    /**
     * Cell adapted to the ICWars game
     */
    public class ICWarsCell extends AreaBehavior.Cell {
        /// Type of the cell following the enum
        private final ICWarsBehavior.ICWarsCellType type;

        /**
         * Default ICWarsCell Constructor
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of thecell
         * @param type (ICWarsCellType), not null
         */
        public ICWarsCell(int x, int y, ICWarsBehavior.ICWarsCellType type){
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if(!entity.takeCellSpace()){
                return true;
            }

            for(Interactable interactable : entities){
                if(interactable.takeCellSpace()){
                    return false;
                }
            }

            return true;
        }

        public ICWarsCellType getType() {
            return type;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ICWarsInteractionVisitor)v).interactWith(this);
        }

    }
}
