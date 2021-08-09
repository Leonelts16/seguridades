package edu.uce.seguridad.service.service;

import edu.uce.seguridad.exception.NoEncontradoExcepcion;
import edu.uce.seguridad.model.EstimacionDano;

public interface EstimacionDanoService extends BaseService<EstimacionDano, String>{
    EstimacionDano buscarFormularioEstimacionPorUsuario(String usuario) throws NoEncontradoExcepcion;
}
