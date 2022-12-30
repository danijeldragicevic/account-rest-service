package account.service.impl;

import account.model.Event;
import account.repository.IEventRepository;
import account.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EventServiceImpl implements IEventService {
    @Autowired
    private IEventRepository repository;

    public EventServiceImpl() {

    }

    @Transactional
    @Override
    public Event saveEvent(Event event) {
        return repository.save(event);
    }

    @Transactional
    public List<Event> saveAllEvents(List<Event> events) {
        return repository.saveAll(events);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = repository.findAll();

        return events;
    }
}
