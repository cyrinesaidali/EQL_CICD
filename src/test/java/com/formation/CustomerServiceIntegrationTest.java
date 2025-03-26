package com.formation;

import com.formation.data.entity.CustomerEntity;
import com.formation.service.CustomerService;
import com.formation.web.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

/*
- Ici, les tests d'intégration : Permettent de tester le chemin entre le DAO et les Services
- Intervient après la mise en place des tests unitaires
 */


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CustomerServiceIntegrationTest {

    @Autowired
    CustomerService customerService;

    @Test
    void getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomers();
        Assertions.assertEquals(5, customerList.size());
    }

    @Test
    void findByEmailAddress() {
        CustomerEntity customerEntity = getCustomerEntity();
        Customer customer = customerService.findByEmailAddress(customerEntity.getEmailAddress());
        Assertions.assertNotNull(customer);
        Assertions.assertEquals("penatibus.et@lectusa.com", customerEntity.getEmailAddress());

    }

    @Test
    void updateCustomer() {
//Creation d'un nouveau customer
        Customer customerToUpdate = new Customer(
                UUID.randomUUID().toString(),
                "Test",
                "Test",
                "test@lectusa.com",
                "(901) 166-8355555555",
                "556 Lakewood Park, Bismarck, ND 585055555555");

        customerToUpdate = customerService.addCustomer(customerToUpdate);
        customerToUpdate.setFirstName("Cally Two Point Zero");
        customerToUpdate = customerService.updateCustomer(customerToUpdate);

        Assertions.assertEquals("Cally Two Point Zero", customerToUpdate.getFirstName(), "Test Update Do not Work !");
    }


    private CustomerEntity getCustomerEntity() {
        return new CustomerEntity(
                UUID.randomUUID(),
                "Cally",
                "Reynolds",
                "penatibus.et@lectusa.com",
                "(901) 166-8355",
                "556 Lakewood Park, Bismarck, ND 58505");
    }



}
