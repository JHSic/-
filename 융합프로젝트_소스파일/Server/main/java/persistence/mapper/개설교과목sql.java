package persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;


public class 개설교과목sql {

    //동적 쿼리를 이용하여 학년, 교수 정보에 해당하는 교수를 출력하는 메소드
    public String 학년교수별개설교과목출력(@Param("대상학년") final Integer 대상학년, @Param("교수명") final String 교수명) {

        SQL 학년교수별개설교과목출력sql;
        학년교수별개설교과목출력sql = new SQL() {{
            SELECT("*");
            FROM("교과목, 개설교과목, 강의시간, 수강신청기간,  강의계획서입력기간");
            WHERE("교과목.교과목번호 = 개설교과목.교과목번호"); AND();
            WHERE("개설교과목.교과목번호 = 강의시간.교과목번호"); AND();
            WHERE("개설교과목.교과목번호 = 수강신청기간.교과목번호"); AND();
            WHERE("개설교과목.교과목번호 = 강의계획서입력기간.교과목번호");

            if(!(대상학년 == null)){
                AND(); WHERE("교과목.대상학년 = #{대상학년}");
            }
            if(!(교수명 == null)) {
                SQL 교번검색 = new SQL() {{
                    SELECT("사용자.사용자ID");
                    FROM("사용자");
                    WHERE("사용자명 = #{교수명}");
                }}; //교수 이름에 해당하는 사용자ID를 검색
                SQL 교과목번호검색 = new SQL(){{
                    SELECT("교과목번호");
                    FROM("담당과목목록");
                    WHERE("사용자ID = (" + 교번검색 + "))");
                }}; //교수 이름에 해당하는 교번의 교과목번호를 검색
                AND(); WHERE("개설교과목.교과목번호 = ANY(" + 교과목번호검색);
            }
        }};
        return 학년교수별개설교과목출력sql.toString();
    }
}