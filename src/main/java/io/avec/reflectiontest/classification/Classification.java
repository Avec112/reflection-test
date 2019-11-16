package io.avec.reflectiontest.classification;

/**
 * Created by avec112 on 12.11.19.
 */
public enum Classification {

    UNCLASSIFIED(1),
    RESTRICTED(2),
    CONFIDENTIAL(3),
    SECRET(4),
    NO_ACCESS(100); // Useful for default or just plain hidden for all

    private int rank;
    Classification(int rank) {
        this.rank = rank;
    }

    boolean isHigherOrEqualTo(Classification classification) {
        return rank >= classification.rank;
    }
}
