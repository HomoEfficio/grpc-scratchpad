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
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
//        super.sayHello(request, responseObserver);
        logger.info("### 메시지 왔다: " + request.getName());
        HelloResponse helloResponse =
                HelloResponse.newBuilder().setWelcomeMessage("Hello " + request.getName()).build();
        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }
}
