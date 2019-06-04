package cph.dao;

import cph.domain.Member;
import org.apache.ibatis.annotations.Select;

public interface IMemberDao {
    @Select("select * from member where id =#{ordersId}")
    public Member findById(String ordersId) throws Exception;

}
