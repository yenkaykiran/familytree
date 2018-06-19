package yuown.yenkay.familytree.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@NodeEntity
@Data
@JsonIgnoreProperties(value = { "son", "daughter", "spouse", "father", "mother" })
public class Member {

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

	@Relationship(type = "SON", direction = Relationship.OUTGOING)
	Set<Member> son = new HashSet<Member>();

	@Relationship(type = "DAUGHTER", direction = Relationship.OUTGOING)
	Set<Member> daughter = new HashSet<Member>();

	@Relationship(type = "SPOUSE", direction = Relationship.UNDIRECTED)
	Set<Member> spouse = new HashSet<Member>();

	@Relationship(type = "FATHER", direction = Relationship.OUTGOING)
	Member father;

	@Relationship(type = "MOTHER", direction = Relationship.OUTGOING)
	Member mother;

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
