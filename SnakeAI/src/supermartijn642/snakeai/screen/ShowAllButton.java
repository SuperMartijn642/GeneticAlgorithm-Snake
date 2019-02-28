package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.Position;

import java.awt.*;

/**
 * Created 2/28/2019 by SuperMartijn642
 */
public class ShowAllButton extends SquareButton {

    private final PopulationMenu menu;

    public ShowAllButton(Position pos, double size, PopulationMenu menu){
        super(pos,(int)size);
        this.menu = menu;
    }

    @Override
    public void drawIcon(Graphics2D graphics) {
        graphics.setColor(this.menu.every_generation ? Color.GREEN : Color.RED);
        graphics.fillRect((int)(this.pos.x + this.size * 0.2D),(int)(this.pos.y + this.size * 0.2D),(int)(0.6D * this.size),(int)(0.6D * this.size));
    }

    @Override
    public boolean canBeClicked() {
        return true;
    }

    @Override
    public void onClick() {
        this.menu.every_generation = !this.menu.every_generation;
    }

}
