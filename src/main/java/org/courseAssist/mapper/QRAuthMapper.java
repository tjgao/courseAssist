package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.courseAssist.model.QRAuth;

public interface QRAuthMapper {
	@Select("select * from authqr where code = #{code}")
	QRAuth getQRAuthByCode(@Param("code") String code);
	
	@Insert("insert into authqr (code, ip, port) values (#{code}, #{ip}, #{port})")
	void insertQRAuthByCode(QRAuth q);
	
	@Update("update authqr set token=#{token}, uid=#{uid}, sid=#{sid}, time=#{time} where code = #{code}")
	void updateQRAuth(QRAuth q);
	
	@Update("update authqr set token=#{token} where code=#{code}")
	void updateToken(String code, String token);
	
	@Delete("delete * from authqr where code = #{code}")
	void cleanQRAuth(@Param("code") String code);
	
	@Select("select * from authqr where sid=#{sid} and timestampdiff(second, time, now()) < #{limit}")
	List<QRAuth> getQRAuthBySid(@Param("sid") int sid, @Param("limit") int seconds);
}
