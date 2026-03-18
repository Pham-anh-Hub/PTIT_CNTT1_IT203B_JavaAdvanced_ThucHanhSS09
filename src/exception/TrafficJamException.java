package exception;

/**
 * Exception tùy chỉnh - Xảy ra khi hàng đợi chờ đèn đỏ quá dài.
 * Được throw khi số xe chờ vượt quá MAX_QUEUE_CAPACITY.
 *
 * Gợi ý: extends Exception, tạo constructor nhận message và cause.
 */
public class TrafficJamException extends Exception {

    public TrafficJamException() {
        super();
    }

    public TrafficJamException(String message) {
        super(message);
    }

    public TrafficJamException(String message, Throwable cause) {
        super(message, cause);
    }
}
