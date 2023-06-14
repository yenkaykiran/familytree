package yuown.yenkay.familytree.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Node
@Data
@JsonIgnoreProperties(value = { "son", "daughter", "spouse", "father", "mother", "gothram" })
public class Member implements Serializable {

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
	
	private Long gothramId;
	
	private Boolean root;
	
	@Relationship(type = "HAS_GOTHRAM", direction = Relationship.Direction.OUTGOING)
	private Gothram gothram;

	@Relationship(type = "SON", direction = Relationship.Direction.OUTGOING)
	Set<Member> son = new HashSet<Member>();

	@Relationship(type = "DAUGHTER", direction = Relationship.Direction.OUTGOING)
	Set<Member> daughter = new HashSet<Member>();

	@Relationship(type = "SPOUSE")
	Set<Member> spouse = new HashSet<Member>();

	@Relationship(type = "FATHER", direction = Relationship.Direction.OUTGOING)
	Member father;

	@Relationship(type = "MOTHER", direction = Relationship.Direction.OUTGOING)
	Member mother;

	@CreatedDate
	public LocalDateTime createDate;

	@LastModifiedDate
	public LocalDateTime lastModifiedDate;

	@CreatedBy
	public String createdBy;

	@LastModifiedBy
	public String lastModifiedBy;

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
