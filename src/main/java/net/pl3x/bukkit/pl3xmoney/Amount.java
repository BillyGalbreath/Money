package net.pl3x.bukkit.pl3xmoney;

import java.util.concurrent.ThreadLocalRandom;

public class Amount {
    private final double min;
    private final double max;

    public Amount(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getRandom() {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
