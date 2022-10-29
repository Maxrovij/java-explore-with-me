package explorewithme.event;

import explorewithme.tools.Client;
import explorewithme.tools.EndpointHit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class EventClient extends Client {

    @Value("${app-name}")
    private String appName;

    @Autowired
    public EventClient(@Value("${main-service.url}") String serverUrl, RestTemplateBuilder builder) {
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
        log.debug("EVENT CLIENT - send {} to stats-server", uri);
        post("/hit", new EndpointHit(appName, uri, ip));
    }
}
