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
    private final Set<IoSession> sessions = Collections.synchronizedSet(new HashSet());
    public void broadcast(String message, IoSession sentSession) {
        synchronized(this.sessions) {
            Iterator var3 = this.sessions.iterator();

            while(var3.hasNext()) {
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
        System.out.println( "IDLE " + session.getIdleCount( status ));
    }

}