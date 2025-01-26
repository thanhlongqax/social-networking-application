package com.tdtu.chat_services.service;

import com.tdtu.chat_services.dto.JoinRoomMessage;
import com.tdtu.chat_services.dto.ResDTO;
import com.tdtu.chat_services.model.Room;

public interface IRoomService {
    public Room saveRoom(Room room);
    public Room findById(String id);
    public Room findExistingRoom(JoinRoomMessage message);
    public Room findExistingRoom(String fromUserId, String toUserId);
    public ResDTO<?> findRoomsByToken(String token);
    public boolean readAllUnreadMessages(String roomId);

}
