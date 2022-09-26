package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.강의계획서입력기간DTO;

import javax.sql.DataSource;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class 강의계획서입력기간DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 강의계획서입력기간DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //강의 계획서 입력 기간을 입력하는 메소드 - 개설교과목 생성 시 null을 넣고 수정하는 방식으로 바꾸어서 시연 시 미사용
    public void 강의계획서입력기간입력(강의계획서입력기간DTO 강의계획서입력기간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("mapper.강의계획서입력기간mapper.강의계획서입력기간입력", 강의계획서입력기간DTO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }
    
    //강의 계획서 입력 기간(시작, 종료)을 수정하는 메소드
    public void 강의계획서입력기간수정(강의계획서입력기간DTO 강의계획서입력기간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.강의계획서입력기간mapper.강의계획서입력기간수정", 강의계획서입력기간DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //강의 계획서 입력 시작 시간만 수정하는 메소드
    public void 강의계획서입력시작시간수정(강의계획서입력기간DTO 강의계획서입력기간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.강의계획서입력기간mapper.강의계획서입력시작시간수정", 강의계획서입력기간DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //강의 계획서 입력 종료 시간만 수정하는 메소드
    public void 강의계획서입력종료시간수정(강의계획서입력기간DTO 강의계획서입력기간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.강의계획서입력기간mapper.강의계획서입력종료시간수정", 강의계획서입력기간DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }
}