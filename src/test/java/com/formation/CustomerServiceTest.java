package com.formation;

import com.formation.data.entity.CustomerEntity;
import com.formation.data.repository.CustomerRepository;
import com.formation.service.CustomerService;
import com.formation.web.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
/*
Ici, Mockito et PostGres nous permet d'automatiser la création des Faux Repository

→ Récap :
- Tests d'intégration : permet de mettre en place le CICD :
    - Permet de tester les liages et les fonctionnalités de toute la chaîne de développement (comment les classes intéragissent entre elles, comment les fonctionalités marchent)
    - Besoin des tests unitaires sur les fonctionnalités, pour permettre la mise en place des tests d'intégration par
- # des tests unitaires → Qui permet de tester une fonctionnalité mise en place dans les services

 */

@ExtendWith(MockitoExtension.class) //→  Ajoute MockitoExtension (qui est injecté via pom.xml → springboot test)

public class CustomerServiceTest {

    @InjectMocks //Injecte les mocks dans le service à tester (CustomerService)
    CustomerService customerService;

    @Mock //Crée un mock pour le repository
    CustomerRepository customerRepository;

/// TEST 1 de CustomerService : getAllCustomers
/*
Je veux tester que ma fonction de chercher les customers marche bien → En indiquant la taille de la liste à 2
Donc ici, je veux mettre en place une méthode privée getMockCustomers qui me permettra d'indiquer la taille de ma liste + ajout d'un customer
Puis, je teste avec la méthode :
Le test va se dérouler en 3 parties : 1 - Given → On donne la quantité de customer à tester
*/
    @Test
    void getAllCustomers() {
        Mockito.doReturn(getMockCustomers(2)).when(customerRepository).findAll();

        //2 - When : Appelle la méthode getAllCustomers à tester
        List<Customer> customerList = customerService.getAllCustomers();

        //3 - Then : Ce qui doit apparaître au niveau du test
        Assertions.assertEquals(2, customerList.size(), "La fonctionnalité ne me montre pas qu'il y a 2 personnes dans la base ");
    }

    /// TEST 2 de CustomerService : findByEmailCustomer
    @Test
    void findByEmailAddress() {
        /*
        - Mise en place d'un Mock → Pour ne pas écrire ou instancier la classe implicite customerEntity
        Si on instancie avec new CustomerEntity() → Obligé de définir ce customer Entity
         */
        CustomerEntity customerEntityFindEmail = getMockCustomerEntity();

        // 1 - Given → Permet de donner l'email address du customerEntity que l'on a créé et de retourner le customerEntity -
        Mockito.when(customerRepository.findByEmailAddress(customerEntityFindEmail.getEmailAddress())).thenReturn(customerEntityFindEmail);

        //2 - When : Appel de la méthode à tester →
        Customer customerCreated  = customerService.findByEmailAddress(customerEntityFindEmail.getEmailAddress());

        //3 - Then : Je vérifie que l'objet créé lors de l'appel de méthode n'est pas déjà présent  → Puis je vérifie le test fonctionne avec assertEquals
        Assertions.assertNotNull(customerCreated);
        Assertions.assertEquals("nibh@ultricesposuere.edu", customerEntityFindEmail.getEmailAddress(), "Ne marche pas sinon ");

    }


    @Test
    void updateCustomer() {
/*
- Mise en place d'un Mock → Pour ne pas écrire ou instancier la classe implicite customerEntity
Si on instancie avec new CustomerEntity() → Obligé de définir ce customer Entity
*/
        CustomerEntity customerEntityUpdate = getMockCustomerEntity();
/*
1 - Given → Je prends mon Customer, je l'update et je retourne mon customerEntityUpdate
Mockito va simuler les méthodes de Spring Data Jpa (pour éviter d'avoir les méthodes réélles) (car customerRepo extends CrudRepository)
- Donc pour udpate : On va sauvegarder le client et retourner le client
 */
        Mockito.when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntityUpdate);
/*  2 - When : Appel de la méthode à tester → update
/!\ Il faut définir ton customer avant de l'update !!
Attention pour l'UUID : je récupère un String UUID, je vais forcer à le convertir en String avec .toString
 */
        Customer customerToUpdate = new Customer(
                customerEntityUpdate.getCustomerId().toString(),
                customerEntityUpdate.getFirstName(),
                customerEntityUpdate.getLastName(),
                customerEntityUpdate.getEmailAddress(),
                customerEntityUpdate.getPhoneNumber(),
                customerEntityUpdate.getAddress());

        customerToUpdate = customerService.updateCustomer(customerToUpdate);

        //3 - Then : Je dois vérifier que ma méthode marche
        Assertions.assertNotNull(customerToUpdate);
        Assertions.assertEquals("testFirst", customerToUpdate.getFirstName(), "Test 3 non fonctional !");
    }




/*
Mise en place d'une méthode privée getMockCustomerEntity : que l'on va appeler pour éviter d'instancier à chaque fois
en début de test
*/
private CustomerEntity getMockCustomerEntity() {
        return new CustomerEntity(
                UUID.randomUUID(),
                "testFirst",
                "testLast",
                "nibh@ultricesposuere.edu",
                "555-515-1234",
                "1234 Test Street");
    }






    private Iterable<CustomerEntity> getMockCustomers(int size) {
        List<CustomerEntity> customerEntityList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            customerEntityList.add(new CustomerEntity
                    (UUID.randomUUID(),
                    "firstName" + i,
                    "lastName" + i,
                    "email@.com" + i,
                    "phone" +i,
                    "addess" + i));
        }
        return customerEntityList;
    }


}
