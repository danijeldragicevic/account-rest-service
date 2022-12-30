package account.service;

import account.model.Event;

import java.util.List;

public interface IEventService {
    Event saveEvent(Event event);
    List<Event> saveAllEvents(List<Event> events);
    List<Event> getAllEvents();
}
