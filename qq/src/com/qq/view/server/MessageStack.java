package com.qq.view.server;

import java.util.HashMap;
import java.util.LinkedList;

import com.qq.view.Chat;
import com.qq.view.FaceJPanel;
import com.qq.view.Msg;
import com.qq.view.util.Config;

import net.sf.json.JSONObject;

/**
 * 	消息栈 - 会存储所有的消息
 * @author 黄龙士
 *
 */

public class MessageStack {		//与在线队列一致 饿汉式单例实现
	
	private MessageStack() {}
	
	private static MessageStack messageStack = new MessageStack();
	
	public static MessageStack getMessageStack() {
		return messageStack;
	}
	
	public static HashMap<String, LinkedList<Msg>> hashMap = new HashMap();
	
	// 不管是给谁的消息都存储起来
	public void addMessage(String json) {
		// {"msg":"123456","code":"1574426820174","toUID":"10002","myUID":"10001","type":"msg"}
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		String toUID = jsonObject.getString("toUID");
		String myUID = jsonObject.getString("myUID");
		String msg = jsonObject.getString("msg");
		String code = jsonObject.getString("code");
		String type = jsonObject.getString("type");		
		
		// 把消息储存在Msg中
		Msg msgObj = new Msg(); 
		msgObj.setCode(code);
		msgObj.setMsg(msg);
		msgObj.setMyUID(myUID);
		msgObj.setToUID(toUID);
		msgObj.setType(type);
		
		try {
			Chat chat = Config.chatTab.get(myUID);
			if (chat.isVisible()) {
				chat.addMessage(msgObj);
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			
			
			FaceJPanel faceJPanel = Config.list.get(myUID);		//当窗口没打开时这样
			faceJPanel.addMessage(msgObj);
			
			//Msg列表
//			LinkedList<Msg> list = hashMap.get(myUID);	//当我窗口打开时这样
//			if (list == null) {
//				list = new LinkedList<Msg>();
//			}
//			
//			list.add(msgObj);
//			hashMap.put(myUID, list);
			
		}
	}
	
}
