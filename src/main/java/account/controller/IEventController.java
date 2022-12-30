package account.controller;

import account.model.dto.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/security")
public interface IEventController {
    @GetMapping("/events")
    ResponseEntity<List<EventDto>> getEvents();
}
