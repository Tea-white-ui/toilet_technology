package cn.tea.toilet.technology.block.multiblock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiblockValidationStateTest {
    @Test
    void requestsValidationOnConstructionAndAtTheConfiguredInterval() {
        MultiblockValidationState state = new MultiblockValidationState(3);

        assertTrue(state.tickAndShouldValidate());
        assertFalse(state.tickAndShouldValidate());
        assertFalse(state.tickAndShouldValidate());
        assertTrue(state.tickAndShouldValidate());
    }

    @Test
    void reportsOnlyActualValidityTransitions() {
        MultiblockValidationState state = new MultiblockValidationState(20);

        assertTrue(state.updateValidity(true));
        assertTrue(state.isValid());
        assertFalse(state.updateValidity(true));
        assertTrue(state.updateValidity(false));
        assertFalse(state.isValid());
    }

    @Test
    void resetRequestsAnImmediateValidationAndClearsValidity() {
        MultiblockValidationState state = new MultiblockValidationState(20);
        state.updateValidity(true);

        state.reset();

        assertFalse(state.isValid());
        assertTrue(state.tickAndShouldValidate());
    }
}
