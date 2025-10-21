package com.hclm.twilio;

import lombok.Data;

@Data
public class TwilioProperties {
    /**
     * 帐户sid
     */
    private String accountSid;
    /**
     * 密钥
     */
    private String authToken;
    /**
     * 短信发送方
     */
    private String fromPhoneNumber;
}
