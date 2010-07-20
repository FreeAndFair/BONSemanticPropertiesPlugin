package ie.ucd.semantic_properties_plugin.file_checker;

import java.io.File;

import junit.framework.TestCase;

public class ConcurrencyTest extends TestCase{

	/**Test Case for concurrency example.
	 * 
	 */
	public final void  testConProp() {
		
		/**Create bon property and match.
		 * 
		 */
		File bonYaml = new File("resources/examples/concurrency_bon.yaml");
		LevelRepresenation conBonProp = new LevelRepresenation(bonYaml);
		String bonInput = 
			"concurrency CONCURRENT 'This class is fully thread-safe.'";
		LevelRepMatch bonMatch = new LevelRepMatch(bonInput, conBonProp);
		/**Check if its a valid match.
		 * 
		 */
		assertTrue(bonMatch.isValid());
		

		/**Create java property and match.
		 * 
		 */
		File javaYaml = new File("resources/examples/concurrency_java.yaml");
		LevelRepresenation conJavaProp = new LevelRepresenation(javaYaml);
		String javaInput = 
			"concurrency PARALLEL 'This class is fully thread-safe.  Moreover, we avoid any mention of locks.'";
		LevelRepMatch javaMatch = new LevelRepMatch(javaInput, conJavaProp);
		/**Check if its a valid match.
		 * 
		 */
		assertTrue(javaMatch.isValid());
		
		/**Create refinement LevelRepresenation, check isValidRefinement().
		 * <p>Create refinement LevelRepresenation and check 
		 * if bonMatch refines to javaMatch.</p>
		 */
		File refinementYaml = 
			new File("resources/examples/concurrency_refinement.yaml");
		
		Refinement refProp =
			new Refinement(refinementYaml);
		
		assertTrue(refProp.isValidRefinement(bonMatch, javaMatch));
//
//		
//		LevelRepMatch refinedMatch=refProp.refine(match);
//		
//		assertTrue(refProp.isValidRefinement(refinedMatch,match));
		
		
		
		
	}
	public final void  testNewConProp() {
		
		File conYaml = new File("resources/examples/concurrency_full.yaml");
		SemanticProperty concurrency = new SemanticProperty(conYaml);
		
	}
}