package one.digitalinnovation.gof.service.ServiceIpml;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ClienteServiceIpml implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClietecomCep(cliente);
    }

    private void salvarClietecomCep(Cliente cliente) { //este bloco inteiro foi refatorado o metodo e transformado em
        String cep = cliente.getEndereco().getCep();   // apenas uma linha onde está no bloco acima chamado "salvarClietecomCep(cliente);"
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() ->{ // e usado para inserir no metodo de atalizar cliente.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClietecomCep(cliente);

        }

    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);

    }

}
