package yuown.yenkay.familytree.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Data;

@Data
public class MemberData {

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

	public LocalDateTime createDate;

	public LocalDateTime lastModifiedDate;

	public String createdBy;

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
