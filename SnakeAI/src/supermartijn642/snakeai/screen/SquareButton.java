package supermartijn642.snakeai.screen;

import com.sun.istack.internal.Nullable;
import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.render.IButton;
import supermartijn642.snakeai.render.Tooltip;
import supermartijn642.snakeai.screen.populationmenu.PopulationMenu;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public abstract class SquareButton implements IButton {

    protected Position pos;
    protected boolean hover = false;
    protected final int width,height;
    protected final PopulationMenu menu;

    public SquareButton(double x,double y,int size,PopulationMenu menu){
        this(x,y,size,size,menu);
    }

    public SquareButton(double x,double y,int width,int height,PopulationMenu menu){
        this.pos = new Position(x,y);
        this.width = width;
        this.height = height;
        this.menu = menu;
    }

    public SquareButton(Position pos,int size,PopulationMenu menu){
        this(pos.x,pos.y,size,menu);
    }

    public SquareButton(Position pos,int width,int height,PopulationMenu menu){
        this(pos.x,pos.y,width,height,menu);
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
        Position mouse = new Position(Screen.lastMousePos).translate(-this.pos.x,-this.pos.y);
        if(!(this.getTooltip() == null || this.getTooltip().equals("")) && mouse.x > 0 && mouse.x < this.width && mouse.y > 0 && mouse.y < this.height)
            this.menu.tooltip = new Tooltip(this.pos.x + this.width / 2D,this.pos.y + this.height / 2D,this.getTooltip(),this.height * 0.75D);
    }

    public abstract void drawIcon(Graphics2D graphics);
    public abstract boolean canBeClicked();
    @Nullable
    public abstract String getTooltip();

    @Override
    public void onHoverEnter() {
        this.hover = true;
    }

    @Override
    public void onHoverExit() {
        this.hover = false;
    }
}
