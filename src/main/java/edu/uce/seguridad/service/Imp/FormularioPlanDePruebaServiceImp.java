package edu.uce.seguridad.service.Imp;

import edu.uce.seguridad.exception.NoEncontradoExcepcion;
import edu.uce.seguridad.model.FormularioPlanDePrueba;
import edu.uce.seguridad.repository.FormularioPlanDePruebaRepository;
import edu.uce.seguridad.service.service.FormularioPlanDePruebaService;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FormularioPlanDePruebaServiceImp implements FormularioPlanDePruebaService{

    private FormularioPlanDePruebaRepository repository;
    private EstadoCompletadoServiceImpl estadoCompletadoService;

    @Override
    @Transactional
    public FormularioPlanDePrueba agregar(FormularioPlanDePrueba pojo) throws DataAccessException {
        FormularioPlanDePrueba aux = this.repository.insert(pojo);
        estadoCompletadoService.verificarEstadoPaso9(pojo.getUser());
        return aux;
    }

    @Override
    @Transactional
    public FormularioPlanDePrueba actualizar(FormularioPlanDePrueba pojo) throws DataAccessException {
        this.buscaPorId(pojo.get_id());
        return this.repository.save(pojo);
    }

    @Override
    @Transactional(readOnly = true)
    public FormularioPlanDePrueba buscaPorId(String identificador) throws NoEncontradoExcepcion {
        FormularioPlanDePrueba contactos = this.repository.findById(identificador).orElse(null);
        if (contactos == null) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros de: ".concat(identificador));
        }
        return contactos;
    }

    @Override
    @Transactional
    public void eliminarDocumento(String identificador) throws NoEncontradoExcepcion {
        FormularioPlanDePrueba contatos = this.buscaPorId(identificador);
        if (contatos == null) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros de: ".concat(identificador));
        }
        this.repository.delete(contatos);
        estadoCompletadoService.verificarEstadoPaso9(contatos.getUser());
    }

    @Override
    public List<FormularioPlanDePrueba> buscarPorUserFiltrarPorTipoDeEjercicio(String user) throws NoEncontradoExcepcion {
        List<FormularioPlanDePrueba> contactos = this.repository.findByUserOrderByTipoDeEjercicio(user);
        if (contactos.isEmpty()) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros para: ".concat(user));
        }
        return contactos;
    }

    @Override
    @Transactional
    public void eliminarPorTipoDeEjercicio(String user) {
        List<FormularioPlanDePrueba> contactos = this.repository.findByUserOrderByTipoDeEjercicio(user);
        if (!contactos.isEmpty()) {
            contactos.forEach(contacto -> this.eliminarDocumento(contacto.get_id()));
            estadoCompletadoService.verificarEstadoPaso9(contactos.get(0).getUser()); // posible desborde
        }

    }

    @Override
    public void eliminarRespuestaFormularioPlanDePruebas(String nombreUsuario) {
        List<FormularioPlanDePrueba> contactos = this.repository.findByUserOrderByTipoDeEjercicio(nombreUsuario);
        if (!contactos.isEmpty()) {
            contactos.forEach(contacto -> this.eliminarDocumento(contacto.getUser()));
            estadoCompletadoService.verificarEstadoPaso9(contactos.get(0).getUser());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<FormularioPlanDePrueba> buscarTodos() throws NoEncontradoExcepcion {
        List<FormularioPlanDePrueba> contactos = this.repository.findAll();
        if (contactos.isEmpty()) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros");
        }
        return contactos;
    }
}
