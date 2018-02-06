package demo.hewe.grpc;

import demo.hewe.io.hello.HelloSerivceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @year 2018
 * @project demo-grpc-client
 * @description:<p>用于连接服务端的客户端 通过建立channel, 从channel中获取存根, 存根也就是客户端, 用于调用方法.
 * <p>
 * 存根包括:阻塞调用和异步调用.
 * <p>
 * 阻塞调用在调用方法后会等待服务端返回.
 * <p>
 * 异步调用则是传一个回调对象,服务端返回后调用,异步处理
 * </p>
 **/
public class HelloClient {
    //连接服务端的channel
    private ManagedChannel channel;

    /**
     * 获取阻塞客户端
     *
     * @return
     */
    public HelloSerivceGrpc.HelloSerivceBlockingStub getBlockingStub() {
        return blockingStub;
    }

    /**
     * 获取异步客户端
     *
     * @return
     */
    public HelloSerivceGrpc.HelloSerivceStub getAsyncStub() {
        return asyncStub;
    }

    private HelloSerivceGrpc.HelloSerivceBlockingStub blockingStub;
    private HelloSerivceGrpc.HelloSerivceStub asyncStub;

    /**
     * 初始化,建立连接获取阻塞客户端和异步客户端
     *
     * @param host
     * @param port
     */
    public HelloClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        this.blockingStub = HelloSerivceGrpc.newBlockingStub(channel);
        this.asyncStub = HelloSerivceGrpc.newStub(channel);
    }

    /**
     * 关闭连接
     *
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
