package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.*;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.개설교과목Mapper;

public class 수강신청DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 수강신청DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //수강신청을 하는 메소드 -> 수강신청 시 트리거를 이용하여 해당 학생의 신청 과목 수, 신청 총 학점이 자동으로 변경된다.
    public void 수강신청입력(수강신청DTO 수강신청DTO) throws Exception{

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper = (개설교과목Mapper)session.getMapper(개설교과목Mapper.class);
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            학생DTO 학생DTO = new 학생DTO();
            학생DTO.set사용자ID(수강신청DTO.get사용자ID());

            개설교과목DTO = 개설교과목Mapper.개설교과목선택출력(수강신청DTO.get교과목번호());

            // 수강신청 기간 예외처리
            if((개설교과목DTO.get수강신청시작시간() == null || 개설교과목DTO.get수강신청종료시간() == null ||
                    개설교과목DTO.get수강신청시작시간().isAfter(LocalDateTime.now()) ||
                    개설교과목DTO.get수강신청종료시간().isBefore(LocalDateTime.now()))
                    &&
                    개설교과목DTO.get대상학년() == 2)    // 아래 문장은 원활한 시연을 위해 추가된 가정 -> db 시연 조건에 있는 조건
            {
                throw new Exception("해당 교과목은 수강신청 기간이 아닙니다.");
            }

            // 수강신청 최대인원 예외처리
            if(개설교과목DTO.get수강신청인원() >= 개설교과목DTO.get최대수강인원()) {
                throw new Exception("해당 교과목은 수강신청 정원이 초과되었습니다.");
            }

            List<개설교과목DTO> list = session.selectList("mapper.수강신청mapper.개인수강신청목록출력", 수강신청DTO);

            // 수업 시간이 맞지않아 시간표에 넣는 것이 불가능한 과목 예외처리
            for(int i=0; i < list.size(); i++) {
                개설교과목DTO 신청된교과목 = list.get(i);
                if(!신청된교과목.get수업요일().equals(개설교과목DTO.get수업요일()))
                    continue;
                else if(
                        개설교과목DTO.get수업종료시간().isAfter(신청된교과목.get수업시작시간()) &&
                                개설교과목DTO.get수업시작시간().isBefore(신청된교과목.get수업종료시간()))
                {
                    throw new Exception("이미 해당시간에 수강 신청한 교과목이 있습니다.");
                }
            }

            // 삽입
            session.insert("mapper.수강신청mapper.수강신청입력", 수강신청DTO);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    //개인 시간표를 출력하기 위한 메소드
    public List<개설교과목DTO> 개인수강신청목록출력(수강신청DTO 수강신청DTO) {
        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.수강신청mapper.개인수강신청목록출력", 수강신청DTO.get사용자ID());
        } finally {
            session.close();
        }

        return list;
    }

    //모든 학생의 시간표를 보는 것이 구현 조건에 없어 미사용
    public List<수강신청DTO> 전체수강신청목록출력() {

        List<수강신청DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.수강신청mapper.전체수강신청목록출력");
        } finally {
            session.close();
        }

        return list;
    }

    //특정 교과목을 수강신청한 학생들의 목록을 출력하는 메소드
    public List<학생DTO> 교과목수강신청학생목록조회(수강신청DTO 수강신청DTO){
        List<학생DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.수강신청mapper.교과목수강신청학생목록조회", 수강신청DTO);
        } finally{
            session.close();
        }
        return list;
    }

    //로그인하고 있는 학생이 원하는 과목을 수강취소하는 메소드
    //트리거를 이용하여 수강신청취소 시 신청 과목 수와 신청 총 학점이 변경된다.
    public void 수강신청삭제(수강신청DTO 수강신청DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("mapper.수강신청mapper.수강신청삭제", 수강신청DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

}


