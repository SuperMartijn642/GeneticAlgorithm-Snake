package supermartijn642.snakeai.providers;

import supermartijn642.nolearnneuralnetwork.NeuralNetwork;
import supermartijn642.snakeai.SnakeGame;

import java.awt.*;
import java.util.function.Function;

/**
 * Created 2/19/2019 by SuperMartijn642
 */
public class CrossDistanceGameProvider extends NeuralNetworkGameProvider {

    public final Function<Double,Double> activation_function;

    public CrossDistanceGameProvider(Function<Double,Double> function, int size, int food_count){
        super(size,8,food_count,300,100);
        this.activation_function = function;
    }

    public CrossDistanceGameProvider(NeuralNetwork network, int size, int food_count){
        super(size,8,food_count,300,100);
        this.network = network;
        this.activation_function = null;
    }

    @Override
    public NeuralNetwork createNetwork(SnakeGame game) {
        NeuralNetwork network = new NeuralNetwork(20,4,20,20);
        network.setActivationFunction(this.activation_function);
        network.setWeights(1);
        network.setMaxWeight(10000);
        network.changeWeights(2,game.getRandom());
        return network;
    }

    @Override
    public double[] getNetworkInput(SnakeGame game) {
        double[] input = new double[20];
        double game_distance = Math.sqrt(this.x_size * this.x_size + this.y_size * this.y_size);
        for(int a = 4; a < 20; a++)
            input[a] = game_distance;
        Point head = game.getHead();
        // walls
        input[0] = 1 / (game.getHead().x + 1D);
        input[1] = 1 / (this.x_size - (double)game.getHead().x);
        input[2] = 1 / (game.getHead().y + 1D);
        input[3] = 1 / (this.y_size - (double)game.getHead().y);
        // tale
        for(Point point : game.getTale()) {
            Point point1 = new Point(point.x - head.x,point.y - head.y);
            double distance = point1.distance(0,0);
            if (point1.x == point1.y){
                if(point1.x < 0){
                    if(distance < input[4])
                        input[4] = distance;
                }
                else{
                    if(distance < input[5])
                        input[5] = distance;
                }
            }
            else if(point1.x == - point1.y){
                if(point1.x < 0){
                    if(distance < input[6])
                        input[6] = distance;
                }
                else{
                    if(distance < input[7])
                        input[7] = distance;
                }
            }
            else if(point1.x == 0){
                if(point1.y < 0){
                    if(distance < input[8])
                        input[8] = distance;
                }
                else{
                    if(distance < input[9])
                        input[9] = distance;
                }
            }
            else if(point1.y == 0){
                if(point1.x < 0){
                    if(distance < input[10])
                        input[10] = distance;
                }
                else{
                    if(distance < input[11])
                        input[11] = distance;
                }
            }
        }
        // food
        for(Point point : game.getFood()) {
            Point point1 = new Point(point.x - head.x,point.y - head.y);
            double distance = point1.distance(0,0);
            if (point1.x == point1.y){
                if(point1.x < 0){
                    if(distance < input[12])
                        input[12] = distance;
                }
                else{
                    if(distance < input[13])
                        input[13] = distance;
                }
            }
            else if(point1.x == - point1.y){
                if(point1.x < 0){
                    if(distance < input[14])
                        input[14] = distance;
                }
                else{
                    if(distance < input[15])
                        input[15] = distance;
                }
            }
            else if(point1.x == 0){
                if(point1.y < 0){
                    if(distance < input[16])
                        input[16] = distance;
                }
                else{
                    if(distance < input[17])
                        input[17] = distance;
                }
            }
            else if(point1.y == 0){
                if(point1.x < 0){
                    if(distance < input[18])
                        input[18] = distance;
                }
                else{
                    if(distance < input[19])
                        input[19] = distance;
                }
            }
        }
        return input;
    }

    @Override
    public Point getHeading(SnakeGame game, double[] result) {
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
