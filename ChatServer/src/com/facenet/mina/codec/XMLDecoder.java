package com.facenet.mina.codec;

import com.facenet.mina.object.messageObj;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serial;
/**
 *
 * @author VietCT
 */

public class XMLDecoder implements ProtocolDecoder {
    /**
     * invoked when a receive a message
     * @param ioSession
     * @param ioBuffer
     * @param protocolDecoderOutput
     */
    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        byte receivedBytes[] = ioBuffer.array();
        int remainingData = ioBuffer.remaining();
        while (remainingData > 0){
            byte pivot = ioBuffer.get();
            remainingData--;
        }
        try {
            messageObj now = (messageObj) (this.deserialize(receivedBytes));
            protocolDecoderOutput.write(now);
            return;
        } catch (Exception e){

        }
        String data = new String(receivedBytes);
        String responseMsg = data.substring(10, data.indexOf("</response>"));
        protocolDecoderOutput.write(responseMsg);
    }
    /**
     * use to deserialize a byte stream
     * @param bytes
     *
     **/
    private Object deserialize(byte[] bytes) throws Exception {
        try (ByteArrayInputStream a = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream b = new ObjectInputStream(a)) {
                return b.readObject();
            }
        }
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}