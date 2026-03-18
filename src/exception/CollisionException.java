package exception;

/**
 * Exception tùy chỉnh - Xảy ra khi 2 xe lao vào ngã tư cùng lúc do lỗi
 * synchronization.
 * Cho thấy cơ chế Lock/Semaphore chưa hoạt động đúng.
 *
 * Gợi ý: extends Exception, tạo constructor nhận message và cause.
 */
public class CollisionException extends Exception {

    public CollisionException() {
        super();
    }

    public CollisionException(String message) {
        super(message);
    }

    public CollisionException(String message, Throwable cause) {
        super(message, cause);
    }
}
