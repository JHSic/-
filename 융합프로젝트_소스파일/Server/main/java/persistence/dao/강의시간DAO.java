package persistence.dao;

import persistence.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import javax.sql.DataSource;

import persistence.dto.강의시간DTO;

public class 강의시간DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 강의시간DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //강의 시간을 입력하는 메소드, 개설교과목 생성 시 넣어주는 식으로 구현하여 시연에서 미사용
    public void 강의시간입력(강의시간DTO 강의시간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("mapper.강의시간mapper.강의시간입력", 강의시간DTO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //강의 시간을 수정하는 메소드, 시연 메뉴에 존재하지않아 시연에서 미사용
    public void 강의시간수정(강의시간DTO 강의시간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("mapper.강의시간mapper.강의시간수정", 강의시간DTO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

}
