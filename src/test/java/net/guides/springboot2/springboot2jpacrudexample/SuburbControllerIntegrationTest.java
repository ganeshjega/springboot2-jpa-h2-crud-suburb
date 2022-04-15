package net.guides.springboot2.springboot2jpacrudexample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import net.guides.springboot2.springboot2jpacrudexample.model.Suburb;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuburbControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetAllSuburbs() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/suburbs",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetSuburbById() {
		Suburb suburb = restTemplate.getForObject(getRootUrl() + "/suburbs/1", Suburb.class);
		System.out.println(suburb.getName());
		assertNotNull(suburb);
	}

	@Test
	public void testCreateSuburb() {
		Suburb suburb = new Suburb();
		suburb.setName("Panvel");
		suburb.setPostalCode(410106);

		ResponseEntity<Suburb> postResponse = restTemplate.postForEntity(getRootUrl() + "/suburbs", suburb, Suburb.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateEmployee() {
		int id = 1;
		Suburb suburb = restTemplate.getForObject(getRootUrl() + "/suburbs/" + id, Suburb.class);
		suburb.setName("Worli");
		suburb.setPostalCode(400018);

		restTemplate.put(getRootUrl() + "/suburbs/" + id, suburb);

		Suburb updatedSuburb = restTemplate.getForObject(getRootUrl() + "/suburbs/" + id, Suburb.class);
		assertNotNull(updatedSuburb);
	}

	@Test
	public void testDeleteEmployee() {
		int id = 2;
		Suburb suburb = restTemplate.getForObject(getRootUrl() + "/suburbs/" + id, Suburb.class);
		assertNotNull(suburb);

		restTemplate.delete(getRootUrl() + "/suburbs/" + id);

		try {
			suburb = restTemplate.getForObject(getRootUrl() + "/suburbs/" + id, Suburb.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}