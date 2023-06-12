package yuown.yenkay.familytree.rest;

import java.util.ArrayList;
import java.util.Arrays;
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

import yuown.yenkay.familytree.model.GENDER;
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
				m.setGothramId(g.getGothramId());
			} else {
				m.setGothram(null);
				m.setGothramId(null);
			}
			m = memberRepository.save(m);
		}
		return m;
	}

	@GetMapping("/export")
	public List<MemberData> export(@RequestParam(name = "member", required = false) Long rootMember) {
		List<MemberData> exported = new ArrayList<>();
		Iterable<Member> all = null;
		MemberData f = null;
		MemberData first = null;
		Member father = null;
		if (null != rootMember && rootMember >= 0) {
			all = memberRepository.findAllById(Arrays.asList(rootMember));
		} else {
//			Member r = memberRepository.findTop1ByRoot(true);
//			if(null != r) {
//				rootMember = r.getId();
//				all = memberRepository.findAllById(Arrays.asList(rootMember));
//			} else {
			all = memberRepository.findAll();
//			}
		}
		for (Member memberFromDb : all) {
			MemberData mData = convert(memberFromDb);
			if(null == first) {
				first = mData;
			}
			Member member = memberRepository.findById(memberFromDb.getMemberId()).get();

			prepareMemberRelations(exported, member.getSon(), mData.getSon(), rootMember, "son");
			prepareMemberRelations(exported, member.getDaughter(), mData.getDaughter(), rootMember, "daughter");
			prepareMemberRelations(exported, member.getSpouse(), mData.getSpouse(), rootMember, "spouse");
			if(null != member.getGothram()) {
				mData.setGothram(member.getGothram().getName());
			}
			if (null != member.getFather()) {
				mData.setFather(member.getFather().getMemberId());
				if (null != rootMember && rootMember >= 0) {
					if(null == f) {
						f = convert(member.getFather());
						father = memberRepository.findById(member.getFather().getMemberId()).get();
						exported.add(f);
					} else {
						exported.add(convert(member.getFather()));
					}
				}
			}
			if (null != member.getMother()) {
				mData.setMother(member.getMother().getMemberId());
				if (null != rootMember && rootMember >= 0) {
					exported.add(convert(member.getMother()));
				}
			}

			exported.add(mData);
		}
		if(null != first && null != f) {
			if(first.getGender().equals(GENDER.MALE)) {
				f.getSon().add(first.getId());
			} else {
				f.getDaughter().add(first.getId());
			}
			for (Member member : father.getSpouse()) {
				f.getSpouse().add(member.getMemberId());
				exported.add(convert(member));
			}
		}
		return exported;
	}

	private MemberData convert(Member memberFromDb) {
		MemberData mData = new MemberData();
		BeanUtils.copyProperties(memberFromDb, mData, new String[] { "son", "daughter", "spouse", "father", "mother", "root" });
		return mData;
	}

	@PostMapping("/import")
	public void importData(@RequestBody List<MemberData> imported) {
		Map<Long, Long> map = new HashMap<>();
		for (MemberData mData : imported) {
			Member member = memberRepository.findOneByNameAndFamilyNameAndAlternateFamilyName(mData.getName(), mData.getFamilyName(), mData.getAlternateFamilyName());
			if (null == member) {
				member = new Member();
			}
			BeanUtils.copyProperties(mData, member, new String[] { "son", "daughter", "spouse", "father", "mother", "id", "gothram", "root" });
			if (StringUtils.isNotBlank(mData.getGothram())) {
				Gothram gothram = gothramRepository.findOneByNameIgnoreCaseContaining(mData.getGothram());
				if(gothram == null) {
					gothram = new Gothram();
					gothram.setName(mData.getGothram());
					gothram = gothramRepository.save(gothram);
				}
				member.setGothram(gothram);
				member.setGothramId(gothram.getGothramId());
			}
			if(mData.getRoot() == null) {
				member.setRoot(false);
			}
			Member saved = memberRepository.save(member);
			map.put(mData.getId(), saved.getMemberId());
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

	private void prepareMemberRelations(List<MemberData> exported, Set<Member> rel, Set<Long> set, Long rootMember, String relation) {
		for (Member member : rel) {
			set.add(member.getMemberId());
			if (null != rootMember && rootMember >= 0) {
				MemberData m = convert(member);
				exported.add(m);
				if(StringUtils.equalsIgnoreCase(relation, "spouse")) {
					m.getSpouse().add(rootMember);
				}
			}
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
			case "spouse":
				s.getSpouse().remove(d);
				break;
			}
			s = memberRepository.save(s);
		}
		d = memberRepository.findById(destination).get();
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
