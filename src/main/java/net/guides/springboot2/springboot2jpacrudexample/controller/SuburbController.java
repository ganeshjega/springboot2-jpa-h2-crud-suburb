package net.guides.springboot2.springboot2jpacrudexample.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Suburb;
import net.guides.springboot2.springboot2jpacrudexample.repository.SuburbRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class SuburbController {

	@Autowired
	private SuburbRepository suburbRepository;

	@GetMapping("/suburbs")
	public List<Suburb> getAllSuburbs() {
		return suburbRepository.findAll();
	}

	@GetMapping("/suburbs/{id}")
	public ResponseEntity<Suburb> getSuburbById(@PathVariable(value = "id") Long suburbId)
			throws ResourceNotFoundException {
		Suburb suburb = suburbRepository.findById(suburbId)
				.orElseThrow(() -> new ResourceNotFoundException("Suburb not found for this id :: " + suburbId));
		return ResponseEntity.ok().body(suburb);
	}

	@GetMapping("/suburbsBetween")
	public ResponseEntity<String> getSuburbBetween(@RequestParam(value = "from") Long from, @RequestParam(value = "to") Long to)
			throws ResourceNotFoundException {
		List<Suburb> filteredList = suburbRepository.findAll().stream().filter(e -> e.getPostalCode() >= from && e.getPostalCode() <= to).collect(Collectors.toList());
		ArrayList<String> names = new ArrayList<String>();
		int charCount = 0;
		for (Suburb suburb : filteredList) {
			names.add(suburb.getName());
			charCount += suburb.getName().length();
		}
		Collections.sort(names);
		return ResponseEntity.ok("Total Char Count: " + charCount + ", Names : " + names.toString());
	}
	
	@PostMapping("/suburbs")
	public Suburb createSuburb(@Valid @RequestBody Suburb suburb) {
		return suburbRepository.save(suburb);
	}

	@PostMapping("/suburbs/bulk")
	public ResponseEntity<String> createSuburbs(@Valid @RequestBody List<Suburb> suburbs) {
		suburbs.stream().forEach(e -> suburbRepository.save(e));
		return ResponseEntity.ok("success");
	}
	
	@PutMapping("/suburbs/{id}")
	public ResponseEntity<Suburb> updateSuburb(@PathVariable(value = "id") Long suburbId,
			@Valid @RequestBody Suburb suburbDetails) throws ResourceNotFoundException {
		Suburb suburb = suburbRepository.findById(suburbId)
				.orElseThrow(() -> new ResourceNotFoundException("Suburb not found for this id :: " + suburbId));

		suburb.setName(suburbDetails.getName());
		suburb.setPostalCode(suburbDetails.getPostalCode());
		final Suburb updatedSuburb = suburbRepository.save(suburb);
		return ResponseEntity.ok(updatedSuburb);
	}

	@DeleteMapping("/suburbs/{id}")
	public Map<String, Boolean> deleteSuburb(@PathVariable(value = "id") Long suburbId)
			throws ResourceNotFoundException {
		Suburb suburb = suburbRepository.findById(suburbId)
				.orElseThrow(() -> new ResourceNotFoundException("Suburb not found for this id :: " + suburbId));

		suburbRepository.delete(suburb);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
