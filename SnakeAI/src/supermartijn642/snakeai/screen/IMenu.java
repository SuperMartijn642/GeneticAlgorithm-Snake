package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.render.IButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public interface IMenu {

    void draw(Graphics2D graphics);
    List<IButton> getButtons();
    default void mouseDown(Point mouse){}
    default void mouseUp(Point mouse){}
    default void keyDown(KeyEvent event){}
    default void keyUp(KeyEvent event){}

}
