package supermartijn642.snakeai;

import supermartijn642.snakeai.providers.CrossDistanceGameProvider;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public SnakeGame(SnakeGameProvider provider, long seed){
        this.provider = provider;
        this.seed = seed;
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

    public double getFitness(){
        return this.provider.getFitness(this);
    }

    public SnakeGameProvider getProvider() {
        return provider;
    }

    public SnakeGame getReplayable(){
        return new SnakeGame(this.provider,this.seed);
    }

    public byte[] toBytes(){
        byte[] provider = this.provider.toBytes();
        ByteBuffer buffer = ByteBuffer.allocate(provider.length + 1 + (7 + (this.tale.size() + this.food.size()) * 2) * 4 + 8);
        buffer.putInt(provider.length);
        buffer.put(provider);
        buffer.put(this.finished ? (byte)1 : (byte)0);
        buffer.putInt(this.score);
        buffer.putInt(this.update);
        buffer.putInt(this.head.x);
        buffer.putInt(this.head.y);
        buffer.putInt(this.tale.size());
        for(Point point : this.tale){
            buffer.putInt(point.x);
            buffer.putInt(point.y);
        }
        buffer.putInt(this.food.size());
        for(Point point : this.food){
            buffer.putInt(point.x);
            buffer.putInt(point.y);
        }
        buffer.putLong(this.seed);
        return buffer.array();
    }

    public static SnakeGame fromBytes(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        byte[] b = new byte[length];
        buffer.get(b);
        CrossDistanceGameProvider provider = new CrossDistanceGameProvider();
        provider.fromBytes(b);
        boolean finished = buffer.get() == 1;
        int score = buffer.getInt();
        int update = buffer.getInt();
        Point head = new Point(buffer.getInt(),buffer.getInt());
        length = buffer.getInt();
        ArrayList<Point> tale = new ArrayList<>(length);
        for(int a = 0; a < length; a++)
            tale.add(new Point(buffer.getInt(),buffer.getInt()));
        length = buffer.getInt();
        ArrayList<Point> food = new ArrayList<>(length);
        for(int a = 0; a < length; a++)
            food.add(new Point(buffer.getInt(),buffer.getInt()));
        long seed = buffer.getLong();
        SnakeGame game = new SnakeGame(provider,seed);
        game.finished = finished;
        game.score = score;
        game.update = update;
        game.head = head;
        game.tale.clear();
        game.tale.addAll(tale);
        game.food.clear();
        game.food.addAll(food);
        return game;
    }
}
