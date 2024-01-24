package cif.rochester.cursedskyblock;

import java.util.*;
import java.util.function.Consumer;

public class ChanceDrop<T> {
    private final T result;
    private final double chance;

    private ChanceDrop(T result, double chance){
        this.result = result;
        this.chance = chance;
    }

    public static <G> ChanceDrop<G> of(G result, double chance) {
        return new ChanceDrop<G>(result,chance);
    }

    public static class ChanceList<T> extends ArrayList<ChanceDrop<T>> {
        @SafeVarargs
        public static <G> ChanceList<G> of(ChanceDrop<G>... drops) {
            ChanceList<G> out = new ChanceList<>();
            out.addAll(Arrays.asList(drops));
            return out;
        }

        public Collection<T> roll(Random random){
            List<T> rolls = new ArrayList<>();
            for (ChanceDrop<T> drop : this) {
                if(drop.chance < random.nextDouble(1.0)){
                    rolls.add(drop.result);
                }
            }
            return rolls;
        }
        public void roll(Random random, Consumer<T> consumer){
            for (ChanceDrop<T> drop : this) {
                if(drop.chance < random.nextDouble(1.0)){
                    consumer.accept(drop.result);
                }
            }
        }
    }
}
