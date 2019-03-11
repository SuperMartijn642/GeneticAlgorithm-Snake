package supermartijn642.snakeai.screen.variablemenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.Screen;

import java.awt.*;

/**
 * Created 3/9/2019 by SuperMartijn642
 */
public class Slider {

    public static final double WIDTH = 0.15D;
    public static final double HEIGHT = 0.03D;
    public static final double BORDER_WIDTH = 0.0025D;
    public static final double KNOB_MARGIN = BORDER_WIDTH * 0.25D;
    public static final double KNOB_WIDTH = WIDTH * 0.15D;
    public static final double VERTICAL_TEXT_DISTANCE = 0.005D;

    public Position pos;

    public int value;
    public int min;
    public int max;

    public Slider(Position pos,int min,int max,int startValue){
        this.pos = pos;
        this.value = startValue;
        this.min = min;
        this.max = max;
    }

    public void draw(Graphics2D graphics){
        double width = Screen.width * WIDTH;
        // bar
        graphics.setColor(Color.GRAY);
        graphics.fillRect((int)this.pos.x,(int)this.pos.y,(int)width,(int)(Screen.height * HEIGHT));
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect((int)(this.pos.x + Screen.width * BORDER_WIDTH),(int)(this.pos.y + Screen.width * BORDER_WIDTH),(int)(Screen.width * (WIDTH - 2 * BORDER_WIDTH)),(int)(Screen.height * HEIGHT - Screen.width * 2 * BORDER_WIDTH));
        // knob
        graphics.setColor(Color.LIGHT_GRAY);
        double x = this.pos.x + Screen.width * (BORDER_WIDTH + KNOB_MARGIN) + (this.value - this.min) / (double)this.max * (width - Screen.width * 2 * (BORDER_WIDTH + KNOB_MARGIN));
        graphics.fillRect((int)x,(int)(this.pos.y + Screen.width * (BORDER_WIDTH + KNOB_MARGIN)),(int)(Screen.width * KNOB_WIDTH),(int)(Screen.height * HEIGHT - Screen.width * 2 * (BORDER_WIDTH + KNOB_MARGIN)));
        // text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("",Font.PLAIN,(int)(Screen.width * 0.01D)));
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString("" + this.min,(int)(this.pos.x - metrics.stringWidth("" + this.min) / 2D),(int)(this.pos.y + Screen.height * (HEIGHT + VERTICAL_TEXT_DISTANCE)));
        graphics.drawString("" + this.max,(int)(this.pos.x + width - metrics.stringWidth("" + this.max) / 2D),(int)(this.pos.y + Screen.height * (HEIGHT + VERTICAL_TEXT_DISTANCE)));
        graphics.drawString("" + this.value,(int)(x + Screen.width * KNOB_WIDTH / 2 - metrics.stringWidth("" + this.value) / 2D),(int)(this.pos.y + Screen.height * (HEIGHT + VERTICAL_TEXT_DISTANCE)));
    }

}
