package com.tdtu.chat_services.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.tdtu.chat_services.dto.JoinRoomMessage;
import com.tdtu.chat_services.dto.MessageNoti;
import com.tdtu.chat_services.dto.SendMessage;
import com.tdtu.chat_services.mapper.RoomResponseMapper;
import com.tdtu.chat_services.model.Message;
import com.tdtu.chat_services.model.Room;
import com.tdtu.chat_services.producer.SendKafkaMsgService;
import com.tdtu.chat_services.service.IRoomService;
import com.tdtu.chat_services.service.ISocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketService implements ISocketService {
    private final IRoomService roomService;
    private final SendKafkaMsgService kafkaMsgService;

    public void sendSocketMessage(SocketIOClient senderClient, Room room, Message message){
        senderClient.getNamespace()
                .getRoomOperations(room.getId())
                .getClients()
                .forEach(client -> {
                    if(!client.getSessionId().equals(senderClient.getSessionId())){
                        client.sendEvent("receive_message", message);
                    }
                });
    }

    private void sendRoomJoinedMessage(SocketIOClient senderClient, String fromUserId, Room room){
        senderClient.getNamespace()
                .getRoomOperations(room.getId())
                .getClients()
                .forEach(client -> {
                    if(client.getSessionId().equals(senderClient.getSessionId())){
                        Map<String, Object> data = new HashMap<>();
                        data.put("roomId", room.getId());
                        data.put("messages",
                                room.getMessages().stream().map(
                                        msg -> RoomResponseMapper.mapMsgToMsgResponse(fromUserId, msg)
                                ).toList()
                        );

                        client.sendEvent("joined", data);
                    }
                });
    }

    public void saveMessage(SocketIOClient senderClient, SendMessage messageDto){
        Message newMessage = Message.builder()
                .id(UUID.randomUUID().toString())
                .content(messageDto.getContent())
                .createdAt(new Date())
                .fromUserId(messageDto.getFromUserId())
                .toUserId(messageDto.getToUserId())
                .read(false)
                .imageUrls(messageDto.getImageUrls())
                .build();

//        Room foundRoom = roomService.findById(messageDto.getRoomId());
        Room foundRoom = roomService.findExistingRoom(messageDto.getFromUserId(), messageDto.getToUserId());

        if(
                foundRoom != null
//                        &&
//                ((foundRoom.getUserId1().equals(messageDto.getToUserId()) ||
//                        foundRoom.getUserId2().equals(messageDto.getToUserId())) &&
//                (foundRoom.getUserId1().equals(messageDto.getFromUserId()) ||
//                        foundRoom.getUserId2().equals(messageDto.getFromUserId())))
        ) {
            foundRoom.getMessages().add(newMessage);
            roomService.saveRoom(foundRoom);
            kafkaMsgService.publishMessageNoti(new MessageNoti(newMessage));
            sendSocketMessage(senderClient, foundRoom, newMessage);
        }
    }

    public void joinRoom(SocketIOClient senderClient, JoinRoomMessage message){
        Room room = roomService.findExistingRoom(message);
        if(room == null){
            room = roomService.saveRoom(Room.builder()
                    .userId1(message.getFromUserId())
                    .userId2(message.getToUserId())
                    .messages(new ArrayList<>())
                    .createdAt(new Date())
                    .build());
        }

        roomService.readAllUnreadMessages(room.getId());

        log.info("Socket ID[{}] Connected to room [{}]", senderClient.getSessionId().toString(), room.getId());

        senderClient.joinRoom(room.getId());
        sendRoomJoinedMessage(senderClient, message.getFromUserId(), room);
    }
}
