package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.SquareButton;

import java.awt.*;

/**
 * Created 2/28/2019 by SuperMartijn642
 */
public class ShowAllButton extends SquareButton {

    public ShowAllButton(Position pos, double size, PopulationMenu menu){
        super(pos,(int)size,menu);
    }

    @Override
    public void drawIcon(Graphics2D graphics) {
        graphics.setColor(this.menu.every_generation ? Color.GREEN : Color.RED);
        graphics.fillRect((int)(this.pos.x + this.width * 0.2D),(int)(this.pos.y + this.height * 0.2D),(int)(0.6D * this.width),(int)(0.6D * this.height));
    }

    @Override
    public boolean canBeClicked() {
        return true;
    }

    @Override
    public void onClick() {
        this.menu.every_generation = !this.menu.every_generation;
    }

    @Override
    public String getTooltip() {
        return this.menu.every_generation ? "Replay Only Last Generation" : "Replay Every Generation";
    }
}
