package io.avec.reflectiontest.classification;

/**
 * Created by avec112 on 12.11.19.
 */
public enum Classification {

    // A is lowest and D highest
    A(1),
    B(2),
    C(3),
    D(4),
    NO_ACCESS(100); // Useful for default or just plain hidden

    private int rank;
    Classification(int rank) {
        this.rank = rank;
    }

    boolean isHigherOrEqualTo(Classification classification) {
        return rank >= classification.rank;
    }
}
