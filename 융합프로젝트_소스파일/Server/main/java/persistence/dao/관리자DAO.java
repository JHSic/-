package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.관리자DTO;
import persistence.dto.교수DTO;
import persistence.dto.사용자DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class 관리자DAO {
    private final DataSource ds = PooledDataSource.getDataSource();

    //관리자 목록을 출력하는 메소드
    public List<관리자DTO> 관리자출력(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<관리자DTO> 관리자DTOS = new ArrayList<관리자DTO>();
        String sql = "SELECT * FROM 사용자 NATURAL JOIN 관리자 WHERE 사용자ID = 관리자.사용자ID";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                관리자DTO 관리자DTO = new 관리자DTO();
                관리자DTO.set사용자ID(rs.getString(1));
                관리자DTO.set사용자명(rs.getString(2));
                관리자DTO.set사용자PW(rs.getString(3));
                관리자DTO.set주소(rs.getString(4));
                관리자DTO.set주민번호(rs.getString(5));
                관리자DTO.set사용자유형(rs.getInt(6));
                관리자DTO.set전화번호(rs.getString(7));
                관리자DTO.set학과명(rs.getString(8));
                관리자DTOS.add(관리자DTO);
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
        return 관리자DTOS;
    }

    //관리자가 여러명이라는 가정을 사용하여 원하는 관리자를 출력
    public 관리자DTO 선택관리자출력(String 사용자ID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM 사용자 NATURAL JOIN 관리자 WHERE 사용자ID = 관리자.사용자ID AND 사용자ID = " + 사용자ID;
        관리자DTO 관리자DTO = new 관리자DTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {

                관리자DTO.set사용자ID(rs.getString(1));
                관리자DTO.set사용자명(rs.getString(2));
                관리자DTO.set사용자PW(rs.getString(3));
                관리자DTO.set주소(rs.getString(4));
                관리자DTO.set주민번호(rs.getString(5));
                관리자DTO.set사용자유형(rs.getInt(6));
                관리자DTO.set전화번호(rs.getString(7));
                관리자DTO.set학과명(rs.getString(8));
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
        return 관리자DTO;
    }

    //관리자를 추가하는 메소드 사용자 테이블에 값을 넣고 사용자ID를 관리자 테이블에 넣어준다.
    //트리거를 이용하여 관리자 추가 시 자동으로 사용자 유형이 변경됨
    public void 관리자추가(관리자DTO 관리자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String userInsertSql = "INSERT INTO 사용자 VALUES (?,?,?,?,?,?,?,?) ";
        String adminInsertSql =  "INSERT INTO 관리자 VALUES (?)";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(userInsertSql);
            pstmt.setString(1, 관리자DTO.get사용자ID());
            pstmt.setString(2,관리자DTO.get사용자명());
            pstmt.setString(3, 관리자DTO.get사용자PW());
            pstmt.setString(4,관리자DTO.get주소());
            pstmt.setString(5, 관리자DTO.get주민번호());
            pstmt.setInt(6,관리자DTO.get사용자유형()); //자동설정된다.
            pstmt.setString(7, 관리자DTO.get전화번호());
            pstmt.setString(8,관리자DTO.get학과명());
            pstmt.executeUpdate();
            conn.commit();
            pstmt = conn.prepareStatement(adminInsertSql);
            pstmt.setString(1, 관리자DTO.get사용자ID());
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
