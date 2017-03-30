package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloRequest;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 30.
 */
public class HelloGrpcClient {

    private final Logger logger = Logger.getLogger(HelloGrpcClient.class.getName());

    private final HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub;

    public HelloGrpcClient(HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public void sendMessage(String msg) {
        logger.info("Request 생성, 메시지: [" + msg + "]");
        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();
        HelloResponse response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.warning("RPC 서버 호출 중 실패: " + e.getStatus());
            return;
        }
        logger.info("서버로부터의 응답: " + response.getWelcomeMessage());
    }
}
