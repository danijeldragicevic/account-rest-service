package account.controller.impl;

import account.controller.IEventController;
import account.mapper.IModelMapper;
import account.model.Event;
import account.model.dto.EventDto;
import account.service.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventControllerImpl implements IEventController {
    private final IEventService eventService;
    private final IModelMapper mapper;

    @Override
    public ResponseEntity<List<EventDto>> getEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event: events) {
            eventDtos.add(mapper.mapToEventDto(event));
        }

        return ResponseEntity.ok(eventDtos);
    }
}
