package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloRequest;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloResponse;
import io.grpc.StatusRuntimeException;
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

    private final HelloGrpcGrpc.HelloGrpcFutureStub futureStub;

    public HelloGrpcClient(HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub,
                           HelloGrpcGrpc.HelloGrpcStub asyncStub,
                           HelloGrpcGrpc.HelloGrpcFutureStub futureStub) {
        this.blockingStub = blockingStub;
        this.asyncStub = asyncStub;
        this.futureStub = futureStub;
    }


    public void sendBlockingUnaryMessage(String msg) {

        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();
        HelloResponse response;

        try {
            logger.info("Unary 서비스 호출, 메시지: [" + msg + "]");
            response = blockingStub.unarySayHello(request);
            // unary SayHello는 클라이언트에서는 여러번 호출 가능
//            response = blockingStub.unarySayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "Unary 서비스 호출 중 실패: " + e.getStatus());
            return;
        }

        logger.info("Unary 서버로부터의 응답: " + response.getWelcomeMessage());
    }

    public void sendServerStreamingMessage(String msg) {

        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();
        Iterator<HelloResponse> responseIterator = null;

        try {
            logger.info("Server Streaming 서비스 호출, 메시지: [" + msg + "]");
            responseIterator = blockingStub.serverStreamingSayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "Server Streaming 서비스 호출 중 실패: " + e.getStatus());
            return;
        }

        responseIterator.forEachRemaining(
                (e) -> logger.info("Server Streaming 서버로부터의 Streaming 응답: " + e.getWelcomeMessage())
        );
    }

    // Blocking Stub은 clientStreaming을 지원하지 않음

    public void sendAsyncClientStreamingMessage(List<String> messages) {

        // 서버에 보낼 콜백 객체
        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse value) {
                logger.info("Client Streaming 서버로부터의 응답\n" + value.getWelcomeMessage());
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, "Clent Streaming responseObserver.onError() 호출됨");
            }

            @Override
            public void onCompleted() {
                logger.info("Client Streaming 서버 응답 completed");
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

    public void sendBiStreamingMessage(List<String> messages) {

        int countForOrder = 1;

        // 서버에 보낼 콜백 객체
        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse value) {
                logger.info("Bi Streaming 서버로부터의 응답\n" + value.getWelcomeMessage());
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, "Bi Streaming responseObserver.onError() 호출됨");
            }

            @Override
            public void onCompleted() {
                logger.info("Bi Streaming 서버 응답 completed");
            }
        };

        StreamObserver<HelloRequest> requestObserver = asyncStub.biStreamingSayHello(responseObserver);
        try {
            for (String msg: messages) {
                requestObserver.onNext(HelloRequest.newBuilder().setName(msg + "-" + countForOrder++).build());
            }
        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }

        requestObserver.onCompleted();
    }

    public void sendAsyncUnaryMessage(String msg) {

        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();

        logger.info("Async Unary 서비스 호출, 메시지: [" + msg + "]");
        asyncStub.unarySayHello(
                request,
                // 서버에 보낼 콜백 객체
                new StreamObserver<HelloResponse>() {
                    @Override
                    public void onNext(HelloResponse value) {
                        logger.info("Async Unary 서버로부터의 응답\n" + value.getWelcomeMessage());
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.log(Level.SEVERE, "Async Unary responseObserver.onError() 호출됨");
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("Async Unary 서버 응답 completed");
                    }
                }
        );
        logger.info("(Nonblocking이면서)Async이니까 서버 응답 기다리지 않고, 결과 신경쓰지 않고 다른 작업 할 수 있다.");
    }
}
