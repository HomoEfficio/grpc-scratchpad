package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloRequest;
import homo.efficio.grpc.scratchpad.hellogrpc.HelloResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 30.
 */
public class HelloGrpcClientRunner {

    private final Logger logger = Logger.getLogger(HelloGrpcClientRunner.class.getName());

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int serverPort = 54321;

        HelloGrpcClientInfra helloGrpcClientInfra = new HelloGrpcClientInfra(host, serverPort);
        HelloGrpcGrpc.HelloGrpcBlockingStub stub = helloGrpcClientInfra.getStub();
        HelloGrpcClient helloGrpcClient = new HelloGrpcClient(stub);

        helloGrpcClient.sendMessage("스프링캠프 2017");

        helloGrpcClientInfra.shutdown();
    }
}
