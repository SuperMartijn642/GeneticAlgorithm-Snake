package supermartijn642.snakeai.screen.variablemenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.render.IButton;
import supermartijn642.snakeai.screen.IMenu;
import supermartijn642.snakeai.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

/**
 * Created 3/9/2019 by SuperMartijn642
 */
public class VariableMenu implements IMenu {

    private static final String TITLE = "NEW SNAKE POPULATION";
    private static final double TITLE_HEIGHT = 0.1D;
    private static final double FIRST_Y = 0.2D;
    private static final double VERTICAL_SPACING = 0.1D;

    private RandomField randomField;
    private Slider sizeSlider;

    public VariableMenu(){
        this.randomField = new RandomField(FIRST_Y);
        this.sizeSlider = new Slider(new Position(Screen.width * (0.5D - 0.01D),Screen.height * (FIRST_Y + VERTICAL_SPACING)),200,10000,2000);
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0,0, Screen.width,Screen.height);
        // title
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0,(int)(Screen.height * (TITLE_HEIGHT - 0.001D)),Screen.width,(int)(Screen.height * 2 * 0.001D));
        graphics.setColor(Color.GREEN);
        graphics.setFont(new Font("",Font.PLAIN,(int)(Screen.height * TITLE_HEIGHT * 0.8D)));
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString(TITLE,(int)((Screen.width - metrics.stringWidth(TITLE)) / 2D),(int)(Screen.height * TITLE_HEIGHT / 2D + metrics.getHeight() * 0.3D));
        // variables
        this.randomField.draw(graphics);
        this.sizeSlider.draw(graphics);
    }

    @Override
    public List<IButton> getButtons() {
        return Collections.emptyList();
    }

    @Override
    public void mouseDown(Point mouse){
        this.randomField.mouseDown(mouse);
    }

    @Override
    public void mouseUp(Point mouse){

    }

    @Override
    public void keyDown(KeyEvent event){
        this.randomField.keyDown(event);
    }
}
