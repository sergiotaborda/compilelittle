package compiler.filesystem;

public class SourcePath {

	public static SourcePath of(SourcePath root, String name) {
		if (root == null) {
			return new RootSourcePath(name);
		}
		return new SourcePath(root, name);
	}
	
	public static SourcePath of(String ... names) {
		if(names == null || names.length == 0) {
			return null;
		}

		var index = 0;
		SourcePath p = null;
		for (var i = 0 ; i < names.length ; i++) {
			if(names[i] != null && names[i].length() > 0) {
				p = new SourcePath(null, names[i]);
				index = i;
				break;
			}
		}


		for (var i = index + 1 ; i < names.length ; i++) {
			if(names[i] != null && names[i].length() > 0) {
				p = new SourcePath(p, names[i]);
			}
		}

		return p;
	}

	private final String name;
	private final SourcePath parent;

	SourcePath(SourcePath parent, String name){
		this.parent = parent;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public SourcePath getParent() {
		return parent;
	}

	public String toString() {
		return (parent == null ? "" : parent.toString() + ">") +  name;
	}



	public SourcePath relativize(SourcePath origin) {

		var s = this.join("/");
		var q = origin.join("/");
		var pos = q.indexOf(s);
		if(pos < 0) {
			return SourcePath.of();
		}

		return SourcePath.of(q.substring(pos + s.length()).split("/"));
	}

	public String join(String separator) {

		if (parent != null) {
			return parent.join(separator) + separator + name;
		}

		return name;
	}

	public boolean equals(Object obj) {

		if (!(obj instanceof SourcePath)) {
			return false;
		}
		var other = (SourcePath)obj;
		return name.equals(other.name) && (parent == null ? true : parent.equals(other.getParent()));
	}

	public int hashCode() {
		return parent == null ? name.hashCode() : 31 * parent.hashCode() +  name.hashCode();
	}
}
