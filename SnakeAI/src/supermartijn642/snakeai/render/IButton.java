package supermartijn642.snakeai.render;

import supermartijn642.snakeai.Position;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public interface IButton {

    Position getPosition();
    double getRenderWidth();
    double getRenderHeight();
    double getClickWidth();
    double getClickHeight();

    void draw(Graphics2D graphics);
    void onClick();
    void onHoverEnter();
    void onHoverExit();

}
