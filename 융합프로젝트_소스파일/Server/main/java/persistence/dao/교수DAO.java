package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.관리자DTO;
import persistence.dto.교수DTO;
import persistence.dto.사용자DTO;
import persistence.dto.학생DTO;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class 교수DAO {
    private final DataSource ds = PooledDataSource.getDataSource();

    //전체 교수의 목록을 출력하는 메소드. 사용자 테이블과 교수 테이블을 조인하여 사용
    public List<교수DTO> 전체교수출력(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<교수DTO> 교수DTOS = new ArrayList<교수DTO>();
        String sql = "SELECT * FROM 사용자 NATURAL JOIN 교수";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                교수DTO 교수DTO = new 교수DTO();
                교수DTO.set사용자ID(rs.getString(1));
                교수DTO.set사용자명(rs.getString(2));
                교수DTO.set사용자PW(rs.getString(3));
                교수DTO.set주소(rs.getString(4));
                교수DTO.set주민번호(rs.getString(5));
                교수DTO.set사용자유형(rs.getInt(6));
                교수DTO.set전화번호(rs.getString(7));
                교수DTO.set학과명(rs.getString(8));
                교수DTO.set교수실(rs.getString(9));
                교수DTO.set교수실전화번호(rs.getString(10));
                교수DTOS.add(교수DTO);
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
        return 교수DTOS;
    }

    //교번(사용자ID)을 이용해 선택 교수를 출력하는 메소드
    public 교수DTO 선택교수출력(String 사용자ID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM 사용자 NATURAL JOIN 교수 WHERE 사용자ID = " + "\"" +사용자ID + "\"";
        교수DTO 교수DTO = new 교수DTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {

                교수DTO.set사용자ID(rs.getString(1));
                교수DTO.set사용자명(rs.getString(2));
                교수DTO.set사용자PW(rs.getString(3));
                교수DTO.set주소(rs.getString(4));
                교수DTO.set주민번호(rs.getString(5));
                교수DTO.set사용자유형(rs.getInt(6));
                교수DTO.set전화번호(rs.getString(7));
                교수DTO.set학과명(rs.getString(8));
                교수DTO.set교수실(rs.getString(9));
                교수DTO.set교수실전화번호(rs.getString(10));
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
        return 교수DTO;
    }

    //해당 교과목의 담당 교수를 출력하는 메소드. 개설교과목, 담당과목목록과 조인하여 사용
    public 사용자DTO 담당교수출력(String 교과목번호) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT 사용자ID, 사용자명 FROM 사용자\n" +
                "NATURAL JOIN 개설교과목\n" +
                "NATURAL JOIN 담당과목목록\n" +
                "WHERE 교과목번호 = " +  "\""  +  교과목번호 +"\"";
        사용자DTO 사용자DTO = new 사용자DTO();
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();

            사용자DTO.set사용자ID(rs.getString(1));
            사용자DTO.set사용자명(rs.getString(2));

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
        return 사용자DTO;
    }

    //교수실을 수정하는 메소드
    public void 교수실수정(교수DTO 교수DTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE  교수 SET 교수실 = ? WHERE 사용자ID = ? ";

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 교수DTO.get교수실());
            pstmt.setString(2, 교수DTO.get사용자ID());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.print("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
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

    //교수실의 전화번호를 수정하는 메소드
    public void 교수실전화번호수정(교수DTO 교수DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE  교수 SET 교수실전화번호 = ? WHERE 사용자ID = ? ";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 교수DTO.get교수실전화번호());
            pstmt.setString(2, 교수DTO.get사용자ID());
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

    //사용자 테이블에 교수에서의 사용자에 대한 공통정보를 추가하고 교수 고유의 정보는 교수 테이블에 추가
    //트리거를 이용하여 사용자 테이블에 사용자의 유형이 자동으로 변경
    public void 교수추가(교수DTO 교수DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String userInsertSql = "INSERT INTO 사용자 VALUES (?,?,?,?,?,?,?,?) ";
        String professorInsertSql =  "INSERT INTO 교수 VALUES (?,?,?)";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(userInsertSql);
            pstmt.setString(1, 교수DTO.get사용자ID());
            pstmt.setString(2,교수DTO.get사용자명());
            pstmt.setString(3, 교수DTO.get사용자PW());
            pstmt.setString(4,교수DTO.get주소());
            pstmt.setString(5, 교수DTO.get주민번호());
            pstmt.setInt(6,교수DTO.get사용자유형());
            pstmt.setString(7, 교수DTO.get전화번호());
            pstmt.setString(8,교수DTO.get학과명());
            pstmt.executeUpdate();
            conn.commit();
            pstmt = conn.prepareStatement(professorInsertSql);
            pstmt.setString(1, 교수DTO.get사용자ID());
            pstmt.setString(2, 교수DTO.get교수실());
            pstmt.setString(3, 교수DTO.get교수실전화번호());

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
