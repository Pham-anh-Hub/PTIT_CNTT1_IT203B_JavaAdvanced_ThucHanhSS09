package test;

import org.junit.jupiter.api.Test;
import engine.TrafficLight;
import pattern.state.TrafficLightState;

import static org.junit.jupiter.api.Assertions.*;

//Test chuyển đổi trạng thái đèn giao thông (State Pattern).
public class TrafficLightStateTest {

    //Test trạng thái ban đầu là GREEN
    @Test
    void testInitialStateIsGreen() {
        TrafficLight light = new TrafficLight();

        assertEquals("GREEN", light.getStateName(),
                "Trạng thái ban đầu phải là GREEN");
    }

    //Test chu kỳ: GREEN → YELLOW → RED → GREEN
    @Test
    void testStateTransitionCycle() {
        TrafficLight light = new TrafficLight();

        // GREEN -> YELLOW
        light.nextState();
        assertEquals("YELLOW", light.getStateName());

        // YELLOW -> RED
        light.nextState();
        assertEquals("RED", light.getStateName());

        // RED -> GREEN
        light.nextState();
        assertEquals("GREEN", light.getStateName());
    }

    //Test thời gian mỗi trạng thái > 0
    @Test
    void testStateDurationPositive() {
        TrafficLight light = new TrafficLight();

        for (int i = 0; i < 3; i++) {
            TrafficLightState state = light.getCurrentState();

            assertTrue(state.getDuration() > 0,
                    "Thời gian trạng thái phải > 0");

            light.nextState();
        }
    }

    //Test tên trạng thái trả về đúng chuỗi
    @Test
    void testStateNameCorrect() {
        TrafficLight light = new TrafficLight();

        assertEquals("GREEN", light.getStateName());

        light.nextState();
        assertEquals("YELLOW", light.getStateName());

        light.nextState();
        assertEquals("RED", light.getStateName());
    }
}