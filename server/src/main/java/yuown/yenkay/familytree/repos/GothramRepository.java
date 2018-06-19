package yuown.yenkay.familytree.repos;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import yuown.yenkay.familytree.model.Gothram;

@RepositoryRestResource(collectionResourceRel = "data", path = "gothram")
public interface GothramRepository extends Neo4jRepository<Gothram, Long> {

	@RestResource(path = "nameStartsWith", rel = "nameStartsWith")
	List<Gothram> findAllByNameIgnoreCaseContaining(@Param("name") String name);
	
	@RestResource(path = "name", rel = "name")
	Gothram findOneByNameIgnoreCaseContaining(@Param("name") String name);

}
