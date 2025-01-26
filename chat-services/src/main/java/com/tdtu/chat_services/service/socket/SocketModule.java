package com.tdtu.chat_services.service.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.tdtu.chat_services.dto.JoinRoomMessage;
import com.tdtu.chat_services.dto.SendMessage;
import com.tdtu.chat_services.service.ISocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketModule {
    private final SocketIOServer server;
    private final ISocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService){
        this.server = server;
        this.socketService = socketService;

        server.addConnectListener(onConnected());
        server.addEventListener("send_message", SendMessage.class, onMessageReceived());
        server.addEventListener("join_room", JoinRoomMessage.class, onJoinRoom());
    }

    public ConnectListener onConnected(){
        return new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                log.info("Socket ID[{}] Connected to chat module through", client.getSessionId().toString());
            }
        };
    }

    public DataListener<SendMessage> onMessageReceived(){
        return new DataListener<SendMessage>() {
            @Override
            public void onData(SocketIOClient socketIOClient, SendMessage sendMessage, AckRequest ackRequest) throws Exception {
                log.info(sendMessage.toString());
                socketService.saveMessage(socketIOClient, sendMessage);
            }
        };
    }

    public DataListener<JoinRoomMessage> onJoinRoom(){
        return new DataListener<JoinRoomMessage>() {
            @Override
            public void onData(SocketIOClient client, JoinRoomMessage joinRoomMessage, AckRequest ackRequest) throws Exception {
                socketService.joinRoom(client, joinRoomMessage);
            }
        };
    }
}
