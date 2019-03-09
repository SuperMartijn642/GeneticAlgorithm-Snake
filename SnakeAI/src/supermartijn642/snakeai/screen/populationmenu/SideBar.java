package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.Screen;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class SideBar {

//    private static final double INFO_HEIGHT =
    private static final double TOP_MARGIN = 0.4D;
    private static final double LEFT_MARGIN = 0.05D;
    private static final double VERTICAL_SPACING = 0.05D;
    private static final double HORIZONTAL_SPACING = 0.02D;
    private static final double BUTTON_HEIGHT = 0.15D;

    public int width;
    public int x;

    public SideBar(PopulationMenu menu,double width,boolean left){
        this.width = (int)(Screen.width * width);
        this.x = left ? 0 : Screen.width - this.width;
        menu.buttons.add(new PlayButton(this.getButtonPos(0,0,0),BUTTON_HEIGHT * this.width,menu));
        menu.buttons.add(new IterationButton(this.getButtonPos(0,1,BUTTON_HEIGHT),BUTTON_HEIGHT * this.width,menu,1,(int)(this.width * 0.15D)));
        menu.buttons.add(new IterationButton(this.getButtonPos(0,2,BUTTON_HEIGHT * 2D), BUTTON_HEIGHT * 1.5D * this.width,BUTTON_HEIGHT * this.width,menu,10,(int)(this.width * 0.15D)));
        menu.buttons.add(new IterationButton(this.getButtonPos(0,3,BUTTON_HEIGHT * 3.5D),BUTTON_HEIGHT * 2D * this.width,BUTTON_HEIGHT * this.width,menu,100,(int)(this.width * 0.15D)));
        menu.buttons.add(new ShowAllButton(this.getButtonPos(1,0,0), BUTTON_HEIGHT * this.width,menu));
    }

    private Position getButtonPos(int row,int column,double prevButtonWidth){
        return new Position(this.x + (LEFT_MARGIN + column * HORIZONTAL_SPACING + prevButtonWidth) * this.width,(TOP_MARGIN + row * (VERTICAL_SPACING + BUTTON_HEIGHT)) * this.width);
    }

    public void draw(Graphics2D graphics){
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(this.x,0,this.width,Screen.height);

        graphics.setFont(new Font("",Font.PLAIN,(int)(this.width * 0.141D)));
        graphics.setColor(Color.WHITE);
        graphics.drawString("Controls",this.x + (int)((this.width - graphics.getFontMetrics().stringWidth("Controls")) / 2D),(int)(0.1 * Screen.height));
    }

}
