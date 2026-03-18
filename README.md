# 🚦 SMART TRAFFIC SIMULATOR

## Hệ thống Mô phỏng Giao thông Thông minh

Ứng dụng console mô phỏng hoạt động của một ngã tư đường phố, quản lý các luồng phương tiện di chuyển, phản ứng với tín hiệu đèn giao thông và giải quyết các bài toán về chia sẻ tài nguyên bằng lập trình đa luồng (Multithreading) trong Java.

---

## 📁 Cấu trúc dự án

```
src/
├── entity/          → Các đối tượng phương tiện (Vehicle, Car, Truck, Ambulance...)
├── pattern/         → Design Patterns (State, Observer, Factory)
├── engine/          → Logic mô phỏng (TrafficLight, Intersection, SimulationEngine)
├── exception/       → Ngoại lệ tự định nghĩa (TrafficJamException, CollisionException)
├── util/            → Tiện ích (TrafficLogger, TrafficStatistics)
├── test/            → Unit Tests (JUnit 5)
└── Main.java        → Điểm khởi chạy
```

---

## ⚙️ Cách hệ thống hoạt động

### Luồng chạy chính

1. `SimulationEngine` khởi tạo `TrafficLight` và `Intersection`
2. `TrafficLight` chạy trên **Daemon Thread**, tự động chuyển trạng thái GREEN → YELLOW → RED theo chu kỳ
3. `VehicleFactory` liên tục sinh ra phương tiện ngẫu nhiên
4. Mỗi phương tiện chạy trên **thread riêng** (thông qua `ExecutorService`), tự di chuyển về phía ngã tư
5. Khi đến ngã tư → phương tiện kiểm tra đèn và xin phép vào `Intersection`
6. Sau khi đi qua → phương tiện giải phóng tài nguyên, phương tiện tiếp theo được vào

### Các Design Patterns

| Pattern | Áp dụng | Mục đích |
|---------|---------|----------|
| **State** | `TrafficLight` + `GreenState`, `YellowState`, `RedState` | Quản lý chuyển trạng thái đèn linh hoạt |
| **Observer** | `TrafficLight` (Subject) → `Vehicle` (Observer) | Đèn thay đổi → tự động thông báo cho xe |
| **Factory Method** | `VehicleFactory` | Sinh ngẫu nhiên các loại phương tiện |

---

## 🔒 Cách hệ thống xử lý Deadlock

### Deadlock là gì?

Deadlock (khóa chết) xảy ra khi **2 hoặc nhiều thread chờ đợi lẫn nhau** để giải phóng tài nguyên, dẫn đến tất cả bị "đóng băng" vĩnh viễn.

**Ví dụ trong giao thông thực tế:**
```
Thread Xe A: Giữ làn NORTH, chờ làn EAST
Thread Xe B: Giữ làn EAST, chờ làn NORTH
→ Cả hai chờ nhau mãi mãi = DEADLOCK
```

### 4 điều kiện gây Deadlock (Coffman Conditions)

Deadlock chỉ xảy ra khi **cả 4 điều kiện** sau đồng thời thỏa mãn:

| # | Điều kiện | Giải thích | Hệ thống phòng chống bằng cách |
|---|-----------|------------|--------------------------------|
| 1 | **Mutual Exclusion** | Tài nguyên chỉ 1 thread dùng tại 1 thời điểm | Dùng `Semaphore` cho phép **N xe** qua cùng lúc thay vì chỉ 1 |
| 2 | **Hold and Wait** | Thread giữ tài nguyên A, chờ tài nguyên B | Xe chỉ cần acquire **1 tài nguyên duy nhất** (semaphore) để qua ngã tư |
| 3 | **No Preemption** | Không thể ép thread nhả tài nguyên | Dùng `tryLock()` với **timeout** → hết thời gian chờ = tự nhả |
| 4 | **Circular Wait** | Chuỗi chờ vòng tròn A→B→C→A | Dùng **Fair Lock** (`new ReentrantLock(true)`) → xử lý theo thứ tự FIFO |

### Chiến lược phòng chống Deadlock trong hệ thống

#### 1. Semaphore thay vì Lock toàn bộ ngã tư

```java
// ❌ SAI: Lock toàn bộ → chỉ 1 xe đi qua → nghẽn cổ chai
synchronized (intersection) {
    // đi qua ngã tư
}

// ✅ ĐÚNG: Semaphore cho phép N xe đi qua đồng thời
Semaphore semaphore = new Semaphore(2, true); // 2 xe, fair = true
semaphore.acquire();  // Xin phép vào
// ... xe đi qua ...
semaphore.release();  // Giải phóng
```

**Tại sao phá được Deadlock?** Semaphore không yêu cầu xe "giữ" nhiều tài nguyên cùng lúc, phá vỡ điều kiện **Hold and Wait**.

