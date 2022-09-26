package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.*;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.개설교과목Mapper;

public class 담당과목목록DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 담당과목목록DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //담당과목을 입력하는 메소드
    public void 담당과목입력(담당과목목록DTO 담당과목목록DTO) throws Exception{

        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 강의계획서 입력 기간 예외처리
            개설교과목Mapper 개설교과목Mapper = (개설교과목Mapper)session.getMapper(개설교과목Mapper.class);
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            교수DTO 교수DTO = new 교수DTO();
            교수DTO.set사용자ID(담당과목목록DTO.get사용자ID());

            개설교과목DTO = 개설교과목Mapper.개설교과목선택출력(담당과목목록DTO.get교과목번호());

            List<개설교과목DTO> list = session.selectList("mapper.담당과목목록mapper.개인담당과목목록출력", 담당과목목록DTO);

            for(int i=0; i < list.size(); i++) {
                개설교과목DTO 담당교과목 = list.get(i);
                if(!담당교과목.get수업요일().equals(개설교과목DTO.get수업요일()))
                    continue;
                else if(
                        개설교과목DTO.get수업종료시간().isAfter(담당교과목.get수업시작시간()) &&
                                개설교과목DTO.get수업시작시간().isBefore(담당교과목.get수업종료시간()))
                {
                    throw new Exception("이미 해당 시간에 담당한 교과목이 있습니다.");
                } //교수의 시간표를 확인하여 해당 시간에 담당할 교과목을 갖는 것이 가능한지 확인하고 불가하면 예외를 던지는 코드
            }

            // 삽입
            session.insert("mapper.담당과목목록mapper.담당과목입력", 담당과목목록DTO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    //현재 로그인 중인 교수의 담당과목 목록을 출력하는 메소드 //시간표에 사용
    public List<개설교과목DTO> 개인담당과목목록출력(담당과목목록DTO 담당과목목록DTO) {
        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.담당과목목록mapper.개인담당과목목록출력", 담당과목목록DTO.get사용자ID());
        } finally {
            session.close();
        }

        return list;
    }

    //전체 교수 담당 과목을 출력할 필요가 없는 구현 조건이라 미사용
    public List<담당과목목록DTO> 전체교수담당과목목록출력() {

        List<담당과목목록DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.담당과목목록mapper.전체교수담당과목목록출력");
        } finally {
            session.close();
        }

        return list;
    }

    //담당 과목을 삭제하는 것이 구현 조건에 없어서 미사용
    public void 담당과목삭제(담당과목목록DTO 담당과목목록DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("mapper.담당과목목록mapper.담당과목삭제", 담당과목목록DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

}


