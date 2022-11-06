package ru.practicum.explorewithme.compilation.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.CompilationDto;
import ru.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.practicum.explorewithme.compilation.services.AdminCompilationService;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class AdminCompilationController {

    private final AdminCompilationService service;

    @PostMapping
    public CompilationDto post(@RequestBody NewCompilationDto newDto) {
        return service.postNew(newDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable Long compId) {
        service.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compId,
                            @PathVariable Long eventId) {
        service.deleteOrAddEvent(compId, eventId, true);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void deleteOrAddEvent(@PathVariable Long compId,
                                 @PathVariable Long eventId) {
        service.deleteOrAddEvent(compId, eventId, false);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpin(@PathVariable Long compId) {
        service.pinOrUnpinComp(compId, false);
    }

    @PatchMapping("/{compId}/pin")
    public void pin(@PathVariable Long compId) {
        service.pinOrUnpinComp(compId, true);
    }
}
