package ads.workshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RadarTrackerAllDuPathsTest {
    private static final double EPS = 1e-9;
    private final RadarTracker tracker = new RadarTracker();

    @Test
    void allDuPaths_directPath_noLoopIteration() {
        double finalDistance = tracker.updateDistance(12.0, new double[]{});
        assertEquals(12.0, finalDistance, EPS);
    }

    @Test
    void allDuPaths_singleIteration_withoutReset() {
        double finalDistance = tracker.updateDistance(12.0, new double[]{2.5});
        assertEquals(9.5, finalDistance, EPS);
    }

    @Test
    void allDuPaths_multiIteration_withResetAndReentry() {
        double finalDistance = tracker.updateDistance(3.0, new double[]{2.0, 3.0, 1.0});
        assertEquals(0.0, finalDistance, EPS);
        assertTrue(tracker.shouldWarn(3.0, new double[]{2.0, 3.0, 1.0}, 0.5));
    }
}
