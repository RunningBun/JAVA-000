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
     * 使用HttpClient的handler处理网关业务
     */
//    private HttpOutboundHandler handler;

    /**
     * 使用Okhttp的handler处理网关业务
     */
    private OkhttpOutboundHandler handler;

    /**
     * 使用自定义Netty Client的handler处理网关业务
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

            //加入过滤器
            filter(fullRequest, ctx);

            //调用网关内部转发接口，这里使用了okHttp去访问内部服务
            handler.handle(fullRequest, ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        //过滤器，加入自定义header
        HttpHeaders headers = fullRequest.headers();
        headers.add("nio", "phantom");
    }
}
