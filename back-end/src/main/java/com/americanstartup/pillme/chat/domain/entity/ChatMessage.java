package com.americanstartup.pillme.chat.domain.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private String message;
    private Long timestamp;
    private boolean read;

    public void markAsRead(){
        this.read = true;
    }
}
