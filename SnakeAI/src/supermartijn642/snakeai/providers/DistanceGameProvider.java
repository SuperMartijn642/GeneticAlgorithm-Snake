package supermartijn642.snakeai.providers;

import supermartijn642.nolearnneuralnetwork.NeuralNetwork;
import supermartijn642.snakeai.SnakeGame;

import java.awt.*;
import java.util.function.Function;

/**
 * Created 2/18/2019 by SuperMartijn642
 */
public class DistanceGameProvider extends NeuralNetworkGameProvider {

    public final Function<Double,Double> activation_function;

    public DistanceGameProvider(Function<Double,Double> function){
        super(35,8,1,300,100);
        this.activation_function = function;
    }

    public DistanceGameProvider(NeuralNetwork network){
        super(35,8,1,300,100);
        this.network = network;
        this.activation_function = null;
    }

    @Override
    public NeuralNetwork createNetwork(SnakeGame game) {
        NeuralNetwork network = new NeuralNetwork(20,4,18,18);
        network.setActivationFunction(this.activation_function);
        network.setWeights(1);
        network.setMaxWeight(1000);
        network.changeWeights(2,game.getRandom());
        return network;
    }

    @Override
    public double[] getNetworkInput(SnakeGame game) {
        double[] input = new double[8 * 2 + 4];
        // walls
        input[0] = 1 / (game.getHead().x + 1D);
        input[1] = 1 / (this.x_size - (double)game.getHead().x);
        input[2] = 1 / (game.getHead().y + 1D);
        input[3] = 1 / (this.y_size - (double)game.getHead().y);
        // tale
        for(int a = 0; a < 9; a++){
            if(a == 4)
                continue;
            Point point = new Point(game.getHead().x,game.getHead().y);
            point.translate(a % 3 - 1,a / 3 - 1);
            double distance = Math.sqrt(this.x_size * this.x_size + this.y_size * this.y_size);
            for(Point point1 : game.getTale()) {
                double distance1 = point.distance(point1);
                if(distance1 < distance)
                    distance = distance1;
            }
            input[4 + (a > 4 ? a - 1 : a)] = 1 / (distance + 1);
        }
        // food
        for(int a = 0; a < 9; a++){
            if(a == 4)
                continue;
            Point point = new Point(game.getHead().x,game.getHead().y);
            point.translate(a % 3 - 1,a / 3 - 1);
            double distance = Math.sqrt(this.x_size * this.x_size + this.y_size * this.y_size);
            for(Point point1 : game.getFood()) {
                double distance1 = point.distance(point1);
                if(distance1 < distance)
                    distance = distance1;
            }
            input[12 + (a > 4 ? a - 1 : a)] = 1 / (distance + 1);
        }
//        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=");
//        for(double d : input)
//            System.out.println("in: " + d);
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
}
