/**
 * @title "Semantic LevelRepresenation Plugin Package"
 * @description "Class that holds all properties in one structure."
 * @author  Eoin O'Connor
 * @copyright ""
 * @license "Public Domain."
 * @version "$Id: 01-07-2010 $"
 */
package ie.ucd.semanticproperties.plugin.structs;

import ie.ucd.semanticproperties.plugin.exceptions.InvalidSemanticPropertySpecificationException;
import ie.ucd.semanticproperties.plugin.yaml.CustomConstructor;
import ie.ucd.semanticproperties.plugin.yaml.CustomRepresenter;
import ie.ucd.semanticproperties.plugin.yaml.CustomResolver;
import ie.ucd.semanticproperties.plugin.yaml.RefinementConstructor;
import ie.ucd.semanticproperties.plugin.yaml.RefinementRepresenter;
import ie.ucd.semanticproperties.plugin.yaml.RefinementResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;


/**
 * <p>
 * A class that takes yaml files,parses them with snakeyaml and constructs and
 * stores the appropiate semantic properties
 * </p>
 * 
 * @see LevelRepresenation
 * @see RegExpStruct
 * @version "$Id: 01-07-2010 $"
 * @author Eoin O'Connor
 **/
public class SemanticProperty {

	// Attributes

	private static LinkedHashMap<Integer, LevelRepresenation> levels;
	private static List<Refinement> refinements;
	private File input;

	/**Constructor for Semantic Property.
	 * 
	 * @param inputFile yaml file  to parse.
	 */
	public SemanticProperty(File inputFile) throws FileNotFoundException, InvalidSemanticPropertySpecificationException, IOException {
		input = inputFile;
		levels = new LinkedHashMap<Integer, LevelRepresenation>();
		refinements = new LinkedList<Refinement>();
		parseFile(input);
	}

	/**Printing class for testing.
	 * <p>
	 * method that prints the name,regexp and map of each regular expression.
	 * Used for testing
	 * </p>
	 */
	public void printProps() {
//		for (LevelRepresenation p : levels) {
//			System.out.println(p);
//			System.out
//					.println("Regular expression is " + (p.getReg()).getExp());
//			System.out.println("Regular expression map is "
//					+ (p.getReg()).getGroupInt());
//		}
	}

	/**Check refinement from String input1 to String input2.
	 * 
	 * @param input1 source string to check against.
	 * @param input2 destination string to check. 
	 * @param level1 source level.
	 * @param level2 destination level.
	 * @return true if valid refinement.
	 */
	public boolean checkRefinement(String input1, String input2, int level1, int level2) {
		/**Create levelMatches for the inputs
		 * 
		 */
		LevelRepMatch p1 = new LevelRepMatch(input1, levels.get(level1));
		LevelRepMatch p2 = new LevelRepMatch(input2, levels.get(level2));

		/**search through refinements for appropriate one.
		 * 
		 */
		for (Refinement pres : refinements) {
			if (pres.getSourceLevel() == level1 
					&& pres.getDestinationLevel() == level2) {
				/**Check refinement.
				 * 
				 */
				return pres.isValidRefinement(p1, p2);
			}			
		}
		return false;
	}

	/** 
	 * parses a file using snakeyaml and creates the appropriate Semantic Property.
	 * 
	 * 
	 * @param inputFile yaml file to parse, may contain multiple levels and refinements
	 */
	private static void parseFile(File inputFile) throws FileNotFoundException, InvalidSemanticPropertySpecificationException, IOException{

		/**
		 * get input stream from file
		 */ 
		InputStream input  = new FileInputStream(inputFile);
		
		/**
		 * create Yaml parsed object for level
		 */
		Yaml levelYaml = new Yaml(new Loader(new CustomConstructor()), new Dumper(
				new CustomRepresenter(), new DumperOptions()),new CustomResolver());
		Object levelData = levelYaml.loadAll(input);

		

		try {
			Iterable s = (Iterable<Object>) levelData;
			Iterator<LinkedHashMap<String, ?>> i;
			i = s.iterator();
			// iterate through the properties and add them
			while (i.hasNext()) {
				Object pres = (Object) i.next();
				if (!(pres instanceof LinkedHashMap< ? , ? >)){
					System.out.println("this yaml file is" +
					"incorrectly formated to be a semantic property ");
					throw new InvalidSemanticPropertySpecificationException();

				}
				
				LinkedHashMap< String , ?> presMap = ((LinkedHashMap< String, ? >)pres);
				/**Check if map represents a level or a refinement.
				 * 
				 */
				//  level.
				if (presMap.get("name") != null) {
					LevelRepresenation temp = new LevelRepresenation(presMap);
					levels.put(temp.getLevel(), temp);
				
				// refinememnt
				} else if (presMap.get("property") != null) {
					
				} else {
					System.out.println("yaml file has linked hashmap that" +
							"isin't refinement or level");
					throw new InvalidSemanticPropertySpecificationException();
				}
					
				 
			}
		} catch (Exception e) {
			throw new InvalidSemanticPropertySpecificationException();
		} finally{
			input.close();
		}

		/**
		 * Create Yaml parsed object for refinements.
		 */
		InputStream input2  = new FileInputStream(inputFile);
		Yaml refinementYaml = new Yaml(new Loader(new RefinementConstructor()), new Dumper(
				new RefinementRepresenter(), new DumperOptions()),new RefinementResolver());
		Object refinementData = refinementYaml.loadAll(input2);

		Iterable s2 = (Iterable<Object>) refinementData;
		Iterator<LinkedHashMap<String, ?>> i2;
		i2 = s2.iterator();
		// iterate through the properties and add them
		while (i2.hasNext()) {
			Object pres = (Object) i2.next();
			if (pres instanceof LinkedHashMap< ? , ? >) {
				LinkedHashMap< String , ?> presMap = ((LinkedHashMap< String, ? >)pres);
				/**Check if map represents a refinement and if so add it to .
				 * 
				 */
				if (presMap.get("property") != null) {
					refinements.add(new Refinement(presMap));
				} 
			} 
		}
		input2.close();

	}

}