package edu.uce.seguridad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimacionDano {
    @Id
    private String _id;
    private String usuario;
    private String riesgo;
    private String probabilidad;
    private String impacto;
    private Map<String, List<Estimacion>> recursosNecesarios;

    public Estimacion definirEstimacion(
            String tipo,
            int dano,
            int dias,
            int grafico,
            boolean medidas
    ) {
        return new Estimacion(tipo, dano, dias, grafico, medidas);
    }
}
