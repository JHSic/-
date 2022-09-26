package network;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Protocol extends Protocol_Const {
    /*
        Protocol
        클라이언트와 서버간의 통신을 위한 규약
        Header = Type + Code + Length = 4 Byte
        Data = 가변 데이터 사용.
        Data의 첫 1Byte는 데이터의 총 갯수를 저장(Count)
        이후 count * 2Byte만큼 각 데이터의 길이 정보를 저장
        마지막으로 모든 데이터들이 직렬화되어 packet에 저장되는 구조로 구현
        
        packet의 body에 담기는 정보는 UTF-8을 기준으로 함
     */
    private byte[] packet;
    private byte type;
    private byte code;
    private short length;   // 전체 패킷 길이가 아닌 가변 데이터 부분의 길이를 의미

    // 생성자
    public Protocol() {
        type = 미정의;
        code = 미정의;
        packet = new byte[최대길이];
        setType(type);
        setCode(code);
    }

    public Protocol(int type) {
        this.type = (byte)type;
        code = 미정의;
        packet = new byte[최대길이];
        setType(this.type);
        setCode(code);
    }

    public Protocol(int type, int code) {
        this.type = (byte)type;
        this.code = (byte)code;
        packet = new byte[최대길이];
        setType(this.type);
        setCode(this.code);
    }

    // getter & setter
    public byte[] getPacket() {
        return packet;
    }

    public void setPacket(byte[] packet) {
        this.packet = packet;
        getHeader();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = (byte)type;
        packet[타입길이 - 1] = this.type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = (byte)code;
        packet[타입길이 + 코드길이 - 1] = this.code;
    }

    public int getLength() {
        return length;
    }

    /*
        data가 1개일 때, 단일 String 객체를 반환하는 메소드
     */
    public String getData() {
        String[] data = getDatas();
        return data[0];
    }

    /*
        Protocol에서 Data부분을 추출해내는 메소드
     */
    public String[] getDatas() {
        int count = getCount();
        short[] particialLength = getParticialLength(count);
        String[] data = byteArrToString(count, particialLength);
        return data;
    }

    /*
        data가 1개일 때, 단일 data를 패킷에 set해주는 메소드
     */
    public void setData(String data) {
        String[] onceData = new String[1];
        onceData[0] = data;
        setData(onceData);
    }

    /*
        Protocol에서  Packet의 Data부분에 Data를 담는 메소드
     */
    public void setData(String[] data) {
        int count = data.length;
        short[] particialLength = new short[count];
        for(int i=0; i<count; i++) {
            particialLength[i] = (short)data[i].getBytes(Charset.forName("UTF-8")).length;
        }
        setCount(count);
        setParticialLength(count, particialLength);
        StringToByteArr(count, data);

        setLength(particialLength);
    }

    /*
        Data를 Set한 후 헤더를 제외한 총 데이터 길이를 set해주는 메소드
     */
    private void setLength(short[] particialLength) {
        short sum = 0;
        for(int i=0; i < particialLength.length; i++)
            sum += particialLength[i];
        length = sum;

        byte[] byteArray = ByteBuffer.allocate(2).putShort(length).array();
        System.arraycopy(byteArray,0,  packet, 타입길이 + 코드길이, 가변데이터길이);
    }

    /*
        Packet을 Set한 후, packet의 정보를 통해 Header에 대한 정보를 추출해 멤버 변수에 저장하는 메소드
     */
    private void getHeader() {
        type = packet[타입길이 - 1];
        code = packet[타입길이 + 코드길이 - 1];
        length = ByteBuffer.allocate(2).put(packet, 타입길이 + 코드길이, 가변데이터길이).getShort(0);
    }

    private int getCount() {
        return (int)packet[헤더길이 + 카운트길이 - 1];
    }

    private void setCount(int count) {
        packet[헤더길이 + 카운트길이 - 1] = (byte) count;
    }

    /*
        packet의 정보를 읽어 각 데이터들의 길이 정보가 담긴 배열을 반환하는 메소드
     */
    private short[] getParticialLength(int count) {
        short[] particialLength = new short[count];
        byte[] byteArray = new byte[2];
        int cursor = 헤더길이 + 카운트길이;
        for(int i=0; i < count; i++) {
            particialLength[i] = ByteBuffer.allocate(2).put(packet, cursor, 개별데이터길이).getShort(0);
            cursor += 개별데이터길이;
        }

        return particialLength;
    }

    /*
        각 데이터들의 길이 정보가 담긴 배열을 통해 해당 정보를 packet에 저장하는 메소드
     */
    private void setParticialLength(int count, short[] particialLength) {
        int cursor = 헤더길이 + 카운트길이;
        byte[] byteArray = new byte[count];
        for(int i=0; i<count; i++) {
            byteArray = ByteBuffer.allocate(2).putShort(particialLength[i]).array();
            System.arraycopy(byteArray,0,  packet, cursor, 개별데이터길이);
            cursor += 개별데이터길이;
        }
    }

    /*
        packet에 저장된 데이터를 String으로 변환하여 반환해주는 메소드
     */
    private String[] byteArrToString(int count, short[] particialLength) {
        int cursor = 헤더길이 + 카운트길이 + 개별데이터길이 * count;
        String[] data = new String[count];
        for(int i = 0; i< count; i++) {
            data[i] = new String(packet, cursor, particialLength[i], Charset.forName("UTF-8")).trim();
            cursor += particialLength[i];
        }

        return data;
    }

    /*
        String으로 작성된 데이터를 Byte로 변환해 packet에 저장하는 메소드
     */
    private void StringToByteArr(int count, String[] data) {
        int cursor = 헤더길이 + 카운트길이 + 개별데이터길이 * count;
        byte[] byteArray;
        for(int i=0; i<count; i++) {
            byteArray = data[i].trim().getBytes(Charset.forName("UTF-8"));
            System.arraycopy(byteArray,0, packet, cursor, byteArray.length);
            cursor += data[i].getBytes(Charset.forName("UTF-8")).length;
        }
    }

}