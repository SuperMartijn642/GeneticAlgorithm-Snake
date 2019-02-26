package supermartijn642.snakeai;

import java.awt.*;

/**
 * Created 2/18/2019 by SuperMartijn642
 */
public abstract class BasicGameProvider implements SnakeGameProvider {

    public final int x_size, y_size;
    public final int snake_length;
    public final int food_count;
    public final int timeout;
    public final int timeout_bonus;

    public BasicGameProvider(int size, int snake_length, int food_count, int timeout, int timeout_bonus){
        this.x_size = size;
        this.y_size = size;
        this.snake_length = snake_length;
        this.food_count = food_count;
        this.timeout = timeout;
        this.timeout_bonus = timeout_bonus;
    }

    @Override
    public void init(SnakeGame game) {}

    @Override
    public int getXSize(SnakeGame game) {
        return this.x_size;
    }

    @Override
    public int getYSize(SnakeGame game) {
        return this.y_size;
    }

    @Override
    public int getSnakeLength(SnakeGame game) {
        return this.snake_length + game.getScore();
    }

    @Override
    public int getFoodCount(SnakeGame game) {
        return this.food_count;
    }

    @Override
    public Point getStartPos() {
        return new Point(this.x_size / 2,this.y_size / 2);
    }

    @Override
    public boolean timeout(SnakeGame game) {
        return !(this.timeout <= 0 || game.getUpdates() < (this.timeout + game.getScore() * this.timeout_bonus));
    }

    @Override
    public Point getNewFoodPos(SnakeGame game) {
        Point point;
        loop: while(true){
            point = new Point(game.getRandom().nextInt(this.x_size),game.getRandom().nextInt(this.y_size));
            if(game.getHead().equals(point))
                continue;
            for(Point point1 : game.getTale())
                if(point1.equals(point))
                    continue loop;
            for(Point point1 : game.getFood())
                if(point1.equals(point))
                    continue loop;
            break;
        }
        return point;
    }

    @Override
    public double getFitness(SnakeGame game) {
        return game.getScore();
    }
}
