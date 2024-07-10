package org.example.Controllers;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.validation.Valid;
import org.example.Misc.AppStuff;
import org.example.Models.*;
import org.example.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class SchedulesRolesController {
    @Autowired
    SchedulesRolesRepository rolesRepository;

    @Autowired
    SchedulesTeamRepository teamRepository;

    @Autowired
    SchedulesMessageRepository messageRepository;

    @Autowired
    SchedulesTeamMessageRepository teamMessageRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Value("${twilio.property.account_sid}")
    String twilioSid;

    @Value("${twilio.property.auth_token}")
    String twilioAuthToken;

    @Value("${twilio.property.sending_phone_number}")
    String sendingPhoneNumber;
    AppStuff appStuff = new AppStuff();

    @PostMapping("/add")
    public ResponseEntity<SchedulesRolesModel> addRole(@Valid @RequestParam("roleName") String roleName, @ModelAttribute SchedulesRolesModel rolesModel, BindingResult bindingResult) {
        SchedulesRolesModel schedulesRolesModel = new SchedulesRolesModel();
        schedulesRolesModel.setRoleName(roleName);
        rolesRepository.save(schedulesRolesModel);
        return ResponseEntity.status(HttpStatus.OK).body(schedulesRolesModel);
    }

    @GetMapping("/find/{roleId}")
    public SchedulesRolesModel findRole(@PathVariable("roleId") String roleId) {
        return rolesRepository.findById(roleId).get();
    }

    @PostMapping("/assignRole")

    @PatchMapping("/update/{roleId}")
    public ResponseEntity<SchedulesRolesModel> updateRole(@PathVariable("roleId") String roleId, @RequestParam("roleName") String roleName) {
        SchedulesRolesModel rolesModel = rolesRepository.findById(roleId).get();
        rolesModel.setRoleName(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(rolesModel);
    }

    @DeleteMapping("/delete/{roleId}")
    public String removeRole(@PathVariable("roleId") String roleId) {
        rolesRepository.deleteById(roleId);
        return "Role deleted";
    }

    @PostMapping("/addTeamMembers")
    public ResponseEntity<SchedulesRolesModel> addTeamMemberToRole(@RequestParam("roleId") String roleId, @RequestParam("teamMemberId") String[] teamMemberIdList) {
        SchedulesRolesModel schedulesRolesModel = rolesRepository.findById(roleId).get();
        for (String teamMemberIdEntry : teamMemberIdList) {
            if (teamRepository.findById(teamMemberIdEntry).isPresent()) {
                SchedulesTeamModel schedulesTeamModel = teamRepository.findById(teamMemberIdEntry).get();
                schedulesRolesModel.getTeam().add(schedulesTeamModel);
            }
        }
        rolesRepository.save(schedulesRolesModel);
        return ResponseEntity.status(HttpStatus.OK).body(schedulesRolesModel);
    }


    @GetMapping("/getAll")
    public List<SchedulesRolesModel> getAllRoles() {
        List<SchedulesRolesModel> rolesModelsList = new ArrayList<>();
        for (SchedulesRolesModel model : rolesRepository.findAll()) {
            rolesModelsList.add(model);
        }
        return rolesModelsList;
    }

    @PostMapping("/buildSchedule")
    public ResponseEntity<ScheduleModel> createSchedule(@RequestParam("roleScheduleName") String roleScheduleName, @RequestParam("roleScheduleDateTime") String roleScheduleDateTime, @RequestParam("selectedRoleMembers") String[] desiredRoleMembers, @RequestParam("messageId") String messageId) {
        appStuff.initialiseTwilioService(twilioSid, twilioAuthToken);
        ScheduleModel scheduleModel = new ScheduleModel();
        scheduleModel.setRoleScheduleName(roleScheduleName);
        scheduleModel.setRoleScheduleDateTime(roleScheduleDateTime);
        ArrayList<Message> messagesList = new ArrayList<>();
        for (String recipientUserId : desiredRoleMembers) {
            if (teamRepository.findById(recipientUserId).isPresent() && messageRepository.findById(messageId).isPresent()) {
                String userPhoneNumber = teamRepository.findById(recipientUserId).get().getUserPhoneNumber();
                String sendingMessageContent = messageRepository.findById(messageId).get().getMessageContent();
                SchedulesTeamMessagesModel teamMessagesModel = new SchedulesTeamMessagesModel();
                teamMessagesModel.setMessageId(messageId);
                teamMessagesModel.setUserId(recipientUserId);
                teamMessageRepository.save(teamMessagesModel);
                Message message = Message.creator(new PhoneNumber("+".concat(userPhoneNumber)), new PhoneNumber("+".concat(sendingPhoneNumber)), sendingMessageContent).create();
                messagesList.add(message);
            }
        }
        scheduleRepository.save(scheduleModel);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleModel);
    }

    @PostMapping("/accept")
    public ResponseEntity<ScheduleModel> acceptScheduleRequest(@RequestParam("scheduleId") String scheduleId, @RequestParam("requestedUserId") String requestedUserId) {
        ScheduleModel scheduleModel = null;
        if (scheduleRepository.findById(scheduleId).isPresent() && teamRepository.findById(requestedUserId).isPresent()) {
            scheduleModel = scheduleRepository.findById(scheduleId).get();
            scheduleModel.getScheduleTeamMemberList().add(teamRepository.findById(requestedUserId).get());
            scheduleRepository.save(scheduleModel);
        }
        return ResponseEntity.status(HttpStatus.OK).body(scheduleModel);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = ((FieldError) error).getRejectedValue().toString();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}