package com.facenet.mina.codec;

import com.facenet.mina.object.messageObj;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
        } else {
            assert (message instanceof messageObj);
            try (ByteArrayOutputStream a = new ByteArrayOutputStream()) {
                try (ObjectOutputStream b = new ObjectOutputStream(a)) {
                    b.writeObject(message);
                }
                IoBuffer buffer = IoBuffer.allocate(a.size(), false);
                buffer.put(a.toByteArray());
                buffer.flip();
                protocolEncoderOutput.write(buffer);
            }
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}