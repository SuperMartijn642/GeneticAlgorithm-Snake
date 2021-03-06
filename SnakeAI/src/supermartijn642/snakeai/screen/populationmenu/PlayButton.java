package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.SquareButton;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class PlayButton extends SquareButton {

    private static final double ICON_MARGIN = 0.2D;

    private final Polygon paused;
    private final Polygon unpaused1,unpaused2;

    public PlayButton(Position pos, double size, PopulationMenu menu){
        super(pos,(int)size,menu);
        double x = pos.x;
        double y = pos.y;
        int[] xpoint = new int[]{(int)(x + ICON_MARGIN * size),(int)(x + ICON_MARGIN * size),(int)(x + (1 - ICON_MARGIN * 0.8D) * size)};
        int[] ypoint = new int[]{(int)(y + (1 - ICON_MARGIN) * size),(int)(y + ICON_MARGIN * size),(int)(y + 0.5D * size)};
        this.paused = new Polygon(xpoint,ypoint,3);
        xpoint = new int[]{(int)(x + ICON_MARGIN * size),(int)(x + ICON_MARGIN * size),(int)(x + 0.475D * size),(int)(x + 0.475D * size)};
        ypoint = new int[]{(int)(y + (1 - ICON_MARGIN) * size),(int)(y + ICON_MARGIN * size),(int)(y + ICON_MARGIN * size),(int)(y + (1 - ICON_MARGIN) * size)};
        this.unpaused1 = new Polygon(xpoint,ypoint,4);
        xpoint = new int[]{(int)(x + (1 - ICON_MARGIN) * size),(int)(x + (1 - ICON_MARGIN) * size),(int)(x + (1 - 0.475D) * size),(int)(x + (1 - 0.475D) * size)};
        ypoint = new int[]{(int)(y + (1 - ICON_MARGIN) * size),(int)(y + ICON_MARGIN * size),(int)(y + ICON_MARGIN * size),(int)(y + (1 - ICON_MARGIN) * size)};
        this.unpaused2 = new Polygon(xpoint,ypoint,4);
    }

    @Override
    public void drawIcon(Graphics2D graphics) {
        graphics.setColor(Color.GREEN);
        if(this.menu.paused){
            graphics.fillPolygon(this.paused);
        }
        else{
            graphics.fillPolygon(this.unpaused1);
            graphics.fillPolygon(this.unpaused2);
        }
    }

    @Override
    public boolean canBeClicked() {
        return true;
    }

    @Override
    public void onClick() {
        this.menu.paused = !this.menu.paused;
    }

    @Override
    public String getTooltip() {
        return this.menu.population.getGeneration() == 0 ? "Start the Simulation" : this.menu.paused ? "Unpause the Simulation" : "Pause the Simulation";
    }
}
