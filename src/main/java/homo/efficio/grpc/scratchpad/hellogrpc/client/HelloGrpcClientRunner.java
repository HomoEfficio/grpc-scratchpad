package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;

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

//        // Blocking Unary
//        helloGrpcClient.sendBlockingUnaryMessage("Blocking Unary gRPC 스프링캠프 2017");
//        helloGrpcClientInfra.shutdown();

//        // ServerStreaming
//        helloGrpcClient.sendServerStreamingMessage("Server Streaming gRPC 스프링캠프 2017");
//        helloGrpcClientInfra.shutdown();

//        // clientStreaming
//        helloGrpcClient.sendClientStreamingMessage(Arrays.asList("Client Streaming", "gRPC", "스프링캠프 2017"));
//        // clientStreaming은 async 이므로 여유를 주고 shutdown 해야 된다
//        Thread.sleep(2000);
//        helloGrpcClientInfra.shutdown();

//        // biStreaming
//        helloGrpcClient.sendBiStreamingMessage(Arrays.asList("Bi Streaming", "gRPC", "스프링캠프 2017"));
//        // biStreaming은 async 이므로 여유를 주고 shutdown 해야 된다
//        Thread.sleep(2000);
//        helloGrpcClientInfra.shutdown();

//        // Async Unary
        helloGrpcClient.sendAsyncUnaryMessage("Async Unary gRPC 스프링캠프 2017");
        // async 이므로 여유를 주고 shutdown 해야 된다
        Thread.sleep(5000);
        helloGrpcClientInfra.shutdown();
    }
}
