package ru.practicum.explorewithme.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    Integer countByUri(String uri);

    @Query(value = "select app as app, uri as uri, count(app) as hits from stats " +
            "where hit_time > ?1 and hit_time < ?2 group by app, uri", nativeQuery = true)
    List<ViewStats> getNotUniqueViews(LocalDateTime start, LocalDateTime end);

    @Query(value = "select  app as app, uri as uri, count(app) as hits from stats " +
            "where hit_time > ?1 and hit_time < ?2 group by app, uri, ip", nativeQuery = true)
    List<ViewStats> getUniqueViews(LocalDateTime start, LocalDateTime end);
}
