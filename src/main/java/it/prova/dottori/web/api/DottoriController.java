package it.prova.dottori.web.api;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.dottori.dto.DottoreDTO;
import it.prova.dottori.dto.DottorePazienteDTO;
import it.prova.dottori.model.Dottore;
import it.prova.dottori.service.DottoreService;

@RestController
@RequestMapping("/api/dottori")
public class DottoriController {

	@Autowired
	private DottoreService dottoreService;

	@GetMapping
	public List<DottoreDTO> listAll() {
		return DottoreDTO.createDottoreDTOListFromModelList(dottoreService.listAllElements());
	}

	@GetMapping("/{cf}")
	public DottoreDTO cercaPerCodiceFiscalePazinete(@PathVariable(required = true) String cf) {
		Dottore result = dottoreService.findByCodFiscalePazienteAttualmenteInVisita(cf);

		if (result == null)
			throw new RuntimeException("nessun dottore sul paziente");

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void inserisciDottore(@RequestBody DottoreDTO dottore) {

		if (dottore.getId() != null)
			throw new RuntimeException("impossibile inserire un nuovo record se contenente id");

		dottoreService.inserisciNuovo(dottore.buildDottoreModel());

	}

	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@RequestBody DottoreDTO dottore) {
		if (dottore.getId() == null)
			throw new RuntimeException("impossibile aggiornare un record se non si inserisce l'id");

		dottoreService.aggiorna(dottore.buildDottoreModel());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		Dottore dottoreDaEliminare = dottoreService.caricaSingoloElemento(id);

		if (dottoreDaEliminare == null)
			throw new RuntimeException("nessun dottore trovato");

		dottoreService.rimuovi(id);
	}

	@GetMapping("/verificaDisponibilitaDottore/{cd}")
	public DottoreDTO assegnaPaziente(@PathVariable(required = true) String cd) {
		Dottore result = dottoreService.verificaDisponibilita(cd);

		if (result == null)
			throw new RuntimeException("dottore non trovato");

		if (!result.isInServizio() || result.isInVisita())
			throw new RuntimeException("dottore non disponibile");

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping("/impostaVisita")
	public DottorePazienteDTO impostaVisita(@RequestBody DottorePazienteDTO dottorePazienteDTO) {

		Dottore dottore = Dottore.builder().codiceDottore(dottorePazienteDTO.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(dottorePazienteDTO.getCodFiscalePazienteAttualmenteInVisita())
				.build();

		return DottorePazienteDTO.buildDottoreDTOFromModel(dottoreService.impostaDottore(dottore));
	}

	@PostMapping("/ricovera")
	public DottorePazienteDTO ricovera(@RequestBody DottorePazienteDTO dottorePazienteDTO) {
		Dottore dottore = Dottore.builder().codiceDottore(dottorePazienteDTO.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(dottorePazienteDTO.getCodFiscalePazienteAttualmenteInVisita())
				.build();
		return DottorePazienteDTO.buildDottoreDTOFromModel(dottoreService.ricovera(dottore));
	}
}
