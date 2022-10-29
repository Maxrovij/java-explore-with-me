package explorewithme.tools;

import explorewithme.event.model.Event;
import explorewithme.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public class EventFilter {
    public static Event filterByEndDate(Event event, LocalDateTime endDate) {
        if (event == null) return null;
        if (endDate == null) return event;
        else if (event.getEventDate().isBefore(endDate)) return event;
        else return null;
    }

    public static Event filterByStartDate(Event event, LocalDateTime start) {
        if (event == null) return null;
        if (start == null) return event;
        if (event.getEventDate().isAfter(start)) return event;
        else return null;
    }

    public static Event filterByAvailable(Event event, boolean available) {
        if (event == null) return null;
        if (available) {
            if (event.getRequests().size() < event.getParticipantLimit()) return event;
            else return null;
        } else return event;
    }

    public static Event filterByCatId(Event event, List<Long> catIds) {
        if (event == null) return null;
        if (catIds.contains(event.getCategoryId())) return event;
        else return null;
    }

    public static Event filterByText(Event event, String text) {
        if (event == null) return null;
        if (text == null) return event;
        String searchText = text.toLowerCase().trim();
        String annotation = event.getAnnotation().toLowerCase().trim();
        String description = event.getDescription().toLowerCase().trim();
        if (annotation.contains(searchText) || description.contains(searchText)) return event;
        else return null;
    }

    public static Event filterByStates(Event event, List<EventState> states) {
        if (states.contains(event.getState())) return event;
        else return null;
    }
}