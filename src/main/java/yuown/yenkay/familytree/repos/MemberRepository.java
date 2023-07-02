package yuown.yenkay.familytree.repos;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import yuown.yenkay.familytree.model.Member;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel = "data", path = "member")
@Repository
public interface MemberRepository extends Neo4jRepository<Member, Long> {

//	@RestResource(path = "nameStartsWith", rel = "nameStartsWith")
	@Cacheable(cacheNames = "members-name-family", unless="#result.getTotalElements() == 0")
	Page<Member> findByNameIgnoreCaseContainingOrFamilyNameIgnoreCaseContaining(@Param("name") String name, @Param("familyName") String familyName, Pageable p);

	@Cacheable(cacheNames = "members-name-family-search", unless="#result.getTotalElements() == 0")
	Page<Member> findByNameOrFamilyNameOrAlternateFamilyNameMatchesRegex(@Param("name") String name, @Param("familyName") String familyName, @Param("alternateFamilyName") String alternateFamilyName, Pageable p);

	@Cacheable(cacheNames = "members-name-family-matched", unless="#result == null")
	Member findOneByNameAndFamilyNameAndAlternateFamilyName(String name, String familyName, String alternateFamilyName);

	@Cacheable(cacheNames = "members-root", unless="#result == null")
	Member findTop1ByRoot(boolean b);

	@Cacheable(cacheNames = "members-id", unless="#result == null")
	@Override
	Optional<Member> findById(Long id);

	@Cacheable(cacheNames = "members-id-list", unless="#result == null || #result.size() == 0")
	@Override
	List<Member> findAllById(Iterable<Long> ids);

	@CacheEvict(cacheNames = "members-id")
	@Override
	void deleteById(Long aLong);
}
