package it.prova.dottori.dto;

import it.prova.dottori.model.Dottore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DottorePazienteDTO {

	private String codiceDottore;
	private String codFiscalePazienteAttualmenteInVisita;

	public void setCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisita) {
		this.codFiscalePazienteAttualmenteInVisita = codFiscalePazienteAttualmenteInVisita;
	}

	public static DottorePazienteDTO buildDottoreDTOFromModel(Dottore dottoreModel) {
		DottorePazienteDTO result = new DottorePazienteDTO(dottoreModel.getCodiceDottore(),
				dottoreModel.getCodFiscalePazienteAttualmenteInVisita());
		return result;
	}

}