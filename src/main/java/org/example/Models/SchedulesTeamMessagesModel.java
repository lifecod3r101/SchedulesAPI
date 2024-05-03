package org.example.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "schedulesteammembersmessages")
public class SchedulesTeamMessagesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "userid")
    String userId;
    @Column(name = "messageid")
    String messageId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
