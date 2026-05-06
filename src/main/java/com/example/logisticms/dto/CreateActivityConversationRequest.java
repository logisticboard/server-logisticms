package com.example.logisticms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateActivityConversationRequest {

    private List<ParticipantInfo> participantIds;

    /**
     * Required for GROUP type
     */
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    private String avatarUrl;

    private ConversationType conversationType;

    @Builder
    @Data
    public static class ParticipantInfo {
        private UUID userId;
        private ParticipantRole role;
    }


}
