package by.bsu.fpmi.apigateway.route;

import by.bsu.fpmi.apigateway.filter.EncodeRequestParametersFilter;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.nio.charset.StandardCharsets;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class Routes {

    @Value("${bsu.cfg-processor.name}")
    private String cfgProcessorServiceName;

    @Value("${bsu.cfg-examples.name}")
    private String cfgExamplesServiceName;

    @Value("${bsu.discovery-server.uri}")
    private String eurekaServerURI;

    @Bean
    public RouterFunction<ServerResponse> cfgProcessorFirstK() {
        return route("cfg-processor-first-k")
                .route(path("/processor/first-k/**"), http())
                .before(EncodeRequestParametersFilter.encodeRequestParameters(StandardCharsets.UTF_8))
                .after(AfterFilterFunctions.addResponseHeader("Access-Control-Allow-Origin", "http://127.0.0.1:63342"))
                .after(AfterFilterFunctions.addResponseHeader("Access-Control-Allow-Methods", "GET, POST"))
                .after(AfterFilterFunctions.addResponseHeader("Access-Control-Allow-Headers", "Content-Type, Origin"))
                .filter(lb(cfgProcessorServiceName))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> cfgExamples() {
        return route("cfg-examples")
                .route(path("/examples/**"), http())
                .before(EncodeRequestParametersFilter.encodeRequestParameters(StandardCharsets.UTF_8))
                .filter(lb(cfgExamplesServiceName))
                .after(AfterFilterFunctions.addResponseHeader("Access-Control-Allow-Origin", "*"))
                .after(AfterFilterFunctions.addResponseHeader("Access-Control-Allow-Methods", "*"))
                .build();
    }



    @Bean
    public RouterFunction<ServerResponse> page() {
        return route("cfg-processor-static")
                .route(path("/*"), http())
                .before(EncodeRequestParametersFilter.encodeRequestParameters(StandardCharsets.UTF_8))
                .filter(lb(cfgProcessorServiceName))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> grafana() {
        return route("grafana")
                .route(path("/grafana/**"), http("http://grafana:3000"))
                .before(EncodeRequestParametersFilter.encodeRequestParameters(StandardCharsets.UTF_8))
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
