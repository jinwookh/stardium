package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.Address;
import com.bb.stardium.bench.dto.RoomRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
class RoomServiceTest {

   @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @DisplayName("create method 성공")
    @Test
    public void createRoom() throws Exception {
        // given
        Address address = new Address("서울시", "송파구", "루터회관 앞");
        LocalDateTime startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        RoomRequestDto roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
        Room room = new Room(1L, "title", "intro", address, startTime, endTime, 10);
        given(roomRepository.save(any())).willReturn(room);

        // when
        Long roomNumber = roomService.create(roomRequest);

        // then
        assertThat(roomNumber).isEqualTo(1L);
    }

}