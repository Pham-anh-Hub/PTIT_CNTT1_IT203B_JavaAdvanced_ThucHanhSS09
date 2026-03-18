package pattern.factory;

import entity.*;
import java.util.Random;

public class VehicleFactory {

    private static final Random random = new Random();

    public static Vehicle createRandomVehicle(String id, String direction) {
        int rand = random.nextInt(100);

        if (rand < 40) {
            return new Motorbike(id, direction);
        } else if (rand < 70) {
            return new Car(id, direction);
        } else if (rand < 90) {
            return new Truck(id, direction);
        } else {
            return new Ambulance(id, direction);
        }
    }

    // Instance method alias - dùng trong test
    public Vehicle createVehicle(String id, String direction) {
        return createRandomVehicle(id, direction);
    }
}