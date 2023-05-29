package it.prova.dottori.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.dottori.model.Dottore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Json include, se un dato è nullo non cerrà messo nell'output
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DottoreDTO {

	private Long id;

	private String nome;

	private String cognome;
	
	@NotBlank(message = "{codiceDottore.notblank}")
	private String codiceDottore;

	private String codFiscalePazienteAttualmenteInVisita;

	private boolean inVisita;

	private boolean inServizio;

	public Dottore buildDottoreModel() {
		Dottore result = Dottore.builder().id(this.id).nome(this.nome).cognome(this.cognome)
				.codFiscalePazienteAttualmenteInVisita(this.codFiscalePazienteAttualmenteInVisita).build();
		return result;
	}

	public static DottoreDTO buildDottoreDTOFromModel(Dottore dottoreModel) {
		DottoreDTO result = DottoreDTO.builder().id(dottoreModel.getId()).nome(dottoreModel.getNome())
				.cognome(dottoreModel.getCognome()).codiceDottore(dottoreModel.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(dottoreModel.getCodFiscalePazienteAttualmenteInVisita())
				.inServizio(dottoreModel.isInServizio()).inVisita(dottoreModel.isInVisita()).build();
		return result;
	}

	public static List<DottoreDTO> createDottoreDTOListFromModelList(List<Dottore> modelListInput) {
		return modelListInput.stream().map(inputEntity -> {
			return DottoreDTO.buildDottoreDTOFromModel(inputEntity);
		}).collect(Collectors.toList());
	}
}
