package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.dto.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder @Getter
public class Room {

    @Id @GeneratedValue
    @Column(name="room_id")
    private Long id;

    private String title;

    private String intro;

    @Embedded
    private Address address;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int playersLimit;

}
