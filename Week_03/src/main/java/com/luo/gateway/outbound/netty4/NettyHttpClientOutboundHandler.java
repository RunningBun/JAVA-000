package com.luo.gateway.outbound.netty4;

import com.luo.client.netty4.NettyHttpClient;
import com.luo.gateway.outbound.NamedThreadFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientOutboundHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpClientOutboundHandler.class);

    private NettyHttpClient nettyHttpClient;
    private ExecutorService proxyService;
    private String backendUrl;
    private int backport;
    private String host;


    public NettyHttpClientOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        URI uri = URI.create(backendUrl);
        backport = uri.getPort();
        host = uri.getHost();
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);

        nettyHttpClient = new NettyHttpClient();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        final String url = this.backendUrl + fullRequest.uri();
        proxyService.submit(() -> {
            try {
                nettyHttpClient.connect(host, backport, url, content -> {
                    try {
                        handleResponse(fullRequest, ctx, content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpContent content) throws Exception {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, content.content());
            response.headers().set("Content-Type", "application/json");
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }

    }

    private void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
