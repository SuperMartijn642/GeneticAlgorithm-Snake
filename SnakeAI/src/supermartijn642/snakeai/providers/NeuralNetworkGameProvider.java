package supermartijn642.snakeai.providers;

import supermartijn642.nolearnneuralnetwork.NeuralNetwork;
import supermartijn642.snakeai.BasicGameProvider;
import supermartijn642.snakeai.SnakeGame;

import java.awt.*;

/**
 * Created 2/18/2019 by SuperMartijn642
 */
public abstract class NeuralNetworkGameProvider extends BasicGameProvider {

    public NeuralNetwork network;

    public NeuralNetworkGameProvider(int size, int snake_length, int food_count, int timeout, int timeout_bonus){
        super(size,snake_length,food_count,timeout,timeout_bonus);
    }

    @Override
    public void init(SnakeGame game) {
        if(this.network == null)
            this.network = this.createNetwork(game);
    }

    @Override
    public Point getHeading(SnakeGame game) {
        double[] input = this.getNetworkInput(game);
        double[] result = this.network.calculate(input);
        return this.getHeading(game,result);
    }

    public abstract NeuralNetwork createNetwork(SnakeGame game);
    public abstract double[] getNetworkInput(SnakeGame game);
    public abstract Point getHeading(SnakeGame game, double[] result);
}
