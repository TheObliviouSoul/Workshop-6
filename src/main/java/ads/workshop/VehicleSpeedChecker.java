package ads.workshop;

public final class VehicleSpeedChecker {
    private final SensorParser sensorParser;

    public VehicleSpeedChecker(SensorParser sensorParser) {
        this.sensorParser = sensorParser;
    }

    public boolean isOverspeeding(String rawSpeed, double speedLimitKph) {
        Double parsedSpeed = sensorParser.parseSpeedKph(rawSpeed);
        if (parsedSpeed == null) {
            throw new IllegalStateException("Sensor failure: data unavailable or invalid");
        }
        return parsedSpeed > speedLimitKph;
    }
}
