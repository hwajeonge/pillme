package com.ssafy.pillme.chat.domain.entity;

import com.ssafy.pillme.auth.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="chat_room", uniqueConstraints = @UniqueConstraint(columnNames = {"send_user_id","receive_user_id"}))
@NoArgsConstructor
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="send_user_id", nullable = false)
    private Member sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receive_user_id", nullable = false)
    private Member receiveUser;

    public void updateChatRoom(Member sendUser, Member receiveUser){
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
    }
}