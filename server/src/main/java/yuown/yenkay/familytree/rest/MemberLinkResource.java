package yuown.yenkay.familytree.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yuown.yenkay.familytree.model.Gothram;
import yuown.yenkay.familytree.model.Member;
import yuown.yenkay.familytree.model.MemberData;
import yuown.yenkay.familytree.repos.GothramRepository;
import yuown.yenkay.familytree.repos.MemberRepository;

@RestController
@RequestMapping("/api")
public class MemberLinkResource {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private GothramRepository gothramRepository;

	@PostMapping("/link")
	public void link(@RequestParam("source") Long source, @RequestParam("relation") String relation,
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
	public void unlink(@RequestParam("source") Long source, @RequestParam("relation") String relation,
			@RequestParam("destination") Long destination) {
		unlinkHelper(source, relation, destination);
		unlinkHelper(source, relation, destination);
	}
	
	@PostMapping("/attach-gothram")
	public Member attachGothram(@RequestParam("member") Long member, @RequestParam("gothram") Long gothram) {
		Member m = memberRepository.findById(member).get();
		Gothram g = gothramRepository.findById(gothram).get();

		if (null != m) {
			if(null != g) {
				m.setGothram(g);
				m.setGothramId(g.getId());
			} else {
				m.setGothram(null);
				m.setGothramId(null);
			}
			m = memberRepository.save(m);
		}
		return m;
	}

	@GetMapping("/export")
	public List<MemberData> export() {
		List<MemberData> exported = new ArrayList<>();
		Iterable<Member> all = memberRepository.findAll();
		for (Member member1 : all) {
			MemberData mData = new MemberData();
			BeanUtils.copyProperties(member1, mData, new String[] { "son", "daughter", "spouse", "father", "mother" });
			Member member = memberRepository.findById(member1.getId()).get();

			prepareMemberRelations(member.getSon(), mData.getSon());
			prepareMemberRelations(member.getDaughter(), mData.getDaughter());
			prepareMemberRelations(member.getSpouse(), mData.getSpouse());
			if(null != member.getGothram()) {
				mData.setGothram(member.getGothram().getName());
			}
			if (null != member.getFather()) {
				mData.setFather(member.getFather().getId());
			}
			if (null != member.getMother()) {
				mData.setMother(member.getMother().getId());
			}

			exported.add(mData);
		}
		return exported;
	}

	@PostMapping("/import")
	public void importData(@RequestBody List<MemberData> imported) {
		Map<Long, Long> map = new HashMap<>();
		for (MemberData mData : imported) {
			Member member = memberRepository.findOneByNameAndFamilyNameAndAlternateFamilyName(mData.getName(), mData.getFamilyName(), mData.getAlternateFamilyName());
			if (null == member) {
				member = new Member();
			}
			BeanUtils.copyProperties(mData, member, new String[] { "son", "daughter", "spouse", "father", "mother", "id", "gothram" });
			if (StringUtils.isNotBlank(mData.getGothram())) {
				Gothram gothram = gothramRepository.findOneByNameIgnoreCaseContaining(mData.getGothram());
				member.setGothram(gothram);
				member.setGothramId(gothram.getId());
			}
			Member saved = memberRepository.save(member);
			map.put(mData.getId(), saved.getId());
		}
		for (MemberData mData : imported) {
			Member saved = memberRepository.findById(map.get(mData.getId())).get();

			prepareDataForImport(saved, mData.getSon(), saved.getSon(), map);
			prepareDataForImport(saved, mData.getDaughter(), saved.getDaughter(), map);
			prepareDataForImport(saved, mData.getSpouse(), saved.getSpouse(), map);

			if (null != mData.getFather()) {
				saved.setFather(memberRepository.findById(map.get(mData.getFather())).get());
			}
			if (null != mData.getMother()) {
				saved.setMother(memberRepository.findById(map.get(mData.getMother())).get());
			}
			memberRepository.save(saved);
		}
	}

	private void prepareDataForImport(Member member, Set<Long> ids, Set<Member> data, Map<Long, Long> map) {
		if (null != ids && !ids.isEmpty()) {
			for (Long id : ids) {
				data.add(memberRepository.findById(map.get(id)).get());
			}
		}
	}

	private void prepareMemberRelations(Set<Member> rel, Set<Long> set) {
		for (Member member : rel) {
			set.add(member.getId());
		}
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
