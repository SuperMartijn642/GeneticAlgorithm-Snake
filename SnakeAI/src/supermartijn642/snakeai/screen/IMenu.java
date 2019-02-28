package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.render.IButton;

import java.awt.*;
import java.util.List;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public interface IMenu {

    void draw(Graphics2D graphics);
    List<IButton> getButtons();

}
