package org.example.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "roleschedule")
public class ScheduleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rolescheduleid")
    String roleScheduleId;
    @Column(name = "roleschedulename")
    String roleScheduleName;
    @Column(name = "rolescheduledate")
    String roleScheduleDateTime;
    @Column(name = "roleschedulepeople")
    String roleSchedulePeople;
    @CreationTimestamp
    Instant roleScheduleCreateTime;
    @UpdateTimestamp
    Instant roleScheduleUpdateTime;

    @JsonIgnoreProperties({"messageHistory", "roles", "scheduleHistoryList"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedulesrolemembers",
            inverseJoinColumns = @JoinColumn(name = "rolescheduleid"),
            joinColumns = @JoinColumn(name = "userid"))
    List<SchedulesTeamModel> scheduleTeamMemberList;

    public String getRoleSchedulePeople() {
        return roleSchedulePeople;
    }

    public void setRoleSchedulePeople(String roleSchedulePeople) {
        this.roleSchedulePeople = roleSchedulePeople;
    }

    public ScheduleModel() {
    }

    public ScheduleModel(String roleScheduleId, String roleScheduleName, String roleScheduleDateTime, Instant roleScheduleCreateTime, Instant roleScheduleUpdateTime, List<SchedulesTeamModel> scheduleTeamMemberList) {
        this.roleScheduleId = roleScheduleId;
        this.roleScheduleName = roleScheduleName;
        this.roleScheduleDateTime = roleScheduleDateTime;
        this.roleScheduleCreateTime = roleScheduleCreateTime;
        this.roleScheduleUpdateTime = roleScheduleUpdateTime;
        this.scheduleTeamMemberList = scheduleTeamMemberList;
    }

    public String getRoleScheduleId() {
        return roleScheduleId;
    }

    public void setRoleScheduleId(String roleScheduleId) {
        this.roleScheduleId = roleScheduleId;
    }

    public String getRoleScheduleName() {
        return roleScheduleName;
    }

    public void setRoleScheduleName(String roleScheduleName) {
        this.roleScheduleName = roleScheduleName;
    }

    public String getRoleScheduleDateTime() {
        return roleScheduleDateTime;
    }

    public void setRoleScheduleDateTime(String roleScheduleDateTime) {
        this.roleScheduleDateTime = roleScheduleDateTime;
    }

    public Instant getRoleScheduleCreateTime() {
        return roleScheduleCreateTime;
    }

    public void setRoleScheduleCreateTime(Instant roleScheduleCreateTime) {
        this.roleScheduleCreateTime = roleScheduleCreateTime;
    }

    public Instant getRoleScheduleUpdateTime() {
        return roleScheduleUpdateTime;
    }

    public void setRoleScheduleUpdateTime(Instant roleScheduleUpdateTime) {
        this.roleScheduleUpdateTime = roleScheduleUpdateTime;
    }

    public List<SchedulesTeamModel> getScheduleTeamMemberList() {
        return scheduleTeamMemberList;
    }

    public void setScheduleTeamMemberList(List<SchedulesTeamModel> scheduleTeamMemberList) {
        this.scheduleTeamMemberList = scheduleTeamMemberList;
    }
}
