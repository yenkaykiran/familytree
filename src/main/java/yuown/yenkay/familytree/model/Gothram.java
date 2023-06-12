package yuown.yenkay.familytree.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.neo4j.core.schema.*;

import lombok.Data;

@Node(labels = "Gothram")
@Data
public class Gothram {

	@Id
	@GeneratedValue
	private Long gothramId;

	private String name;

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
