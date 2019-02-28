package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.Position;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class IterationButton extends SquareButton {

    private final PopulationMenu menu;
    private final int iterations;
    private int fontSize;

    public IterationButton(Position pos, double size, PopulationMenu menu, int iterations,int fontSize) {
        super(pos,(int)size);
        this.menu = menu;
        this.iterations = iterations;
        this.fontSize = fontSize;
    }

    @Override
    public void drawIcon(Graphics2D graphics) {
        graphics.setColor(this.canBeClicked() ? Color.GREEN : new Color(0,120,0));
        graphics.setFont(new Font("",Font.PLAIN,this.fontSize));
        graphics.drawString("" + this.iterations,(int)(this.pos.x + (this.size - graphics.getFontMetrics().stringWidth("" + this.iterations)) / 2D),
            (int)(this.pos.y + graphics.getFontMetrics().getAscent() + (this.size - graphics.getFontMetrics().getHeight()) / 2D));
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
