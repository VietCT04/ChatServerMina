package com.facenet.mina.server;

import java.util.*;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author VietCT
 */

public class TimeServerHandler extends IoHandlerAdapter
{
    public TimeServerHandler() {

        allowIp.add("127.0.0.1");
        allowIp.add("127.0.0.2");
    }

    private final int ipOccurrenceLimit = 2;
    private ArrayList<String> allowIp = new ArrayList<String>();

    private HashMap<String,Integer> ipOccurrence = new HashMap<String, Integer>();
    private final Set<IoSession> sessions = Collections.synchronizedSet(new HashSet());

    /**
     * To broadcast a message to all session connected to server
     * @param message
     * @param sentSession
     */
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

    /**
     * invoked when received a message
     * @param session
     * @param message
     */
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
        System.out.println("message Received: " + message);
        String str = message.toString();
        System.out.println(str);
        Date date = new Date();
        session.write( date.toString() );
        if (str.equals("/quit")) sessionClosed(session);
        else broadcast(str, session);
    }

    /**
     * invoked when session in idle status
     * @param session
     * @param status
     */
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
        System.out.println( "IDLE " + session.getIdleCount( status ));
    }

    /**
     * Check if the ip connecting to server valid or not
     * @param ip
     */
    public boolean isValidIP(String ip){
        boolean isValid = false;
        for (int i = 0; i < allowIp.size(); i++) {
            if (allowIp.get(i).equals(ip)) {
                isValid = true;
            }
        }
        if (ipOccurrence.get(ip) != null){
            if ((ipOccurrence.get(ip) + 1) > ipOccurrenceLimit){
                isValid = false;
            }
        }
        return isValid;
    }
    /**
     * invoked when a session try to connect to the server
     * @param session
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        String clientIP = session.getRemoteAddress().toString().split(":")[0].substring(1);
        System.out.println("IP " + clientIP + " is connecting ...");
        if(isValidIP(clientIP)) {
            System.out.println("ALLOW CONNECTION FROM " + clientIP);
            System.out.println("sessionCreated " + session.getRemoteAddress().toString());
            sessions.add(session);
            int currentIpOcc = 0;
            if (ipOccurrence.get(clientIP) != null){
                currentIpOcc = ipOccurrence.get(clientIP);
                ipOccurrence.put(clientIP, currentIpOcc + 1);
            } else {
                ipOccurrence.put(clientIP, currentIpOcc + 1);
            }
        } else{
            int currentIpOcc = 0;
            if (ipOccurrence.get(clientIP) != null){
                currentIpOcc = ipOccurrence.get(clientIP);
                ipOccurrence.put(clientIP, currentIpOcc + 1);
            } else {
                ipOccurrence.put(clientIP, currentIpOcc + 1);
            }
            System.out.println("NOT ALLOW CONNECTION FROM " + clientIP);
            session.close();
        }

    }

    // This method invoked when close a session (IoSession)
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        String clientIP = session.getRemoteAddress().toString().split(":")[0].substring(1);
        System.out.println("sessionClosed ");
        int currentIpOcc = 0;
        sessions.remove(session);
        if (ipOccurrence.get(clientIP) != null){
            currentIpOcc = ipOccurrence.get(clientIP);
            ipOccurrence.put(clientIP, currentIpOcc - 1);
        } else {
            ipOccurrence.put(clientIP, currentIpOcc - 1);
        }
    }

    // This method invoked when a session opened (IoSession)
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpened ");
    }
}