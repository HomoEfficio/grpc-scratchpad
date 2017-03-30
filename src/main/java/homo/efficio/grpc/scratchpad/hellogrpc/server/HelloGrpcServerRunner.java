package homo.efficio.grpc.scratchpad.hellogrpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2017. 3. 28.
 */
public class HelloGrpcServerRunner {

    public static void main(String[] args) throws IOException, InterruptedException {

        final int port = 54321;

        Server server = new HelloGrpcServerInfra(port).getServer();

        // 이게 없으면 서버 실행 후 바로 종료됨
        server.awaitTermination();

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.err.println("*** gRPC 종료...");
                    server.shutdown();
                    System.err.println("*** gRPC 종료 완료");
                })
        );

    }
}
