package util;

import java.util.Random;

public class SimpleRandomizer implements Randomizer {
    public SimpleRandomizer(Random random) {
        _random = random;
    }

    @Override
    public int next() {
        return _random.nextInt();
    }

    private Random _random;
}
