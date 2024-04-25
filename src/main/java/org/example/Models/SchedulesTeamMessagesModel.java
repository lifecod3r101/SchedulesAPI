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
}
