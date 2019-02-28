package supermartijn642.snakeai.screen;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class Graph {

    public ArrayList<Integer> points = new ArrayList<>();
    private int width, height;
    private int x;

    public Graph(PopulationMenu menu,double height,double x,double width){
        this.width = (int)(width * Screen.width);
        this.height = (int)(Screen.height - height * Screen.width);
        this.x = (int)(Screen.width * x);
        this.points.add(0);
    }

    public void draw(Graphics2D graphics){
        graphics.setColor(Color.BLACK);
        graphics.fillRect(this.x,0,this.width,this.height);
        graphics.setColor(Color.GREEN);
        int highest = 1;
        for(int score : points)
            if(score > highest)
                highest = score;
        double deltaX = points.size() == 1 ? 1 : (double)this.width / (this.points.size() - 1);
        for (int a = 1; a < points.size(); a++) {
            graphics.drawLine((int)(this.x + (a - 1) * deltaX),(int)((1 - (double)points.get(a - 1) / highest) * this.height),
                (int)(this.x + a * deltaX),(int)((1 - (double)points.get(a) / highest) * this.height));
        }
    }
}
