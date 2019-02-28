package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.Population;
import supermartijn642.snakeai.SnakeGame;
import supermartijn642.snakeai.providers.CrossDistanceGameProvider;
import supermartijn642.snakeai.render.AnimatedButton;
import supermartijn642.snakeai.render.IButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created 2/23/2019 by SuperMartijn642
 */
public class MainMenu implements IMenu {

    private BufferedImage imgGame;
    private BufferedImage imgGameBlurred;
    private ArrayList<IButton> buttons = new ArrayList<>(2);
    private SnakeGame game;
    private double gridSize = Screen.width / 144D;

    public MainMenu(int width, int height){
        this.imgGame = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.imgGameBlurred = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.buttons.add(new AnimatedButton(0.25D * width,0.45D * height,0.2D * width,0.1D * height,Color.BLUE,Color.cyan,"New Population") {
            @Override
            public void onClick() {
                Screen.addMenu(new PopulationMenu(new Population(new Random().nextLong(),2000,100,100,35,1)));
            }
        });
        this.buttons.add(new AnimatedButton(0.55D * width,0.45D * height,0.2D * width,0.1D * height,Color.BLUE,Color.cyan,"Load Population") {
            @Override
            public void onClick() {

            }
        });
        this.createNewGame();
    }

    private void createNewGame(){
        this.game = new SnakeGame(new CrossDistanceGameProvider(x -> x,1,10){
            @Override
            public int getXSize(SnakeGame game) {
                return 144 - 2;
            }
            @Override
            public int getYSize(SnakeGame game) {
                return 81 - 2;
            }
            @Override
            public Point getNewFoodPos(SnakeGame game) {
                Point point;
                loop: while(true){
                    point = new Point(game.getRandom().nextInt(this.getXSize(game)),game.getRandom().nextInt(this.getYSize(game)));
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
            public Point getStartPos() {
                return new Point((int)(this.getXSize(null) / 2D),(int)(this.getYSize(null) / 2D));
            }
        },new Random().nextLong());
    }

    public void draw(Graphics2D graphics){
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,this.imgGame.getWidth(),this.imgGame.getHeight());
        this.drawGame();
        this.blurGame();
        graphics.drawImage(this.imgGameBlurred,0,0,this.imgGame.getWidth(),this.imgGame.getHeight(),null);
        for(IButton button : this.buttons)
            button.draw(graphics);
    }

    private void drawGame(){
        game.update();
        Graphics2D graphics = this.imgGame.createGraphics();
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0,0,this.imgGame.getWidth(),this.imgGame.getHeight());
        // walls
        graphics.setColor(Color.WHITE);
        for(int x = 0; x <= 1; x++){
            for(int y = 0; y < 81; y++){
                graphics.fillRect((int)(x * (144 - 1) * gridSize + 1),(int)(y * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            }
        }
        for(int y = 0; y <= 1; y++){
            for(int x = 0; x < 144; x++){
                graphics.fillRect((int)(x * gridSize + 1),(int)(y * (81 - 1) * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            }
        }
        // snake
        graphics.setColor(Color.GREEN);
        graphics.fillRect((int)((this.game.getHead().x + 1) * gridSize + 1),(int)((this.game.getHead().y + 1) * gridSize + 1),(int)gridSize - 2, (int)gridSize - 2);
        for(Point point : this.game.getTale())
            graphics.fillRect((int)((point.x + 1) * gridSize + 1),(int)((point.y + 1) * gridSize + 1),(int)gridSize - 2, (int)gridSize - 2);
        // food
        graphics.setColor(Color.RED);
        for(Point point : this.game.getFood())
            graphics.fillRect((int)((point.x + 1) * gridSize + 1),(int)((point.y + 1) * gridSize + 1),(int)gridSize - 2, (int)gridSize - 2);
        if(game.isFinished())
            this.createNewGame();
    }

    private void blurGame(){
        float weight = 1f / 9;
        float[] data = new float[9];
        for(int a = 0; a < data.length; a++)
            data[a] = weight;
        Kernel kernel = new Kernel(3,3,data);
        BufferedImageOp op = new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP,null);
        imgGameBlurred = op.filter(imgGame,null);
    }

    @Override
    public List<IButton> getButtons() {
        return this.buttons;
    }
}
