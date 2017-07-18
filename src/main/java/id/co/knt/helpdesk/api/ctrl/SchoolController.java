/**
 * 
 */
package id.co.knt.helpdesk.api.ctrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import id.co.knt.helpdesk.api.model.School;
import id.co.knt.helpdesk.api.repositories.SchoolRepo;

/**
 * @author marlina_kreatif
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/schoolManagement")
public class SchoolController {
	
	private static final Logger LOG = LoggerFactory.getLogger(School.class);

	@Autowired
	SchoolRepo schoolRepo;

	/**
	 * @return List<School>
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<School>> getAllSchool() {
		LOG.info("=============[getAllSchool]============");
		List<School> schools = schoolRepo.findAll();

		if (schools.size() > 0) {
			return new ResponseEntity<List<School>>(schools, HttpStatus.OK);
		}

		return new ResponseEntity<List<School>>(schools, HttpStatus.NOT_FOUND);
	}

	/**
	 * @param schoolId
	 */
	@RequestMapping(value = "/schoolDetail/{schoolId}", method = RequestMethod.GET)
	public ResponseEntity<School> getDetailSchool(@PathVariable Integer schoolId) {
		LOG.info("============[getDetailSchool]===============");
		School school = schoolRepo.findOne(schoolId);

		if (!school.equals(null)) {
			return new ResponseEntity<School>(school, HttpStatus.OK);
		}

		return new ResponseEntity<School>(school, HttpStatus.NOT_FOUND);
	}

	/**
	 * @param school
	 */
	@RequestMapping(value = "/createSchool/", method = RequestMethod.POST)
	public ResponseEntity<School> createSchool(@RequestBody School school) {
		LOG.info("============[createSChool]===============");
		if (schoolRepo.findBySchoolName(school.getSchoolName()) != null) {
			school = null;
			return new ResponseEntity<School>(school, HttpStatus.FORBIDDEN);
		}
		School schoolResult = schoolRepo.save(school);

		return new ResponseEntity<School>(schoolResult, HttpStatus.OK);
	}

	/**
	 * @param schoolId
	 * soft delete school
	 */
	@RequestMapping(value = "/deleteSchool/{schoolId}", method = RequestMethod.DELETE)
	public ResponseEntity<School> deleteSchool(@PathVariable Integer schoolId) {
		School school = schoolRepo.findOne(schoolId);
		
		if (!school.equals(null)) {
			school.setDeleted(true);
			schoolRepo.saveAndFlush(school);
			return new ResponseEntity<School>(school, HttpStatus.OK);
		}

		return new ResponseEntity<School>(school, HttpStatus.NOT_FOUND);
	}

}
