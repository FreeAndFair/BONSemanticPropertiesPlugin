package ie.ucd.semantic_properties_plugin.file_checker;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

import custom_yaml.RefinementConstructor;
import custom_yaml.RefinementRepresenter;
import custom_yaml.RefinementResolver;

/**Stores a refinement property from one level to another. 
 * 
 * @author eo
 *
 */
public class RefinementProperty {
	
	private LinkedHashMap<String,String> sConversions;
	private LinkedHashMap<String, Transitions> oConversions;
	private int sourceLevel;
	private int destinationLevel;
	private String propertyName;
	
	/**Constructs Refinement Property from input String. 
	 * 
	 * @param input name of input file to parse
	 * @throws FileNotFoundException 
	 */
	public RefinementProperty(final String input) {
		
		Yaml yaml = new Yaml(
				new Loader(new RefinementConstructor()), 
				new Dumper(new RefinementRepresenter(), new DumperOptions()),
				new RefinementResolver());
		FileInputStream io = null;

		try {
			io = new FileInputStream(input);
		} catch (Exception e) {
			System.out.println("invalid string for refinment prop");
		}
		Object ob = yaml.load(io);
		parse(ob);
	}
	/**Constructs Refinement Property from input file. 
	 * 
	 * @param input file to parse
	 * @throws FileNotFoundException 
	 */
	public RefinementProperty(final File input) {
		
		Yaml yaml = new Yaml(new Loader(new RefinementConstructor()),
				new Dumper(new RefinementRepresenter(), new DumperOptions()),
				new RefinementResolver());
		FileInputStream io = null;

		try {
			io = new FileInputStream(input);
		} catch (Exception e) {
			System.out.println("no file for refinement prop");
		}
		Object ob = yaml.load(io);
		parse(ob);
	}
	/**parse object for values of Refinement Property.
	 * 
	 * @param currentOb Object to parse.
	 */
	private void parse(final Object currentOb){
		/**Entry case
		 * <p>We assume key is string.Possibly fix this later. </p>
		 */
		if (currentOb instanceof Entry< ? , ? >) {
			Entry<Object, Object> ent = (Entry<Object, Object>) currentOb;
			/*check  for key relation(int,int).
			 * 
			 */
			
			Pattern p = Pattern.compile("relation\\((\\d+),(\\d+)\\)");
			Matcher m = p.matcher((String) ent.getKey());
			/**If it matches set source and destination levels. 
			 */
			if (m.matches()) {
				sourceLevel = Integer.parseInt(m.group(1));
				destinationLevel = Integer.parseInt(m.group(2));
				
			}
			/**Check for property name.
			 * 
			 */
			if (ent.getKey().equals("property")) {
				propertyName = (String) ent.getValue();
				if (sConversions == null) {
					sConversions = new LinkedHashMap<String, String>();
				}
				sConversions.put(propertyName, propertyName);
			}
			
			/**If value is string  add entry to sConversion .
			 * 
			 */
	
			else if (ent.getValue() instanceof String) {
				if (sConversions == null) {
					sConversions = new LinkedHashMap<String, String>();
				}
				sConversions.put((String) ent.getKey(), (String) ent.getValue());
			}
			/**If value is Transitions add entry to oConversions
			 * 
			 */
			else if(ent.getValue() instanceof Transitions){
				if(oConversions==null) {
					oConversions = new LinkedHashMap<String,Transitions>();
				}
				oConversions.put((String)ent.getKey(), (Transitions)ent.getValue());
			}
			/**Any other object as key should be parsed.
			 * 
			 */
			else {
				parse(ent.getValue());
			}
		}
		/**Map case.
		 * 
		 */
		else if (currentOb instanceof LinkedHashMap< ? , ? >) {
			/**Cast to map and loop through the values.
			 * 
			 */
			LinkedHashMap<Object,Object> map = (LinkedHashMap<Object,Object>)currentOb;
			Set<Entry<Object, Object>> entries = map.entrySet();
			for(Entry<Object, Object> entry:entries ){
				parse(entry);
			}
		}
		/**List case.
		 * 
		 */
		else if(currentOb instanceof ArrayList<?>){
			/**loop through list and parse each object. 
			 * 
			 */
			for(Object val: (ArrayList<Object>)currentOb){
				parse(val);
			}
		}
		else{
			System.out.println("unexpected object "+
					currentOb.toString()+
					" in RefinementProperty Parse() method");
		}
		
	}
	/**Check Validity Of RefinementProp.
	 * <p> Basic test to check that all variables are not null</p>
	 */
	public  boolean isValidProperty(){
		if(propertyName==null ||oConversions==null || sConversions==null){
			return false;
		}
		return true;		
	}
	
	/**Check if Property match p1 refines to p2.
	 * @param p1 source property match to check.
	 * @param p2 destination property to check.
 	 * @return true if p1 refines to p2.
	 */
	public final boolean isValidRefinement(final PropertyMatch p1, final PropertyMatch p2){
		/**Check that p1 and p2 are right levels for this refinement.
		 * 
		 */
		if (!(sourceLevel == p1.getProp().getLevel() 
				|| destinationLevel == p2.getProp().getLevel())) {
			return false;
		}
		String p1in = p1.getInputToMatch();
		StringTokenizer parser1 = new StringTokenizer(p1in);
		String p2in = p2.getInputToMatch();
		StringTokenizer parser2 = new StringTokenizer(p2in);
		while (parser1.hasMoreTokens()) {
		    String i = parser1.nextToken();
		    String j = parser2.nextToken();
		    if (sConversions.containsKey(i)) {
		    	if (!j.equals(sConversions.get(i))) {
		    		return false;
		    		}
		    } else if (oConversions.containsKey(i)) {
		    	if (!j.equals(oConversions.get(i))) {
		    		return false;
		    	}
		    } else {
		    	System.out.println("problem with " + i + " and " + j);
		    	return false;
		    
		    }
		
		}
		return true;	
		
	}
	/**Refine p1 based on rules in this Refinement Property.
	 * 
	 * @param p1 Source Property Match.
	 * @return The refinement of p1 using this refinement property.
	 */
	public final PropertyMatch refine(final PropertyMatch p1){
		return p1;
		
	}
	/**Getters.
	 * 
	 */
	
	/**
	 * @return the sConversions.
	 */
	public final LinkedHashMap<String, String> getSConversions() {
		return sConversions;
	}
	/**
	 * @return the oConversions
	 */
	public final LinkedHashMap<String, Transitions> getOConversions() {
		return oConversions;
	}
	/**
	 * @return the sourceLevel
	 */
	public final int getSourceLevel() {
		return sourceLevel;
	}
	/**
	 * @return the destinationLevel
	 */
	public final int getDestinationLevel() {
		return destinationLevel;
	}

}
