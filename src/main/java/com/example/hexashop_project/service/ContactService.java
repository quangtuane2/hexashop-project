package com.example.hexashop_project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hexashop_project.model.Contact;
import com.example.hexashop_project.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact saveOrUpdate(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteById(Integer id) {
        contactRepository.deleteById(id);
    }
}