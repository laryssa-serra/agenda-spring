package br.com.agenda.service;

import br.com.agenda.model.entities.*;
import br.com.agenda.model.repositories.AddressRepository;
import br.com.agenda.model.repositories.CustomerRepository;
import br.com.agenda.model.repositories.TelephoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TelephoneRepository telephoneRepository;
    @Autowired
    private AddressRepository addressRepository;
    private List<Customer> customerList = new ArrayList<>();

    public List<Customer> listCustomers() {
            return customerRepository.findAll();
    }

     public Customer findByCpf(String cpf) throws NotFoundException{
        return customerRepository.findByCpf(cpf).orElseThrow(() -> new NotFoundException("Usuário não encontrado a partir do CPF informado."));
    }

    public Customer addCustomer(Customer customer) throws InvalidFormatException {

        customer.setActivated(true);

        if (!isCpfValid(customer.getCpf())) {
            throw new InvalidFormatException("Usuário não cadastrado. CPF inválido.");
        }

        for (var telephone : customer.getTelephones()) {
            if (!areTelephonesValids(customer.getTelephones())) {
                throw new InvalidFormatException("O número de telefone inserido está incorreto.");
            }
            telephone.setCustomer(customer);
        }

        for(var address : customer.getAddresses()){
            address.setCustomer(customer);
        }

        if (!isEmailValid(customer.getEmail())) {
            throw new InvalidFormatException("Usuário não cadastrado. E-mail inserido não é válido");
        }

        Customer customerSaved = customerRepository.save(customer);

        return customerSaved;
    }

    public Customer editCustomer(String cpf, Customer customerChanges) throws InvalidFormatException {

        Customer customerFound = customerRepository.findByCpf(cpf).get();

        for (var telephone : customerChanges.getTelephones()) {
            if (!areTelephonesValids(customerChanges.getTelephones()))
                throw new InvalidFormatException("O número de telefone inserido está incorreto.");

                telephoneRepository.findById(telephone.getId())
                        .map(dbTelephone ->{
                            dbTelephone.setDdd(telephone.getDdd());
                            dbTelephone.setPhoneNumber(telephone.getPhoneNumber());
                            Telephone telephoneChanged = telephoneRepository.save(dbTelephone);
                            return ResponseEntity.ok().body(telephoneChanged);
                        }).orElse(ResponseEntity.notFound().build());
        }

        for (var address : customerChanges.getAddresses()) {
            addressRepository.findById(address.getId())
                    .map(dbAddress ->{
                        dbAddress.setState(address.getState());
                        dbAddress.setCity(address.getCity());
                        dbAddress.setStreet(address.getStreet());
                        dbAddress.setZipCode(address.getZipCode());
                        dbAddress.setReference(address.getReference());
                        Address addressChanged = addressRepository.save(dbAddress);
                        return ResponseEntity.ok().body(addressChanged);
                    }).orElse(ResponseEntity.notFound().build());
        }

        if (!isEmailValid(customerChanges.getEmail())){
            throw new InvalidFormatException("E-mail não pode ser salvo. E-mail inserido não é válido.");
        }

        customerFound.setFullName(customerChanges.getFullName());
        customerFound.setBirthDate(customerChanges.getBirthDate());
        customerFound.setEmail(customerChanges.getEmail());
        Customer customerChanged = customerRepository.save(customerFound);

        return customerChanged;
    }

    public void removeCustomer(String cpf) {
        Customer customerRemove = customerRepository.findByCpf(cpf).get();
        customerRemove.setActivated(false);
        customerRepository.save(customerRemove);
    }

    public boolean areTelephonesValids(List<Telephone> telephones) {
        for (Telephone tel : telephones) {
            String completePhoneNumber = tel.getDdd().concat(tel.getPhoneNumber());
            if (!isTelephoneValid(completePhoneNumber)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCpfExist(String cpf) {
        return customerList
                .stream()
                .anyMatch(customer -> customer.getCpf().equals(cpf));
    }

    //valida se campo email esta correto
    private boolean isEmailValid(String email) {
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    //valida se campo telefone esta correto
    private boolean isTelephoneValid(String telephone) {
        telephone = telephone.replaceAll("\\D", "");

        if (!(telephone.length() >= 10 && telephone.length() <= 11)) {
            return false;
        }

        if (telephone.length() == 11 && Integer.parseInt(telephone.substring(2, 3)) != 9) {
            return false;
        }

        Pattern pattern = java.util.regex.Pattern.compile(telephone.charAt(0) + "{" + telephone.length() + "}");
        Matcher matcher = pattern.matcher(telephone);
        if (matcher.find()) {
            return false;
        }

        Integer[] codigosDDD = {
                11, 12, 13, 14, 15, 16, 17, 18, 19,
                21, 22, 24, 27, 28, 31, 32, 33, 34,
                35, 37, 38, 41, 42, 43, 44, 45, 46,
                47, 48, 49, 51, 53, 54, 55, 61, 62,
                64, 63, 65, 66, 67, 68, 69, 71, 73,
                74, 75, 77, 79, 81, 82, 83, 84, 85,
                86, 87, 88, 89, 91, 92, 93, 94, 95,
                96, 97, 98, 99};
        if (java.util.Arrays.asList(codigosDDD).indexOf(Integer.parseInt(telephone.substring(0, 2))) == -1) {
            return false;
        }

        Integer[] prefixo = {2, 3, 4, 5, 7};
        if (telephone.length() == 10 && java.util.Arrays.asList(prefixo).indexOf(Integer.parseInt(telephone.substring(2, 3))) == -1) {
            return false;
        }

        return true;
    }

    //valida se cpf esta correto
    private boolean isCpfValid(String cpf) {

        return cpf.matches("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})");
    }
}
