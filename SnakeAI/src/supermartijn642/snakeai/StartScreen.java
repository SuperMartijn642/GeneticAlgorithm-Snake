package supermartijn642.snakeai;

import javax.swing.*;
import java.awt.*;

/**
 * Created 2/23/2019 by SuperMartijn642
 */
public class StartScreen {

    private static final int IDEAL_WIDTH = 1440;
    private static final int IDEAL_HEIGHT = 810;
    private static final double HEIGHT_PER_WIDTH = (double)IDEAL_HEIGHT / IDEAL_WIDTH;
    private static final double WIDTH_PER_HEIGHT = (double)IDEAL_HEIGHT / IDEAL_WIDTH;

    public static JFrame frame;
    public static Canvas canvas;

    public static int width;
    public static int height;

    public static void init(){
        frame = new JFrame("Snake Neural Network Trainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if((double)screen.height / screen.width < HEIGHT_PER_WIDTH){
            height = screen.height;
            width = (int)(screen.height * WIDTH_PER_HEIGHT);
        }
        else{
            width = screen.width;
            height = (int)(screen.width * HEIGHT_PER_WIDTH);
        }
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        canvas = new Canvas();
        canvas.setSize(frame.getSize());
        canvas.setBackground(Color.BLACK);
        frame.add(canvas);
        frame.setVisible(true);
    }

}
