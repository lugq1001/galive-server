package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LeaveRoomPush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "离开房间", command = Command.ROOM_LEAVE)
public class LeaveRoomHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--LeaveRoomHandler(离开房间)--");

		Room room = roomService.findRoom(FindRoomBy.Member, account);
		
		if (room != null) {
			roomService.leaveRoom(account);
			LeaveRoomPush push = new LeaveRoomPush();
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
		
		CommandOut out = new CommandOut(Command.ROOM_LEAVE_PUSH);
		return out;
	}
	
	
}
