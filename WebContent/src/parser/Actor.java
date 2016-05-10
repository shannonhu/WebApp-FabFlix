package parser;


public class Actor {

	private String firstName;
	private String lastName;
	private String dob;

	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getfirstName() {
		return firstName;
	}

	public void setlastName(String lastName) {
		this.lastName = lastName;
	}

	public String getlastName() {
		return lastName;
	}

	public void setdob(String dob) {
		this.dob = dob;
	}

	public String getdob() {
		return dob;
	}


	@Override
	public int hashCode() {

		int hash = 7;
		hash = 31 * hash + (null == this.firstName ? 0 : this.firstName.hashCode());
		hash = 31 * hash + (null == this.lastName ? 0 : this.lastName.hashCode());
		hash = 31 * hash + (null == this.dob ? 0 : this.dob.hashCode());

		return hash;
	}

	@Override

	public boolean equals(Object o) {
		Actor comparison = (Actor) o;

		if (this.firstName != null && comparison.getfirstName() != null
				&& !this.firstName.equals(comparison.getfirstName()))
			return false;
		if (this.lastName != null && comparison.getlastName() != null && this.lastName.equals(comparison.getlastName()))
			return false;
		if (this.dob != null && comparison.getdob() != null && this.dob.equals(comparison.getdob()))
			return false;

		return true;

	}
}
