package homo.efficio.grpc.scratchpad.hellogrpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 30.
 */
public class HelloGrpcServerInfra {

    private final Logger logger = Logger.getLogger(HelloGrpcServerInfra.class.getName());

    private final Server server;

    public HelloGrpcServerInfra(int port) throws IOException {

        this.server = ServerBuilder.forPort(port)
                                   .addService(new HelloGrpcServerService())
                                   .build()
                                   .start();
        logger.info("gRPC Server 시작 on " + port);
    }

    public Server getServer() {
        return server;
    }
}
