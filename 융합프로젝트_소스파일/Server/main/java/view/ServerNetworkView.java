package view;

import network.Protocol;

import java.net.Socket;

public class ServerNetworkView {
    /*
        서버에서 클라이언트와의 통신 로그를 출력해주는 클래스
     */
    private Socket socket;
    private String[] userInfo;  // 현재 로그인된 클라이언트 유저의 ID와 PW 정보

    private static String boundLine = "====================================================================================\n";

    // Constructor
    public ServerNetworkView(Socket socket, String[] userInfo) {
        this.socket = socket;
        this.userInfo = userInfo;
    }

    /*
        최초 연결 시, 연결된 클라이언트의 네트워크 정보를 출력하는 메소드
     */
    public void printConnectInfo() {
        String clientInfo =
                "[Client]\n" +
                        "HostAddress:\t" + socket.getInetAddress().getHostAddress() + "\n" +
                        "HostName:\t\t" + socket.getInetAddress().getHostName();

        System.out.println(boundLine + clientInfo + "\n" + boundLine);
    }

    /*
        프로토콜 수신 시, 프로토콜을 전송한 클라이언트의 정보와 해당 프로토콜의 정보를 출력해주는 메소드
     */
    public void printReceiveProtocol(Protocol protocol) {
        String clientInfo =
                "[Client]\n" +
                        "HostAddress:\t" + socket.getInetAddress().getHostAddress() + "\n" +
                        "HostName:\t\t" + socket.getInetAddress().getHostName() + "\n" +
                        "From UserID:\t" + userInfo[0] + "\n";

        String request =
                        "ProtocolType:\t0x" + Integer.toHexString(protocol.getType()) + " (" + protocol.getType() + ")" + "\n" +
                        "ProtocolCode:\t0x" + Integer.toHexString(protocol.getCode()) + " (" + protocol.getCode() + ")" + "\n";

        String data = new String();

        if(protocol.getLength() > 0) {
            String[] datas = protocol.getDatas();
            for(int i=0; i<datas.length; i++) {
                data +=
                        "Data[" + i + "]:\t" + datas[i] + "\n";
            }
        } else {
            data = "Data: No Data\n";
        }

        System.out.println(boundLine + clientInfo + "\n" + request + "\n" + data + boundLine);
    }


    /*
        프로토콜 송신 시, 프로토콜을 전송할 서버의 정보와 해당 프로토콜의 정보를 출력해주는 메소드
     */
    public void printSendMessage(Protocol protocol) {
        String serverInfo =
                "[Server]\n" +
                        "HostAddress:\t" + socket.getLocalAddress().getHostAddress() + "\n" +
                        "HostName:\t" + socket.getLocalAddress().getHostName() + "\n" +
                        "To UserID:\t" + userInfo[0] + "\n";

        String response =
                        "ProtocolType:\t0x" + Integer.toHexString(protocol.getType()) + " (" + protocol.getType() + ")" + "\n" +
                        "ProtocolCode:\t0x" + Integer.toHexString(protocol.getCode()) + " (" + protocol.getCode() + ")" + "\n";

        String data = new String();

        if(protocol.getLength() > 0) {
            String[] datas = protocol.getDatas();
            for(int i=0; i<datas.length; i++) {
                data +=
                        "Data[" + i + "]:\t" + datas[i] + "\n";
            }
        } else {
            data = "Data: No Data\n";
        }

        System.out.println(boundLine + serverInfo + "\n" + response + "\n" + data + boundLine);
    }

    public String getInvalidProtocolMessage() {
        return "ERROR: 옳지 않은 타입 및 코드입니다.";
    }

    public String getRedundantKeyMessage() {
        return "ERROR: 중복 키입니다.";
    }

    public String getEmptyKeyMessage() {
        return "ERROR: 존재하지 않는 키입니다.";
    }

}
