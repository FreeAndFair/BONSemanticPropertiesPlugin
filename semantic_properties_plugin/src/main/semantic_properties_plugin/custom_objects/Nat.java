package semantic_properties_plugin.custom_objects;

public class Nat extends MyObject {
	public Nat() {
		super();
	}

	public Nat(String newId, long newValue) {
		super(newId,newValue);
	}
	@Override
	public MyObjectKind getKind() {
		return MyObjectKind.Nat;
	}
//	@Override
//	public String getReg() {
//		String reg1="([0]*?[1-9]\\d*(?:,\\d*)?\\.?[0]*)";
//		String reg2 ="([1-9][0-9]+)";
//		return getKind().getReg();
//	}

}
