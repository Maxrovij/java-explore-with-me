package ru.practicum.explorewithme.compilation.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.CompilationMapper;
import ru.practicum.explorewithme.compilation.CompilationRepository;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;
import ru.practicum.explorewithme.exceptions.OperationForbiddenException;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AdminCompilationService {

    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    public CompilationDto postNew(NewCompilationDto newDto) {
        log.info("NewCompilationDto: \n " +
                "eventIds: " + newDto.getEvents());
        Compilation compilation = mapper.mapNewCompilationDtoToCompilation(newDto);
        log.info("Compilation: \n" +
                "id: " + compilation.getId() + "\n events: " +
                "\n    " + Arrays.toString(compilation.getEvents().toArray()));
        return mapper.mapToDto(repository.save(compilation));
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

