package ads.workshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdaptiveCruiseControllerLkwTest {
    private static final double EPS = 1e-9;
    private final AdaptiveCruiseController controller = new AdaptiveCruiseController();

    @Test
    void allDefs_branchObstacleDetected_reachesReturnUse() {
        double target = controller.calculateTargetVelocity(80.0, 50.0, 60.0, true);
        assertEquals(0.0, target, EPS);
    }

    @Test
    void allDefs_branchRadarTooClose_reachesReturnUse() {
        double target = controller.calculateTargetVelocity(80.0, 3.0, 60.0, false);
        assertEquals(0.0, target, EPS);
    }

    @Test
    void allUses_currentSpeedPath_whenCurrentIsLowerThanV2x() {
        double target = controller.calculateTargetVelocity(55.0, 20.0, 80.0, false);
        assertEquals(55.0, target, EPS);
    }

    @Test
    void allUses_v2xPath_whenV2xLimitIsLowerThanCurrent() {
        double target = controller.calculateTargetVelocity(100.0, 20.0, 70.0, false);
        assertEquals(70.0, target, EPS);
    }

    @Test
    void inputSanitization_negativeValuesDoNotLeakIntoVelocityCommand() {
        double target = controller.calculateTargetVelocity(-10.0, 20.0, -5.0, false);
        assertEquals(0.0, target, EPS);
    }
}
