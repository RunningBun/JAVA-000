## 学习笔记
### 1.（必做）整合你上次作业的 httpclient/okhttp；
 - 将上一次的作业完成的netty集成进来(```xxx.xxx.backserver```)，作为本地后台服务启动(```BackServerApplication```)，绑定端口8088
 - 启动本次示例的网关服务(```xxx.xxx.gateway.NettyServerApplication```)，设置代理服务地址为启动的本地后台地址+端口（```http://localhost:8088/test```），代理端口设置为8888
 - 启动后，通过浏览器访问```http://localhost:8888/test```，本地网关会收到请求后，会访问后台服务地址，即```http://localhost:8088/test```，由此实现简易的网关操作
### 2.（选做）使用 netty 实现后端 http 访问（代替上一步骤）
 - 自定义```client.netty4.NettyHttpClient```类，用于代替OkHttpClient实现Http请求
 - 自定义```HttpClientInboundHandler```类，用于监听```NettyHttpClient```的请求回调
