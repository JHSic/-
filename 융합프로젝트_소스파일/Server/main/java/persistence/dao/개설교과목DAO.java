package persistence.dao;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.mapper.개설교과목Mapper;
import persistence.dto.학생DTO;
import persistence.dto.개설교과목DTO;

public class 개설교과목DAO {
    private final DataSource ds = PooledDataSource.getDataSource();
    private SqlSessionFactory sqlSessionFactory = null;

    public 개설교과목DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //개설 교과목의 정보를 입력하는 메소드, 강의시간 입력과 강의계획서입력기간을 같이 받아서 설정해준다.
    //개설 교과목 입력 시 이미 같은 학년의 수강신청 기간이 설정되어 있으면 트리거를 이용하여 같은 학년의 수강신청기간과 같게 설정해준다.
    public void 개설교과목정보입력(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 개설교과목 정보 입력
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목정보입력(개설교과목DTO);

            // 강의시간 정보 입력
            session.insert("mapper.강의시간mapper.강의시간입력", 개설교과목DTO);

            //강의계획서입력기간 정보 입력
            session.insert("mapper.강의계획서입력기간mapper.강의계획서입력기간입력", 개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //강의계획을 입력하는 메소드
    public void 개설교과목강의계획서입력(개설교과목DTO 개설교과목DTO) throws Exception { // update라서 수정으로 명시하는것이 맞지만 실질적으로는 입력이기 때문에 메소드명을 입력으로 설정
        SqlSession session = sqlSessionFactory.openSession();
        try{
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목DTO 해당개설교과목DTO = 개설교과목Mapper.개설교과목선택출력(개설교과목DTO.get교과목번호());
            개설교과목Mapper.개설교과목강의계획서입력(개설교과목DTO);

            //입력시간, 종료시간을 비교하여 해당 과목이 강의계획서를 작성할 수 있는 기간인지 파악하고 아니라면 예외를 던져준다.
            if((해당개설교과목DTO.get강의계획서입력시작시간() == null || 해당개설교과목DTO.get강의계획서입력종료시간() == null ||
                    해당개설교과목DTO.get강의계획서입력시작시간().isAfter(LocalDateTime.now()) ||
                    해당개설교과목DTO.get강의계획서입력종료시간().isBefore(LocalDateTime.now()))
                   )
            {
                throw new Exception("해당 교과목은 강의계획서 입력 기간이 아닙니다.");
            }

           session.commit();
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    //개설교과목의 전체적인 정보를 수정할 수 있는 메소드
    public void 개설교과목전체정보수정(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목전체정보수정(개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //개설교과목의 최대수강인원을 수정할 수 있는 메소드 - 사용자가 원하는 정보를 버튼으로 골라서 바꾸는 것이 아닌
    //실제 계정의 정보를 수정하는 것처럼 바꾸려는 것만 바꾸는 식이 맞다고 생각해서 네트워크 구현에서 전체 정보를 수정하는 식으로 변경하여 미사용
    public void 개설교과목최대수강인원수정(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목최대수강인원수정(개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //개설교과목의 수강신청인원을 수정할 수 있는 메소드 - 사용자가 원하는 정보를 버튼으로 골라서 바꾸는 것이 아닌
    //실제 계정의 정보를 수정하는 것처럼 바꾸려는 것만 바꾸는 식이 맞다고 생각해서 네트워크 구현에서 전체 정보를 수정하는 식으로 변경하여 미사용
    public void 개설교과목수강신청인원수정(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목수강신청인원수정(개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //개설교과목의 강의실을 수정할 수 있는 메소드 - 사용자가 원하는 정보를 버튼으로 골라서 바꾸는 것이 아닌
    //실제 계정의 정보를 수정하는 것처럼 바꾸려는 것만 바꾸는 식이 맞다고 생각해서 네트워크 구현에서 전체 정보를 수정하는 식으로 변경하여 미사용
    public void 개설교과목강의실수정(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목강의실수정(개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //개설교과목의 강의계획획을 수정할 수 있는 메소드 - 사용자가 원하는 정보 버튼으로 골라서 바꾸는 것이 아닌
    //실제 계정의 정보를 수정하는 것처럼 바꾸려는 것만 바꾸는 식이 맞다고 생각해서 네트워크 구현에서 전체 정보를 수정하는 식으로 변경하여 미사용
    public void 개설교과목강의계획수정(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목강의계획수정(개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //개설교과목의 분반을 수정할 수 있는 메소드 - 사용자가 원하는 정보를 버튼으로 골라서 바꾸는 것이 아닌
    //실제 계정의 정보를 수정하는 것처럼 바꾸려는 것만 바꾸는 식이 맞다고 생각해서 네트워크 구현에서 전체 정보를 수정하는 식으로 변경하여 미사용
    public void 개설교과목분반수정(개설교과목DTO 개설교과목DTO) {

        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목분반수정(개설교과목DTO);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    //개설교과목 목록을 전체 출력하는 메소드
    public List<개설교과목DTO> 개설교과목전체출력() {

        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            list = 개설교과목Mapper.개설교과목전체출력();
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    //교과목 번호를 이용하여 원하는 개설교과목만 출력하는 메소드
    public 개설교과목DTO 개설교과목선택출력(String 교과목번호) {

        개설교과목DTO 개설교과목DTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            개설교과목DTO = 개설교과목Mapper.개설교과목선택출력(교과목번호);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return 개설교과목DTO;
    }

    //원하는 학년의 개설교과목을 출력하는 메소드
    public List<개설교과목DTO> 학년별개설교과목출력(int 대상학년) {

        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            list = 개설교과목Mapper.학년교수별개설교과목출력(대상학년, null); //동적쿼리이용
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    //원하는 교수의 개설교과목을 출력하는 메소드
    public List<개설교과목DTO> 교수별개설교과목출력(String 교수명) {

        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            list = 개설교과목Mapper.학년교수별개설교과목출력(null, 교수명); //동적쿼리이용
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    //원하는 학년, 교수의 개셜교과목을 출력하는 메소드
    public List<개설교과목DTO> 학년교수별개설교과목출력(String 교수명, int 대상학년) {

        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            list = 개설교과목Mapper.학년교수별개설교과목출력(대상학년, 교수명); //동적쿼리이용
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    //교수가 자신의 담당개설교과목의 강의 계획을 출력할 때 사용하는 메소드
    public List<개설교과목DTO> 담당개설교과목강의계획서출력(String 사용자ID) {
        List<개설교과목DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper = session.getMapper(개설교과목Mapper.class);
            list = 개설교과목Mapper.담당개설교과목강의계획서출력(사용자ID);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return list;
    }

    //db 시연에서 사용한 메소드
    public List<학생DTO> 개설교과목수강생정보출력(개설교과목DTO 개설교과목DTO, String 교과목번호, int 페이징) {

        List<학생DTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper =  session.getMapper(개설교과목Mapper.class);
            list = 개설교과목Mapper.개설교과목수강생정보출력(개설교과목DTO, 교과목번호, 페이징);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return list;
    }

    //개설교과목을 삭제하는 메소드
    public void 개설교과목삭제(개설교과목DTO 개설교과목DTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            개설교과목Mapper 개설교과목Mapper = session.getMapper(개설교과목Mapper.class);
            개설교과목Mapper.개설교과목삭제(개설교과목DTO);
            session.commit();
        } catch(Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }
}