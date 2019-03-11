package supermartijn642.snakeai;

import supermartijn642.snakeai.screen.Screen;

/**
 * Created 2/23/2019 by SuperMartijn642
 */
public class Main {

    public static void main(String[] args){
        Screen.init();
//        SnakeGame game = new SnakeGame(new CrossDistanceGameProvider((Function<Double,Double>) null,35,1),new Random().nextLong());
//        CrossDistanceGameProvider provider = new CrossDistanceGameProvider((Function<Double,Double>) null,35,1);
//        provider.network = new NeuralNetwork(20,4,18,18);
//        provider.network.initializeWeights(2,new Random());
//        byte[] bytes = provider.toBytes();
//        System.out.println("size: " + bytes.length);
//        try {
//            Thread.sleep(100);
//        }catch (Exception e){e.printStackTrace();}
//        new CrossDistanceGameProvider().fromBytes(bytes);
    }

}
