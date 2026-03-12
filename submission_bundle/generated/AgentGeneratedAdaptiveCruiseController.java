package ads.workshop.legacy;

public final class AgentGeneratedAdaptiveCruiseController {
    public double calculateTargetVelocity(
            double currentSpeed,
            double radarDist,
            double v2xSpeedLimit,
            boolean isObstacleDetected
    ) {
        double margin = 0.5;
        double speedCap = v2xSpeedLimit;
        double targetVelocity = currentSpeed;

        if (isObstacleDetected) {
            targetVelocity = 0.0;
        } else if (radarDist < 5.0) {
            margin = 0.0;
            targetVelocity = 0.0;
        } else {
            margin = 1.0;
            speedCap = currentSpeed;
            targetVelocity = speedCap + margin;
        }

        return targetVelocity;
    }
}