#### 2. tryLock() với Timeout

```java
ReentrantLock lock = new ReentrantLock(true);

// ❌ SAI: Chờ mãi mãi → nguy cơ Deadlock
lock.lock();

// ✅ ĐÚNG: Chờ tối đa 5 giây, nếu không được → bỏ qua
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // ... xe đi qua ngã tư ...
    } finally {
        lock.unlock();
    }
} else {
    // Không lấy được lock → xe quay lại hàng đợi
    TrafficLogger.warn("Xe " + id + " không thể vào ngã tư, quay lại hàng đợi");
}
```

**Tại sao phá được Deadlock?** `tryLock()` với timeout phá vỡ điều kiện **No Preemption** — thread tự nhả nếu chờ quá lâu.

#### 3. Fair Lock (Khóa công bằng)

```java
// ❌ SAI: Không công bằng → xe mới có thể "chen ngang" xe cũ
ReentrantLock lock = new ReentrantLock();          // unfair

// ✅ ĐÚNG: Xe đến trước được phục vụ trước (FIFO)
ReentrantLock lock = new ReentrantLock(true);       // fair
Semaphore semaphore = new Semaphore(2, true);       // fair
```

**Tại sao phá được Deadlock?** Fair lock đảm bảo thứ tự FIFO, phá vỡ điều kiện **Circular Wait** — không có chuỗi chờ vòng tròn.

#### 4. Xe ưu tiên (PriorityVehicle) — Không chờ Lock

```java
// Xe cứu thương bỏ qua đèn đỏ, không cần chờ đợi
if (vehicle.isPriority()) {
    // Cho qua ngay, không acquire semaphore thông thường
    // → giảm số thread tranh chấp tài nguyên
}
```

#### 5. Daemon Thread cho đèn giao thông

```java
Thread lightThread = new Thread(trafficLight);
lightThread.setDaemon(true);  // Tự dừng khi main thread kết thúc
lightThread.start();
```

**Tại sao quan trọng?** Nếu không đặt daemon → khi muốn dừng chương trình, thread đèn vẫn chạy mãi → chương trình không bao giờ thoát (một dạng "deadlock" với main thread).

---

### Tóm tắt cơ chế phòng chống Deadlock

```
┌─────────────────────────────────────────────────────────┐
│              PHÒNG CHỐNG DEADLOCK                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Semaphore(N, fair=true)                                │
│  ├─ Cho phép N xe qua cùng lúc (không khóa toàn bộ)   │
│  └─ Fair = FIFO → không chen ngang                     │
│                                                         │
│  tryLock(timeout)                                       │
│  ├─ Không chờ vô hạn                                   │
│  └─ Hết timeout → tự nhả, quay lại hàng đợi           │
│                                                         │
│  Single Resource Acquisition                            │
│  ├─ Mỗi xe chỉ cần 1 tài nguyên (1 slot semaphore)    │
│  └─ Không giữ nhiều lock cùng lúc                      │
│                                                         │
│  Daemon Thread                                          │
│  └─ Đèn giao thông tự dừng khi chương trình kết thúc  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 🚀 Nâng cấp hiệu năng (Tối ưu hóa)

| Cấp độ | Phương pháp | Giải thích |
|--------|-------------|------------|
| **Cơ bản** | `ReentrantLock` | Lock toàn bộ ngã tư, chỉ 1 xe qua |
| **Trung bình** | `Semaphore(N)` | Cho N xe qua cùng lúc |
| **Nâng cao** | `ReadWriteLock` | Nhiều xe đọc trạng thái đồng thời, chỉ lock khi ghi |
| **Cao cấp** | Lock từng làn đường | Mỗi làn có lock riêng, các làn không xung đột chạy song song |

---

## 🧪 Chạy Unit Test

Yêu cầu: JUnit 5 + Mockito

```bash
# Trong IntelliJ IDEA: Click chuột phải vào thư mục test/ → Run All Tests
```

| Test | Kiểm tra |
|------|----------|
| `TrafficLightStateTest` | Chu kỳ chuyển đổi trạng thái đèn |
| `IntersectionStressTest` | 100 xe vào ngã tư cùng lúc (stress test) |
| `VehicleFactoryTest` | Factory tạo đúng loại xe |
| `TrafficStatisticsTest` | Thống kê thread-safe |

---

## 👥 Phân công

| Thành viên | Phụ trách |
|------------|-----------|
| **Người 1** | `entity/` + `VehicleFactory` |
| **Người 2** | `pattern/state/`, `pattern/observer/` + `TrafficLight` |
| **Người 3** | `Intersection`, `SimulationEngine`, `Main.java` |
| **Người 4** | `exception/`, `util/`, `test/` |
#   P T I T _ C N T T 1 _ I T 2 0 3 B _ J a v a A d v a n c e d _ T h u c H a n h S S 0 9  
 