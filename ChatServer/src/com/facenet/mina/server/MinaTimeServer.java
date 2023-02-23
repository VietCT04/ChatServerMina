package com.facenet.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import com.facenet.mina.codec.XMLCodecFactory;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
/**
 *
 * @author VietCT
 */

public class MinaTimeServer {

    // declare constants in uppercase
    private static final int PORT = 1565;

    public static void main(String[] args) throws IOException {
        // create an acceptor
        IoAcceptor acceptor = new NioSocketAcceptor();

        // add a logging filter
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());

        // set an integer variable using
        int using = 0;

        // use an if else statement to decide which codec to add
        if (using == 1) {
            acceptor.getFilterChain().addLast("codec",
                    new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        } else {
            acceptor.getFilterChain().addLast("codec",
                    new ProtocolCodecFilter(new XMLCodecFactory()));
        }

        // set the handler
        acceptor.setHandler(new TimeServerHandler());

        // set read buffer size and idle status
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        // bind address
        acceptor.bind(new InetSocketAddress(PORT));

        // log port info
        System.out.println("Listening on port " + PORT);
    }
}
