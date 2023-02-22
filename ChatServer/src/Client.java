
import java.net.InetSocketAddress;
import java.util.Scanner;

import com.facenet.mina.codec.XMLCodecFactory;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.sumup.codec.SumUpProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 34   * (<strong>Entry Point</strong>) Starts SumUp client.
 35   *
 36   * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 37   */
public class Client {
    private static final String HOSTNAME = "localhost";

    private static final int PORT = 1565;

    private static final long CONNECT_TIMEOUT = 30*1000L; // 30 seconds

    // Set this to false to use object serialization instead of custom codec.
    private static final boolean USE_CUSTOM_CODEC = true;

    public static void main(String[] args) throws Throwable {

        // prepare values to sum up
        int[] values = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            values[i] = Integer.parseInt(args[i]);
        }

        NioSocketConnector connector = new NioSocketConnector();

        // Configure the service.
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        int using = 0;
        if (using == 0) {
            connector.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(
                            new XMLCodecFactory()));
        } else {
            if (USE_CUSTOM_CODEC) {
                connector.getFilterChain().addLast(
                        "codec",
                        new ProtocolCodecFilter(
                                new SumUpProtocolCodecFactory(false)));
            } else {
                connector.getFilterChain().addLast(
                        "codec",
                        new ProtocolCodecFilter(
                                new XMLCodecFactory()));
            }
        }
        connector.getFilterChain().addLast("logger", new LoggingFilter());

        connector.setHandler(new ClientSessionHandler(values));
        IoSession session;
        for (;;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(
                        HOSTNAME, PORT));
                System.out.println("Connect to server " + HOSTNAME + " and PORT " + PORT);
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                System.err.println("Failed to connect.");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }
        System.out.println("???");
        Scanner in = new Scanner(System.in);
        while (true){
            String message = in.nextLine();
            session.write(message);
            if (message == "15615") break;
        }
        session.getCloseFuture().awaitUninterruptibly();

        connector.dispose();
    }
}