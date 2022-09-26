package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.사용자DTO;
import persistence.dto.학생DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class 사용자DAO {
   private final DataSource ds = PooledDataSource.getDataSource();

   // 순수한 사용자의 정보를 알기 위한 일이 없으므로 미사용
    public List<사용자DTO> 전체사용자출력(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<사용자DTO> 사용자DTOS = new ArrayList<사용자DTO>();
        String sql = "SELECT * FROM 사용자";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                사용자DTO 사용자DTO = new 사용자DTO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8));
                사용자DTOS.add(사용자DTO);
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
        return 사용자DTOS;
    }

    //선택사용자를 출력하는 메소드 -> 간접적으로 다른 함수에서 호출용도로 사용
    public 사용자DTO 선택사용자출력(String 사용자ID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM 사용자 WHERE 사용자ID = " + "\"" +사용자ID + "\"";

        사용자DTO 사용자DTO = new 사용자DTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();

            사용자DTO.set사용자ID(rs.getString(1));
            사용자DTO.set사용자명(rs.getString(2));
            사용자DTO.set사용자PW(rs.getString(3));
            사용자DTO.set주소(rs.getString(4));
            사용자DTO.set주민번호(rs.getString(5));
            사용자DTO.set사용자유형(rs.getInt(6));
            사용자DTO.set전화번호(rs.getString(7));
            사용자DTO.set학과명(rs.getString(8));

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

    //교수, 관리자, 학생의 공통 정보를 수정하는 경우 호출하여 사용
    public void 사용자정보수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 사용자명 = ?, 사용자PW = ?, 주소 = ?, 주민번호 = ?, 전화번호 =?, 학과명 = ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get사용자명());
            pstmt.setString(2, 사용자DTO.get사용자PW());
            pstmt.setString(3, 사용자DTO.get주소());
            pstmt.setString(4, 사용자DTO.get주민번호());
            pstmt.setString(5, 사용자DTO.get전화번호());
            pstmt.setString(6, 사용자DTO.get학과명());
            pstmt.setString(7, 사용자DTO.get사용자ID());
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

    //사용자정보수정에서 하나만 받아서 사용할 수 있도록 네트워크를 프로그래밍하여 미사용
    public void 사용자명수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 사용자명 = ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get사용자명());
            pstmt.setString(2, 사용자DTO.get사용자ID());
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

    //사용자정보수정에서 하나만 받아서 사용할 수 있도록 네트워크를 프로그래밍하여 미사용
    public void 사용자PW수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 사용자PW = ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get사용자PW());
            pstmt.setString(2, 사용자DTO.get사용자ID());
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

    //사용자정보수정에서 하나만 받아서 사용할 수 있도록 네트워크를 프로그래밍하여 미사용
    public void 주소수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 주소 = ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get주소());
            pstmt.setString(2, 사용자DTO.get사용자ID());
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

    //사용자정보수정에서 하나만 받아서 사용할 수 있도록 네트워크를 프로그래밍하여 미사용
    public void 주민번호수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 주민번호 = ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get주민번호());
            pstmt.setString(2, 사용자DTO.get사용자ID());
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

    //사용자정보수정에서 하나만 받아서 사용할 수 있도록 네트워크를 프로그래밍하여 미사용
    public void 전화번호수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 전화번호 = ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get전화번호());
            pstmt.setString(2, 사용자DTO.get사용자ID());
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

    //사용자정보수정에서 하나만 받아서 사용할 수 있도록 네트워크를 프로그래밍하여 미사용
    public void 학과명수정(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " UPDATE 사용자 SET 학과명= ? WHERE 사용자ID = ?";

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get학과명());
            pstmt.setString(2, 사용자DTO.get사용자ID());
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

    //사용자를 삭제하는 메소드
    public void 사용자삭제(사용자DTO 사용자DTO){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " DELETE FROM 사용자 WHERE 사용자ID = ?";
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, 사용자DTO.get사용자ID());
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