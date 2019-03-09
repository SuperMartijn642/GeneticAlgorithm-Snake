package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.screen.Screen;

import java.awt.*;

/**
 * Created 2/28/2019 by SuperMartijn642
 */
public class InfoBar {

    private static final double TOP_MARGIN = 0.2D;
    private static final double LEFT_MARGIN = 0.15D;
    private static final double RIGHT_MARGIN = 0.2D;
    private static final double VERTICAL_SPACING = 0.10D;

    public int width, height;
    public int x, y;

    public InfoBar(PopulationMenu menu,double width,double x,double y){
        this.width = (int)(Screen.width * width);
        this.height = (int)(Screen.width * y);
        this.x = (int)(Screen.width * x);
        this.y = (int)(Screen.height - Screen.width * y);
    }

    public void draw(Graphics2D graphics,PopulationMenu menu){
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(this.x,this.y,this.width,this.height);
        graphics.setFont(new Font("",Font.PLAIN,(int)(this.width * 0.07)));
        int x = (int)(this.x + LEFT_MARGIN * this.width);
        int x2 = (int)(this.x + (1 - RIGHT_MARGIN) * this.width);
        // generation
        int y = (int)(this.y + TOP_MARGIN * this.width);
        graphics.setColor(Color.WHITE);
        graphics.drawString("GENERATION",x,y);
        int num = menu.population.getGeneration();
        graphics.setColor(num == 0 ? Color.RED : Color.CYAN);
        graphics.drawString("" + num,(int)(x2 - graphics.getFontMetrics().stringWidth("" + num) / 2D),y);
        // best score
        y = (int)(this.y + (TOP_MARGIN + 1 * VERTICAL_SPACING) * this.width);
        graphics.setColor(Color.WHITE);
        graphics.drawString("BEST SCORE",x,y);
        if(menu.population.getGeneration() == 0) {
            graphics.setColor(Color.RED);
            graphics.drawString("-", (int) (x2 - graphics.getFontMetrics().stringWidth("-") / 2D), y);
        }
        else {
            num = menu.population.getCurrentBestGame().getScore();
            graphics.setColor(num == 0 ? Color.RED : Color.GREEN);
            graphics.drawString("" + num, (int) (x2 - graphics.getFontMetrics().stringWidth("" + num) / 2D), y);
        }
        // score
        y = (int)(this.y + (TOP_MARGIN + 2.5D * VERTICAL_SPACING) * this.width);
        graphics.setColor(Color.WHITE);
        graphics.drawString("SCORE",x,y);
        if(menu.game == null){
            graphics.setColor(Color.RED);
            graphics.drawString("-", (int) (x2 - graphics.getFontMetrics().stringWidth("-") / 2D), y);
        }
        else {
            num = menu.game.getScore();
            graphics.setColor(num == 0 ? Color.RED : Color.GREEN);
            graphics.drawString("" + num, (int) (x2 - graphics.getFontMetrics().stringWidth("" + num) / 2D), y);
        }
        // updates
        y = (int)(this.y + (TOP_MARGIN + 3.5D * VERTICAL_SPACING) * this.width);
        graphics.setColor(Color.WHITE);
        graphics.drawString("UPDATES",x,y);
        if(menu.game == null){
            graphics.setColor(Color.RED);
            graphics.drawString("-", (int) (x2 - graphics.getFontMetrics().stringWidth("-") / 2D), y);
        }
        else {
            num = menu.game.getUpdates();
            graphics.setColor(num == 0 ? Color.RED : Color.CYAN);
            graphics.drawString("" + num, (int) (x2 - graphics.getFontMetrics().stringWidth("" + num) / 2D), y);
        }
    }
}
