package supermartijn642.snakeai;

import supermartijn642.snakeai.screen.Screen;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

/**
 * Created 3/10/2019 by SuperMartijn642
 */
public class PopulationLoader implements ActionListener {

    private static final String POPULATION_EXTENSION = ".snakepop";
    private static final String DEFAULT_NAME = "Snake Population";

    private static JFrame frame;
    private static JFileChooser chooser;

    public static boolean handleSave(Population population){
        if(frame != null)
            return false;
        // hide screen
        Screen.getFrame().setVisible(false);
        // new frame
        frame = new JFrame("Choose a File");
        frame.setSize(Screen.getFrame().getSize());
        frame.getContentPane().setPreferredSize(new Dimension(Screen.width, Screen.height));
        frame.pack();
        frame.setLocation(Screen.getFrame().getLocation());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(POPULATION_EXTENSION);
            }
            @Override
            public String getDescription() {
                return "Snake Population (*" + POPULATION_EXTENSION + ")";
            }
        });
        chooser.setApproveButtonText("Save");
        chooser.setMultiSelectionEnabled(false);
        chooser.setSize(Screen.width, Screen.height);
        chooser.addActionListener(new PopulationLoader());
        frame.add(chooser);
        frame.setVisible(true);
        while(frame.isVisible()){
            try {
                Thread.sleep(1);
            }catch(Exception e){e.printStackTrace();}
        }
        if(chooser == null) {
            frame.dispose();
            frame = null;
            Screen.getFrame().setVisible(true);
            return false;
        }
        File file = chooser.getSelectedFile();
        if(file == null || (!file.getName().endsWith(POPULATION_EXTENSION) && file.getName().contains(".") && !file.isDirectory())) {
            frame.dispose();
            frame = null;
            Screen.getFrame().setVisible(true);
            return false;
        }
        if(!file.getName().endsWith(POPULATION_EXTENSION) && !file.getName().contains("."))
            file = new File(file.getParentFile().toURI().getPath() + File.separator + file.getName() + POPULATION_EXTENSION);
        try {
            if(file.isDirectory()) {
                file.mkdirs();
                file = new File(file.toURI().getPath() + File.separator + DEFAULT_NAME + POPULATION_EXTENSION);
            }
            else
                file.getParentFile().mkdirs();
            byte[] bytes = population.toBytes();
            if(!file.exists())
                file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);
            output.write(bytes);
        }catch(Exception e){e.printStackTrace();return false;}
        finally {
            frame.dispose();
            frame = null;
            Screen.getFrame().setVisible(true);
        }
        return true;
    }

    public static Population handleLoad(){
        if(frame != null)
            return null;
        // hide screen
        Screen.getFrame().setVisible(false);
        // new frame
        frame = new JFrame("Choose a Population");
        frame.setSize(Screen.getFrame().getSize());
        frame.getContentPane().setPreferredSize(new Dimension(Screen.width, Screen.height));
        frame.pack();
        frame.setLocation(Screen.getFrame().getLocation());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(POPULATION_EXTENSION);
            }
            @Override
            public String getDescription() {
                return "Snake Population (*" + POPULATION_EXTENSION + ")";
            }
        });
        chooser.setApproveButtonText("Load");
        chooser.setMultiSelectionEnabled(false);
        chooser.setSize(Screen.width, Screen.height);
        chooser.addActionListener(new PopulationLoader());
        frame.add(chooser);
        frame.setVisible(true);
        while(frame.isVisible()){
            try {
                Thread.sleep(1);
            }catch(Exception e){e.printStackTrace();}
        }
        if(chooser == null) {
            frame.dispose();
            frame = null;
            Screen.getFrame().setVisible(true);
            return null;
        }
        File file = chooser.getSelectedFile();
        if(file == null || !file.getName().endsWith(POPULATION_EXTENSION) || file.isDirectory()) {
            frame.dispose();
            frame = null;
            Screen.getFrame().setVisible(true);
            return null;
        }
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Population.fromBytes(bytes);
        }catch(Exception e){e.printStackTrace();return null;}
        finally {
            frame.dispose();
            frame = null;
            Screen.getFrame().setVisible(true);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(frame != null)
            frame.setVisible(false);
    }
}
