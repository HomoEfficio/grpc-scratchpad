package homo.efficio.grpc.scratchpad.hellogrpc.server;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloRequest;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloResponse;
import io.grpc.stub.StreamObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 28.
 */
public class HelloGrpcServerService extends HelloGrpcGrpc.HelloGrpcImplBase {

    private final Logger logger = Logger.getLogger(HelloGrpcServerService.class.getName());

    @Override
    public void unarySayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
//        super.sayHello(request, responseObserver);
        logger.info("Unary 메시지 왔다: " + request.getName());
        HelloResponse helloResponse =
                HelloResponse.newBuilder().setWelcomeMessage("Unary Hello " + request.getName()).build();
        // unary이면 onNext()를 두 번 이상 호출할 수 없다.
        responseObserver.onNext(helloResponse);
//        responseObserver.onNext(helloResponse);
        // 두 번 호출 시 클라이언트의 blockingStub.unarySayHello() 호출부에서 아래와 같은 예외 발생
//        Status{code=CANCELLED, description=Failed to read message., cause=io.grpc.StatusRuntimeException: INTERNAL: More than one value received for unary call
//            at io.grpc.Status.asRuntimeException(Status.java:531)
//            at io.grpc.stub.ClientCalls$UnaryStreamToFuture.onMessage(ClientCalls.java:423)
//            at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1MessageRead.runInContext(ClientCallImpl.java:491)
//            at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:52)
//            at io.grpc.internal.SerializingExecutor$TaskRunner.run(SerializingExecutor.java:152)
//            at io.grpc.stub.ClientCalls$ThreadlessExecutor.waitAndDrain(ClientCalls.java:587)
//            at io.grpc.stub.ClientCalls.blockingUnaryCall(ClientCalls.java:135)
//            at homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc$HelloGrpcBlockingStub.unarySayHello(HelloGrpcGrpc.java:197)
//            at homo.efficio.grpc.scratchpad.hellogrpc.client.HelloGrpcClient.sendUnaryMessage(HelloGrpcClient.java:49)
//            at homo.efficio.grpc.scratchpad.hellogrpc.client.HelloGrpcClientRunner.main(HelloGrpcClientRunner.java:31)
        responseObserver.onCompleted();
    }

    @Override
    public void serverStreamingSayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        logger.info("Server Streaming 메시지 왔다: " + request.getName());
        HelloResponse helloResponse =
                HelloResponse.newBuilder().setWelcomeMessage("Server Streaming Hello " + request.getName()).build();
        // Server Streaming이면 onNext()를 두 번 이상 호출할 수 있다.
        responseObserver.onNext(helloResponse);
        responseObserver.onNext(helloResponse);
        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> clientStreamingSayHello(StreamObserver<HelloResponse> responseObserver) {
//        return super.clientStreamingSayHello(responseObserver);
        return new StreamObserver<HelloRequest>() {
            StringBuilder sb = new StringBuilder();
            @Override
            public void onNext(HelloRequest value) {
                sb.append(value.getName())
                        .append("\n============================\n");
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, "Client Streaming requestObserver.onError() 호출");
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(HelloResponse.newBuilder().setWelcomeMessage(sb.toString()).build());
                // server는 Streaming이 아니므로 responseObserver.onNext()를 2회 이상 호출할 수 없음.
                // 2회 이상 호출하면 responseObserver.onError() 호출됨
//                responseObserver.onNext(HelloResponse.newBuilder().setWelcomeMessage(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<HelloRequest> biStreamingSayHello(StreamObserver<HelloResponse> responseObserver) {
//        return super.biStreamingSayHello(responseObserver);
        return new StreamObserver<HelloRequest>() {
            StringBuilder sb = new StringBuilder();

            @Override
            public void onNext(HelloRequest value) {
                sb.append(value.getName())
                        .append("\n============================\n");
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, "Bi Streaming requestObserver.onError() 호출");
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(HelloResponse.newBuilder().setWelcomeMessage(sb.toString()).build());
                // Bi Streaming 이므로 responseObserver.onNext()를 2회 이상 호출할 수 있음.
                responseObserver.onNext(HelloResponse.newBuilder().setWelcomeMessage(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }
}
