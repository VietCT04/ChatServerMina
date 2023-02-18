package com.facenet.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class XMLEncoder implements ProtocolEncoder {
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

//        String xmlData = "";
//        if(message instanceof String) {
//            xmlData = "<response>" + (String) o + "</response>";
//        }
//        IoBuffer buffer  = IoBuffer.allocate(xmlData.length(), false);
//        buffer .put(xmlData.getBytes());
//        buffer.flip();
//        protocolEncoderOutput.write(buffer);


        if(message instanceof String) {
            String response = (String) message;
            String xmlData = "<response>" + message + "</response>";
            IoBuffer buffer = IoBuffer.allocate(response.length(), false);
            buffer.setAutoExpand(true);
            buffer.put(response.getBytes());
            buffer.flip();
            protocolEncoderOutput.write(buffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
