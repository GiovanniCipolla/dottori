package it.prova.dottori.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.dottori.model.Dottore;
import it.prova.dottori.repository.DottoreRepository;

@Service
@Transactional(readOnly = true)
public class DottoreServiceImpl implements DottoreService {

	@Autowired
	private DottoreRepository repository;

	@Override
	public List<Dottore> listAllElements() {
		return (List<Dottore>) repository.findAll();
	}

	@Override
	public Dottore caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Dottore aggiorna(Dottore dottoreInstance) {
		Dottore dottore = repository.findById(dottoreInstance.getId()).orElse(null);

		if (dottore.isInServizio() == true)
			dottoreInstance.setInServizio(true);
		else
			dottoreInstance.setInServizio(false);
		if (dottore.isInVisita() == true)
			dottoreInstance.setInServizio(true);
		else
			dottoreInstance.setInServizio(false);

		return repository.save(dottoreInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Dottore dottoreInstance) {

		dottoreInstance.setInServizio(true);
		dottoreInstance.setInVisita(false);

		repository.save(dottoreInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {

		Dottore dottoreInstance = repository.findById(idToRemove).orElse(null);
		if (dottoreInstance.isInServizio() == true || dottoreInstance.isInVisita() == true)
			throw new RuntimeException();

		repository.deleteById(idToRemove);
	}

	@Override
	public Dottore findByCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisitaInstance) {
		return repository
				.findDottoreByCodFiscalePazienteAttualmenteInVisita(codFiscalePazienteAttualmenteInVisitaInstance);
	}

	@Override
	public Dottore findByCodiceDottore(String codiceDottoreInstance) {
		return repository.caricaDottoreFromCodiceDottore(codiceDottoreInstance);

	}

	@Override
	public Dottore verificaDisponibilita(String cd) {
		return repository.caricaDottoreFromCodiceDottore(cd);
	}

	@Override
	@Transactional
	public Dottore impostaDottore(Dottore dottore) {

		Dottore dottoreInstance = repository.caricaDottoreFromCodiceDottore(dottore.getCodiceDottore());

		if (!dottoreInstance.isInServizio() || dottoreInstance.isInVisita())
			throw new RuntimeException("dottore non disponibile");

		dottoreInstance.setCodFiscalePazienteAttualmenteInVisita(dottore.getCodFiscalePazienteAttualmenteInVisita());

		dottoreInstance.setInVisita(true);

		return repository.save(dottoreInstance);
	}

	@Override
	@Transactional
	public Dottore ricovera(Dottore dottore) {

		Dottore result = repository.caricaDottoreFromCodiceDottore(dottore.getCodiceDottore());

		if (result.getCodFiscalePazienteAttualmenteInVisita()
				.equals(dottore.getCodFiscalePazienteAttualmenteInVisita()))
			throw new RuntimeException("Questo dottore non ha un paziente con questo CF");

		result.setCodFiscalePazienteAttualmenteInVisita(null);
		result.setInVisita(false);

		return repository.save(result);
	}

	@Override
	@Transactional
	public Dottore cambiaServizio(Long id) {

		Dottore dottore = repository.findById(id).orElse(null);

		if (dottore.isInServizio() == true)
			throw new RuntimeException("impossibile cambiare lo stato in servizio se ha dei pazienti");

		if (dottore.isInServizio() == true)
			dottore.setInServizio(false);
		else
			dottore.setInServizio(true);

		return dottore;
	}

	@Override
	public Page<Dottore> findByExampleWithPagination(Dottore example, Integer pageNo, Integer pageSize, String sortBy) {
		Specification<Dottore> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getNome()))
				predicates.add(cb.like(cb.upper(root.get("nome")), "%" + example.getNome().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCognome()))
				predicates.add(cb.like(cb.upper(root.get("cognome")), "%" + example.getCognome().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCodiceDottore()))
				predicates.add(cb.like(cb.upper(root.get("codiceDottore")),
						"%" + example.getCodiceDottore().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCodFiscalePazienteAttualmenteInVisita()))
				predicates.add(cb.like(cb.upper(root.get("codFiscalePazienteAttualmenteInVisita")),
						"%" + example.getCodFiscalePazienteAttualmenteInVisita().toUpperCase() + "%"));

//			if (example.isInServizio() != null)
//				predicates.add(cb.equal(root.get("inServizio"), example.isInServizio()));
//			if (example.isInVisita() != null)
//				predicates.add(cb.equal(root.get("inVisita"), example.isInVisita()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		Pageable paging = null;
		// se non passo parametri di paginazione non ne tengo conto
		if (pageSize == null || pageSize < 10)
			paging = Pageable.unpaged();
		else
			paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		return repository.findAll(specificationCriteria, paging);
	}
}
