package it.prova.dottori.web.api;

import java.util.List;

import javax.validation.Valid;

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
	@ResponseStatus(HttpStatus.ACCEPTED)
	public List<DottoreDTO> listAll() {
		return DottoreDTO.createDottoreDTOListFromModelList(dottoreService.listAllElements());
	}

	@GetMapping("/{cf}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public DottoreDTO cercaPerCodiceFiscalePazinete(@PathVariable(required = true) String cf) {

		Dottore result = dottoreService.findByCodFiscalePazienteAttualmenteInVisita(cf);

		if (result == null)
			throw new RuntimeException("nessun dottore sul paziente");

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void inserisciDottore(@Valid @RequestBody DottoreDTO dottore) {

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
	@ResponseStatus(HttpStatus.ACCEPTED)
	public DottoreDTO assegnaPaziente(@PathVariable(required = true) String cd) {
		Dottore result = dottoreService.verificaDisponibilita(cd);

		if (result == null)
			throw new RuntimeException("dottore non trovato");

		if (!result.isInServizio() || result.isInVisita())
			throw new RuntimeException("dottore non disponibile");

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping("/impostaVisita")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public DottorePazienteDTO impostaVisita(@RequestBody DottorePazienteDTO dottorePazienteDTO) {

		if (dottorePazienteDTO.getCodFiscalePazienteAttualmenteInVisita() == null)
			throw new RuntimeException("i valori di input non devono essere nulli");

		Dottore dottore = Dottore.builder().codiceDottore(dottorePazienteDTO.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(dottorePazienteDTO.getCodFiscalePazienteAttualmenteInVisita())
				.build();

		return DottorePazienteDTO.buildDottoreDTOFromModel(dottoreService.impostaDottore(dottore));
	}

	@PostMapping("/ricovera")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public DottorePazienteDTO ricovera(@RequestBody DottorePazienteDTO dottorePazienteDTO) {
		
		Dottore dottore = Dottore.builder().codiceDottore(dottorePazienteDTO.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(dottorePazienteDTO.getCodFiscalePazienteAttualmenteInVisita())
				.build();
		
		return DottorePazienteDTO.buildDottoreDTOFromModel(dottoreService.ricovera(dottore));
	}

	
	@PutMapping("/cambiaInServizio/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public DottoreDTO cambiaServizio(@PathVariable(required = true) Long id){
		
		Dottore dottore = dottoreService.caricaSingoloElemento(id);

		if (dottore == null)
			throw new RuntimeException("nessun dottore trovato");

		return DottoreDTO.buildDottoreDTOFromModel(dottoreService.cambiaServizio(id));
		
	}
	
}
