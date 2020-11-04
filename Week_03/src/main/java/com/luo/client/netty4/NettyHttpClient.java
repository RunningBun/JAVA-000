package com.luo.client.netty4;//package com.luo.gateway.outbound;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class NettyHttpClient {

    private CallBack callBack;

    public void connect(String host, int port, String url, CallBack callBack) throws Exception {
        this.callBack = callBack;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler(NettyHttpClient.this.callBack));
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public interface CallBack {
        void onResponse(HttpContent content);
    }
}
