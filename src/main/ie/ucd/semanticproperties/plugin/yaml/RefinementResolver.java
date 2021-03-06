package ie.ucd.semanticproperties.plugin.yaml;

import java.util.regex.Pattern;

import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

public class RefinementResolver extends Resolver {

  @Override
	protected void addImplicitResolvers() {
		addImplicitResolver(new Tag("!transitions"), Pattern.compile("(?:equivalent|equals|prefix|suffix|substring|<|gr|=|<=|gr=)"), null);
	}
}