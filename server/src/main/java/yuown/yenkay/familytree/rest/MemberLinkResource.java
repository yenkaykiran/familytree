package yuown.yenkay.familytree.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yuown.yenkay.familytree.model.Member;
import yuown.yenkay.familytree.repos.MemberRepository;

@RestController
@RequestMapping("/api")
public class MemberLinkResource {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@PostMapping("/link")
	public void link(@RequestParam("source") Long source,
					 @RequestParam("relation") String relation,
					 @RequestParam("destination") Long destination) {
		Member s = memberRepository.findById(source).get();
		Member d = memberRepository.findById(destination).get();

		if (null != s && null != d) {
			switch (relation) {
			case "son":
				d.getSon().add(s);
				s.setFather(d);
				break;
			case "daughter":
				d.getDaughter().add(s);
				s.setFather(d);
				break;
			case "spouse":
				d.getSpouse().add(s);
				s.getSpouse().add(d);
				break;
			}
			memberRepository.save(s);
		}
	}
	
	@PostMapping("/unlink")
	public void unlink(@RequestParam("source") Long source,
					 @RequestParam("relation") String relation,
					 @RequestParam("destination") Long destination) {
		unlinkHelper(source, relation, destination);
		unlinkHelper(source, relation, destination);
	}

	private void unlinkHelper(Long source, String relation, Long destination) {
		Member s = memberRepository.findById(source).get();
		Member d = memberRepository.findById(destination).get();

		if (null != s && null != d) {
			switch (relation) {
			case "son":
				s.setFather(null);
				break;
			case "daughter":
				s.setFather(null);
				break;
			}
			s = memberRepository.save(s);
		}
		if (null != s && null != d) {
			switch (relation) {
			case "son":
				d.getSon().remove(s);
				break;
			case "daughter":
				d.getDaughter().remove(s);
				break;
			case "spouse":
				d.getSpouse().remove(s);
				break;
			}
			d = memberRepository.save(d);
		}
	}
}
