package com.xcordia.document.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.xcordia.document.web.rest.TestUtil;

public class MessageRecipientTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageRecipient.class);
        MessageRecipient messageRecipient1 = new MessageRecipient();
        messageRecipient1.setId(1L);
        MessageRecipient messageRecipient2 = new MessageRecipient();
        messageRecipient2.setId(messageRecipient1.getId());
        assertThat(messageRecipient1).isEqualTo(messageRecipient2);
        messageRecipient2.setId(2L);
        assertThat(messageRecipient1).isNotEqualTo(messageRecipient2);
        messageRecipient1.setId(null);
        assertThat(messageRecipient1).isNotEqualTo(messageRecipient2);
    }
}
