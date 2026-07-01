package com.logistics.serviceimpl;

import com.logistics.dto.CustomerDto;
import com.logistics.entity.Customer;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CustomerRepository;
import com.logistics.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    @Override @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public CustomerDto findById(Long id) {
        return toDto(repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "id", id)));
    }

    @Override
    public CustomerDto save(CustomerDto dto) {
        Customer c = new Customer();
        copyFields(dto, c);
        return toDto(repo.save(c));
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer c = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "id", id));
        copyFields(dto, c);
        return toDto(repo.save(c));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Customer", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<CustomerDto> search(String query) {
        return repo.search(query).stream().map(this::toDto).collect(Collectors.toList());
    }

    private void copyFields(CustomerDto dto, Customer c) {
        c.setName(dto.getName()); c.setCode(dto.getCode()); c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone()); c.setBillingAddress(dto.getBillingAddress());
        c.setShippingAddress(dto.getShippingAddress()); c.setCity(dto.getCity());
        c.setState(dto.getState()); c.setCountry(dto.getCountry()); c.setZipCode(dto.getZipCode());
        c.setContactPerson(dto.getContactPerson());
        c.setCustomerType(dto.getCustomerType() != null ? dto.getCustomerType() : Customer.CustomerType.RETAIL);
        c.setCreditLimit(dto.getCreditLimit() != null ? dto.getCreditLimit() : BigDecimal.ZERO);
        c.setStatus(dto.getStatus() != null ? dto.getStatus() : Customer.Status.ACTIVE);
        c.setNotes(dto.getNotes());
    }

    private CustomerDto toDto(Customer c) {
        return CustomerDto.builder()
                .id(c.getId()).name(c.getName()).code(c.getCode()).email(c.getEmail())
                .phone(c.getPhone()).billingAddress(c.getBillingAddress())
                .shippingAddress(c.getShippingAddress()).city(c.getCity())
                .state(c.getState()).country(c.getCountry()).zipCode(c.getZipCode())
                .contactPerson(c.getContactPerson()).customerType(c.getCustomerType())
                .creditLimit(c.getCreditLimit()).totalRevenue(c.getTotalRevenue())
                .status(c.getStatus()).notes(c.getNotes()).build();
    }
}
