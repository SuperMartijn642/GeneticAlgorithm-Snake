package supermartijn642.nolearnneuralnetwork;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.function.Function;

/**
 * Created 2/1/2019 by SuperMartijn642
 */
public class NeuralNetwork {

    private static int ID = 1;

    /**
     * format: double[toLayer][toLayerNode][fromLayerNode]
     */
    private final double[][][] weights;
    private final double[] biasWeights;
    private Function<Double,Double> activationFunction;
    private double maxWeight = 1;
    public final int id;

    public NeuralNetwork(int inputs, int outputs, int... layers) throws IllegalArgumentException {
        if(inputs <= 0)
            throw new IllegalArgumentException("The number of inputs must be bigger than 0!");
        if(outputs <= 0)
            throw new IllegalArgumentException("The number of outputs must be bigger than 0!");
        if(layers == null || layers.length <= 0)
            throw new IllegalArgumentException("The network should have at least one layer!");
        for(int a : layers)
            if(a <= 0)
                throw new IllegalArgumentException("The number of nodes in a layer must be bigger than 0!");
        this.id = ID++;
        this.weights = new double[layers.length + 1][][];
        this.weights[0] = new double[layers[0]][];
        for(int a = 0; a < this.weights[0].length; a++)
            this.weights[0][a] = new double[inputs];
        for(int a = 1; a < layers.length; a++){
            this.weights[a] = new double[layers[a]][];
            for(int b = 0; b < this.weights[a].length; b++)
                this.weights[a][b] = new double[layers[a - 1]];
        }
        this.weights[this.weights.length - 1] = new double[outputs][];
        for(int a = 0; a < this.weights[this.weights.length - 1].length; a++)
            this.weights[this.weights.length - 1][a] = new double[layers[layers.length - 1]];
        this.biasWeights = new double[layers.length + 1];
    }
    /**
     * @param weights format: double[toLayer][toLayerNode][fromLayerNode]
     */
    public NeuralNetwork(double[][][] weights, double[] biasWeights, Function<Double,Double> activationFunction, double maxWeight){
        this.id = ID++;
        this.weights = new double[weights.length][][];
        for(int a = 0; a < weights.length; a++){
            this.weights[a] = new double[weights[a].length][];
            for(int b = 0; b < weights[a].length; b++){
                this.weights[a][b] = new double[weights[a][b].length];
                for(int c = 0; c < weights[a][b].length; c++)
                    this.weights[a][b][c] = weights[a][b][c];
            }
        }
        this.biasWeights = new double[biasWeights.length];
        for(int a = 0; a < biasWeights.length; a++)
            this.biasWeights[a] = biasWeights[a];
        this.activationFunction = activationFunction;
        this.maxWeight = maxWeight;
    }

    public void initializeWeights(double value, @NotNull Random random){
        for(double[][] arr1 : this.weights){
            for(double[] arr2 : arr1){
                for(int a = 0; a < arr2.length; a++){
                    arr2[a] = Math.max(-this.maxWeight,Math.min(this.maxWeight,(random.nextDouble() * 2 - 1) * value));
                }
            }
        }
        for(int a = 0; a < this.biasWeights.length; a++)
            this.biasWeights[a] = 1;
    }

    public void setWeights(double value){
        for(double[][] arr1 : this.weights){
            for(double[] arr2 : arr1){
                for(int a = 0; a < arr2.length; a++){
                    arr2[a] = value;
                }
            }
        }
        for(int a = 0; a < this.biasWeights.length; a++)
            this.biasWeights[a] = 1;
    }

    /**
     * Adds or subtracts an amount <= value from each weight.
     * @param value maximum value a weight can be changed by
     */
    public void changeWeights(double value, @NotNull Random random){
        for(double[][] arr1 : this.weights){
            for(double[] arr2 : arr1){
                for(int a = 0; a < arr2.length; a++){
                    arr2[a] += (random.nextDouble() * 2 - 1) * value;
                    arr2[a] = Math.max(-this.maxWeight,Math.min(this.maxWeight,arr2[a]));
                }
            }
        }
        for(int a = 0; a < this.biasWeights.length; a++) {
            this.biasWeights[a] += (random.nextDouble() * 2 - 1) * value;
            this.biasWeights[a] = Math.max(-this.maxWeight,Math.min(this.maxWeight,this.biasWeights[a]));
        }
    }

    /**
     * Adds or subtracts an amount from each weight based on random Gaussian.
     * @param mutationRate the likeliness of a weight being changed. [0,1]
     */
    public void mutateWeights(double mutationRate, @NotNull Random random){
        for(double[][] arr1 : this.weights){
            for(double[] arr2 : arr1){
                for(int a = 0; a < arr2.length; a++){
                    double rand = random.nextDouble();
                    if(rand < mutationRate) {
                        arr2[a] += random.nextGaussian() / 2;
                        arr2[a] = Math.max(-this.maxWeight, Math.min(this.maxWeight, arr2[a]));
                    }
                }
            }
        }
        for(int a = 0; a < this.biasWeights.length; a++) {
            double rand = random.nextDouble();
            if(rand < mutationRate) {
                this.biasWeights[a] += random.nextGaussian() / 5;
                this.biasWeights[a] = Math.max(-this.maxWeight, Math.min(this.maxWeight, this.biasWeights[a]));
            }
        }
    }

