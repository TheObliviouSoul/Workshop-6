package ads.workshop;

public final class RadarTracker {
    public double updateDistance(double initialDistance, double[] deltas) {
        double distance = Math.max(0.0, initialDistance);

        for (double delta : deltas) {
            distance = distance - delta;
            if (distance < 0.0) {
                distance = 0.0;
            }
        }

        return distance;
    }

    public boolean shouldWarn(double initialDistance, double[] deltas, double safeThreshold) {
        return updateDistance(initialDistance, deltas) < safeThreshold;
    }
}
