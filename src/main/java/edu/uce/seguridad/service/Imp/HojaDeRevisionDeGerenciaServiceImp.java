package edu.uce.seguridad.service.Imp;

import edu.uce.seguridad.exception.EliminacionException;
import edu.uce.seguridad.exception.NoEncontradoExcepcion;
import edu.uce.seguridad.model.HojaDeRevisionDeGerencia;
import edu.uce.seguridad.repository.HojaDeRevisionDeGerenciaRepository;
import edu.uce.seguridad.service.service.HojaDeRevisionDeGerenciaService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import static edu.uce.seguridad.util.Utileria.jasper;

@Service
@AllArgsConstructor
public class HojaDeRevisionDeGerenciaServiceImp implements HojaDeRevisionDeGerenciaService{

    private HojaDeRevisionDeGerenciaRepository repository;
    private EstadoCompletadoServiceImpl estadoCompletadoService;
    
    @Override
    public List<HojaDeRevisionDeGerencia> buscarPorUserFiltrarPorAsuntoARevisaryVerificar(String user) throws NoEncontradoExcepcion {
            List<HojaDeRevisionDeGerencia> contactos = this.repository.findByUserOrderByAsuntoARevisaryVerificar(user);
        if (contactos.isEmpty()) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros para: ".concat(user));
        }
        return contactos;
    }

    @Override
    @Transactional
    public void eliminarPorAsuntoARevisaryVerificar(String user) {
            List<HojaDeRevisionDeGerencia> contactos = this.repository.findByUserOrderByAsuntoARevisaryVerificar(user);
        if (!contactos.isEmpty()) {
            contactos.forEach(contacto -> this.eliminarDocumento(contacto.get_id()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HojaDeRevisionDeGerencia> buscarTodos() throws NoEncontradoExcepcion {
            List<HojaDeRevisionDeGerencia> contactos = this.repository.findAll();
        if (contactos.isEmpty()) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros");
        }
        return contactos;
    }
    

    @Override
    public HojaDeRevisionDeGerencia agregar(HojaDeRevisionDeGerencia pojo) throws DataAccessException {
        HojaDeRevisionDeGerencia aux = this.repository.insert(pojo);
        estadoCompletadoService.verificarEstadoPaso10(pojo.getUser());
       return aux;
    }

    @Override
    @Transactional
    public HojaDeRevisionDeGerencia actualizar(HojaDeRevisionDeGerencia pojo) throws DataAccessException {
        this.buscaPorId(pojo.get_id());
        return this.repository.save(pojo);
    }

    @Override
    @Transactional(readOnly = true)
    public HojaDeRevisionDeGerencia buscaPorId(String identificador) throws NoEncontradoExcepcion {
        HojaDeRevisionDeGerencia contactos = this.repository.findById(identificador).orElse(null);
        if (contactos == null) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros de: ".concat(identificador));
        }
        return contactos;
    }

    @Override
    @Transactional
    public void eliminarDocumento(String identificador) throws NoEncontradoExcepcion {
        HojaDeRevisionDeGerencia contatos = this.buscaPorId(identificador);
        if (contatos == null) {
            throw new NoEncontradoExcepcion("respuesta", "No se han encontrado registros de: ".concat(identificador));
        }
        this.repository.delete(contatos);
        estadoCompletadoService.verificarEstadoPaso10(contatos.getUser());
    }

    @Override
    public byte[] generarPdfEnBytes(String user) throws IOException, JRException {
        List<HojaDeRevisionDeGerencia> data = this.buscarPorUserFiltrarPorAsuntoARevisaryVerificar(user);
        InputStream resource = new ClassPathResource("gerencia.jrxml").getInputStream();

        List<JSONObject> dataJson = new ArrayList<>();
        if (!data.isEmpty()) {
            data.forEach(var -> {

                JSONObject aux = new JSONObject();
                aux.put("asuntoARevisaryVerificar", var.getAsuntoARevisaryVerificar());
                aux.put("PersonaACargo", var.getPersonaACargo());
                aux.put("fechaLimite", var.getFechaLimite().toString());
                aux.put("altaGerencia", var.getAltaGerencia());

                dataJson.add(aux);

            });
            return JasperExportManager.exportReportToPdf(jasper(resource, dataJson));
        }
        return new byte[0];
    }

}
