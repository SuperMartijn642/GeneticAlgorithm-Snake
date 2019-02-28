package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.render.IButton;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public abstract class SquareButton implements IButton {

    protected Position pos;
    protected boolean hover = false;
    protected final int size;

    public SquareButton(double x,double y,int size){
        this.pos = new Position(x,y);
        this.size = size;
    }

    public SquareButton(Position pos,int size){
        this(pos.x,pos.y,size);
    }

    @Override
    public Position getPosition() {
        return this.pos;
    }

    @Override
    public double getRenderWidth() {
        return this.size;
    }

    @Override
    public double getRenderHeight() {
        return this.size;
    }

    @Override
    public double getClickWidth() {
        return this.size;
    }

    @Override
    public double getClickHeight() {
        return this.size;
    }

    @Override
    public void draw(Graphics2D graphics) {
        if(this.hover && this.canBeClicked()) {
            graphics.setColor(Color.GRAY);
            graphics.fillRoundRect((int) this.pos.x, (int) this.pos.y, this.size, this.size, (int) (this.size * 0.2D), (int) (this.size * 0.2D));
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawRoundRect((int) this.pos.x, (int) this.pos.y, this.size, this.size, (int) (this.size * 0.2D), (int) (this.size * 0.2D));
        }
        this.drawIcon(graphics);
    }

    public abstract void drawIcon(Graphics2D graphics);
    public abstract boolean canBeClicked();

    @Override
    public void onHoverEnter() {
        this.hover = true;
    }

    @Override
    public void onHoverExit() {
        this.hover = false;
    }
}
