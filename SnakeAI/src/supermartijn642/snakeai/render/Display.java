package supermartijn642.snakeai.render;

import supermartijn642.snakeai.Main;
import supermartijn642.snakeai.SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

/**
 * Created 2/19/2019 by SuperMartijn642
 */
public class Display implements KeyListener {

    public static JFrame frame;
    public static JPanel panel;
    public static Canvas canvas;

    public static void createFrame(){
        frame = new JFrame("Snake Game Neural Network Trainer");
        frame.setSize(370,370);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.addKeyListener(new Display());
        panel = new JPanel(null);
        panel.setSize(frame.getSize());
        panel.addKeyListener(new Display());
        frame.add(panel);
        canvas = new Canvas();
        canvas.setBackground(Color.BLACK);
        canvas.setSize(panel.getSize());
        canvas.addKeyListener(new Display());
        panel.add(canvas);
        frame.setVisible(true);
    }

    public static void drawGame(SnakeGame game){
        if(canvas.getBufferStrategy() == null)
            canvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics2D graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
        graphics.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        // walls
        graphics.setColor(Color.GRAY);
        for(int x = 0; x < 2; x++){
            for(int y = 0; y < 37; y++){
                int xx = x * 360 + 1;
                int yy = y * 10 + 1;
                graphics.fillRect(xx,yy,8,8);
            }
        }
        for(int y = 0; y < 2; y++){
            for(int x = 0; x < 37; x++){
                int yy = y * 360 + 1;
                int xx = x * 10 + 1;
                graphics.fillRect(xx,yy,8,8);
            }
        }
        // snake
        graphics.setColor(Color.GREEN);
        graphics.fillRect((game.getHead().x + 1) * 10 + 1,(game.getHead().y + 1) * 10 + 1,8,8);
        for(Point point : game.getTale())
            graphics.drawRect((point.x + 1) * 10 + 1,(point.y + 1) * 10 + 1,7,7);
        // food
        graphics.setColor(Color.RED);
        for(Point point : game.getFood())
            graphics.drawRect((point.x + 1) * 10 + 1,(point.y + 1) * 10 + 1,7,7);

        bufferStrategy.show();
        graphics.dispose();
    }

    public static void playGame(SnakeGame game,int ups,int delay){
        drawGame(game);
        final double delta = 1000D / ups;
        double past = 0;
        double last, now = System.currentTimeMillis();
        while(!game.isFinished()){
            last = now;
            now = System.currentTimeMillis();
            past += now - last;
            if(past >= delta){
                game.update();
                drawGame(game);
                past -= delta;
            }
            if(delta - past > 0)
            try {
                Thread.sleep((long)((delta - past) * 0.8));
            } catch(Exception e) {e.printStackTrace();}
        }
        try {
            Thread.sleep(delay);
        } catch(Exception e) {e.printStackTrace();}
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
//        if(e.getKeyCode() == KeyEvent.VK_SPACE)
//            Main.DISPLAY_BEST = !Main.DISPLAY_BEST;
    }
}
