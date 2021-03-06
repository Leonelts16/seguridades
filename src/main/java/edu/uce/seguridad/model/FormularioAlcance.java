package edu.uce.seguridad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class FormularioAlcance {
    @Id
    private String _id;

    private String user;
    private String personal;
    private String negocio;
    private String departamento;
    private String actividades;
    private String recuperacion;
}
