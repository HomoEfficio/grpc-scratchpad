package homo.efficio.grpc.scratchpad.hellogrpc.server;

import homo.efficio.grpc.scratchpad.hellogrpc.server.service.HelloGrpcServerImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 28.
 */
public class HelloGrpcServerRunner {

    private static final Logger logger = Logger.getLogger(HelloGrpcServerRunner.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {

        final int port = 54321;

        Server server = ServerBuilder.forPort(port)
                .addService(new HelloGrpcServerImpl())
                .build()
                .start();

        logger.info("gRPC Server started on " + port);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.err.println("*** shutting down gRPC server since JVM is shutting down");
                    server.shutdown();
                    System.err.println("*** server shut down");
                })
        );

        // 이게 없으면 서버 실행 후 바로 종료됨
        server.awaitTermination();
    }
}