    public double[][][] getWeights() {
        return this.weights;
    }

    public void setActivationFunction(@Nullable Function<Double,Double> function){
        this.activationFunction = function;
    }

    public double[] calculate(double[] input) throws IllegalArgumentException {
        if(this.weights[0][0].length != input.length)
            throw new IllegalArgumentException("The size of input should match the amount of input nodes this network has!");
        double[] lastLayer, currentLayer = input;
        for(int a = 0; a < this.weights.length; a++){
            double[][] layer = this.weights[a];
            lastLayer = currentLayer;
            currentLayer = new double[layer.length];
            for(int toNode = 0; toNode < layer.length; toNode++){
                currentLayer[toNode] = this.biasWeights[a];
                for(int fromNode = 0; fromNode < layer[toNode].length; fromNode++){
                    currentLayer[toNode] += lastLayer[fromNode] * layer[toNode][fromNode];
                }
                if(this.activationFunction != null)
                    currentLayer[toNode] = this.activationFunction.apply(currentLayer[toNode]);
//                System.out.println("layer: " + a + " node: " + toNode + " value: " + currentLayer[toNode]);
            }
        }
        return currentLayer;
    }

    public NeuralNetwork cross(@NotNull NeuralNetwork network, @NotNull Random random){
        if(network.weights.length != this.weights.length)
            throw new IllegalArgumentException("This network and the given network must have the same number of layers!");
        for(int a = 0; a < this.weights.length; a++)
            if(this.weights[a].length != network.weights[a].length)
                throw new IllegalArgumentException("This network and the given network must have the same number of nodes in each layer!");

        double[][][] newWeights = new double[this.weights.length][][];
        for(int a = 0; a < newWeights.length; a++){
            newWeights[a] = new double[this.weights[a].length][];
            for(int b = 0; b < newWeights[a].length; b++){
                newWeights[a][b] = new double[this.weights[a][b].length];
                for(int c = 0; c < newWeights[a][b].length; c++){
                    if(random.nextInt(2) == 0)
                        newWeights[a][b][c] = this.weights[a][b][c];
                    else
                        newWeights[a][b][c] = network.weights[a][b][c];
                }
            }
        }
        double[] newBiasWeights = new double[this.biasWeights.length];
        for(int a = 0; a < newBiasWeights.length; a++){
            if(random.nextInt(2) == 0)
                newBiasWeights[a] = this.biasWeights[a];
            else
                newBiasWeights[a] = network.biasWeights[a];
        }
        return new NeuralNetwork(newWeights,newBiasWeights,this.activationFunction,this.maxWeight);
    }

    public NeuralNetwork clone(){
        return new NeuralNetwork(this.weights, this.biasWeights, this.activationFunction, this.maxWeight);
    }

    public void setMaxWeight(double maxWeight){
        this.maxWeight = maxWeight;
    }

    public String toString(){
        return "NeuralNetwork@id=" + this.id + "@inputs=" + this.weights[0][0].length + "@hiddenLayers=" + (this.weights.length - 1) + "@outputs=" + this.weights[this.weights.length - 1];
    }

    public byte[] toBytes(){
        int size = 8; // 8 for max weight
        size += 4; // for weights
        for(double[][] arr1 : this.weights){
            size += 4;
            for(double[] arr2 : arr1){
                size += 8 * arr2.length + 4;
            }
        }
        size += 4 + 8 * this.biasWeights.length; // for bias weights
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putInt(this.weights.length);
        for(double[][] arr1 : this.weights){
            buffer.putInt(arr1.length);
            for(double[] arr2 : arr1){
                buffer.putInt(arr2.length);
                for(double d : arr2)
                    buffer.putDouble(d);
            }
        }
        buffer.putInt(this.biasWeights.length);
        for(double d : this.biasWeights)
            buffer.putDouble(d);
        buffer.putDouble(this.maxWeight);
        return buffer.array();
    }

    public static NeuralNetwork fromBytes(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        double[][][] weights = new double[buffer.getInt()][][];
        for(int a = 0; a < weights.length; a++){
            weights[a] = new double[buffer.getInt()][];
            for(int b = 0; b < weights[a].length; b++){
                weights[a][b] = new double[buffer.getInt()];
                for(int c = 0; c < weights[a][b].length; c++)
                    weights[a][b][c] = buffer.getDouble();
            }
        }
        double[] bias_weights = new double[buffer.getInt()];
        for(int a = 0; a < bias_weights.length; a++)
            bias_weights[a] = buffer.getDouble();
        double max_weight = buffer.getDouble();
        return new NeuralNetwork(weights,bias_weights,null,max_weight);
    }
}
