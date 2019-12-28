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

	//��Ʒ����:��ͨ�Ŷ���API��Ʒ,�����������滻
    static final String product = "Dysmsapi";
    //��Ʒ����,�����������滻
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO �˴���Ҫ�滻�ɿ������Լ���AK(�ڰ����Ʒ��ʿ���̨Ѱ��)
    static final String accessKeyId = "LTAI4Fe23dmkXaVhxRf7fPtT";
    static final String accessKeySecret = "139e80gcrAmP6llfqlyMInNJVCcV3l";

    public static boolean send(String phoneNumber,String code) throws ClientException {		//�����ֻ���֤��

    	try {
            //������������ʱʱ��
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");	//10����
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //��ʼ��acsClient,�ݲ�֧��region��
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //��װ�������-��������������̨-�ĵ���������
            SendSmsRequest request = new SendSmsRequest();
            //����:�������ֻ���
            request.setPhoneNumbers(phoneNumber);	//�������ֻ���
            //����:����ǩ��-���ڶ��ſ���̨���ҵ�
            request.setSignName("����ʿ����ҵ����ͨѶ");		//ǩ��
            //����:����ģ��-���ڶ��ſ���̨���ҵ�
            request.setTemplateCode("SMS_177548966");		//����ģ��
            //��ѡ:ģ���еı����滻JSON��,��ģ������Ϊ"�װ��Ļ�Ա,������֤��Ϊ${code}"ʱ,�˴���ֵΪ
            request.setTemplateParam("{\"code\":\""+code+"\"}");

            //ѡ��-���ж�����չ��(�����������û�����Դ��ֶ�)
            //request.setSmsUpExtendCode("90997");

            //��ѡ:outIdΪ�ṩ��ҵ����չ�ֶ�,�����ڶ��Ż�ִ��Ϣ�н���ֵ���ظ�������
            //request.setOutId("yourOutId");

            //hint �˴����ܻ��׳��쳣��ע��catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);	//����ʧ�ܿ��ܾ��ǰ������˻�ûǮ��

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
        	email.setFrom("iym070010@163.com","�����˸��˼�ʱͨѶϵͳ");
        	email.setAuthentication("iym070010@163.com", "kg150520");
        	email.setSubject("����ʿ���˼�ʱͨѶϵͳע����֤");
        	email.setMsg("�𾴵��û�������ע���Ա��̬����Ϊ��"+code+"������й©�����ˣ�");
        	email.send();
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
    
    
	
}
