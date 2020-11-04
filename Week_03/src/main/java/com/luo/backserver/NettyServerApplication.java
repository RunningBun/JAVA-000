package com.luo.backserver;


public class NettyServerApplication {

    public static void main(String[] args) {

        //启动后端实际端口服务，提供给api-server访问
        HttpServer server = new HttpServer(false,8088);
        try {
            server.run();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
