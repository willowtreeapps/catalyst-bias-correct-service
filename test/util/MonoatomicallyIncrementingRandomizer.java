package util;

public class MonoatomicallyIncrementingRandomizer implements Randomizer {
    @Override
    public int next() {
        return _value++;
    }

    private int _value = 0;
}
