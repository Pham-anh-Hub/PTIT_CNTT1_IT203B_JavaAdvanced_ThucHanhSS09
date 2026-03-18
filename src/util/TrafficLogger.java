package util;

/**
 * Tiện ích ghi log ra Console theo định dạng chuẩn.
 * Định dạng: [Time: XXs] <Nội dung log>
 *
 * ★ NGƯỜI 4 phụ trách implement ★
 */
public class TrafficLogger {

    private static long startTime = System.currentTimeMillis();

    /**
     * Reset thời gian bắt đầu (gọi khi bắt đầu mô phỏng).
     */
    public static void resetTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * In log ra console với timestamp.
     * Định dạng: [Time: 12s] Xe cứu thương #01 đang đi qua ngã tư
     *
     * @param message nội dung log
     */
    public static synchronized void log(String message) {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        long seconds = elapsedMillis / 1000;
        System.out.println("[Time: " + seconds + "s] " + message);
    }

    /**
     * In log cảnh báo (VD: kẹt xe, lỗi).
     * 
     * @param message nội dung cảnh báo
     */
    public static synchronized void warn(String message) {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        long seconds = elapsedMillis / 1000;
        System.out.println("[WARNING - Time: " + seconds + "s] " + message);
    }
}
