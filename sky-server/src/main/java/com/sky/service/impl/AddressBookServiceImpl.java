package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> list() {

            return addressBookMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public AddressBook getDefault() {
        return addressBookMapper.getDefault(BaseContext.getCurrentId());
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.update(addressBook);
    }

    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);

    }

    @Override
    public AddressBook getById(Long id) {
       return  addressBookMapper.getById(id);
    }

    @Override
    public void updateDefault(AddressBook addressBook) {
        AddressBook addressBook1=new AddressBook();
        addressBook1=addressBookMapper.selcetDefault();
        if(addressBook1!=null){
            addressBook1.setIsDefault(0);
            addressBookMapper.updateaa(addressBook1);
        }
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        addressBookMapper.updateDefault(addressBook);



    }


}
