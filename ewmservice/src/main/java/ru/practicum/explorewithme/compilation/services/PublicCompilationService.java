package ru.practicum.explorewithme.compilation.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.CompilationMapper;
import ru.practicum.explorewithme.compilation.CompilationRepository;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;
import ru.practicum.explorewithme.tools.PageCreator;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicCompilationService {

    private final CompilationRepository repository;
    private final PageCreator<CompilationDto> pageCreator;
    private final CompilationMapper mapper;

    public List<CompilationDto> getAll(boolean pinned, int from, int size) {
        List<Compilation> found = repository.findAllByPinned(pinned);
        return pageCreator
                .getPage(mapper.mapCompilationList(found), from, size)
                .getContent();
    }

    public CompilationDto getById(Long compId) {
        Optional<Compilation> maybeComp = repository.findById(compId);
        if (maybeComp.isPresent())
            return mapper.mapToDto(maybeComp.get());
        else throw new EntityNotFoundException("Compilation not found");
    }
}
