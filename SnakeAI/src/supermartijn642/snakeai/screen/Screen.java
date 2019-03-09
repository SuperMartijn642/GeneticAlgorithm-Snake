package supermartijn642.snakeai.screen;

import supermartijn642.snakeai.render.IButton;
import supermartijn642.snakeai.screen.mainmenu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created 2/23/2019 by SuperMartijn642
 */
public class Screen implements MouseListener, MouseMotionListener {

    private static final int IDEAL_WIDTH = 1440;
    private static final int IDEAL_HEIGHT = 810;
    public static final double HEIGHT_PER_WIDTH = (double)IDEAL_HEIGHT / IDEAL_WIDTH;
    public static final double WIDTH_PER_HEIGHT = (double)IDEAL_HEIGHT / IDEAL_WIDTH;
    private static final int TRANSITION_FRAMES = 30;

    private static JFrame frame;
    private static Canvas canvas;

    public static int width;
    public static int height;
    public static Point lastMousePos;

    private static ArrayList<IMenu> menus;
    private static int transitionFrame;

    private static boolean shouldClose = false;

    public static void init(){
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if((double)screen.height / screen.width < HEIGHT_PER_WIDTH){
            height = screen.height / 2;
            width = (int)(screen.height * WIDTH_PER_HEIGHT / 2);
        }
        else{
            width = screen.width / 2;
            height = (int)(screen.width * HEIGHT_PER_WIDTH / 2);
        }
        shouldClose = false;
        menus = new ArrayList<>();
        menus.add(new MainMenu(width,height));
        transitionFrame = 0;
        frame = new JFrame("Snake Neural Network Trainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(true);
        frame.getContentPane().setPreferredSize(new Dimension(width,height));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.addMouseListener(new Screen());
        frame.addMouseMotionListener(new Screen());
        canvas = new Canvas();
        canvas.setSize(width,height);
        canvas.setBackground(Color.BLACK);
        canvas.addMouseListener(new Screen());
        canvas.addMouseMotionListener(new Screen());
        frame.add(canvas);
        frame.setVisible(true);
        new Thread("Start Frame"){
            @Override
            public void run() {
                final double expected_delta = 1000D / 60D;
                double last, now = System.currentTimeMillis();
                double delta = 0;
                double deltaCounter = 0;
                int frames = 0;
                while(!shouldClose){
                    last = now;
                    now = System.currentTimeMillis();
                    delta += now - last;
                    if(delta >= expected_delta){
                        if(canvas.getBufferStrategy() == null)
                            canvas.createBufferStrategy(2);
                        BufferStrategy strategy = canvas.getBufferStrategy();
                        Graphics2D graphics = (Graphics2D)strategy.getDrawGraphics();
                        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        draw(graphics);
                        strategy.show();
                        graphics.dispose();
                        delta -= expected_delta;
                        frames++;
                    }
                    deltaCounter += now - last;
                    if(deltaCounter >= 1000){
                        System.out.println("fps: " + frames);
                        frames = 0;
                        deltaCounter -= 1000;
                    }
                    try {
                        Thread.sleep(1);
                    } catch(Exception e) {e.printStackTrace();}
                }
                frame.setVisible(false);
                frame.dispose();
            }
        }.start();
    }

    public static void draw(Graphics2D graphics){
        if(menus.size() > 1 && transitionFrame != 0){
            BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            Graphics2D imgGraphics = image.createGraphics();
            menus.get(0).draw(imgGraphics);
            graphics.drawImage(image,0,(int)(-Math.sin((1 - (double)transitionFrame / TRANSITION_FRAMES) * Math.PI / 2) * height),width,height,null);
            imgGraphics.setColor(Color.BLACK);
            imgGraphics.fillRect(0,0,image.getWidth(),image.getHeight());
            menus.get(1).draw(imgGraphics);
            graphics.drawImage(image,0,(int)((1 - Math.sin((1 - (double)transitionFrame / TRANSITION_FRAMES) * Math.PI / 2)) * height),width,height,null);
            imgGraphics.dispose();
            transitionFrame--;
        }
        else {
            if (menus.size() > 1)
                menus.remove(0);
            menus.get(0).draw(graphics);
        }
    }

    public static void dispose(){
        shouldClose = true;
    }

    public static void addMenu(IMenu menu){
        menus.add(menu);
        transitionFrame = TRANSITION_FRAMES;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if(transitionFrame == 0 && (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON2)){
            for(IButton button : menus.get(0).getButtons()){
                if(e.getPoint().x > button.getPosition().x && e.getPoint().x < button.getPosition().x + button.getClickWidth() &&
                    e.getPoint().y > button.getPosition().y && e.getPoint().y < button.getPosition().y + button.getClickHeight()) {
                    button.onClick();
                    break;
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
        lastMousePos = new Point(-1,-1);
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if(transitionFrame == 0 && lastMousePos != null){
            for(IButton button : menus.get(0).getButtons()){
                if(e.getPoint().x > button.getPosition().x && e.getPoint().x < button.getPosition().x + button.getClickWidth() &&
                    e.getPoint().y > button.getPosition().y && e.getPoint().y < button.getPosition().y + button.getClickHeight()) {
                    if(!(lastMousePos.x > button.getPosition().x && lastMousePos.x < button.getPosition().x + button.getClickWidth() &&
                        lastMousePos.y > button.getPosition().y && lastMousePos.y < button.getPosition().y + button.getClickHeight()))
                        button.onHoverEnter();
                }
                else if(lastMousePos.x > button.getPosition().x && lastMousePos.x < button.getPosition().x + button.getClickWidth() &&
                    lastMousePos.y > button.getPosition().y && lastMousePos.y < button.getPosition().y + button.getClickHeight()){
                    if(!(e.getPoint().x > button.getPosition().x && e.getPoint().x < button.getPosition().x + button.getClickWidth() &&
                        e.getPoint().y > button.getPosition().y && e.getPoint().y < button.getPosition().y + button.getClickHeight()))
                        button.onHoverExit();
                }
            }
        }
        lastMousePos = e.getPoint();
    }
}
