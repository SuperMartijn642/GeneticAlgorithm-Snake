package supermartijn642.snakeai;

import java.awt.*;

/**
 * Created 2/16/2019 by SuperMartijn642
 */
public interface SnakeGameProvider {

    void init(SnakeGame game);

    int getXSize(SnakeGame game);
    int getYSize(SnakeGame game);
    int getSnakeLength(SnakeGame game);
    int getFoodCount(SnakeGame game);
    Point getStartPos();
    boolean timeout(SnakeGame game);
    Point getHeading(SnakeGame game);
    Point getNewFoodPos(SnakeGame game);
    double getFitness(SnakeGame game);

}
