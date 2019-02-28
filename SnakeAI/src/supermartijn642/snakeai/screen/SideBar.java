package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.Position;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class SideBar {

    private static final double TOP_MARGIN = 0.4D;
    private static final double LEFT_MARGIN = 0.1D;
    private static final double VERTICAL_SPACING = 0.15D;
    private static final double HORIZONTAL_SPACING = 0.2D;
    private static final double SQUARE_SIZE = 0.15D;

    public int width;
    public int x;

    public SideBar(PopulationMenu menu,double width,boolean left){
        this.width = (int)(Screen.width * width);
        x = left ? 0 : Screen.width - this.width;
        int row = 0, column = 0;
        Position pos = new Position(this.x + (LEFT_MARGIN + column * HORIZONTAL_SPACING) * this.width,(TOP_MARGIN + row * VERTICAL_SPACING) * this.width);
        menu.buttons.add(new PlayButton(pos,SQUARE_SIZE * this.width,menu));
        column++;
        pos = new Position(this.x + (LEFT_MARGIN + column * HORIZONTAL_SPACING) * this.width,(TOP_MARGIN + row * VERTICAL_SPACING) * this.width);
        menu.buttons.add(new IterationButton(pos,SQUARE_SIZE * this.width,menu,1,50));
        column++;
        pos = new Position(this.x + (LEFT_MARGIN + column * HORIZONTAL_SPACING) * this.width,(TOP_MARGIN + row * VERTICAL_SPACING) * this.width);
        menu.buttons.add(new IterationButton(pos,SQUARE_SIZE * this.width,menu,10,40));
        column++;
        pos = new Position(this.x + (LEFT_MARGIN + column * HORIZONTAL_SPACING) * this.width,(TOP_MARGIN + row * VERTICAL_SPACING) * this.width);
        menu.buttons.add(new IterationButton(pos,SQUARE_SIZE * this.width,menu,100,30));
        column = 0;
        row++;
        pos = new Position(this.x + (LEFT_MARGIN + column * HORIZONTAL_SPACING) * this.width,(TOP_MARGIN + row * VERTICAL_SPACING) * this.width);
        menu.buttons.add(new ShowAllButton(pos,SQUARE_SIZE * this.width,menu));
    }

    public void draw(Graphics2D graphics){
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(this.x,0,this.width,Screen.height);
        graphics.setFont(new Font("",Font.PLAIN,40));
        graphics.setColor(Color.WHITE);
        graphics.drawString("Controls",this.x + (int)((this.width - graphics.getFontMetrics().stringWidth("Controls")) / 2D),(int)(0.1 * Screen.height));
    }

}
