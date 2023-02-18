package com.facenet.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class XMLDecoder implements ProtocolDecoder {
    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

        byte receivedBytes[] = ioBuffer.array();
        String data = new String(receivedBytes);

            System.out.println("resultObj instanceof String");

            String responseMsg = data.substring(data.indexOf("<response>") + 10, data.indexOf("</response>"));
            System.out.println("Message received: " + data);
            System.out.println("Content received: " + responseMsg);

//        String message = ioBuffer.getet
//        if(message instanceof String) {
//            String response = (String) message;
//            String xmlData = "<response>" + message + "</response>";
//            IoBuffer buffer = IoBuffer.allocate(response.length(), false);
//            buffer.setAutoExpand(true);
//            buffer.put(response.getBytes());
//            buffer.flip();
//            protocolDecoderOutput.write(buffer);
//        }
//
//        if(message instanceof String) {
//            String response = (String) message;
//            String xmlData = "<response>" + message + "</response>";
//            IoBuffer buffer = IoBuffer.allocate(response.length(), false);
//            buffer.setAutoExpand(true);
//            buffer.put(response.getBytes());
//            buffer.flip();
//            protocolEncoderOutput.write(buffer);
//        }
    }


    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
