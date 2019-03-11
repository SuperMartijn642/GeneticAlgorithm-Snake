package supermartijn642.snakeai.render;

import supermartijn642.snakeai.screen.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created 3/10/2019 by SuperMartijn642
 */
public class Tooltip {

    private static final double MAX_WIDTH = 0.1D;
    private static final double LINE_HEIGHT = 0.02D;
    private static final double VERTICAL_SPACING = LINE_HEIGHT * 0.1D;
    private static final double MARGIN = VERTICAL_SPACING * 2D;
    private static final double ARC_WIDTH = LINE_HEIGHT * 0.5D;
    private static final double BORDER_WIDTH = MARGIN * 0.2D;

    /**
     * @param y_offset must be scaled to the screen size before given
     */
    public static void drawTooltip(Graphics2D graphics,double x,double y,String text,double y_offset){
        // separating lines
        graphics.setFont(new Font("",Font.PLAIN,(int)(Screen.width * LINE_HEIGHT)));
        FontMetrics metrics = graphics.getFontMetrics();
        StringTokenizer tokenizer = new StringTokenizer(text," ");
        ArrayList<String> lines = new ArrayList<>();
        String token1 = tokenizer.nextToken();
        while(tokenizer.hasMoreTokens()){
            String token2 = tokenizer.nextToken();
            if(metrics.stringWidth(token1 + ' ' + token2) < Screen.width * MAX_WIDTH)
                token1 += ' ' + token2;
            else{
                lines.add(token1);
                token1 = token2;
            }
        }
        lines.add(token1);
        // getting width and height
        double width = 0;
        for(String s : lines)
            if(metrics.stringWidth(s) > width)
                width = metrics.stringWidth(s);
        width += Screen.width * 2 * (MARGIN + BORDER_WIDTH);
        double height = Screen.width * (2 * (MARGIN + BORDER_WIDTH) + (lines.size() - 1) * VERTICAL_SPACING + lines.size() * LINE_HEIGHT);
        double arc_width = Screen.width * ARC_WIDTH;
        double border = Math.max(1,Screen.width * BORDER_WIDTH);
        double margin = Screen.width * MARGIN;
        double spacing = Screen.width * VERTICAL_SPACING;
        int xx = (int)Math.max(margin,Math.min(Screen.width - margin - width,x - width / 2D));
        int yy = (int)(y + y_offset);
        if(yy > Screen.height - margin)
            yy = (int)(y - y_offset - height);
        // background
        graphics.setColor(Color.GRAY);
        graphics.fillRoundRect(xx,yy,(int)width,(int)height,(int)arc_width,(int)arc_width);
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRoundRect((int)(xx + border),(int)(yy + border),(int)((int)width - 2 * border),(int)((int)height - 2 * border),(int)arc_width,(int)arc_width);
        // text
        graphics.setColor(Color.WHITE);
        for(int a = 0; a < lines.size(); a++)
            graphics.drawString(lines.get(a),(int)(xx + border + margin),(int)(yy + border + margin + metrics.getHeight() * 0.3D + (a + 0.5D) * metrics.getFont().getSize() + a * spacing));
    }

    private final double x, y;
    private final String text;
    private final double y_offset;

    public Tooltip(double x,double y,String text,double y_offset){
        this.x = x;
        this.y = y;
        this.text = text;
        this.y_offset = y_offset;
    }

    public void draw(Graphics2D graphics){
        drawTooltip(graphics,this.x,this.y,this.text,this.y_offset);
    }

}
