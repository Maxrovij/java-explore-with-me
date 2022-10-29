package explorewithme.compilation.services;

import explorewithme.compilation.CompilationMapper;
import explorewithme.compilation.CompilationRepository;
import explorewithme.compilation.model.Compilation;
import explorewithme.compilation.model.CompilationDto;
import explorewithme.exception.EntityNotFoundException;
import explorewithme.tools.PageCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
                .getPage(mapper.mapCompilationList(found, CompilationDto.class), from, size)
                .getContent();
    }

    public CompilationDto getById(Long compId) {
        Optional<Compilation> maybeComp = repository.findById(compId);
        if (maybeComp.isPresent())
            return mapper.map(maybeComp.get(), CompilationDto.class);
        else throw new EntityNotFoundException("Compilation not found");
    }
}
