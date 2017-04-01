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
        logger.info("### 메시지 왔다: " + request.getName());
        HelloResponse helloResponse =
                HelloResponse.newBuilder().setWelcomeMessage("Hello " + request.getName()).build();
        // unary이면 onNext()를 두 번 이상 호출할 수 없다.
        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void serverStreamingSayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        logger.info("### 메시지 왔다: " + request.getName());
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
                // server는 Streaming이 아니므로 responseObserver를 2회 이상 호출할 수 없음.
                // 2회 이상 호출하면 responseObserver.onError() 호출됨
                responseObserver.onNext(HelloResponse.newBuilder().setWelcomeMessage(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }
}
