package com.galive.logic.network.socket.handler;

import java.util.List;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.ScreenShareStopPush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "结束分享", command = Command.SCREEN_SHARE_STOP)
public class ShareStoptHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ShareStoptHandler(结束分享)--");

		roomService.updateScreenShareState(account, false);

		Room room = roomService.findRoom(FindRoomBy.Owner, account);
		
		if (room != null) {
			ScreenShareStopPush push = new ScreenShareStopPush();
			push.accountSid = account;
			String pushContent = push.socketResp();
			List<String> members = room.getMembers();
			for (String m : members) {
				if (!m.equals(account)) {
					pushMessage(m, pushContent);
					appendLog("推送房间内成员:" + m + " " + pushContent);
				}
			}
		}
		
		
		CommandOut out = new CommandOut(Command.SCREEN_SHARE_STOP);
		return out;

	}
}
