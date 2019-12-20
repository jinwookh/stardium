package com.bb.stardium.chat.dto;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.common.util.EscapedCharacters;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponseDto {
    private Long roomId;
    private String nickname;
    private String message;

    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private OffsetDateTime timestamp;

    public ChatMessageResponseDto(final ChatMessage message) {
        this.roomId = message.getRoomId();
        this.nickname = EscapedCharacters.of(message.getPlayerNickname());
        this.message = EscapedCharacters.of(message.getContents());
        this.timestamp = message.getTimestamp();
    }
}
