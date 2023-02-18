import java.net.SocketAddress;
import java.util.*;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

public class TimeServerHandler extends IoHandlerAdapter
{
    public TimeServerHandler() {

        allowIp.add("127.0.0.1");
        allowIp.add("127.0.0.2");
    }

    private ArrayList<String> allowIp = new ArrayList<String>();
    private final Set<IoSession> sessions = Collections.synchronizedSet(new HashSet());
    public void broadcast(String message, IoSession sentSession) {
        synchronized(this.sessions) {
            Iterator var3 = this.sessions.iterator();

            while(var3.hasNext()){
                IoSession session = (IoSession)var3.next();
                if (session.isConnected() && sentSession != session) {
                    session.write(sentSession.getId() + ": " + message + '\n');
                }
            }

        }
    }
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
        String str = message.toString();
        if( str.trim().equalsIgnoreCase("quit") ) {
            session.close();
            return;
        }
        Date date = new Date();
        session.write( date.toString() );
        sessions.add(session);
        broadcast(str, session);
    }
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
        System.out.println("sessionIdle ");
        System.out.println( "IDLE " + session.getIdleCount( status ));
        session.write("WARMLY WELCOME");

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        String clientIP = session.getRemoteAddress().toString().split(":")[0].substring(1);
        System.out.println("IP " + clientIP + " is connecting ...");
        if("127.0.0.1".equals(clientIP)) {
            System.out.println("ALLOW CONNECTION FROM " + clientIP);
        }
        System.out.println("sessionCreated " + session.getRemoteAddress().toString());

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed ");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpened ");
    }
}