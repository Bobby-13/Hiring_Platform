package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.GoogleEventRequestDto;
import com.divum.hiring_platform.dto.GoogleEventResponseDto;
import com.divum.hiring_platform.repository.service.RedisService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GoogleMeetService{

    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    private final RedisService redisService;

    @Value("${spring.data.security.oauth2.client.registration.client-id}")
    private String clientId;

    @Value("${spring.data.security.oauth2.client.registration.client-secret}")
    private String clientSecret;

    public GoogleEventResponseDto googleMeetCreation(GoogleEventRequestDto googleEventRequestDto) throws IOException {
        Event event= createEvent(googleEventRequestDto);
        Event calenderEvent= null;
        calenderEvent = eventExecution(event);
        return GoogleEventResponseDto.builder()
                .eventId(calenderEvent.getId())
                .gmeetLink(calenderEvent.getHangoutLink())
                .build();

    }
    @SneakyThrows
    public GoogleEventResponseDto updateEvent(String eventId, @Email String email, GoogleEventRequestDto updateDto) {
        Calendar service = getCalendarService();
        Calendar.CalendarList calendarList = service.calendarList();
        Event existingEvent = service.events().get(email, eventId).execute();
        existingEvent.setSummary(updateDto.getSummary());
        EventDateTime start = new EventDateTime().setDateTime(new DateTime(updateDto.getStartTime()));
        EventDateTime end = new EventDateTime().setDateTime(new DateTime(updateDto.getEndTime()));
        existingEvent.setStart(start);
        existingEvent.setEnd(end);
        existingEvent.setDescription(updateDto.getDescription());
        existingEvent.setAttendees(new ArrayList<>());
        EventAttendee organizer = new EventAttendee().setEmail(updateDto.getOrganizerEmail()).setOrganizer(true);
        existingEvent.getAttendees().add(organizer);

        for (String attendeeEmail : updateDto.getAttendees()) {
            EventAttendee attendee = new EventAttendee().setEmail(attendeeEmail);
            existingEvent.getAttendees().add(attendee);
        }
        Event updatedEvent = service.events().update(email, eventId,existingEvent).execute();
        return GoogleEventResponseDto.builder()
                .eventId(updatedEvent.getId())
                .gmeetLink(updatedEvent.getHangoutLink())
                .build();
    }

    @SneakyThrows
    public void deleteEvent(String eventId, String email) {
        Calendar service = getCalendarService();
        service.events().delete(email, eventId).execute();
    }


    public Event createEvent(GoogleEventRequestDto eventRequestDto)
    {
        Event event = new Event();
        event.setSummary(eventRequestDto.getSummary());
        EventDateTime start = new EventDateTime().setDateTime(new DateTime(eventRequestDto.getStartTime()));
        EventDateTime end = new EventDateTime().setDateTime(new DateTime(eventRequestDto.getEndTime()));
        event.setStart(start);
        event.setEnd(end);
        event.setDescription(eventRequestDto.getDescription());
        event.setAttendees(new ArrayList<>());
        EventAttendee organizer = new EventAttendee().setEmail(eventRequestDto.getOrganizerEmail()).setOrganizer(true);
        event.getAttendees().add(organizer);
        for (String attendeeEmail : eventRequestDto.getAttendees()) {
            EventAttendee attendee = new EventAttendee().setEmail(attendeeEmail);
            event.getAttendees().add(attendee);
        }
        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest createRequest = new CreateConferenceRequest();
        createRequest.setRequestId("onMeet0942364HP");
        createRequest.setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"));
        conferenceData.setCreateRequest(createRequest);
        event.setConferenceData(conferenceData);
        return event;
    }
    public Event eventExecution(Event event) throws IOException {
        Calendar service = getCalendarService();
        Calendar.Events.Insert intermediate = service.events().insert("primary",event);
        intermediate.setConferenceDataVersion(1);
        return intermediate.execute();
    }
    public Calendar getCalendarService() throws UnsupportedEncodingException {
        if(!isAccessTokenValid(redisService.getAccessToken())){
            String newAccessToken = refreshAccessToken(redisService.getRefreshToken());
            redisService.addAccessToken(newAccessToken);
        }
        GoogleCredential credential = new GoogleCredential().setAccessToken(redisService.getAccessToken());
        return new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("GMeetGeneration")
                .build();
    }


    public boolean isAccessTokenValid(String accessToken) {
        accessToken = accessToken.trim();
        String endPoint = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(endPoint, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                JSONObject jsonResponse = (JSONObject) JSONValue.parse(responseBody);
                String expiresInString = jsonResponse.get("expires_in").toString();
                int expiresIn = Integer.parseInt(expiresInString);
                return expiresIn > 100;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public String refreshAccessToken(String refreshToken) throws UnsupportedEncodingException {
            String requestBody = "client_id="+clientId+
                    "&client_secret="+clientSecret+
                    "&grant_type=refresh_token"+
                    "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token", requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            JSONObject jsonObject = (JSONObject) JSONValue.parse(responseBody);
            return jsonObject.get("access_token").toString();
    }


}
