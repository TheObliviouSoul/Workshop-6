package ads.workshop;

public final class SensorParser {
    public static final double MIN_SPEED_KPH = 0.0;
    public static final double MAX_SPEED_KPH = 250.0;

    public Double parseSpeedKph(String rawSpeed) {
        if (rawSpeed == null) {
            return null;
        }

        String trimmed = rawSpeed.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        try {
            double parsed = Double.parseDouble(trimmed);
            if (parsed < MIN_SPEED_KPH || parsed > MAX_SPEED_KPH) {
                return null;
            }
            return parsed;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
