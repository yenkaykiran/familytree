package yuown.yenkay.familytree.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import lombok.Data;

@Data
public class MemberData {

	@Id
	@GeneratedValue
	private Long id;

	private String familyName;

	private String alternateFamilyName;

	private String initials;

	private String name;

	private GENDER gender;

	private LocalDateTime dateOfBirth;

	private String place;

	private String contact;
	
	private String gothram;

	Set<Long> son = new HashSet<Long>();

	Set<Long> daughter = new HashSet<Long>();

	Set<Long> spouse = new HashSet<Long>();

	Long father;

	Long mother;

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
