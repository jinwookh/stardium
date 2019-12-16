import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.chat.dto.ChatMessageResponseDto;
import com.bb.stardium.player.dto.PlayerRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.bb.stardium.StardiumApplication.class)
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ChatTest extends BaseAcceptanceTest {

    private static final int TIME_LIMIT = 5;
    private static final long ROOM_ID = 1L;
    private static final String SOCKET_SEND_URL = "/chat/";
    private static final String SOCKET_SUBSCRIBE_URL = "/subscribe/chat/";

    @LocalServerPort
    int port;

    private WebSocketStompClient webSocketStompClient;
    private String socketConnectUrl;
    private CompletableFuture<ChatMessageResponseDto> completableFuture;

    @BeforeEach
    void setup() {
        socketConnectUrl = "ws://localhost:" + port + "/stomp-connect";

        webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        PlayerRequestDto playerRequestDto = new PlayerRequestDto("test", "create@room", "password", "");
        createPlayer(playerRequestDto);

        completableFuture = new CompletableFuture<>();
    }

    @Test
    @DisplayName("소켓에 메세지 수신되는지 확인")
    public void 소켓에_메시지_수신() throws ExecutionException, InterruptedException, TimeoutException {
        StompSession stompSession =
                webSocketStompClient.connect(socketConnectUrl,
                        new StompSessionHandlerAdapter() {
                        }).get(TIME_LIMIT, TimeUnit.SECONDS);

        stompSession.subscribe(SOCKET_SUBSCRIBE_URL + ROOM_ID, getStompFrameHandler());

        ChatMessageRequestDto chatMessageRequestDto =
                new ChatMessageRequestDto(ROOM_ID, 1L, "contents");

        stompSession.send(SOCKET_SEND_URL + ROOM_ID, chatMessageRequestDto);

        ChatMessageResponseDto futureResponse = completableFuture.get(TIME_LIMIT, SECONDS);

        assertThat(chatMessageRequestDto.getContents())
                .isEqualTo(futureResponse.getMessage());

    }

    private StompFrameHandler getStompFrameHandler() {
        return new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageResponseDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete((ChatMessageResponseDto) payload);
            }
        };
    }

}
