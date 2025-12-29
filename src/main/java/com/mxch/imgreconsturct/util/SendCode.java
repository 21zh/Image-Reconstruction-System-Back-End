package com.mxch.imgreconsturct.util;

import com.aliyun.dysmsapi20170525.models.QuerySendDetailsRequest;
import com.aliyun.dysmsapi20170525.models.QuerySendDetailsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;

import static com.aliyun.teautil.Common.toJSONString;

public class SendCode {
    public static Client createClient() throws Exception {
        Config config = new Config()
                // 配置 AccessKey ID，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                // 配置 AccessKey Secret，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
        // System.getenv()方法表示获取系统环境变量，不要直接在getenv()中填入AccessKey信息。

        // 配置 Endpoint。
        config.endpoint = "dysmsapi.aliyuncs.com";

        return new Client(config);
    }

    public static void sendCodeMessage(String phone, String code) throws Exception {
        // 初始化请求客户端
        Client client = SendCode.createClient();

        // 构造API请求对象，请替换请求参数值
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName("外卖速送")
                .setTemplateCode("SMS_499270949")
                .setTemplateParam("{\"code\":" + code + "}"); // TemplateParam为序列化后的JSON字符串。

        // 获取响应对象
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

        // 响应包含服务端响应的 body 和 headers
        System.out.println(toJSONString(sendSmsResponse));

        QuerySendDetailsRequest request = new QuerySendDetailsRequest()
                .setPhoneNumber(phone)
                .setBizId("985704666991170554^0")
                .setSendDate("20251229") // 日期：YYYYMMDD
                .setPageSize(10L)
                .setCurrentPage(1L);

        QuerySendDetailsResponse resp = client.querySendDetails(request);
        System.out.println(new Gson().toJson(resp));
    }
}
