package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;

import java.util.Arrays;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 30.
 */
public class HelloGrpcClientRunner {

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int serverPort = 54321;
        HelloGrpcClientInfra helloGrpcClientInfra = new HelloGrpcClientInfra(host, serverPort);

        HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub = helloGrpcClientInfra.getBlockingStub();
        HelloGrpcGrpc.HelloGrpcStub asyncStub = helloGrpcClientInfra.getAsyncStub();
        HelloGrpcGrpc.HelloGrpcFutureStub futureStub = helloGrpcClientInfra.getFutureStub();
        HelloGrpcClient helloGrpcClient = new HelloGrpcClient(blockingStub, asyncStub, futureStub);

//        // Unary
//        helloGrpcClient.sendUnaryMessage("Unary gPRC 스프링캠프 2017");
//        helloGrpcClientInfra.shutdown();

//        // ServerStreaming
//        helloGrpcClient.sendServerStreamingMessage("ServerStreaming gRPC 스프링캠프 2017");
//        helloGrpcClientInfra.shutdown();

//        // clientStreaming
//        helloGrpcClient.sendClientStreamingMessage(Arrays.asList("ClientStreaming", "gRPC", "스프링캠프 2017"));
//        // clientStreaming은 async 이므로 여유를 주고 shutdown 해야 된다
//        Thread.sleep(2000);
//        helloGrpcClientInfra.shutdown();

        // biStreaming
        helloGrpcClient.sendBiStreamingMessage(Arrays.asList("ClientStreaming", "gRPC", "스프링캠프 2017"));
        // biStreaming은 async 이므로 여유를 주고 shutdown 해야 된다
        Thread.sleep(2000);
        helloGrpcClientInfra.shutdown();
    }
}
