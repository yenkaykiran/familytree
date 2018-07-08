package yuown.yenkay.familytree.rest;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yuown.yenkay.familytree.model.Member;
import yuown.yenkay.familytree.repos.MemberRepository;

@RestController
@RequestMapping("/api/member")
public class MemberResource {

	private static final Logger logger = LoggerFactory.getLogger(MemberResource.class);

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/{member}")
	public ResponseEntity<Member> getOne(@PathVariable("member") Long member) {
		Optional<Member> optional = memberRepository.findById(member);
		if (optional.isPresent()) {
			return new ResponseEntity<Member>(optional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Member>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Member> save(@RequestBody Member member) {
		ResponseEntity<Member> response = null;
		try {
			Member saved = memberRepository.save(member);
			response = new ResponseEntity<Member>(saved, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<Member>(HttpStatus.BAD_REQUEST);
			response.getHeaders().add("reason", e.getMessage());
		}
		return response;
	}

	@DeleteMapping("/{member}")
	public ResponseEntity<Member> delete(@PathVariable("member") Long member) {
		ResponseEntity<Member> response = null;
		try {
			memberRepository.deleteById(member);
			response = new ResponseEntity<Member>(HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<Member>(HttpStatus.BAD_REQUEST);
			response.getHeaders().add("reason", e.getMessage());
		}
		return response;
	}

	@GetMapping
	public Page<Member> getAll(@RequestParam(name = "name", required = false) String name, Pageable p) {
		if (StringUtils.isNotBlank(name)) {
			return memberRepository.findByNameOrFamilyNameIgnoreCaseContaining(name, name, p);
		} else {
			return memberRepository.findAll(p);
		}
	}

	@GetMapping("/{member}/{relation}")
	public Set<Member> getAllByRelation(@PathVariable("member") Long member, @PathVariable("relation") String relation) {
		Set<Member> related = null;
		try {
			Optional<Member> optional = memberRepository.findById(member);
			if (optional.isPresent()) {
				Member fromDb = optional.get();
				switch (relation.toLowerCase()) {
				case "son":
					related = fromDb.getSon();
					break;
				case "daughter":
					related = fromDb.getDaughter();
					break;
				case "spouse":
					related = fromDb.getSpouse();
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return related;
	}
}
