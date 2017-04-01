package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloRequest;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 30.
 */
public class HelloGrpcClient {

    private final Logger logger = Logger.getLogger(HelloGrpcClient.class.getName());

    private final HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub;

    private final HelloGrpcGrpc.HelloGrpcStub asyncStub;

    public HelloGrpcClient(HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
        this.asyncStub = null;
    }

    public HelloGrpcClient(HelloGrpcGrpc.HelloGrpcStub asyncStub) {
        this.blockingStub = null;
        this.asyncStub = asyncStub;
    }

    public void sendMessage(String msg) {
        logger.info("Request 생성, 메시지: [" + msg + "]");
//        unary, serverStreaming
        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();

//        unary
        HelloResponse response;

//        serverStreaming
//        Iterator<HelloResponse> helloResponseIterator = null;
        try {
//            unary
            response = blockingStub.unarySayHello(request);
            // unary SayHello는 클라이언트에서는 여러번 호출 가능
            response = blockingStub.unarySayHello(request);

//            serverStreaming
//            helloResponseIterator = blockingStub.serverStreamingSayHello(request);

        } catch (StatusRuntimeException e) {
            logger.warning("RPC 서버 호출 중 실패: " + e.getStatus());
            return;
        }
//        unary
        logger.info("서버로부터의 응답: " + response.getWelcomeMessage());

//        server Streaming
//        helloResponseIterator.forEachRemaining(
//                (e) -> logger.info("서버로부터의 Streaming 응답: " + e.getWelcomeMessage())
//        );
    }

    public void sendMessage(List<String> messages) {

        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse value) {
                logger.info("서버로부터의 응답\n" + value.getWelcomeMessage());
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, "Clent Streaming gRPC responseObserver.onError() 호출됨");
            }

            @Override
            public void onCompleted() {
                logger.info("서버 응답 completed");
            }
        };

        StreamObserver<HelloRequest> requestObserver = asyncStub.clientStreamingSayHello(responseObserver);
        try {
            for (String msg: messages) {
                requestObserver.onNext(HelloRequest.newBuilder().setName(msg).build());
            }
        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }

        requestObserver.onCompleted();
    }
}
