package yuown.yenkay.familytree.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import yuown.yenkay.familytree.model.Member;

//@RepositoryRestResource(collectionResourceRel = "data", path = "member")
@Repository
public interface MemberRepository extends Neo4jRepository<Member, Long> {

//	@RestResource(path = "nameStartsWith", rel = "nameStartsWith")
	Page<Member> findByNameOrFamilyNameIgnoreCaseContaining(@Param("name") String name, @Param("name") String familyName, Pageable p);
	
	Member findOneByNameAndFamilyNameAndAlternateFamilyName(String name, String familyName, String alternateFamilyName);

}
