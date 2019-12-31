package util;

public class MonotonicallyIncrementingRandomizer implements Randomizer {
    @Override
    public int next() {
        return _value++;
    }

    private int _value = 0;
}
