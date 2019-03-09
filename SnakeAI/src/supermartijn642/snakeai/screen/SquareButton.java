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
    protected final int width,height;

    public SquareButton(double x,double y,int size){
        this(x,y,size,size);
    }

    public SquareButton(double x,double y,int width,int height){
        this.pos = new Position(x,y);
        this.width = width;
        this.height = height;
    }

    public SquareButton(Position pos,int size){
        this(pos.x,pos.y,size);
    }

    public SquareButton(Position pos,int width,int height){
        this(pos.x,pos.y,width,height);
    }

    @Override
    public Position getPosition() {
        return this.pos;
    }

    @Override
    public double getRenderWidth() {
        return this.width;
    }

    @Override
    public double getRenderHeight() {
        return this.height;
    }

    @Override
    public double getClickWidth() {
        return this.width;
    }

    @Override
    public double getClickHeight() {
        return this.height;
    }

    @Override
    public void draw(Graphics2D graphics) {
        if(this.hover && this.canBeClicked()) {
            graphics.setColor(Color.GRAY);
            graphics.fillRoundRect((int) this.pos.x, (int) this.pos.y, this.width, this.height, (int) (this.height * 0.2D), (int) (this.height * 0.2D));
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawRoundRect((int) this.pos.x, (int) this.pos.y, this.width, this.height, (int) (this.height * 0.2D), (int) (this.height * 0.2D));
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
