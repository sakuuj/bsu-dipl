package by.bsu.fpmi.apigateway.route;

import by.bsu.fpmi.apigateway.filter.EncodeRequestParametersFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.nio.charset.StandardCharsets;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class Routes {

    @Value("${bsu.cfg-first-k-service.name}")
    private String cfgFirstKServiceName;

    @Value("${bsu.discovery-server.uri}")
    private String eurekaServerURI;

    @Bean
    public RouterFunction<ServerResponse> newsSerivice() {
        return route("news-serivice")
                .route(path("/api/first-k/**"), http())
                .before(EncodeRequestParametersFilter.encodeRequestParameters(StandardCharsets.UTF_8))
                .filter(lb(cfgFirstKServiceName))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> eurekaServer() {
        return route("eureka-server")
                .route(path("/eureka/web/**"), http(eurekaServerURI))
                .before(BeforeFilterFunctions.stripPrefix(2))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eurekaServerStatic() {
        return route("eureka-server-static")
                .route(path("/eureka/**"), http(eurekaServerURI))
                .build();
    }
}
