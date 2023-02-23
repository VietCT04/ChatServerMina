package com.facenet.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
/**
 *
 * @author VietCT
 */

public class XMLEncoder implements ProtocolEncoder {
    /**
     * invoked when sent a message
     * @param ioSession
     * @param message
     * @param protocolEncoderOutput
     */
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

        if(message instanceof String) {
            String response = (String) message;
            String xmlData = "<response>" + message + "</response>";
            IoBuffer buffer = IoBuffer.allocate(xmlData.length(), false);
            buffer.setAutoExpand(true);
            buffer.put(xmlData.getBytes());
            buffer.flip();
            protocolEncoderOutput.write(buffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}