package supermartijn642.snakeai;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created 2/16/2019 by SuperMartijn642
 */
public class SnakeGame {

    private final SnakeGameProvider provider;

    private boolean finished = false;
    private int score = 0;
    private int update = 0;

    private Point head;
    private final ArrayList<Point> tale = new ArrayList<>();
    private final ArrayList<Point> food = new ArrayList<>();

    private final long seed;
    private final Random random;

    public SnakeGame(SnakeGameProvider provider, long seed){
        this.provider = provider;
        this.seed = seed;
        this.random = new Random(seed);
        this.provider.init(this);
        this.head = provider.getStartPos();
        while(food.size() < provider.getFoodCount(this))
            food.add(provider.getNewFoodPos(this));
    }

    public void update(){
        if(this.finished)
            return;

        this.tale.add(new Point(this.head.x,this.head.y));
        Point heading = this.provider.getHeading(this);
        this.head.translate(heading.x,heading.y);
        // check for walls
        if(this.head.x < 0 || this.head.x >= this.provider.getXSize(this) || this.head.y < 0 || this.head.y >= this.provider.getYSize(this)){
            this.finished = true;
            return;
        }
        // check for tale
        for(Point point : this.tale)
            if(this.head.equals(point)){
                this.finished = true;
                return;
            }

        // update tale
        if(this.tale.size() > this.provider.getSnakeLength(this))
            this.tale.remove(0);

        // check for food
        for(int a = food.size() - 1; a >= 0; a--)
            if(this.head.equals(food.get(a))){
                this.score++;
                this.food.remove(a);
                if(this.provider.getFoodCount(this) > this.food.size())
                    this.food.add(this.provider.getNewFoodPos(this));
            }

        this.update++;
        if(this.provider.timeout(this))
            this.finished = true;
//        System.out.println("id: " + ((NeuralNetworkGameProvider)this.provider).network.id + " x: " + this.head.x + " y: " + this.head.y);
    }

    public void update(int updates){
        for(int a = 0; a < updates && !this.finished; a++)
            this.update();
    }

    public void finish(){
        while (!this.finished)
            this.update();
    }

    public boolean isFinished(){
        return this.finished;
    }

    public int getScore(){
        return this.score;
    }

    public int getUpdates(){
        return this.update;
    }

    public Point getHead(){
        return new Point(this.head.x,this.head.y);
    }

    public List<Point> getTale(){
        return Collections.unmodifiableList(this.tale);
    }

    public List<Point> getFood(){
        return Collections.unmodifiableList(this.food);
    }

    public long getSeed(){
        return this.seed;
    }

    public Random getRandom(){
        return this.random;
    }

    public double getFitness(){
        return this.provider.getFitness(this);
    }

    public SnakeGameProvider getProvider() {
        return provider;
    }

    public SnakeGame getReplayable(){
        return new SnakeGame(this.provider,this.seed);
    }
}
