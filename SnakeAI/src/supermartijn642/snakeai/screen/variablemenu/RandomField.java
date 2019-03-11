package supermartijn642.snakeai.screen.variablemenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created 3/10/2019 by SuperMartijn642
 */
public class RandomField {

    public static final double WIDTH = 0.15D;
    public static final double HEIGHT = 0.025D;
    public static final double BORDER_WIDTH = 0.0025D;

    public static final int ANIM_TICKS = 30;

    public Position pos;

    public String text;

    private int ticks = 0;
    private boolean selected = false;

    public RandomField(double y){
        this.pos = new Position(Screen.width * ((1 - WIDTH) / 2),Screen.height * y);
        this.text = "" + Math.round(new Random().nextDouble() * Math.pow(10,12+1));
    }

    public void draw(Graphics2D graphics){
        graphics.setColor(Color.GRAY);
        graphics.fillRect((int)this.pos.x,(int)this.pos.y,(int)(Screen.width * WIDTH),(int)(Screen.width * HEIGHT));
        graphics.setColor(Color.DARK_GRAY);
        int border = (int)(Screen.width * BORDER_WIDTH);
        graphics.fillRect((int)(this.pos.x + border),(int)(this.pos.y + border),(int)(Screen.width * WIDTH - 2 * border),(int)(Screen.width * HEIGHT - 2 * border));
        graphics.setColor(Color.CYAN);
        graphics.setFont(new Font("",Font.PLAIN,(int)(Screen.width * HEIGHT * 0.8D)));
        graphics.drawString(this.text,(int)(this.pos.x + Screen.width * 1.5D * BORDER_WIDTH),(int)(this.pos.y + Screen.width * HEIGHT / 2D + graphics.getFontMetrics().getHeight() / 3D));
        // anim
        if(this.selected) {
//            System.out.println("ticks: " + this.ticks);
            if(this.ticks < ANIM_TICKS){
                int x = (int)(this.pos.x + Screen.width * 1.5D * BORDER_WIDTH + graphics.getFontMetrics().stringWidth(this.text));
                graphics.setColor(Color.LIGHT_GRAY);
                border = (int)(Screen.width * BORDER_WIDTH * 1.6D);
                graphics.fillRect(x,(int)(this.pos.y + border),(int)(Screen.width * 0.004D),(int)(Screen.width * HEIGHT - 2 * border));
            }
            if(this.ticks >= ANIM_TICKS * 2)
                this.ticks = 0;
            this.ticks++;
        }
    }

    public void mouseDown(Point mouse){
        if(mouse.x > this.pos.x && mouse.x < this.pos.x + Screen.width * WIDTH && mouse.y > this.pos.y && mouse.y < this.pos.y + Screen.width * HEIGHT)
            this.selected = true;
        else
            this.selected = false;
    }

    public void keyDown(KeyEvent event){
        if(!this.selected)
            return;
        if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if(this.text.length() > 0)
                this.text = this.text.substring(0, this.text.length() - 1);
        }
        else if(this.text.length() < 12){
            char c = event.getKeyChar();
            if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
                this.text += c;
        }
    }

}
