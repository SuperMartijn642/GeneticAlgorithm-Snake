package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.SquareButton;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class IterationButton extends SquareButton {

    private final PopulationMenu menu;
    private final int iterations;
    private int fontSize;

    public IterationButton(Position pos,double width,double height,PopulationMenu menu,int iterations,int fontSize){
        super(pos,(int)width,(int)height);
        this.menu = menu;
        this.iterations = iterations;
        this.fontSize = fontSize;
    }

    public IterationButton(Position pos,double size,PopulationMenu menu,int iterations,int fontSize) {
        this(pos,size,size,menu,iterations,fontSize);
    }

    @Override
    public void drawIcon(Graphics2D graphics) {
        graphics.setColor(this.canBeClicked() ? Color.GREEN : new Color(0,120,0));
        graphics.setFont(new Font("",Font.PLAIN,this.fontSize));
        graphics.drawString("" + this.iterations,(int)(this.pos.x + (this.width - graphics.getFontMetrics().stringWidth("" + this.iterations)) / 2D),
            (int)(this.pos.y + graphics.getFontMetrics().getAscent() + (this.height - graphics.getFontMetrics().getHeight()) / 2D));
    }

    @Override
    public boolean canBeClicked() {
        return this.menu.paused && this.menu.todo <= 0;
    }

    @Override
    public void onClick() {
        if(this.canBeClicked()) {
            this.menu.todo = this.iterations;
            this.menu.paused = false;
        }
    }
}
