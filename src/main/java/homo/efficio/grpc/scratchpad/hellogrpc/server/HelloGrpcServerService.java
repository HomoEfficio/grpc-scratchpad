package homo.efficio.grpc.scratchpad.hellogrpc.server;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloRequest;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloResponse;
import io.grpc.stub.StreamObserver;

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
}
