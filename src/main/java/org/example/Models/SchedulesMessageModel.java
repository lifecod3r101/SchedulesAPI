package org.example.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedulesmessages")
public class SchedulesMessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String messageId;

    @Column(name = "messagetitle")
    @NotBlank
    private String messageTitle;
    @Column(name = "messagecontent")
    @NotBlank
    private String messageContent;

    @JsonIgnoreProperties({"messageHistory"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedulesteammembersmessages",
            joinColumns = @JoinColumn(name = "messageid"),
            inverseJoinColumns = @JoinColumn(name = "userid"))
    List<SchedulesTeamModel> messageRecipientHistory = new ArrayList<>();

    public List<SchedulesTeamModel> getMessageRecipientHistory() {
        return messageRecipientHistory;
    }

    public void setMessageRecipientHistory(List<SchedulesTeamModel> messageRecipientHistory) {
        this.messageRecipientHistory = messageRecipientHistory;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
