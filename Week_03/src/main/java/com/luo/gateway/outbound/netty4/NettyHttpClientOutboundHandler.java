package com.luo.gateway.outbound.netty4;

import com.luo.client.netty4.NettyHttpClient;
import com.luo.gateway.outbound.AbstractOutBoundHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientOutboundHandler extends AbstractOutBoundHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpClientOutboundHandler.class);

    private NettyHttpClient nettyHttpClient;
    private int backport;
    private String host;


    public NettyHttpClientOutboundHandler(String backendUrl) {
        super(backendUrl);
        URI uri = URI.create(backendUrl);
        backport = uri.getPort();
        host = uri.getHost();

        nettyHttpClient = new NettyHttpClient();
    }

    @Override
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
