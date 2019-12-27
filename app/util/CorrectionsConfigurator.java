package util;

public interface CorrectionsConfigurator {
    BiasCorrector createCorrector(Randomizer randomizer);
}
