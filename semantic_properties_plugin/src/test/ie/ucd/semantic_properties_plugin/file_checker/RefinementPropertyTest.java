
package ie.ucd.semantic_properties_plugin.file_checker;

import java.io.File;

import junit.framework.TestCase;


/**
 * @author eo
 *
 */
public class RefinementPropertyTest extends TestCase {

	/** Check refinement prefix, suffix, substring.
	 * 
	 */
	public final void testPrefixRefinement() {
		
		String refinementPropertyString = "";
		Refinement refinement = 
			new Refinement(refinementPropertyString);
		
		
		String sourcePropertyString = "";
		LevelRepresenation sourceProperty = new LevelRepresenation(sourcePropertyString);
		
		String refinedPropertyString = "";
		LevelRepresenation refinedProperty = new LevelRepresenation(refinedPropertyString);
		
		
		
		assertTrue(true);
	}

}