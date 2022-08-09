package br.com.agenda.controller;

import br.com.agenda.model.entities.*;
import br.com.agenda.model.repositories.AddressRepository;
import br.com.agenda.model.repositories.CustomerRepository;
import br.com.agenda.model.repositories.TelephoneRepository;
import br.com.agenda.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "customer")
@RequestMapping("/customer")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TelephoneRepository telephoneRepository;
    @Autowired
    private AddressRepository addressRepository;

    @ApiOperation("metodo para listar customers")
    @GetMapping
    public ResponseEntity<List<Customer>> listCustomers (){
       return ResponseEntity.ok(customerService.listCustomers());
    }

    @GetMapping(path="/{cpf}")
    public ResponseEntity<Customer> customerByCpf(@PathVariable String cpf)  {
        Customer customerFound = customerService.findByCpf(cpf);

        return ResponseEntity.ok(customerFound);
    }

    @PostMapping
    public ResponseEntity<Customer> newCustomer(@RequestBody @Valid Customer customer) {

        return ResponseEntity.ok(customerService.addCustomer(customer));
    }

    @PutMapping(value="/{cpf}")
    public ResponseEntity <Customer> editCustomer(@PathVariable("cpf") String cpf,
                                                   @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.editCustomer(cpf, customer));
    }

    @DeleteMapping(path="/{cpf}")
    public ResponseEntity<Customer> delete(@PathVariable String cpf){
            customerService.removeCustomer(cpf);
            return ResponseEntity.noContent().build();
    }
}
