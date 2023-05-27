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
import it.prova.dottori.service.DottoreService;

@RestController
@RequestMapping("/api/dottori")
public class DottoriController {

	@Autowired
	private DottoreService dottoreService;
	
	@GetMapping
	public List<DottoreDTO> visualizzaTavoli() {
		return dottoreService.listAll();
	}
	
	@GetMapping("/{id}")
	public DottoreDTO visualizza(@PathVariable(required = true) Long id) {
		return dottoreService.caricaSingoloElemento(id);
	}
	
	@PostMapping
	public DottoreDTO createNew(@Valid @RequestBody DottoreDTO dottoreInput) {
		if (dottoreInput.getId() != null)
			throw new RuntimeException();
		return dottoreService.inserisciNuovo(dottoreInput);
	}
	@PutMapping("/{id}")
	public DottoreDTO update(@Valid @RequestBody DottoreDTO dottoreInput, @PathVariable(required = true) Long id) {
		if (dottoreInput.getId() != null)
			throw new RuntimeException();
		return dottoreService.aggiorna(dottoreInput);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		dottoreService.rimuovi(id);
	}
}
