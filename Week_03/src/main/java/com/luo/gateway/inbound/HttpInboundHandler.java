package com.luo.gateway.inbound;

import com.luo.gateway.filter.HttpRequestFilter;
import com.luo.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter implements HttpRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;

    /***
     * ʹ��HttpClient��handler��������ҵ��
     */
//    private HttpOutboundHandler handler;

    /**
     * ʹ��Okhttp��handler��������ҵ��
     */
    private OkhttpOutboundHandler handler;

    /**
     * ʹ���Զ���Netty Client��handler��������ҵ��
     */
//    private NettyHttpClientOutboundHandler handler;
    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = new OkhttpOutboundHandler(this.proxyServer);
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
}
