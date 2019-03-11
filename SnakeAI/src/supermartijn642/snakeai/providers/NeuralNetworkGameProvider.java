package supermartijn642.snakeai.providers;

import supermartijn642.nolearnneuralnetwork.NeuralNetwork;
import supermartijn642.snakeai.BasicGameProvider;
import supermartijn642.snakeai.SnakeGame;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * Created 2/18/2019 by SuperMartijn642
 */
public abstract class NeuralNetworkGameProvider extends BasicGameProvider {

    public NeuralNetwork network;

    public NeuralNetworkGameProvider(int size, int snake_length, int food_count, int timeout, int timeout_bonus){
        super(size,snake_length,food_count,timeout,timeout_bonus);
    }

    public NeuralNetworkGameProvider(){
        super();
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

    @Override
    public byte[] toBytes() {
        byte[] prev = super.toBytes();
        byte[] network = this.network.toBytes();
        ByteBuffer buffer = ByteBuffer.allocate(prev.length + 4 * 2 + network.length);
        buffer.putInt(prev.length);
        buffer.put(prev);
        buffer.putInt(network.length);
        buffer.put(network);
        return buffer.array();
    }

    @Override
    public void fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        byte[] prev = new byte[length];
        buffer.get(prev);
        super.fromBytes(prev);
        length = buffer.getInt();
        byte[] network = new byte[length];
        buffer.get(network);
        this.network = NeuralNetwork.fromBytes(network);
    }
}
