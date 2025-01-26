package com.tdtu.chat_services.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tdtu.chat_services.dto.JoinRoomMessage;
import com.tdtu.chat_services.dto.SendMessage;
import com.tdtu.chat_services.model.Message;
import com.tdtu.chat_services.model.Room;

public interface ISocketService {
    public void sendSocketMessage(SocketIOClient senderClient, Room room, Message message);

    public void saveMessage(SocketIOClient senderClient, SendMessage messageDto);
    public void joinRoom(SocketIOClient senderClient, JoinRoomMessage message);


}
