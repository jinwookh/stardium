package com.bb.stardium.bench.controller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomApiController {

    private final RoomService roomService;

    public RoomApiController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/rooms")
    public ResponseEntity crate(RoomRequestDto roomRequest) {
        Long roomNumber = roomService.create(roomRequest);
        return ResponseEntity.ok().body(roomNumber);
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity update(@PathVariable Long roomId, RoomRequestDto roomRequestDto) {
        Long updatedRoomId = roomService.update(roomId, roomRequestDto);
        return ResponseEntity.ok().body(updatedRoomId);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity delete(@PathVariable Long roomId) {
        roomService.delete(roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity get(@PathVariable Long roomId) {
        Room room = roomService.findRoom(roomId);
        return ResponseEntity.ok(room);
    }
}
