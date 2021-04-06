package com.xcordia.document.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.xcordia.document.web.rest.TestUtil;

public class OrderUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderUser.class);
        OrderUser orderUser1 = new OrderUser();
        orderUser1.setId(1L);
        OrderUser orderUser2 = new OrderUser();
        orderUser2.setId(orderUser1.getId());
        assertThat(orderUser1).isEqualTo(orderUser2);
        orderUser2.setId(2L);
        assertThat(orderUser1).isNotEqualTo(orderUser2);
        orderUser1.setId(null);
        assertThat(orderUser1).isNotEqualTo(orderUser2);
    }
}
