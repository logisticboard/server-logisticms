package com.example.logisticms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ConversationResponse {
	private UUID id;
	private ConversationType type;
	private String name;
	private String description;
	private String avatarUrl;
	private boolean active;
	private Instant lastMessageAt;
	private String lastMessagePreview;
	private Instant createdAt;
	private Instant updatedAt;
	private long unreadCount;
}
