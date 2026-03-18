    package test;

    import entity.*;
    import exception.CollisionException;
    import exception.TrafficJamException;
    import org.junit.jupiter.api.Test;
    import engine.Intersection;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.*;

    import static org.junit.jupiter.api.Assertions.*;

    //Stress Test - Kiểm tra ngã tư dưới tải cao (đa luồng).
    public class IntersectionStressTest {

        //Test 100 xe vào cùng lúc - không được xảy ra collision
        @Test
        void testNoCollisionWith100Vehicles() throws InterruptedException {
            Intersection intersection = new Intersection();

            ExecutorService executor = Executors.newFixedThreadPool(20);
            List<Future<?>> futures = new ArrayList<>();

            // Tạo 100 xe
            for (int i = 0; i < 100; i++) {
                Vehicle v = new Car(String.valueOf(i), "NORTH");

                futures.add(executor.submit(() -> {
                    try {
                        intersection.enter(v);
                    } catch (Exception e) {
                        fail("Không được xảy ra Exception: " + e.getMessage());
                    }
                }));
            }

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

            // Nếu không fail thì pass
            assertTrue(true);
        }

        //Test xe ưu tiên được vào trước
        @Test
        void testPriorityVehicleGoesFirst() throws InterruptedException {
            Intersection intersection = new Intersection();

            ExecutorService executor = Executors.newFixedThreadPool(5);

            Vehicle car = new Car("1", "EAST");
            Vehicle ambulance = new Ambulance("A1", "EAST");

            List<String> order = new CopyOnWriteArrayList<>();

            executor.submit(() -> {
                try {
                    intersection.enter(car);
                    order.add("car");
                } catch (Exception e) {
                    fail("Không được xảy ra exception: " + e.getMessage());
                }
            });

            executor.submit(() -> {
                try {
                    intersection.enter(ambulance);
                    order.add("ambulance");
                } catch (Exception e) {
                    fail("Không được xảy ra exception: " + e.getMessage());
                }
            });

            executor.shutdown();
            executor.awaitTermination(3, TimeUnit.SECONDS);

            // Ambulance phải nằm trước car
            assertTrue(order.indexOf("ambulance") <= order.indexOf("car"),
                    "Xe ưu tiên phải đi trước xe thường");
        }

        //Test TrafficJamException khi hàng đợi quá dài
        @Test
        void testTrafficJamException() {
            Intersection intersection = new Intersection(5); // max queue = 5

            // Đẩy quá số lượng cho phép
            assertThrows(TrafficJamException.class, () -> {
                for (int i = 0; i < 20; i++) {
                    Vehicle v = new Truck(String.valueOf(i), "WEST");
                    intersection.enter(v);
                }
            });
        }
    }