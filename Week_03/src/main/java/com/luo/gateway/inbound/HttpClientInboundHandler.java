package com.luo.gateway.inbound;

import com.luo.gateway.outbound.netty4.NettyHttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

    private NettyHttpClient.CallBack callBack;

    public HttpClientInboundHandler(NettyHttpClient.CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) msg;
            System.out.println("CONTENT_TYPE:" + res.headers().get(HttpHeaderNames.CONTENT_TYPE));
        }
        if (msg instanceof HttpContent) {
            callBack.onResponse((HttpContent) msg);
        }
    }

}

