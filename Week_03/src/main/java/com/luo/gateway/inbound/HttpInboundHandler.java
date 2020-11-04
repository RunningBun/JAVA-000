package com.luo.gateway.inbound;

import com.luo.gateway.filter.HttpRequestFilter;
import com.luo.gateway.outbound.AbstractOutBoundHandler;
import com.luo.gateway.outbound.httpclient4.HttpOutboundHandler;
import com.luo.gateway.outbound.netty4.NettyHttpClientOutboundHandler;
import com.luo.gateway.outbound.okhttp.OkhttpOutboundHandler;
import com.luo.gateway.router.HttpEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter implements HttpRequestFilter, HttpEndpointRouter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;

    /***
     * ʹ��HttpClient��handler��������ҵ��
     */
//    private HttpOutboundHandler handler;

    /**
     * ʹ��Okhttp��handler��������ҵ��
     */
//    private OkhttpOutboundHandler handler;

    /**
     * ʹ���Զ���Netty Client��handler��������ҵ��
     */
//    private NettyHttpClientOutboundHandler handler;

    private AbstractOutBoundHandler handler;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;

        switch (route(new ArrayList<>())) {
            case "server-1":
                handler = new OkhttpOutboundHandler(this.proxyServer);
                break;
            case "server-2":
                handler = new NettyHttpClientOutboundHandler(this.proxyServer);
                break;
            case "server-0":
            default:
                handler = new HttpOutboundHandler(this.proxyServer);
                break;
        }

//        handler = new NettyHttpClientOutboundHandler(this.proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            //���������
            filter(fullRequest, ctx);
            //���������ڲ�ת���ӿڣ�����ʹ����okHttpȥ�����ڲ�����
            handler.handle(fullRequest, ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        //�������������Զ���header
        HttpHeaders headers = fullRequest.headers();
        headers.add("nio", "phantom");
    }

    @Override
    public String route(List<String> endpoints) {
        switch ((new Random().nextInt() % 3)) {
            case 1:
                return "server-1";
            case 2:
                return "server-2";
            default:
                return "server-0";
        }
    }
}
