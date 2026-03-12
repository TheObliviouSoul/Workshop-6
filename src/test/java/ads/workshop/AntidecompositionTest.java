package ads.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AntidecompositionTest {

    @Nested
    @DisplayName("Program P: system-level behavior")
    class ProgramP {
        @Test
        void testSetT_canBeAdequateForHighLevelBranchesOnly() {
            VehicleSpeedChecker checker = new VehicleSpeedChecker(new SensorParser());
            assertTrue(checker.isOverspeeding("130.0", 120.0));
            assertFalse(checker.isOverspeeding("80.0", 120.0));
        }
    }

    @Nested
    @DisplayName("Component Q: isolated parser integrity")
    class ComponentQ {
        private final SensorParser parser = new SensorParser();

        @Test
        void nullAndMalformedInputsAreRejected() {
            assertNull(parser.parseSpeedKph(null));
            assertNull(parser.parseSpeedKph(""));
            assertNull(parser.parseSpeedKph("abc"));
        }

        @Test
        void nonPhysicalValuesAreRejected() {
            assertNull(parser.parseSpeedKph("-1.0"));
            assertNull(parser.parseSpeedKph("400.0"));
        }

        @Test
        void validInputRemainsAccepted() {
            assertTrue(parser.parseSpeedKph("42.5") != null);
        }

        @Test
        void programP_throwsWhenComponentQFails() {
            VehicleSpeedChecker checker = new VehicleSpeedChecker(parser);
            assertThrows(IllegalStateException.class, () -> checker.isOverspeeding("abc", 100.0));
        }
    }
}
