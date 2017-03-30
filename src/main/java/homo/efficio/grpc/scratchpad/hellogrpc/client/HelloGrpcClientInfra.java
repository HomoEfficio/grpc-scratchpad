package homo.efficio.grpc.scratchpad.hellogrpc.client;

import homo.efficio.grpc.scratchpad.hellogrpc.HelloGrpcGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;

import java.util.concurrent.TimeUnit;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 30.
 */
public class HelloGrpcClientInfra {

    private final ManagedChannel channel;
    private final HelloGrpcGrpc.HelloGrpcBlockingStub blockingStub;

    public HelloGrpcClientInfra(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                                  .usePlaintext(true));
    }

    public HelloGrpcClientInfra(ManagedChannelBuilder<?> channelBuilder) {
        this.channel = channelBuilder.build();
        this.blockingStub = HelloGrpcGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
    }

    public HelloGrpcGrpc.HelloGrpcBlockingStub getStub() {
        return blockingStub;
    }
}
