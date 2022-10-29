package explorewithme;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsService {
    private final StatsRepository repository;
    private final ModelMapper mapper;

    public void saveHit(HitDto hitDto) {
        repository.save(mapper.map(hitDto, EndpointHit.class));
    }

    public Integer countViewsByUri(String uri) {
        return repository.countByUri(uri);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> viewStats;
        if (unique)
            viewStats = repository.getUniqueViews(start, end);
        else
            viewStats = repository.getNotUniqueViews(start, end);

        return uris == null ? viewStats : viewStats.stream()
                .map(viewStatsObj -> filterByUris(viewStatsObj, uris))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private ViewStats filterByUris(ViewStats v, List<String> uris) {
        if (uris.contains(v.uri)) return v;
        else return null;
    }
}
