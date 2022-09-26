package persistence.dao;

import persistence.MyBatisConnectionFactory;
import persistence.PooledDataSource;
import persistence.dto.사용자DTO;
import persistence.dto.학생DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class 학생DAO {
    private final DataSource ds = PooledDataSource.getDataSource();

    //사용자 테이블과 학생 테이블을 조인하여 전체 학생 목록을 출력하는 메소드
    public List<학생DTO> 전체학생출력(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<학생DTO> 학생DTOS = new ArrayList<학생DTO>();
        String sql = "SELECT * FROM 사용자 NATURAL JOIN 학생 WHERE 사용자ID = 학생.사용자ID";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                학생DTO 학생DTO = new 학생DTO();
                학생DTO.set사용자ID(rs.getString(1));
                학생DTO.set사용자명(rs.getString(2));
                학생DTO.set사용자PW(rs.getString(3));
                학생DTO.set주소(rs.getString(4));
                학생DTO.set주민번호(rs.getString(5));
                학생DTO.set사용자유형(rs.getInt(6));
                학생DTO.set전화번호(rs.getString(7));
                학생DTO.set학과명(rs.getString(8));
                학생DTO.set학년(rs.getInt(9));
                학생DTO.set신청과목수(rs.getInt(10));
                학생DTO.set신청총학점(rs.getInt(11));
                학생DTOS.add(학생DTO);
                conn.commit();
            }
        } catch(SQLException e){
            System.out.print("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 학생DTOS;
    }

    //원하는 학생을 출력하는 메소드
    public 학생DTO 선택학생출력(String 사용자ID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM 사용자 NATURAL JOIN 학생 WHERE 사용자ID = " + 사용자ID;

        학생DTO 학생DTO = new 학생DTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();

            학생DTO.set사용자ID(rs.getString(1));
            학생DTO.set사용자명(rs.getString(2));
            학생DTO.set사용자PW(rs.getString(3));
            학생DTO.set주소(rs.getString(4));
            학생DTO.set주민번호(rs.getString(5));
            학생DTO.set사용자유형(rs.getInt(6));
            학생DTO.set전화번호(rs.getString(7));
            학생DTO.set학과명(rs.getString(8));
            학생DTO.set학년(rs.getInt(9));
            학생DTO.set신청과목수(rs.getInt(10));
            학생DTO.set신청총학점(rs.getInt(11));

            conn.commit();

        } catch(SQLException e){
            System.out.print("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        finally {
            try {
                if (conn != null && !rs.isClosed()) {
                    rs.close();
                }
                if (conn != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 학생DTO;
    }

    //학생의 학년을 수정하는 메소드
    public void 학년수정(학생DTO 학생DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE  학생 SET 학년 = ? WHERE 사용자ID = ? ";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 학생DTO.get학년());
            pstmt.setString(2, 학생DTO.get사용자ID());
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

    //학생을 추가하는 메소드
    //트리거를 이용하여 학생을 추가하면 사용자유형이 자동으로 변경됨
    public void 학생추가(학생DTO 학생DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String userInsertSql = "INSERT INTO 사용자 VALUES (?,?,?,?,?,?,?,?) ";
        String studentInsertSql =  "INSERT INTO 학생 VALUES (?,?,?,?)";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(userInsertSql);
            pstmt.setString(1, 학생DTO.get사용자ID());
            pstmt.setString(2,학생DTO.get사용자명());
            pstmt.setString(3, 학생DTO.get사용자PW());
            pstmt.setString(4,학생DTO.get주소());
            pstmt.setString(5, 학생DTO.get주민번호());
            pstmt.setInt(6, 0);
            pstmt.setString(7, 학생DTO.get전화번호());
            pstmt.setString(8,학생DTO.get학과명());
            pstmt.executeUpdate();
            conn.commit();
            pstmt = conn.prepareStatement(studentInsertSql);
            pstmt.setString(1, 학생DTO.get사용자ID());
            pstmt.setInt(2, 학생DTO.get학년());
            pstmt.setInt(3, 0);
            pstmt.setInt(4, 0);

            pstmt.execute();
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