package com.xcordia.document.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.xcordia.document.web.rest.TestUtil;

public class LookupValueTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LookupValue.class);
        LookupValue lookupValue1 = new LookupValue();
        lookupValue1.setId(1L);
        LookupValue lookupValue2 = new LookupValue();
        lookupValue2.setId(lookupValue1.getId());
        assertThat(lookupValue1).isEqualTo(lookupValue2);
        lookupValue2.setId(2L);
        assertThat(lookupValue1).isNotEqualTo(lookupValue2);
        lookupValue1.setId(null);
        assertThat(lookupValue1).isNotEqualTo(lookupValue2);
    }
}
