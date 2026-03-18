package test;

import entity.*;
import org.junit.jupiter.api.Test;
import pattern.factory.VehicleFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Test VehicleFactory - kiểm tra Factory Method Pattern.
public class VehicleFactoryTest {

    //Test factory tạo xe không null
    @Test
    void testCreateVehicleNotNull() {
        VehicleFactory factory = new VehicleFactory();

        Vehicle v = factory.createVehicle("1", "NORTH");

        assertNotNull(v, "Vehicle không được null");
    }

    //Test factory tạo đúng kiểu Vehicle
    @Test
    void testCreateVehicleInstance() {
        VehicleFactory factory = new VehicleFactory();

        Vehicle v = factory.createVehicle("2", "SOUTH");

        assertTrue(v instanceof Vehicle, "Phải là instance của Vehicle");
    }

    //Test factory có tạo đủ các loại xe
    @Test
    void testCreateDifferentTypes() {
        VehicleFactory factory = new VehicleFactory();

        boolean hasCar = false;
        boolean hasMotorbike = false;
        boolean hasTruck = false;
        boolean hasAmbulance = false;

        // Tạo nhiều xe để đảm bảo random có đủ loại
        for (int i = 0; i < 200; i++) {
            Vehicle v = factory.createVehicle(String.valueOf(i), "EAST");

            if (v instanceof Car) hasCar = true;
            if (v instanceof Motorbike) hasMotorbike = true;
            if (v instanceof Truck) hasTruck = true;
            if (v instanceof Ambulance) hasAmbulance = true;
        }

        assertTrue(hasCar, "Phải có Car");
        assertTrue(hasMotorbike, "Phải có Motorbike");
        assertTrue(hasTruck, "Phải có Truck");
        assertTrue(hasAmbulance, "Phải có Ambulance");
    }

    //Test phân phối xác suất (1000 xe)
    @Test
    void testDistribution() {
        VehicleFactory factory = new VehicleFactory();

        int carCount = 0;
        int motorbikeCount = 0;
        int truckCount = 0;
        int ambulanceCount = 0;

        int total = 1000;

        for (int i = 0; i < total; i++) {
            Vehicle v = factory.createVehicle(String.valueOf(i), "WEST");

            if (v instanceof Car) carCount++;
            else if (v instanceof Motorbike) motorbikeCount++;
            else if (v instanceof Truck) truckCount++;
            else if (v instanceof Ambulance) ambulanceCount++;
        }

        System.out.println("Car: " + carCount);
        System.out.println("Motorbike: " + motorbikeCount);
        System.out.println("Truck: " + truckCount);
        System.out.println("Ambulance: " + ambulanceCount);

        // Kiểm tra tổng đúng
        assertEquals(total, carCount + motorbikeCount + truckCount + ambulanceCount);

        // Kiểm tra mỗi loại phải xuất hiện (không = 0)
        assertTrue(carCount > 0);
        assertTrue(motorbikeCount > 0);
        assertTrue(truckCount > 0);
        assertTrue(ambulanceCount > 0);
    }
}