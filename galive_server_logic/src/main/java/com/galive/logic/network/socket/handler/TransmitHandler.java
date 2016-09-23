package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.TransmitPush;

@SocketRequestHandler(desc = "消息转发", command = Command.TRANSMIT)
public class TransmitHandler extends SocketBaseHandler {

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--MeetingTransmitHandler(消息转发)--");
		
		MeetingTransmitgIn in = JSON.parseObject(reqData, MeetingTransmitgIn.class);
		List<String> to = in.to;
		String content = in.content;
		
		TransmitPush push = new TransmitPush();
		push.content = content;
		push.sender = account;
		String pushContent = push.socketResp();
		appendLog("推送内容:" + pushContent);
		for (String s : to) {
			pushMessage(s, pushContent);
			appendLog("推送对象:" + s + " ");
		}
	
		CommandOut out = new CommandOut(Command.TRANSMIT);
		return out;
	}
	
	public class MeetingTransmitgIn {
		public List<String> to = new ArrayList<String>();
		public String content = "";
	}
	
}
