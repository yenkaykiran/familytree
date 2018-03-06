package yuown.yenkay.familytree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yuown.yenkay.familytree.model.Member;
import yuown.yenkay.familytree.repos.MemberRepository;

@RestController("/api/link")
public class MemberLinkResource {
	
	@Autowired
	private MemberRepository memberRepository;
	
	public void link(@RequestParam("source") Long source,
					 @RequestParam("destination") Long destination,
					 @RequestParam("source") String relation) {
		Member s = memberRepository.findById(source).get();
		Member d = memberRepository.findById(destination).get();

		if (null != s && null != d) {
			switch (relation) {
			case "son":
				s.getSon().add(d);
				d.setFather(s);
				break;
			case "daughter":
				s.getDaughter().add(d);
				d.setFather(s);
				break;
			case "spouse":
				s.getSpouse().add(d);
				d.getSpouse().add(s);
				break;
			}
		}
	}
}
