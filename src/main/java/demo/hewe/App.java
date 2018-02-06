package demo.hewe;

import demo.hewe.grpc.HelloClient;
import demo.hewe.io.hello.HelloProto;
import io.grpc.stub.StreamObserver;

/**
 * Hello world!
 */
public class App {
    private static final String HOST = "localhost";
    public static final int PORT = 50051;

    public static void main(String[] args) {
        HelloClientTest();
    }

    /**
     * 测试: hello.proto 客户端
     */
    public static void HelloClientTest() {
        HelloClient helloClient = new HelloClient(HOST, PORT);
        HelloProto.HelloRequest request = HelloProto.HelloRequest.newBuilder().setAge(44).setName("hewe").build();
        HelloProto.HelloReply reply = helloClient.getBlockingStub().tickInfo(request);
        System.out.println("==========阻塞调用====");
        System.out.println(reply.getInfo());
        System.out.println(reply.getReceiveTime());

        System.out.println("==========异步调用调用====");
        helloClient.getAsyncStub().tickInfo(request, new StreamObserver<HelloProto.HelloReply>() {
            @Override
            public void onNext(HelloProto.HelloReply helloReply) {
                System.out.println(helloReply.getInfo());
                System.out.println(helloReply.getReceiveTime());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("recieve completed!");
            }
        });
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
