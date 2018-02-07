package demo.hewe;

import demo.hewe.grpc.HelloClient;
import demo.hewe.io.hello.HelloProto;
import demo.hewe.io.jihe.Jihe;
import demo.hewe.io.jihe.JiheServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Map;

/**
 * Hello world!
 */
public class App {
    private static final String HOST = "localhost";
    public static final int PORT = 50051;

    public static void main(String[] args) {
//        HelloClientTest();
        JiheClient();
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

    /**
     * 测试集合的使用,包括list和map
     */
    public static void JiheClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext(true).build();
        JiheServiceGrpc.JiheServiceBlockingStub stub = JiheServiceGrpc.newBlockingStub(channel);
        Jihe.ListCar.Builder builder = Jihe.ListCar.newBuilder();
        //模拟数据
        Jihe.Car car1 = Jihe.Car.newBuilder().setId(1001).setName("救护车").setColor("white").build();
        Jihe.Car car2 = Jihe.Car.newBuilder().setId(1002).setName("消防车").setColor("red").build();
        Jihe.Car car3 = Jihe.Car.newBuilder().setId(1003).setName("垃圾车").setColor("green").build();
        //封装list
        builder.addCar(car1);
        builder.addCar(car2);
        builder.addCar(car3);

        //调用方法
        Jihe.MapCar reply = stub.transferToMap(builder.build());
        Map<Integer, Jihe.Car> carsMap = reply.getCarsMap();
        Jihe.Car car = carsMap.get(1002);
        System.out.println(String.format("id: %d,name: %s,color: %s", car.getId(), car.getName(), car.getColor()));
    }

}
