package com.luo.gateway.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.concurrent.*;

/**
 * project_name:    gateway
 * package :        com.luo.gateway.outbound
 * describe :
 *
 * @author :        Luo
 * creat_date :     2020/11/4 19:13
 */
public abstract class AbstractOutBoundHandler {

    protected ExecutorService proxyService;
    protected String backendUrl;

    public AbstractOutBoundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
    }

    public abstract void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx);
}
