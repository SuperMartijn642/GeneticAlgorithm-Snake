package supermartijn642.snakeai.screen.mainmenu;

import supermartijn642.snakeai.Population;
import supermartijn642.snakeai.PopulationLoader;
import supermartijn642.snakeai.SnakeGame;
import supermartijn642.snakeai.providers.CrossDistanceGameProvider;
import supermartijn642.snakeai.render.AnimatedButton;
import supermartijn642.snakeai.render.IButton;
import supermartijn642.snakeai.screen.IMenu;
import supermartijn642.snakeai.screen.Screen;
import supermartijn642.snakeai.screen.populationmenu.PopulationMenu;
import supermartijn642.snakeai.screen.variablemenu.VariableMenu;

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

    private static final int GAME_COLUMNS = 70;
    private static final int GAME_ROWS = (int)(Screen.HEIGHT_PER_WIDTH * GAME_COLUMNS);
    private static final double GRID_SIZE = Screen.width / (double)(GAME_COLUMNS + 2);
    private static final int FRAMES_PER_UPDATE = 2;

    private BufferedImage imgGame;
    private BufferedImage imgGameBlurred;
    private ArrayList<IButton> buttons = new ArrayList<>(2);
    private SnakeGame game;

    private int frameCount = 0;

    public MainMenu(int width, int height){
        this.imgGame = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.imgGameBlurred = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.buttons.add(new AnimatedButton(0.25D * width,0.45D * height,0.2D * width,0.1D * height,Color.DARK_GRAY,new Color(0,230,0),"New Population") {
            @Override
            public void onClick() {
                Screen.addMenu(new PopulationMenu(new Population(new Random().nextLong(),2000,100,100,35,1)));
//                Screen.addMenu(new VariableMenu());
            }
        });
        this.buttons.add(new AnimatedButton(0.55D * width,0.45D * height,0.2D * width,0.1D * height,Color.DARK_GRAY,new Color(0,230,0),"Load Population") {
            @Override
            public void onClick() {
                Population population = PopulationLoader.handleLoad();
                if(population != null)
                    Screen.addMenu(new PopulationMenu(population));
            }
        });
        this.createNewGame();
    }

    private void createNewGame(){
        this.game = new SnakeGame(new CrossDistanceGameProvider(x -> x,1,10){
            @Override
            public int getXSize(SnakeGame game) {
                return GAME_COLUMNS;
            }
            @Override
            public int getYSize(SnakeGame game) {
                return GAME_ROWS;
            }
            @Override
            public Point getNewFoodPos(SnakeGame game) {
                Random random = new Random(game.getSeed() + game.getUpdates());
                Point point;
                loop: while(true){
                    point = new Point(random.nextInt(this.getXSize(game)),random.nextInt(this.getYSize(game)));
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

            @Override
            public boolean timeout(SnakeGame game) {
                return false;
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
        if(frameCount >= FRAMES_PER_UPDATE) {
            game.update();
            frameCount = 0;
        }
        else
            frameCount++;
        Graphics2D graphics = this.imgGame.createGraphics();
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0,0,this.imgGame.getWidth(),this.imgGame.getHeight());
        // walls
        graphics.setColor(Color.WHITE);
        for(int x = 0; x <= 1; x++){
            for(int y = 0; y < GAME_ROWS + 2; y++){
                graphics.fillRect((int)(x * (GAME_COLUMNS + 1) * GRID_SIZE + 1),(int)(y * GRID_SIZE + 1),(int)GRID_SIZE - 2,(int)GRID_SIZE - 2);
            }
        }
        for(int y = 0; y <= 1; y++){
            for(int x = 0; x < GAME_COLUMNS + 2; x++){
                graphics.fillRect((int)(x * GRID_SIZE + 1),(int)(y * (GAME_ROWS + 1) * GRID_SIZE + 1),(int)GRID_SIZE - 2,(int)GRID_SIZE - 2);
            }
        }
        // snake
        graphics.setColor(Color.GREEN);
        graphics.fillRect((int)((this.game.getHead().x + 1) * GRID_SIZE + 1),(int)((this.game.getHead().y + 1) * GRID_SIZE + 1),(int)GRID_SIZE - 2, (int)GRID_SIZE - 2);
        for(Point point : this.game.getTale())
            graphics.fillRect((int)((point.x + 1) * GRID_SIZE + 1),(int)((point.y + 1) * GRID_SIZE + 1),(int)GRID_SIZE - 2, (int)GRID_SIZE - 2);
        // food
        graphics.setColor(Color.RED);
        for(Point point : this.game.getFood())
            graphics.fillRect((int)((point.x + 1) * GRID_SIZE + 1),(int)((point.y + 1) * GRID_SIZE + 1),(int)GRID_SIZE - 2, (int)GRID_SIZE - 2);
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
