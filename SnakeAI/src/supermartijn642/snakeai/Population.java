package supermartijn642.snakeai;

import supermartijn642.snakeai.providers.CrossDistanceGameProvider;
import supermartijn642.snakeai.providers.NeuralNetworkGameProvider;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created 2/23/2019 by SuperMartijn642
 */
public class Population {

    private final int populationSize;
    private final int survivors;
    private final int newSnakes;
    private final int gameSize;
    private final int gameFoodCount;

    private final long seed;
    private final LinkedHashMap<NeuralNetworkGameProvider,SnakeGame> networks;
    private final ArrayList<SnakeGame> bestGames = new ArrayList<>();
    private int bestGameGeneration = 0;

    private int iteration = 0;

    private boolean running = false;
    private boolean shouldStop = false;

    public Population(long seed, int size, int survivors, int newSnakes, int gameSize, int gameFoodCount){
        this.seed = seed;
        this.populationSize = size;
        this.survivors = survivors;
        this.newSnakes = newSnakes;
        this.gameSize = gameSize;
        this.gameFoodCount = gameFoodCount;
        this.networks = new LinkedHashMap<>(populationSize);
        this.createNetworks();
    }

    public Population(int size, int survivors, int newSnakes, int gameSize, int gameFoodCount){
        this(new Random().nextLong(),size,survivors,newSnakes,gameSize,gameFoodCount);
    }

    private void createNetworks(){
        for (int a = 0; a < populationSize; a++) {
            NeuralNetworkGameProvider provider = new CrossDistanceGameProvider(x -> x,this.gameSize,this.gameFoodCount);
            networks.put(provider, new SnakeGame(provider, new Random(this.seed + this.iteration + a).nextInt()));
        }
    }

    private void singleRun(){
        this.mutateNetworks();
        Thread[] threads = new Thread[networks.size() % 8 == 0 ? 8 : networks.size() % 10 == 0 ? 10 : 1];
        final int networksPerThread = networks.size() / threads.length;
        for (int a = 0; a < threads.length; a++) {
            final int currentThread = a;
            threads[currentThread] = new Thread(() -> {
                for (int b = currentThread * networksPerThread; b < (currentThread + 1) * networksPerThread; b++) {
                    ((SnakeGame) networks.values().toArray()[b]).finish();
                }
            });
            threads[currentThread].start();
        }
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.sortNetworks();
        this.bestGames.add((SnakeGame)this.networks.values().toArray()[0]);
        this.iteration++;
    }

    public void run(){
        if(this.running)
            return;
        this.running = true;
        while(!this.shouldStop)
            this.singleRun();
        this.shouldStop = false;
        this.running = false;
    }

    public void runAsync(){
        if(this.running)
            return;
        new Thread("SnakeIterationsThread"){
            @Override
            public void run() {
                Population.this.run();
            }
        }.start();
    }

    public void run(int iterations){
        if(this.running)
            return;
        this.running = true;
        for(int a = 0; a < iterations; a++)
            this.singleRun();
        this.running = false;
    }

    public void runAsync(final int iterations){
        if(this.running)
            return;
        new Thread("SnakeIterationsThread"){
            @Override
            public void run() {
                Population.this.run(iterations);
            }
        }.start();
    }

    private void sortNetworks(){
        ArrayList<Map.Entry<NeuralNetworkGameProvider,SnakeGame>> sorted = new ArrayList<>(networks.entrySet());
        sorted.sort((a,b) -> {
            int score = Double.compare(b.getValue().getScore(),a.getValue().getScore());
            return score == 0 ? Integer.compare(a.getKey().network.id,b.getKey().network.id) : score; // smaller than 0 if a is better than b
        });
        networks.clear();
        for(Map.Entry<NeuralNetworkGameProvider,SnakeGame> entry : sorted)
            networks.put(entry.getKey(),entry.getValue());
    }

