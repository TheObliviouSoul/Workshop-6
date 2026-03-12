package ads.workshop;

public final class AdaptiveCruiseController {
    private static final double STOP_DISTANCE_METERS = 5.0;

    public double calculateTargetVelocity(
            double currentSpeed,
            double radarDist,
            double v2xSpeedLimit,
            boolean isObstacleDetected
    ) {
        double sanitizedCurrentSpeed = clampNonNegative(currentSpeed);
        double sanitizedV2xSpeedLimit = clampNonNegative(v2xSpeedLimit);
        double targetVelocity;

        if (isObstacleDetected || radarDist <= STOP_DISTANCE_METERS) {
            targetVelocity = 0.0;
        } else {
            targetVelocity = Math.min(sanitizedCurrentSpeed, sanitizedV2xSpeedLimit);
        }

        return targetVelocity;
    }

    private static double clampNonNegative(double value) {
        return Math.max(0.0, value);
    }
}
