package ru.practicum.explorewithme.compilation.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.compilation.services.PublicCompilationService;

import java.util.List;

@RestController
@RequestMapping("compilations")
@AllArgsConstructor
public class PublicCompilationController {
    private final PublicCompilationService service;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(defaultValue = "false") boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return service.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        return service.getById(compId);
    }
}
