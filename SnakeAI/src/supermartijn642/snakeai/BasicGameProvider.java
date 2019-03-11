package supermartijn642.snakeai;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created 2/18/2019 by SuperMartijn642
 */
public abstract class BasicGameProvider implements SnakeGameProvider {

    public int x_size, y_size;
    public int snake_length;
    public int food_count;
    public int timeout;
    public int timeout_bonus;

    public BasicGameProvider(int size, int snake_length, int food_count, int timeout, int timeout_bonus){
        this.x_size = size;
        this.y_size = size;
        this.snake_length = snake_length;
        this.food_count = food_count;
        this.timeout = timeout;
        this.timeout_bonus = timeout_bonus;
    }

    public BasicGameProvider(){}

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
        return new Point((int)(this.x_size / 2D),(int)(this.y_size / 2D));
    }

    @Override
    public boolean timeout(SnakeGame game) {
        return !(this.timeout <= 0 || game.getUpdates() < (this.timeout + game.getScore() * this.timeout_bonus));
    }

    @Override
    public Point getNewFoodPos(SnakeGame game) {
        Random random = new Random(game.getSeed() + game.getUpdates());
        Point point;
        loop: while(true){
            point = new Point(random.nextInt(this.x_size),random.nextInt(this.y_size));
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

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(6 * 4);
        buffer.putInt(this.x_size);
        buffer.putInt(this.y_size);
        buffer.putInt(this.snake_length);
        buffer.putInt(this.food_count);
        buffer.putInt(this.timeout);
        buffer.putInt(this.timeout_bonus);
        return buffer.array();
    }

    @Override
    public void fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        this.x_size = buffer.getInt();
        this.y_size = buffer.getInt();
        this.snake_length = buffer.getInt();
        this.food_count = buffer.getInt();
        this.timeout = buffer.getInt();
        this.timeout_bonus = buffer.getInt();
    }
}
