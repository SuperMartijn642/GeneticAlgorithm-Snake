package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.Population;
import supermartijn642.snakeai.SnakeGame;
import supermartijn642.snakeai.render.IButton;
import supermartijn642.snakeai.screen.IMenu;
import supermartijn642.snakeai.screen.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class PopulationMenu implements IMenu {

    private static final boolean BAR_LEFT = true;
    private static final double GAME_SIZE = 0.4D;
    private static final double INFO_SIZE = 0.3D;
    private static final double BAR_SIZE = 1 - GAME_SIZE - INFO_SIZE;

    private int gameSize;

    public ArrayList<IButton> buttons = new ArrayList<>();

    private SideBar sideBar;
    private Graph graph;
    public Population population;
    public SnakeGame game;
    private InfoBar infoBar;

    public boolean display_best = true;
    public boolean every_generation = true;
    public boolean paused = true;
    public int todo = -1;

    public PopulationMenu(Population population){
        this.sideBar = new SideBar(this,BAR_SIZE,BAR_LEFT);
        this.graph = new Graph(this,GAME_SIZE,BAR_LEFT ? BAR_SIZE : 0,GAME_SIZE + INFO_SIZE);
        this.population = population;
        this.gameSize = (int)(Screen.width * GAME_SIZE);
        this.infoBar = new InfoBar(this,INFO_SIZE,(BAR_LEFT ? BAR_SIZE : 0) + GAME_SIZE,GAME_SIZE);
    }

    @Override
    public void draw(Graphics2D graphics) {
        this.graph.draw(graphics);
        this.sideBar.draw(graphics);
        this.drawGame(graphics);
        this.infoBar.draw(graphics,this);
        this.buttons.forEach(b -> b.draw(graphics));
    }

    private void drawGame(Graphics2D graphics){
        if(!this.paused && !this.population.isRunning() && (this.todo == -1 || this.todo > 0)) {
            if(!every_generation || (this.game == null || this.game.isFinished())) {
                if(this.population.getGeneration() > 0)
                    this.graph.points.add(this.population.getCurrentBestGame().getScore());
                this.population.runAsync(1);
                if(this.todo > 0)
                    this.todo--;
            }
        }
        if(this.game == null && !this.paused && this.population.getGeneration() > 0)
            this.game = this.population.getCurrentBestGame();
        graphics.setColor(Color.BLACK);
        // variables
        int left = BAR_LEFT ? this.sideBar.width : 0;
        int top = Screen.height - this.gameSize;
        double gridSize = (double)this.gameSize / (this.population.getGameSize() + 2);
        graphics.fillRect(left,top,this.gameSize,this.gameSize);
        // updating the game
        if(this.game != null){
            if(this.game.isFinished()){
                if(!this.paused)
                    this.game = this.population.getCurrentBestGame().getReplayable();
            }
            else if(!this.paused)
                this.game.update();
        }
        // walls
        graphics.setColor(Color.WHITE);
        for(int x = 0; x <= 1; x++){
            for(int y = 0; y < this.population.getGameSize() + 2; y++){
                graphics.fillRect((int)(left + x * (this.population.getGameSize() + 1) * gridSize + 1),(int)(top + y * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            }
        }
        for(int y = 0; y <= 1; y++){
            for(int x = 0; x < this.population.getGameSize() + 2; x++){
                graphics.fillRect((int)(left + x * gridSize + 1),(int)(top + y * (this.population.getGameSize() + 1) * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            }
        }
        // the game
        if(this.game != null){
            // snake
            graphics.setColor(Color.GREEN);
            graphics.fillRect((int)(left + (this.game.getHead().x + 1) * gridSize + 1),(int)(top + (this.game.getHead().y + 1) * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            for(Point point : this.game.getTale()){
                graphics.fillRect((int)(left + (point.x + 1) * gridSize + 1),(int)(top + (point.y + 1) * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            }
            // food
            graphics.setColor(Color.RED);
            for(Point point : this.game.getFood()){
                graphics.fillRect((int)(left + (point.x + 1) * gridSize + 1),(int)(top + (point.y + 1) * gridSize + 1),(int)gridSize - 2,(int)gridSize - 2);
            }
        }
        if(!this.paused && this.todo == 0) {
            this.paused = true;
            this.todo = -1;
        }
    }

    @Override
    public List<IButton> getButtons() {
        return this.buttons;
    }
}
