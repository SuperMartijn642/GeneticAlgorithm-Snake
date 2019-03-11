package supermartijn642.snakeai.render;

import supermartijn642.snakeai.Position;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public abstract class AnimatedButton implements IButton {

    private static final int HOVER_FRAMES = 30;
    private static final double FONT_SIZE = 0.105D;

    private Position pos;
    private final double width, height;
    private final Color background, hover;
    private final String text;
    private Color lastColor;
    private ArrayList<Position> hoverFrames = new ArrayList<>();

    public AnimatedButton(double x,double y,double width,double height,Color background,Color hover,String text){
        this.pos = new Position(x,y);
        this.width = width;
        this.height = height;
        this.background = background;
        this.hover = hover;
        this.text = text;
        this.lastColor = background;
    }

    public AnimatedButton(Position pos,double width,double height,Color background,Color hover,String text){
        this(pos.x,pos.y,width,height,background,hover,text);
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
        graphics.setColor(lastColor);
        graphics.fillRect((int)this.pos.x,(int)this.pos.y,(int)this.width,(int)this.height);
        synchronized (hoverFrames) {
            for (Position pos : hoverFrames) {
                graphics.setColor(pos.y == 1 ? hover : background);
                double delta = Math.sin((1 - pos.x / HOVER_FRAMES) * Math.PI / 2);
                int[] xpoints = new int[]{(int) this.pos.x, (int) (this.pos.x + (1 - delta) * this.width / 2), (int) (this.pos.x + (1 + delta) * this.width / 2), (int) (this.pos.x + this.width)};
                int[] ypoints = new int[]{(int) (this.pos.y + this.height), (int) (this.pos.y + (1 - delta) * this.height), (int) (this.pos.y + (1 - delta) * this.height), (int) (this.pos.y + this.height)};
                graphics.fillPolygon(xpoints, ypoints, 4);
                pos.x--;
            }
            for (int a = 0; a < hoverFrames.size(); a++)
                if (hoverFrames.get(a).x == 0) {
                    lastColor = hoverFrames.get(a).y == 1 ? hover : background;
                    hoverFrames.remove(a);
                    a--;
                }
        }
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("",Font.PLAIN,(int)(this.width * FONT_SIZE)));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        graphics.drawString(this.text,(int)(this.pos.x + (this.width - fontMetrics.stringWidth(this.text)) / 2D),(int)(this.pos.y + fontMetrics.getAscent() + (this.height - fontMetrics.getHeight()) / 2D));
    }

    @Override
    public void onHoverEnter() {
        synchronized (hoverFrames) {
            this.hoverFrames.add(new Position(HOVER_FRAMES, 1));
        }
    }

    @Override
    public void onHoverExit() {
        synchronized (hoverFrames) {
            this.hoverFrames.add(new Position(HOVER_FRAMES, 0));
        }
    }
}
