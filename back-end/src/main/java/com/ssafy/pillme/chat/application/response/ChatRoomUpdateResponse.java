package com.ssafy.pillme.chat.application.response;

public record ChatRoomUpdateResponse
        (Long chatRoomId, int unreadCount, Long lastMessageTime, String lastMessage) { }
