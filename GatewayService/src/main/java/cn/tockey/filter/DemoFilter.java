package cn.tockey.filter;

import cn.tockey.config.FilterProperties;
import cn.tockey.config.JwtProperties;
import cn.tockey.domain.User;
import cn.tockey.utils.JwtUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 桐叔
 * @email liangtong@itcast.cn
 * @description
 */
@Component
@EnableConfigurationProperties({FilterProperties.class})
public class DemoFilter implements GlobalFilter, Ordered {
    @Resource // 注入配置文件
    private FilterProperties filterProperties;
    @Resource
    private JwtProperties jwtProperties;

    @Override // exchange 交换机  chain 链
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 【所请求的路径是白名单，直接放行】
        // 白名单列表
        List<String> allowPaths = filterProperties.getAllowPaths();
        for (String path : allowPaths) {
            // 请求的路径是否包含白名单中的路径
            if (exchange.getRequest().getURI().getPath().contains(path)) {
                // 放行
                //System.out.println("白名单");
                return chain.filter(exchange);
            }
        }

        // 【所请求的路径不是白名单，需要进行权限校验】
        // 获取 token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 校验 token
        try {
            JwtUtils.getObjectFromToken(token, jwtProperties.getPublicKey(), User.class);
            return chain.filter(exchange); // 放行
        } catch (Exception e) {
            System.out.println("没有权限");
            //e.printStackTrace();
            //throw new RuntimeException(e);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            DataBuffer wrap = response.bufferFactory().wrap("没有权限".getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Flux.just(wrap));
        }


        // 放行
        //return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        //    System.out.println("最后处理");
        //}));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
