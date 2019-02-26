package supermartijn642.snakeai.providers;

import supermartijn642.nolearnneuralnetwork.NeuralNetwork;
import supermartijn642.snakeai.SnakeGame;

import java.awt.*;
import java.util.function.Function;

/**
 * Created 2/19/2019 by SuperMartijn642
 */
public class PositionGameProvider extends NeuralNetworkGameProvider {

    public final Function<Double,Double> activation_function;

    public PositionGameProvider(Function<Double,Double> function){
        super(35,8,1,300,100);
        this.activation_function = function;
    }

    public PositionGameProvider(NeuralNetwork network){
        super(35,8,1,300,100);
        this.network = network;
        this.activation_function = null;
    }

    @Override
    public NeuralNetwork createNetwork(SnakeGame game) {
        NeuralNetwork network = new NeuralNetwork(24,4,20,20);
        network.setActivationFunction(this.activation_function);
        network.setWeights(1);
        network.setMaxWeight(10000);
        network.changeWeights(2,game.getRandom());
        return network;
    }

    @Override
    public double[] getNetworkInput(SnakeGame game) {
        double[] input = new double[24];
        input[0] = game.getHead().x + 1;
        input[1] = game.getHead().y + 1;
        input[2] = this.x_size - game.getHead().x;
        input[3] = this.y_size - game.getHead().y;
        for(int a = 0; a < game.getTale().size(); a++) {
            input[a * 2 + 4] = game.getTale().get(a).x + 1;
            input[a * 2 + 5] = game.getTale().get(a).y + 1;
        }
        for(int a = 0; a < game.getFood().size(); a++) {
            input[a * 2 + 19] = game.getTale().get(a).x + 1;
            input[a * 2 + 20] = game.getTale().get(a).y + 1;
        }
        return input;
    }

    @Override
    public Point getHeading(SnakeGame game, double[] result) {
//        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=");
//        for(double d : result)
//            System.out.println("res: " + d);
        if(result[0] > result[1] && result[0] > result[2] && result[0] > result[3])
            return new Point(-1,0);
        if(result[1] > result[0] && result[1] > result[2] && result[1] > result[3])
            return new Point(1,0);
        if(result[2] > result[0] && result[2] > result[1] && result[2] > result[3])
            return new Point(0,-1);
        if(result[3] > result[0] && result[3] > result[1] && result[3] > result[2])
            return new Point(0,1);
        return new Point();
    }

    @Override
    public int getSnakeLength(SnakeGame game) {
        return 8;
    }
}
