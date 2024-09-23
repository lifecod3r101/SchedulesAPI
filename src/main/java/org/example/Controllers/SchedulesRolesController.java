package org.example.Controllers;

import com.infobip.ApiCallback;
import com.infobip.ApiException;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.Misc.AppStuff;
import org.example.Models.*;
import org.example.Repositories.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.MessageFormat;
import java.util.*;

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

    @Value("${infobip.property.api_key}")
    String infobipApiKey;

    @Value("${infobip.property.base_url}")
    String infobipBaseUrl;


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

    @GetMapping("/getAllSchedules")
    public ResponseEntity<List<ScheduleModel>> getAllSchedules() {
        List<ScheduleModel> schedulesList = new ArrayList<>();
        for (ScheduleModel model : scheduleRepository.findAll()) {
            schedulesList.add(model);
        }
        return ResponseEntity.status(HttpStatus.OK).body(schedulesList);
    }

    public String getAppBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }

    @PostMapping("/buildSchedule")
    public ResponseEntity<ScheduleModel> createSchedule(@RequestParam("roleScheduleName") String roleScheduleName, @RequestParam("roleScheduleDateTime") String roleScheduleDateTime, @RequestParam("selectedRoleMembers") String[] desiredRoleMembers, @RequestParam("messageId") String messageId) {

        for (String recipientUserId : desiredRoleMembers) {
            System.out.println(recipientUserId.substring(recipientUserId.indexOf(";") + 1));
        }
        SmsApi smsApi = new SmsApi(appStuff.initialiseInfobipService(infobipApiKey, infobipBaseUrl));
        ScheduleModel scheduleModel = new ScheduleModel();
        scheduleModel.setRoleScheduleName(roleScheduleName);
        scheduleModel.setRoleScheduleDateTime(roleScheduleDateTime);
        String nameTarget = "{{name}}";
        String roleTarget = "{{role}}";
        String dateTarget = "{{date}}";
        String sendingMessageContent = messageRepository.findById(messageId).get().getMessageContent();
        for (String recipientUserId : desiredRoleMembers) {
            if (teamRepository.findById(recipientUserId).isPresent() && messageRepository.findById(messageId).isPresent()) {
                SchedulesTeamMessagesModel teamMessagesModel = new SchedulesTeamMessagesModel();
                teamMessagesModel.setMessageId(messageId);
                teamMessagesModel.setUserId(recipientUserId.substring(0, recipientUserId.indexOf(";")));
                teamMessageRepository.save(teamMessagesModel);
            }
        }
        scheduleRepository.save(scheduleModel);
        for (String recipientUserId : desiredRoleMembers) {
            if (teamRepository.findById(recipientUserId.substring(0, recipientUserId.indexOf(";"))).isPresent() && messageRepository.findById(messageId).isPresent() && rolesRepository.findById(recipientUserId.substring(recipientUserId.indexOf(";") + 1)).isPresent()) {
                SchedulesTeamModel schedulesTeamModel = teamRepository.findById(recipientUserId.substring(0, recipientUserId.indexOf(";"))).get();
                SmsDestination userDestination = new SmsDestination();
                String userPhoneNumber = teamRepository.findById(recipientUserId.substring(0, recipientUserId.indexOf(";"))).get().getUserPhoneNumber();
                userDestination.setTo(userPhoneNumber);
                String finalNameReplaced = sendingMessageContent.replace(nameTarget, teamRepository.findById(recipientUserId.substring(0, recipientUserId.indexOf(";"))).get().getUserName());
                String finalDateReplaced = finalNameReplaced.replace(dateTarget, scheduleModel.getRoleScheduleDateTime());
                String finalRoleReplaced = finalDateReplaced.replace(roleTarget, rolesRepository.findById(recipientUserId.substring(recipientUserId.indexOf(";") + 1)).get().getRoleName());
                SmsTextualMessage smsMessage = new SmsTextualMessage().from("TeamDream").addDestinationsItem(new SmsDestination().to(userPhoneNumber)).text(finalRoleReplaced);
                SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                        .messages(List.of(smsMessage));
                smsApi.sendSmsMessage(smsMessageRequest).executeAsync(new ApiCallback<>() {
                    @Override
                    public void onSuccess(SmsResponse result, int responseStatusCode, Map<String, List<String>> responseHeaders) {
                        System.out.println("Message Sent");
                    }

                    @Override
                    public void onFailure(ApiException exception, int responseStatusCode, Map<String, List<String>> responseHeaders) {
                        System.out.println("Message Failed to be Sent");
                    }
                });
                acceptScheduleRequest(scheduleModel.getRoleScheduleId(), recipientUserId.substring(0, recipientUserId.indexOf(";")), recipientUserId.substring(recipientUserId.indexOf(";") + 1));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(scheduleModel);
    }

    //    @PostMapping("/accept")
    public void /*ResponseEntity<ScheduleModel>*/ acceptScheduleRequest(/*@RequestParam("scheduleId") */String scheduleId, /*@RequestParam("requestedUserId") */String requestedUserId, String roleId) {
        ScheduleModel scheduleModel = null;
        List<SchedulesTeamModel> allMembers = new ArrayList<>();
        Map<String, Object> acceptedScheduleModel = new HashMap<>();
        if (scheduleRepository.findById(scheduleId).isPresent() && teamRepository.findById(requestedUserId).isPresent()) {
            scheduleModel = scheduleRepository.findById(scheduleId).get();
            allMembers.add(teamRepository.findById(requestedUserId).get());
            JSONArray allPeopleArray = new JSONArray();
            if (scheduleModel.getRoleSchedulePeople() != null) {
                JSONArray originalPeopleArray = new JSONArray(scheduleModel.getRoleSchedulePeople());
                acceptedScheduleModel.put("acceptedUserId", requestedUserId);
                acceptedScheduleModel.put("selectedRoleId", roleId);
                JSONObject newPersonObject = new JSONObject(acceptedScheduleModel);
                originalPeopleArray.put(newPersonObject);
                String finalScheduleObject = originalPeopleArray.toString();
                scheduleModel.setRoleSchedulePeople(finalScheduleObject);
            } else {
                acceptedScheduleModel.put("acceptedUserId", requestedUserId);
                acceptedScheduleModel.put("selectedRoleId", roleId);
                JSONObject scheduleObject = new JSONObject(acceptedScheduleModel);
                allPeopleArray.put(scheduleObject);
                String finalScheduleObject = allPeopleArray.toString();
                scheduleModel.setRoleSchedulePeople(finalScheduleObject);
            }
            scheduleRepository.save(scheduleModel);
        }
        assert scheduleModel != null;
        scheduleModel.setScheduleTeamMemberList(allMembers);
//        return ResponseEntity.status(HttpStatus.OK).body(scheduleModel);
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