package com.hclm.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

/**
 * twilio 工具
 *
 * @author hanhua
 * @since 2025/10/21
 */
@Slf4j
public class TwilioUtil {
    private static PhoneNumber fromPhoneNumber;
    private static boolean init = false;

    public TwilioUtil(TwilioProperties twilioProperties) {
        fromPhoneNumber = new PhoneNumber(twilioProperties.getFromPhoneNumber());
        Twilio.init(
                twilioProperties.getAccountSid(),
                twilioProperties.getAuthToken()
        );
        init = true;
        log.info("初始化twilio工具类 {}", twilioProperties);
    }

    private static void checkInit() {
        if (!init) {
            throw new RuntimeException("请先初始化TwilioUtil");
        }
    }

    /**
     * 发送短信
     *
     * @param toPhoneNumber 到电话号码
     * @param message       信息
     */
    public static void sendSms(String toPhoneNumber, String message) {
        checkInit();
        // 发送短信
        Message.creator(
                new PhoneNumber(toPhoneNumber),
                fromPhoneNumber,
                message
        ).create();
    }
}
