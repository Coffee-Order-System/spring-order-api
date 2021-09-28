package gc.cafe.order.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    @DisplayName("이메일 주소 범위가 다른 경우")
    void invalidEmailLength() {
        assertThrows(IllegalArgumentException.class, () -> new Email("a@a"));
        assertThrows(IllegalArgumentException.class, () -> new Email("tester@aaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
}