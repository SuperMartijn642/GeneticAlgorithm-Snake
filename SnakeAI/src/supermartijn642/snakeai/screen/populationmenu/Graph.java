package supermartijn642.snakeai.screen.populationmenu;

import supermartijn642.snakeai.Position;
import supermartijn642.snakeai.screen.Screen;

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
        // line
        double deltaX = points.size() == 1 ? 1 : (double)this.width / (this.points.size() - 1);
        for (int a = 1; a < points.size(); a++) {
            graphics.drawLine((int)(this.x + (a - 1) * deltaX),(int)((1 - (double)points.get(a - 1) / highest) * this.height),
                (int)(this.x + a * deltaX),(int)((1 - (double)points.get(a) / highest) * this.height));
        }
        // scale
        graphics.setFont(new Font("",Font.PLAIN,(int)(this.width * 0.015D)));
        graphics.setColor(Color.WHITE);
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString("0",(int)(this.x + (this.width * 0.01D)),(int)(this.height - (this.width * 0.01D) - metrics.getHeight() / 2D + 0.8D * metrics.getAscent()));
        graphics.drawString("" + highest,(int)(this.x + (this.width * 0.01D)),(int)(this.width * 0.01D + metrics.getHeight() / 2D));
        // mouse highlight
        Position mouse = new Position((Screen.lastMousePos.x - this.x) / (double)this.width,Screen.lastMousePos.y);
        if(points.size() > 1 && mouse.x > 0 && mouse.x < 1 && mouse.y > 0 && mouse.y < this.height){
            graphics.setFont(new Font("",Font.PLAIN,(int)(this.width * 0.023D)));
            metrics = graphics.getFontMetrics();
            int gen = (int)(mouse.x * points.size());
            String s = "Generation: " + gen + " Score: " + points.get(gen);
            graphics.drawString(s,(int)(this.x + this.width - metrics.stringWidth(s) - this.width * 0.01D),(int)(this.height - (this.width * 0.01D) - metrics.getHeight() / 2D + 0.8D * metrics.getAscent()));
            double size = Screen.width * 0.01D;
            graphics.setColor(Color.GREEN);
            graphics.fillOval((int)(this.x + gen * deltaX - size / 2D),(int)((1 - (double)points.get(gen) / highest) * this.height - size / 2D),(int)size,(int)size);
        }
    }
}
