package com.ym.server;

import org.apache.commons.mail.HtmlEmail;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;


public class SendCode {

	//产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI4Fe23dmkXaVhxRf7fPtT";
    static final String accessKeySecret = "139e80gcrAmP6llfqlyMInNJVCcV3l";

    public static boolean send(String phoneNumber,String code) throws ClientException {		//发送手机验证码

    	try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");	//10分钟
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumber);	//待发送手机号
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("黄龙士大作业个人通讯");		//签名
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_177548966");		//短信模板
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的会员,您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\""+code+"\"}");

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);	//发送失败可能就是阿里云账户没钱了

            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public static boolean sendEmail(String useremail,String code) {
			
    	try {
			
        	HtmlEmail email = new HtmlEmail();
        	email.setHostName("smtp.163.com");
        	email.setCharset("UTF-8");
        	email.addTo(useremail);
        	email.setFrom("iym070010@163.com","黄龙人个人即时通讯系统");
        	email.setAuthentication("iym070010@163.com", "kg150520");
        	email.setSubject("黄龙士个人即时通讯系统注册验证");
        	email.setMsg("尊敬的用户，您的注册会员动态密码为："+code+"，请勿泄漏于他人！");
        	email.send();
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
    
    
	
}
