package com.luo.gateway.outbound.okhttp;

import com.luo.gateway.outbound.AbstractOutBoundHandler;
import com.luo.gateway.outbound.NamedThreadFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler extends AbstractOutBoundHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(OkhttpOutboundHandler.class);

    private OkHttpClient client;

    public OkhttpOutboundHandler(String backendUrl) {
        super(backendUrl);

        client = new OkHttpClient();
    }

    @Override
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        final String url = this.backendUrl + fullRequest.uri();
        proxyService.submit(() -> request(fullRequest, ctx, url));
    }

    private void request(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        //获取到client请求的header，复制到后台服务请求的header中
        Headers.Builder headers = new Headers.Builder();
        for (Map.Entry<String, String> entry : inbound.headers()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .headers(headers.build())
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LOGGER.info("failed", e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    handleResponse(inbound, ctx, response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final Response endpointResponse) throws Exception {
        FullHttpResponse response = null;
        try {
            byte[] body = endpointResponse.body().bytes();

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            List<String> headers = endpointResponse.headers("Content-Length");
            response.headers().setInt("Content-Length", (headers == null || headers.size() == 0) ? 0 : Integer.parseInt(headers.get(0)));
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

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
