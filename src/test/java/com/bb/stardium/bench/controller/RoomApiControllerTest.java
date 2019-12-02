package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.dto.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class RoomApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("방 만들기 성공 테스트")
    @Test
    void createRoomTest() {
        Address address = new Address("서울시", "송파구", "루터회관 앞");
        LocalDateTime startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        RoomRequestDto roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);

        webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 정보 수정 성공 테스트")
    @Test
    void updateRoom() {
        Address address = new Address("서울시", "송파구", "루터회관 앞");
        LocalDateTime startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        RoomRequestDto originRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle", "updatedIntro", address, startTime, endTime, 5);

        createRoom(originRequest);

        webTestClient.put().uri("/rooms/1")
                .body(Mono.just(updateRequest), RoomRequestDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("방 삭제 성공 테스트")
    @Test
    void deleteRoomTest() {
        Address address = new Address("서울시", "송파구", "루터회관 앞");
        LocalDateTime startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        RoomRequestDto roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);

        createRoom(roomRequest);

        webTestClient.delete().uri("/rooms/1")
                .exchange()
                .expectStatus()
                .isOk();
    }

    private void createRoom(RoomRequestDto roomRequest) {
        webTestClient.post().uri("/rooms")
                .body(Mono.just(roomRequest), RoomRequestDto.class)
                .exchange();
    }
}
