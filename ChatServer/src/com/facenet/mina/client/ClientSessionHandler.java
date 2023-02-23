package com.facenet.mina.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.sumup.message.AddMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author VietCT
 */

public class ClientSessionHandler extends IoHandlerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientSessionHandler.class);
    private final int[] values;
    private boolean finished;
    public ClientSessionHandler(int[] values) {
        this.values = values;
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * invoked when session opened
     * @param session
     */
    @Override
    public void sessionOpened(IoSession session) {
        System.out.println("sessionOpened ");
        // send summation requests
        for (int i = 0; i < values.length; i++) {
            AddMessage m = new AddMessage();
            m.setSequence(i);
            m.setValue(values[i]);
            session.write(m);
        }
    }
    /**
     * invoked when session created
     * @param session
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated ");
    }
    /**
     * invoked when session closed
     * @param session
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed ");
    }
    /**
     * invoked when session in idle state
     * @param session
     * @param status
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("sessionIdle ");
    }
    /**
     * invoked when receive a message
     * @param session
     * @param message
     */
    @Override
    public void messageReceived(IoSession session, Object message) {
        System.out.println(message.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.closeNow();
    }
}