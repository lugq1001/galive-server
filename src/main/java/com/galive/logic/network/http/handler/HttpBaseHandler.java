package com.galive.logic.network.http.handler;

import com.galive.logic.network.protocol.CommandIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.exception.LogicException;

public abstract class HttpBaseHandler {
	
	private static Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);
	
	public abstract CommandOut handle(String account, String reqData) throws Exception;
	
	protected StringBuffer logBuffer = new StringBuffer();
	
	public String handle(CommandIn in) {
		appendSplit();
		long start = System.currentTimeMillis();
		CommandOut out = null;
		String command = in.getCommand();
		String account = in.getAccount();
		String token = in.getToken();
		String params = in.getParams();
		String tag = in.getTag();
		appendLog("请求参数:");
		appendLog("command:" + command);
		appendLog("account:" + account);
		appendLog("token:" + token);
		appendLog("params:" + params);
		
		try {
			out = handle(account, params);
		} catch (LogicException logicException) {
			logicException.printStackTrace();
			String error = logicException.getMessage();
			appendLog("逻辑错误:" + error);
			out = respFail(error, command);
		} catch (Exception exception) {
		 	exception.printStackTrace();
			String error = exception.getMessage();
			appendLog("发生错误:" + error);
			out = respFail(error, command);
		}
		out.setTag(tag);
		
		String resp = out.httpResp();
		appendLog("响应:");
		appendLog(resp);
		appendLog("处理时间:" + (System.currentTimeMillis() - start) + " ms");
		appendSplit();
		logger.info(loggerString());
		return resp;
	}
	
	private CommandOut respFail(String message, String command) {
		CommandOut resp = CommandOut.failureOut(command, message);
		return resp;
	}
	
	protected void appendLog(String log) {
		if (logBuffer != null) {
			logBuffer.append("|" + log);
			logBuffer.append("\n");
		}
	}
	
	protected void appendSplit() {
		if (logBuffer != null) {
			logBuffer.append("\n＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝\n");
		}
	}
	
	protected String loggerString() {
		return logBuffer == null ? "" : logBuffer.toString();
	}
}
