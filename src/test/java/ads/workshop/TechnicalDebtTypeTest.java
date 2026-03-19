package ads.workshop;

import ads.workshop.legacy.AgentGeneratedAdaptiveCruiseController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TechnicalDebtTypeTest {
    private static final double EPS = 1e-9;

    @Nested
    @DisplayName("Dead data debt")
    class DeadDataDebt {
        @Test
        void generatedController_overwritesInitialDefinitionsBeforeStableUse() {
            AgentGeneratedAdaptiveCruiseController generated = new AgentGeneratedAdaptiveCruiseController();

            double target = generated.calculateTargetVelocity(80.0, 20.0, 60.0, false);

            assertEquals(81.0, target, EPS);
        }
    }

    @Nested
    @DisplayName("Logic leak debt")
    class LogicLeakDebt {
        @Test
        void correctedController_preservesLowerV2xSpeedLimitWhileGeneratedVersionLeaksPastIt() {
            AgentGeneratedAdaptiveCruiseController generated = new AgentGeneratedAdaptiveCruiseController();
            AdaptiveCruiseController corrected = new AdaptiveCruiseController();

            double generatedTarget = generated.calculateTargetVelocity(80.0, 20.0, 60.0, false);
            double correctedTarget = corrected.calculateTargetVelocity(80.0, 20.0, 60.0, false);

            assertEquals(60.0, correctedTarget, EPS);
            assertNotEquals(correctedTarget, generatedTarget);
            assertTrue(generatedTarget > 60.0);
        }
    }

    @Nested
    @DisplayName("State drift debt")
    class StateDriftDebt {
        @Test
        void loopReentry_keepsDistanceClampedAfterResetAcrossMultipleUpdates() {
            RadarTracker tracker = new RadarTracker();

            double finalDistance = tracker.updateDistance(3.0, new double[]{2.0, 3.0, 1.0});

            assertEquals(0.0, finalDistance, EPS);
            assertTrue(tracker.shouldWarn(3.0, new double[]{2.0, 3.0, 1.0}, 0.5));
        }
    }

    @Nested
    @DisplayName("Component boundary debt")
    class ComponentBoundaryDebt {
        @Test
        void invalidSensorInput_requiresDedicatedComponentChecksRatherThanOnlySystemHappyPaths() {
            VehicleSpeedChecker checker = new VehicleSpeedChecker(new SensorParser());

            assertThrows(IllegalStateException.class, () -> checker.isOverspeeding("abc", 100.0));
            assertFalse(checker.isOverspeeding("80.0", 100.0));
        }
    }
}
