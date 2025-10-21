package com.hclm.merchant;

import com.hclm.twilio.TwilioUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MerchantTest {
    @Test
    public void sms() {
        TwilioUtil.sendSms("+8618611111111", "测试");
    }
}
