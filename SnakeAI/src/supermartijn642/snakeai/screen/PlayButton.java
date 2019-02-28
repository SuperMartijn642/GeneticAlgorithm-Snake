package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.Position;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class PlayButton extends SquareButton {

    private final PopulationMenu menu;
    private final Polygon paused;
    private final Polygon unpaused1,unpaused2;

    public PlayButton(Position pos, double size, PopulationMenu menu){
        super(pos,(int)size);
        this.menu = menu;
        double x = pos.x;
        double y = pos.y;
        int[] xpoint = new int[]{(int)(x + 0.15D * size),(int)(x + 0.15D * size),(int)(x + (1 - 0.15D) * size)};
        int[] ypoint = new int[]{(int)(y + (1 - 0.9D) * size),(int)(y + 0.9D * size),(int)(y + 0.5D * size)};
        this.paused = new Polygon(xpoint,ypoint,3);
        xpoint = new int[]{(int)(x + 0.12D * size),(int)(x + 0.12D * size),(int)(x + 0.475D * size),(int)(x + 0.475D * size)};
        ypoint = new int[]{(int)(y + 0.9D * size),(int)(y + 0.1D * size),(int)(y + 0.1D * size),(int)(y + 0.9D * size)};
        this.unpaused1 = new Polygon(xpoint,ypoint,4);
        xpoint = new int[]{(int)(x + (1 - 0.12D) * size),(int)(x + (1 - 0.12D) * size),(int)(x + (1 - 0.475D) * size),(int)(x + (1 - 0.475D) * size)};
        ypoint = new int[]{(int)(y + 0.9D * size),(int)(y + 0.1D * size),(int)(y + 0.1D * size),(int)(y + 0.9D * size)};
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
}
