//개설교과목DAO 인터페이스
package persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import persistence.dto.개설교과목DTO;
import persistence.dto.학생DTO;

public interface 개설교과목Mapper {
    //개설교과목과 연관된 모든 정보를 출력
    String 개설교과목출력sql =
            "SELECT * FROM 교과목 \n" +
                    "NATURAL JOIN 개설교과목 \n" +
                    "NATURAL JOIN 강의시간 \n" +
                    "NATURAL JOIN 수강신청기간\n" +
                    "NATURAL JOIN 강의계획서입력기간 \n" +
                    "NATURAL JOIN (SELECT 교과목번호, 수강신청시작시간 <= now() AND now() <= 수강신청종료시간 AS 수강신청가능여부 \n" +
                    "FROM 수강신청기간) AS 수강신청가능여부 \n" +
                    "NATURAL JOIN (SELECT 교과목번호, 강의계획서입력시작시간 <= now() AND now() <= 강의계획서입력종료시간 AS 강의계획서입력가능여부 \n" +
                    "FROM 강의계획서입력기간) AS 강의계획서입력가능여부";

    @Select(개설교과목출력sql)
    @Results(id = "개설교과목mapper", value = {
            @Result( property = "교과목번호", column="교과목번호"),
            @Result( property = "교과목이름", column="교과목이름"),
            @Result( property = "대상학년", column="대상학년"),
            @Result( property = "교육과정", column="교육과정"),
            @Result( property = "이수구분", column="이수구분"),
            @Result( property = "학점", column="학점"),
            @Result( property = "설계과목여부", column="설계과목여부"),
            @Result( property = "개설여부", column="개설여부"),

            @Result( property = "최대수강인원", column="최대수강인원"),
            @Result( property = "수강신청인원", column="수강신청인원"),
            @Result( property = "강의실", column="강의실"),
            @Result( property = "강의계획", column="강의계획"),
            @Result( property = "분반", column="분반"),

            @Result( property = "수업요일", column="수업요일"),
            @Result( property = "수업시작시간", column="수업시작시간"),
            @Result( property = "수업종료시간", column="수업종료시간"),

            @Result( property = "수강신청시작시간", column="수강신청시작시간"),
            @Result( property = "수강신청종료시간", column="수강신청종료시간"),

            @Result( property = "수강신청가능여부", column="수강신청가능여부"),

            @Result( property = "강의계획서입력시작시간", column="강의계획서입력시작시간"),
            @Result( property = "강의계획서입력종료시간", column="강의계획서입력종료시간"),

            @Result( property = "강의계획서입력가능여부", column="강의계획서입력가능여부")
    })
    public List<개설교과목DTO> 개설교과목전체출력();

    //원하는 개설교과목만 출력
    @Select(개설교과목출력sql +
            "\n WHERE 교과목번호 = #{선택교과목번호}")
    public 개설교과목DTO 개설교과목선택출력(@Param("선택교과목번호")String 교과목번호);

    //개설교과목의 정보를 입력
    @Insert("INSERT INTO 개설교과목 VALUES (#{교과목번호}, #{최대수강인원}, #{수강신청인원}, #{강의실}, #{강의계획}, #{분반})")
    public void 개설교과목정보입력(개설교과목DTO 개설교과목DTO);

    //개설교과목의 전체 정보를 수정
    @Update("UPDATE 개설교과목 SET 최대수강인원 = #{최대수강인원}, 강의실 = #{강의실}, 분반 = #{분반} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목전체정보수정(개설교과목DTO 개설교과목DTO);

    //나머지 수정은 전체정보수정만 이용하도록 구현하여 미사용
    @Update("UPDATE 개설교과목 SET 최대수강인원 = #{최대수강인원} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목최대수강인원수정(개설교과목DTO 개설교과목DTO);

    @Update("UPDATE 개설교과목 SET 강의계획 = #{강의계획} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목강의계획서입력(개설교과목DTO 개설교과목DTO);

    @Update("UPDATE 개설교과목 SET 수강신청인원 = #{수강신청인원} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목수강신청인원수정(개설교과목DTO 개설교과목DTO);

    @Update("UPDATE 개설교과목 SET 강의실 = #{강의실} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목강의실수정(개설교과목DTO 개설교과목DTO);

    @Update("UPDATE 개설교과목 SET 강의계획 = #{강의계획} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목강의계획수정(개설교과목DTO 개설교과목DTO);

    @Update("UPDATE 개설교과목 SET 분반 = #{분반} WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목분반수정(개설교과목DTO 개설교과목DTO);

    //동적 쿼리를 이용하여 학년, 교수의 정보에 해당하는 개설교과목 출력
    @SelectProvider(type = persistence.mapper.개설교과목sql.class, method ="학년교수별개설교과목출력")
    @ResultMap("개설교과목mapper")
    List<개설교과목DTO> 학년교수별개설교과목출력(@Param("대상학년")Integer 대상학년, @Param("교수명")String 교수명);

    //개설교과목의 수강생을 출력
    @Select("select 사용자ID, 사용자명, 전화번호, 학과명, 학년, 신청과목수, 신청총학점 from 사용자\n" +
            "natural join 수강신청\n" +
            "natural join 학생\n" +
            "where 교과목번호 = (select 교과목번호\n" +
            "from 개설교과목\n" +
            "where 교과목번호 = #{교과목번호})\n" +
            "LIMIT #{페이징}, 2")
    public List<학생DTO> 개설교과목수강생정보출력(개설교과목DTO 개설교과목DTO, @Param("교과목번호")String 교과목번호, @Param("페이징")int 페이징);

    //로그인하고 있는 교수가 담당하고 있는 개설교과목의 강의계획서 출력
    @Select("SELECT 교과목번호, 교과목이름, 강의계획 \n" +
            "FROM 개설교과목 \n" +
            "natural join 교과목 \n" +
            "WHERE 교과목번호 = ANY(SELECT 교과목번호 FROM 담당과목목록 WHERE 사용자ID = #{사용자ID});")
    List<개설교과목DTO> 담당개설교과목강의계획서출력(@Param("사용자ID")String 사용자ID);

    //개설교과목 삭제
    @Delete("DELETE FROM 개설교과목 WHERE 교과목번호 = #{교과목번호}")
    public void 개설교과목삭제(개설교과목DTO 개설교과목DTO);
}
