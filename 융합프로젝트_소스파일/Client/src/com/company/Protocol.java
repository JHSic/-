package com.company;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Protocol extends Protocol_Const {
    /*
        사용법
        1. 생성자로 타입과 코드를 때려 넣어주세요. (안 넣으면 undefined)
        2. 생성자로 안 넣었으면 setType setCode 해주세요.
        3. setData를 하면 알아서 패킷이 생성돼요.
        4. 가져올때는 getData를 쓰면 string배열로 넘어와요.
        5. getPacket,setPacket은 stream에 바이트 배열을 담기 위해 사용됨. 서버 및 클라이언트가 가지고 있는 버퍼에 byte[]를 담기 위한 메소드에요.

        packet의 body에 담기는 정보는 UTF-8기준
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

    public String getData() {
        String[] data = getDatas();
        return data[0];
    }

    public String[] getDatas() {
        int count = getCount();
        short[] particialLength = getParticialLength(count);
        String[] data = byteArrToString(count, particialLength);
        return data;
    }

    public void setData(String data) {
        String[] onceData = new String[1];
        onceData[0] = data;
        setData(onceData);
    }

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

    private void setLength(short[] particialLength) {
        short sum = 0;
        for(int i=0; i < particialLength.length; i++)
            sum += particialLength[i];
        length = sum;

        byte[] byteArray = new byte[가변데이터길이];
        byteArray = ByteBuffer.allocate(2).putShort(length).array();
        System.arraycopy(byteArray,0,  packet, 타입길이 + 코드길이, 가변데이터길이);
    }

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

    private void setParticialLength(int count, short[] particialLength) {
        int cursor = 헤더길이 + 카운트길이;
        byte[] byteArray = new byte[count];
        for(int i=0; i<count; i++) {
            byteArray = ByteBuffer.allocate(2).putShort(particialLength[i]).array();
            System.arraycopy(byteArray,0,  packet, cursor, 개별데이터길이);
            cursor += 개별데이터길이;
        }
    }

    private String[] byteArrToString(int count, short[] particialLength) {
        int cursor = 헤더길이 + 카운트길이 + 개별데이터길이 * count;
        String[] data = new String[count];
        for(int i = 0; i< count; i++) {
            data[i] = new String(packet, cursor, particialLength[i], Charset.forName("UTF-8")).trim();
            cursor += particialLength[i];
        }

        return data;
    }

    private void StringToByteArr(int count, String[] data) {
        int cursor = 헤더길이 + 카운트길이 + 개별데이터길이 * count;
        byte[] byteArray;
        for(int i=0; i<count; i++) {
            byteArray = data[i].trim().getBytes(Charset.forName("UTF-8"));
            System.arraycopy(byteArray,0,  packet, cursor, data[i].getBytes(Charset.forName("UTF-8")).length);
            cursor += data[i].getBytes(Charset.forName("UTF-8")).length;
        }
    }

}