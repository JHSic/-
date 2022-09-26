package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.교과목DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class 교과목DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 교과목DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //새로운 교과목을 추가하는 메소드
    public void 교과목입력(교과목DTO 교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("mapper.교과목mapper.교과목입력", 교과목DTO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //전체 교과목을 출력하는 메소드
    public List<교과목DTO> 전체교과목출력() {

        List<교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.교과목mapper.전체교과목출력");
        } finally {
            session.close();
        }

        return list;
    }

    //선택 교과목을 출력하는 메소드
    public 교과목DTO 선택교과목출력(String 교과목번호) {

        교과목DTO 교과목DTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            교과목DTO = session.selectOne("mapper.교과목mapper.선택교과목출력", 교과목번호);
        } finally {
            session.close();
        }

        return 교과목DTO;
    }

    //학년별 교과목을 출력하는 메소드
    public List<교과목DTO> 학년별교과목출력(교과목DTO 교과목DTO) {

        List<교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.교과목mapper.학년별교과목출력", 교과목DTO);
        } finally {
            session.close();
        }

        return list;
    }

    //교과목 전체 정보를 수정하는 메소드
    public void 교과목전체정보수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.교과목전체정보수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목전체정보수정 메소드로 통합하여 사용할 수 있게 네트워크를 프로그래밍하여 미사용
    public void 교과목이름수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.교과목이름수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목전체정보수정 메소드로 통합하여 사용할 수 있게 네트워크를 프로그래밍하여 미사용
    public void 대상학년수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.대상학년수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목전체정보수정 메소드로 통합하여 사용할 수 있게 네트워크를 프로그래밍하여 미사용
    public void 교육과정수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.교육과정수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목전체정보수정 메소드로 통합하여 사용할 수 있게 네트워크를 프로그래밍하여 미사용
    public void 이수구분수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.이수구분수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목전체정보수정 메소드로 통합하여 사용할 수 있게 네트워크를 프로그래밍하여 미사용
    public void 학점수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.학점수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목전체정보수정 메소드로 통합하여 사용할 수 있게 네트워크를 프로그래밍하여 미사용
    public void 설계과목여부수정(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.교과목mapper.설계과목여부수정", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //교과목을 삭제하는 메소드
    public void 교과목삭제(교과목DTO 교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.delete("mapper.교과목mapper.교과목삭제", 교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

}


