package yuown.yenkay.familytree.repos;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import org.springframework.stereotype.Repository;
import yuown.yenkay.familytree.model.Gothram;

//@RepositoryRestResource(collectionResourceRel = "data", path = "gothram")
@Repository
public interface GothramRepository extends Neo4jRepository<Gothram, Long> {

	//@RestResource(path = "nameStartsWith", rel = "nameStartsWith")

	@Cacheable(cacheNames = "gothram-name-list", unless="#result == null")
	List<Gothram> findAllByNameIgnoreCaseContaining(@Param("name") String name);
	
	//@RestResource(path = "name", rel = "name")
	@Cacheable(cacheNames = "gothram-names", unless="#result == null")
	Gothram findOneByNameIgnoreCaseContaining(@Param("name") String name);

	@Cacheable(cacheNames = "gothram-list-paged", unless="#result == null")
	Page<Gothram> findAllByNameIgnoreCaseContaining(@Param("name") String name, Pageable pageable);

	@CacheEvict(cacheNames = {"gothram-list", "gothram-names", "gothram-list-paged", "gothram-name-list"}, allEntries = true)
	@Override
	void deleteById(Long id);

	@Cacheable(cacheNames = "gothram-list", unless="#result == null")
	@Override
	Page<Gothram> findAll(Pageable pageable);
}
