package edu.uce.seguridad.service.service;

import edu.uce.seguridad.model.FormularioLiderazgo;

public interface FormularioLiderazgoService extends  BaseService<FormularioLiderazgo, String> {
    FormularioLiderazgo buscarFormularioPorUsuario(String usuario);


}