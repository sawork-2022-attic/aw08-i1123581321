package com.micropos.gateway;

import com.micropos.dto.WaybillDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.webflux.dsl.WebFlux;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    private static final String PRODUCT = "lb://product-service";
    private static final String CART = "lb://cart-service";

    private static final String ORDER = "lb://order-service";


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator route(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p -> p.path("/products/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri(PRODUCT))
                .route(p -> p.path("/carts/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri(CART))
                .route(p -> p.path("/orders/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri(ORDER))
                .build();
    }

    @Bean
    public DirectChannel httpChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inGate(){
        return IntegrationFlows.from(WebFlux.inboundGateway("/waybills")
//                                .headerExpression("waybillId","#pathVariables.id")
                        )
//                .headerFilter("accept-encoding", false)
                .channel("httpChannel")
                .get();
    }

    @Bean
    public IntegrationFlow outGate(){
        return IntegrationFlows.from("httpChannel")
                .handle(Http.outboundGateway("http://localhost:9000/api/waybills")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(List.class))
                .get();
    }
}
