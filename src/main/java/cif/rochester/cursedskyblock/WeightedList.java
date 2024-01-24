package cif.rochester.cursedskyblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class WeightedList<E extends IWeighted> extends ArrayList<E> {
    private int sum = 0;

    @Override
    public boolean add(E e) {
        if(super.add(e)){
            sum+=e.getWeight();
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(super.remove(o)){
            if(o instanceof IWeighted) {
                sum -= ((IWeighted) o).getWeight();
            }
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        E prev = super.set(index, element);
        if(prev != null){
            sum-=prev.getWeight();
        }
        sum+=element.getWeight();
        return prev;
    }

    @Override
    public void clear() {
        super.clear();
        sum=0;
    }

    public E getRandom(Random rand){
        int randInt = rand.nextInt(sum);
        for (E e : this) {
            randInt-=e.getWeight();
            if(randInt < 0){
                return e;
            }
        }
        throw new IllegalStateException("Reached impossible state.");
    }
}
