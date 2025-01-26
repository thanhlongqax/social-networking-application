package com.tdtu.chat_services.service.Impl;


import com.tdtu.chat_services.dto.JoinRoomMessage;
import com.tdtu.chat_services.dto.ResDTO;
import com.tdtu.chat_services.dto.RoomResponse;
import com.tdtu.chat_services.mapper.RoomResponseMapper;
import com.tdtu.chat_services.model.Room;
import com.tdtu.chat_services.repository.RoomRepository;
import com.tdtu.chat_services.service.IRoomService;
import com.tdtu.chat_services.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RoomRepository roomRepository;
    private final JwtUtils jwtUtils;
    private final RoomResponseMapper roomResponseMapper;

    public Room saveRoom(Room room){
        return roomRepository.save(room);
    }

    public Room findById(String id){
        return roomRepository.findById(id).orElse(null);
    }

    public Room findExistingRoom(JoinRoomMessage message){
        return roomRepository.findByUserId1AndUserId2OrUserId2AndUserId1(message.getFromUserId(), message.getToUserId(), message.getFromUserId(), message.getToUserId())
                .orElse(null);
    }

    public Room findExistingRoom(String fromUserId, String toUserId){
        return roomRepository.findByUserId1AndUserId2OrUserId2AndUserId1(fromUserId, toUserId, fromUserId, toUserId)
                .orElse(null);
    }

    public ResDTO<?> findRoomsByToken(String token){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        List<Room> rooms = roomRepository.findByUserId1OrUserId2(userId, userId);

        ResDTO<List<RoomResponse>> response = new ResDTO<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(rooms.stream()
                .map(room -> roomResponseMapper.mapToDTO(userId, room))
                .sorted((room1, room2) -> room2.getLatestMessage().getCreatedAt().compareTo(room1.getLatestMessage().getCreatedAt()))
                .toList()
        );

        return response;
    }

    public boolean readAllUnreadMessages(String roomId){

//        AtomicBoolean success = new AtomicBoolean(false);
//        roomRepository.findById(roomId).ifPresentOrElse(
//                room -> {
//                    room.getMessages().forEach(msg -> {
//                        msg.setRead(true);
//                    });
//
//                    roomRepository.save(room);
//
//                    success.set(true);
//                }, () -> {
//                    success.set(false);
//                }
//        );
//
//        return success.get();

        return true;
    }
}
