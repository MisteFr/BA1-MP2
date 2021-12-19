package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaPauseMenu;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class ICWarsPauseMenu extends AreaPauseMenu {

    private Background background;

    @Override
    protected void drawMenu(Canvas c) {
        background.draw(c);
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if(super.begin(window, fileSystem)){
            Area area = (Area) getOwner();
            background = new Background(area);
            area.registerActor(background);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getTitle() {
        return "Hello";
    }
}
