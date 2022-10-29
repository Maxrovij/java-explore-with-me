package explorewithme.compilation.services;

import explorewithme.compilation.CompilationMapper;
import explorewithme.compilation.CompilationRepository;
import explorewithme.compilation.model.Compilation;
import explorewithme.compilation.model.CompilationDto;
import explorewithme.compilation.model.NewCompilationDto;
import explorewithme.event.EventRepository;
import explorewithme.event.model.Event;
import explorewithme.exception.EntityNotFoundException;
import explorewithme.exception.OperationForbiddenException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminCompilationService {

    private final CompilationRepository repository;

    private final EventRepository eventRepository;

    private final CompilationMapper mapper;

    public CompilationDto postNew(NewCompilationDto newDto) {
        List<Long> eventIds = newDto.getEvents();
        Set<Event> events = eventIds.stream().map(id -> {
                    Optional<Event> maybeEvent = eventRepository.findById(id);
                    return maybeEvent.orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Compilation compilation = mapper.map(newDto, Compilation.class);
        compilation.setEvents(events);
        return mapper.map(repository.save(compilation), CompilationDto.class);
    }

    public void deleteById(Long id) {
        if (repository.existsById(id)) repository.deleteById(id);
        else throw new EntityNotFoundException("Compilation not found!");
    }

    @Transactional
    public void deleteOrAddEvent(Long compId, Long eventId, boolean shouldDelete) {
        Optional<Compilation> maybeComp = repository.findById(compId);
        if (maybeComp.isEmpty()) throw new EntityNotFoundException("Compilation not found!");
        Optional<Event> maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) throw new EntityNotFoundException("Event not found!");

        Compilation compilation = maybeComp.get();
        Set<Event> compEvents = compilation.getEvents();

        if (shouldDelete) compEvents.remove(maybeEvent.get());
        else compEvents.add(maybeEvent.get());
        compilation.setEvents(compEvents);

        repository.save(compilation);
    }

    @Transactional
    public void pinOrUnpinComp(Long compId, boolean pined) {
        Optional<Compilation> maybeComp = repository.findById(compId);
        if (maybeComp.isEmpty()) throw new EntityNotFoundException("Compilation not found!");
        Compilation compilation = maybeComp.get();
        if (compilation.isPinned() == pined)
            throw new OperationForbiddenException("Compilation already pinned or unpinned!");
        compilation.setPinned(pined);
        repository.save(compilation);
    }

}