    private void mutateNetworks(){
        LinkedHashMap<NeuralNetworkGameProvider, SnakeGame> new_networks = new LinkedHashMap<>(this.populationSize);
        // survivors
        for (int a = 0; a < survivors; a++) {
            NeuralNetworkGameProvider provider = (NeuralNetworkGameProvider) networks.keySet().toArray()[a];
            provider = new CrossDistanceGameProvider(provider.network.clone(),this.gameSize,this.gameFoodCount); // clone network for old games to be able to be replayed
            new_networks.put(provider, new SnakeGame(provider, new Random(this.seed + this.iteration + a + 1).nextInt()));
        }
        // new snakes
        for (int a = 0; a < newSnakes; a++) {
            NeuralNetworkGameProvider provider = new CrossDistanceGameProvider(x -> x,this.gameSize,this.gameFoodCount);
            new_networks.put(provider, new SnakeGame(provider, new Random(this.seed + this.iteration + a + 2).nextInt()));
        }
        // re-population
        for (int a = survivors + newSnakes; a < populationSize; a++) {
            NeuralNetworkGameProvider provider = (NeuralNetworkGameProvider) networks.keySet().toArray()[a % survivors];
            provider = new CrossDistanceGameProvider(provider.network.clone(),this.gameSize,this.gameFoodCount);
            provider.network.changeWeights(2,new Random(this.seed + this.iteration + a));
            new_networks.put(provider, new SnakeGame(provider,new Random(this.seed + this.iteration + a + 3).nextInt()));
        }
        networks.clear();
        networks.putAll(new_networks);
    }

    public SnakeGame getBestGame(int generation){
        return this.bestGames.get(generation - 1);
    }

    public SnakeGame getBestGame(){
        return this.getBestGame(this.bestGameGeneration);
    }

    public SnakeGame getCurrentBestGame(){
        return this.getBestGame(this.iteration);
    }

    public boolean isRunning(){
        return this.running;
    }

    public void stop(){
        if(this.running)
            this.shouldStop = true;
    }

    public int getGameSize(){
        return this.gameSize;
    }

    public int getGeneration(){
        return this.iteration;
    }

    public byte[] toBytes(){
        int networksBytes = 4;
        ArrayList<byte[]> bytes1 = new ArrayList<>(this.networks.size());
        for(Map.Entry<NeuralNetworkGameProvider,SnakeGame> entry : this.networks.entrySet()){
            byte[] b = entry.getValue().toBytes();
            bytes1.add(b);
            networksBytes += b.length + 4;
        }
        int bestGamesBytes = 4;
        ArrayList<byte[]> bytes2 = new ArrayList<>(this.bestGames.size());
        for(SnakeGame game : this.bestGames){
            byte[] b = game.toBytes();
            bytes2.add(b);
            bestGamesBytes += b.length + 4;
        }
        int iBytes = 7 * 4;
        int lBytes = 8;
        ByteBuffer buffer = ByteBuffer.allocate(networksBytes + bestGamesBytes + iBytes + lBytes);
        buffer.putInt(this.populationSize);
        buffer.putInt(this.survivors);
        buffer.putInt(this.newSnakes);
        buffer.putInt(this.gameSize);
        buffer.putInt(this.gameFoodCount);
        buffer.putLong(this.seed);
        buffer.putInt(this.networks.size());
        for(byte[] b : bytes1){
            buffer.putInt(b.length);
            buffer.put(b);
        }
        buffer.putInt(this.bestGames.size());
        for(byte[] b : bytes2){
            buffer.putInt(b.length);
            buffer.put(b);
        }
        buffer.putInt(this.bestGameGeneration);
        buffer.putInt(this.iteration);
        return buffer.array();
    }

    public static Population fromBytes(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int populationSize = buffer.getInt();
        int survivors = buffer.getInt();
        int newSnakes = buffer.getInt();
        int gameSize = buffer.getInt();
        int gameFoodCount = buffer.getInt();
        long seed = buffer.getLong();
        int length = buffer.getInt();
        LinkedHashMap<NeuralNetworkGameProvider,SnakeGame> networks = new LinkedHashMap<>(length);
        for(int a = 0; a < length; a++){
            byte[] b = new byte[buffer.getInt()];
            buffer.get(b);
            SnakeGame game = SnakeGame.fromBytes(b);
            networks.put((NeuralNetworkGameProvider)game.getProvider(),game);
        }
        length = buffer.getInt();
        ArrayList<SnakeGame> bestGames = new ArrayList<>(length);
        for(int a = 0; a < length; a++){
            byte[] b = new byte[buffer.getInt()];
            buffer.get(b);
            bestGames.add(SnakeGame.fromBytes(b));
        }
        int bestGameGeneration = buffer.getInt();
        int iteration = buffer.getInt();
        Population population = new Population(seed,populationSize,survivors,newSnakes,gameSize,gameFoodCount);
        population.networks.clear();
        population.networks.putAll(networks);
        population.bestGames.clear();
        population.bestGames.addAll(bestGames);
        population.bestGameGeneration = bestGameGeneration;
        population.iteration = iteration;
        return population;
    }
}
