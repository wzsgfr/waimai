package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface AddressBookMapper {
    @Insert("insert into address_book (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void insert(AddressBook addressBook);
    @Select("select * from address_book where user_id = #{currentId}")
    List<AddressBook> list(Long currentId);
    @Select("select * from address_book where is_default=1 and user_id = #{currentId}")
    AddressBook getDefault(Long currentId);
    @Update("update address_book set city_code=#{cityCode},city_name=#{cityName},consignee=#{consignee} ,detail=#{detail},district_code=#{districtCode},district_name=#{districtName},label=#{label}," +
            "phone=#{phone},province_code=#{provinceCode},province_name=#{provinceName},sex=#{sex} where user_id = #{userId}")
    void update(AddressBook addressBook);
    @Delete("delete from address_book where id = #{id}")
    void delete(Long id);
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);
    @Update("update address_book set is_default=1 where user_id = #{userId}")
    void updateDefault(AddressBook addressBook);
    @Select("select * from address_book where is_default=1 and user_id = #{userId}")
    AddressBook selcetDefault();
    @Update("update address_book set is_default=0 where user_id = #{userId}")
    void updateaa(AddressBook addressBook1);
}
