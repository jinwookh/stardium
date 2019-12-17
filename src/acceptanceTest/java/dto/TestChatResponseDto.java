package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestChatResponseDto {
    private Long roomId;
    private String nickname;

    @JsonProperty("message")
    private String contents;
    private String timestamp;
}
