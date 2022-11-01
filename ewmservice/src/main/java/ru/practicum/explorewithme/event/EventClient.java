package ru.practicum.explorewithme.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.tools.Client;
import ru.practicum.explorewithme.tools.EndpointHit;

import javax.servlet.http.HttpServletRequest;

@Service
public class EventClient extends Client {

    @Autowired
    public EventClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .build()
        );
    }

    public Object getViews(String uri) {
        return get("/hit?uri=" + uri).getBody();
    }

    public void addRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        post("/hit", new EndpointHit("main-service", uri, ip));
    }
}
