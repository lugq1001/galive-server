package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;



public interface RoomService {

	public enum FindRoomBy {
		Owner,
		Member;
	}

	public Room findRoom(FindRoomBy by, String byId) throws LogicException;
	
	public Room createRoom(String accountSid) throws LogicException;
	
	public Room joinRoom(String roomSid, String accountSid) throws LogicException;
	
	public Room leaveRoom(String accountSid) throws LogicException;
	
	public Room destroyRoom(String accountSid) throws LogicException;
	
	/**
	 * 更新屏幕分享状态
	 * @param accountSid
	 * @param started
	 * @throws Exception
	 */
	public void updateScreenShareState(String accountSid, boolean started) throws Exception; 
	
	/**
	 * 查找屏幕分享状态
	 * @param accountSid
	 * @return
	 * @throws Exception
	 */
	public boolean findScreenShareState(String accountSid) throws Exception; 
}
