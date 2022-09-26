package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.수강신청기간DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class 수강신청기간DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 수강신청기간DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //수강신청기간을 초기에 null로 넣고 수정하는 식으로 네트워크 프로그래밍을 구현하여 미사용
    public void 수강신청기간입력(수강신청기간DTO 수강신청기간DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("mapper.수강신청기간mapper.수강신청기간입력", 수강신청기간DTO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //원하는 학년의 수강신청기간(시작, 종료)을 수정하는 메소드
    public void 학년별수강신청기간수정(수강신청기간DTO 수강신청기간DTO, int 대상학년){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String formatDate = 수강신청기간DTO.get수강신청시작시간().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String formatDate2 = 수강신청기간DTO.get수강신청종료시간().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String sql = "UPDATE 수강신청기간 SET 수강신청시작시간 = " + formatDate + ", 수강신청종료시간 = " + formatDate2 +
                "        WHERE 교과목번호 =  ANY(SELECT 교과목번호 FROM 교과목\n" +
                "        WHERE 대상학년 = ?)";
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 대상학년);
            pstmt.executeUpdate();
            conn.commit();
        }catch(SQLException e){
            System.out.print("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        finally {
            try {
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //원하는 학년의 수강신청시작기간을 변경하는 메소드
    public void 학년별수강신청시작기간수정(수강신청기간DTO 수강신청기간DTO, int 대상학년){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String formatDate = 수강신청기간DTO.get수강신청시작시간().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String sql = "UPDATE 수강신청기간 SET 수강신청시작시간 = " + formatDate +
                "        WHERE 교과목번호 =  ANY(SELECT 교과목번호 FROM 교과목\n" +
                "        WHERE 대상학년 = ?)";
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 대상학년);
            pstmt.executeUpdate();
            conn.commit();
        }catch(SQLException e){
            System.out.print("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        finally {
            try {
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //원하는 학년의 수강신청종료기간을 변경하는 메소드
    public void 학년별수강신청종료기간수정(수강신청기간DTO 수강신청기간DTO, int 대상학년){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String formatDate2 = 수강신청기간DTO.get수강신청종료시간().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String sql = "UPDATE 수강신청기간 SET 수강신청종료시간 = " + formatDate2 +
                "        WHERE 교과목번호 =  ANY(SELECT 교과목번호 FROM 교과목\n" +
                "        WHERE 대상학년 = ?)";
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 대상학년);
            pstmt.executeUpdate();
            conn.commit();
        }catch(SQLException e){
            System.out.print("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        finally {
            try {
                if (conn != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}