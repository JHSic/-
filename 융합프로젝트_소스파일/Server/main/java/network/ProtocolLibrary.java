package network;

import view.ServerNetworkView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class ProtocolLibrary {
    /*
        Protocol을 통한 통신을 간편하게 하기 위해 여러 메소드들을 구현한 클래스
     */
    
    /*
        요청에 대한 성공 메시지를 전송하는 메소드
     */
    public static void sendSuccessProtocol(OutputStream os, Protocol protocol, byte[] buf, ServerNetworkView sv) throws IOException, ClassNotFoundException, InterruptedException {
        protocol = new Protocol(Protocol.타입_응답, Protocol.코드_응답_성공);
        os.write(protocol.getPacket());
        sv.printSendMessage(protocol);
        os.flush();
    }

    /*
        요청에 대한 성공 메시지와 함께 데이터를 함께 보내는 메소드
     */
    public static void sendSuccessProtocolWithData(OutputStream os, Protocol protocol, byte[] buf, ServerNetworkView sv, String data) throws IOException, ClassNotFoundException, InterruptedException {
        protocol = new Protocol(Protocol.타입_응답, Protocol.코드_응답_데이터전송);
        protocol.setData(data);
        os.write(protocol.getPacket());
        sv.printSendMessage(protocol);
        os.flush();
    }

    /*
        요청에 대한 성공 메시지와 함께 데이터를 배열 형태로 함께 보내는 메소드
     */
    public static void sendSuccessProtocolWithData(OutputStream os, Protocol protocol, byte[] buf, ServerNetworkView sv, String[] data) throws IOException, ClassNotFoundException, InterruptedException {
        protocol = new Protocol(Protocol.타입_응답, Protocol.코드_응답_데이터전송);
        protocol.setData(data);
        os.write(protocol.getPacket());
        sv.printSendMessage(protocol);
        os.flush();
    }

    /*
        요청에 대한 실패 메시지와 함께 데이터를 함께 보내는 메소드
     */
    public static void sendFailProtocol(OutputStream os, Protocol protocol, byte[] buf, ServerNetworkView sv, String errorMessage) throws IOException, ClassNotFoundException, InterruptedException {
        // 계정 생성 결과 전송
        protocol = new Protocol(Protocol.타입_응답, Protocol.코드_응답_실패);
        // 실패메세지 생성
        protocol.setData(errorMessage);
        os.write(protocol.getPacket());
        sv.printSendMessage(protocol);
        os.flush();
    }

    /*
        매개변수로 받은 type과 code에 해당하는 프로토콜을 수신하였는지를 판단해주는 메소드
     */
    public static boolean isValidReceiveProtocol(InputStream is, Protocol protocol, byte[] buf, ServerNetworkView sv, byte validType, byte validCode) throws IOException, ClassNotFoundException, InterruptedException {
        is.read(buf);
        protocol.setPacket(buf);
        sv.printReceiveProtocol(protocol);
        return (protocol.getType() == validType && protocol.getCode() == validCode);
    }

    /*
        매개변수로 받은 type에 해당하는 프로토콜을 수신하였는지를 판단해주는 메소드
     */
    public static boolean isValidReceiveProtocolType(InputStream is, Protocol protocol, byte[] buf, ServerNetworkView sv, byte validType) throws IOException, ClassNotFoundException, InterruptedException {
        is.read(buf);
        protocol.setPacket(buf);
        sv.printReceiveProtocol(protocol);
        return (protocol.getType() == validType);
    }

    /*
        매개변수로 받은 code에 해당하는 프로토콜을 수신하였는지를 판단해주는 메소드
     */
    public static boolean isValidReceiveProtocolCode(InputStream is, Protocol protocol, ServerNetworkView sv, byte[] buf, byte validCode) throws IOException, ClassNotFoundException, InterruptedException {
        is.read(buf);
        protocol.setPacket(buf);
        sv.printReceiveProtocol(protocol);
        return (protocol.getCode() == validCode);
    }

}
