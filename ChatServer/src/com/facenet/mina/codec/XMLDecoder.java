package com.facenet.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class XMLDecoder implements ProtocolDecoder {
    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        System.out.println("Message in: ");
        byte receivedBytes[] = ioBuffer.array();
        int remainingData = ioBuffer.remaining();
        while (remainingData > 0){
            byte pivot = ioBuffer.get();
            remainingData--;
        }
        String data = new String(receivedBytes);
        String responseMsg = data.substring(10, data.indexOf("</response>"));
        protocolDecoderOutput.write(responseMsg);
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}