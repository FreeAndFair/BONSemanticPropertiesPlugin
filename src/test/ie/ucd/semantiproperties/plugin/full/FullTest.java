package ie.ucd.semantiproperties.plugin.full;


import ie.ucd.semanticproperties.plugin.api.LevelId;
import ie.ucd.semanticproperties.plugin.api.ScopeId;
import ie.ucd.semanticproperties.plugin.api.SemanticPropertiesHandler;
import ie.ucd.semanticproperties.plugin.api.SemanticPropertyInstance;
import ie.ucd.semanticproperties.plugin.exceptions.IncompatibleSemanticPropertyInstancesException;
import ie.ucd.semanticproperties.plugin.exceptions.InvalidRefinementException;
import ie.ucd.semanticproperties.plugin.exceptions.InvalidSemanticPropertySpecificationException;
import ie.ucd.semanticproperties.plugin.exceptions.InvalidSemanticPropertyUseException;
import ie.ucd.semanticproperties.plugin.exceptions.UndefinedLevelException;
import ie.ucd.semanticproperties.plugin.exceptions.UnknownPropertyException;
import ie.ucd.semanticproperties.plugin.exceptions.UndefinedScopeException;
import ie.ucd.semanticproperties.plugin.exceptions.UnknownVariableIdentifierException;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FullTest {
  private static final String PATH = "resources/examples/junit/";
  private SemanticPropertiesHandler handler;
  private SemanticPropertiesHandler handler2;
  
  @Before
  public void setUp() throws Exception {
    handler = new SemanticPropertiesHandler();
    handler2 = new SemanticPropertiesHandler();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected=IOException.class)
  public void invalidFile() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "sdfasdfkjskdfjsa.yaml"));
  }  
  
  @Test public void addProperty() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
  }
  
  @Test(expected=InvalidSemanticPropertySpecificationException.class)
  public void addGarbageProperty() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "garbage.yaml"));
  }
  
  @Test(expected=InvalidSemanticPropertySpecificationException.class)
  public void addEmptyProperty() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "empty.yaml"));
  }
  
  @Test(expected=InvalidSemanticPropertySpecificationException.class) 
  public void addSomewhatInvalidProperty() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "invalidproperty.yaml"));
  }
  
  @Test(expected=UndefinedLevelException.class) 
  public void addUnknownLevelProperty() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "invalidlevel.yaml"));
  }
  
  
  @Test(expected=UndefinedScopeException.class) 
  public void addUnknownScopeProperty() throws InvalidSemanticPropertySpecificationException, IOException {
    handler.add(new File(PATH + "invalidscope.yaml"));
  }
  
  
  @Test 
  public void parsingTest1() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    handler.parse("concurrency SEQUENTIAL",LevelId.BON_FORMAL, ScopeId.MODULE);
    handler.parse("concurrency CONCURRENT", LevelId.BON_FORMAL, ScopeId.MODULE);
  }
  
  @Test 
  public void parsingTest2() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    handler.parse("TIMEOUT 5 Esadfadfxception", "concurrency", LevelId.BON_FORMAL, ScopeId.MODULE);
    handler.parse("FAILURE a.Esadfadfxception", "concurrency", LevelId.BON_FORMAL, ScopeId.MODULE);
  }
  
  @Test 
  public void parsingTest3() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    handler.parse("concurrency SPECIAL 'dsfasdfads'",  LevelId.BON_FORMAL, ScopeId.MODULE);
    handler.parse("concurrency SPECIAL \"dsf'asd'fads\"",  LevelId.BON_FORMAL, ScopeId.MODULE);
  }
  
  @Test(expected=UnknownVariableIdentifierException.class)
  public void parsingTest4() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, UnknownVariableIdentifierException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency TIMEOUT 5 java.lang.Exception",  LevelId.BON_FORMAL , ScopeId.MODULE);
    inst.getVariable("xxx");
  }
  
  @Test
  public void parsingTest5() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, UnknownVariableIdentifierException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency TIMEOUT 5 java.lang.Exception",  LevelId.BON_FORMAL, ScopeId.MODULE);
    assertEquals(inst.getVariable("c1"), "TIMEOUT");
    assertEquals(inst.getVariable("to"), 5);
    assertEquals(inst.getVariable("e"), "java.lang.Exception");
  }
  
  @Test
  public void parsingTest6() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, UnknownVariableIdentifierException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency TIMEOUT 5 java.lang.Exception",  LevelId.BON_FORMAL, ScopeId.MODULE);
    assertEquals(inst.getVariable("to"), 5);
  }
  
  @Test (expected=InvalidSemanticPropertyUseException.class)
  public void parsingTest7() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException  {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENPPPPAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
  }
  
  
  @Test (expected=UnknownPropertyException.class)
  public void parsingTest8() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException  {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency2222 SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
  }
  
  
  
  @Test(expected=UndefinedLevelException.class) 
  public void parsingTest9() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException  {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_INFORMAL, ScopeId.MODULE);
  }
  

  @Test(expected=UndefinedScopeException.class) 
  public void parsingTest10() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException  {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.VARIABLE);
  }
  
  @Test 
  public void refinementTest1() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, IncompatibleSemanticPropertyInstancesException, InvalidRefinementException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
    SemanticPropertyInstance refInst = handler.generate(inst, LevelId.JAVA_JML);
    // I changed this from concurrency SEQUENTIAL to concurrency SEQ as i think that should be the correct outcome.
    assertEquals(inst.toString(), "concurrency SEQUENTIAL");

    assertEquals(refInst.toString(), "concurrency SEQ");
  }
  
  ///eoins added tests

  @Test(expected=IncompatibleSemanticPropertyInstancesException.class)
  public void refinementTest2() throws IncompatibleSemanticPropertyInstancesException, InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, UnknownVariableIdentifierException, InvalidRefinementException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
    handler.isValidRefinement(inst,inst);

  }
  
  @Test(expected=InvalidRefinementException.class)
  public void refinementTest3() throws InvalidRefinementException, InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, UnknownVariableIdentifierException, InvalidRefinementException, IncompatibleSemanticPropertyInstancesException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
    SemanticPropertyInstance inst2 = handler.parse("concurrency GUARD",  LevelId.JAVA_JML, ScopeId.MODULE);
    handler.isValidRefinement(inst,inst2);

  }
  
  
  @Test(expected=IncompatibleSemanticPropertyInstancesException.class) 
  public void validRefinementTest1() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, IncompatibleSemanticPropertyInstancesException, UnknownVariableIdentifierException, InvalidRefinementException  {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    handler.add(new File(PATH + "substringEg.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
    SemanticPropertyInstance inst2 = handler.parse("substringEg a des. (sd) 'hd'",  LevelId.BON_FORMAL, ScopeId.MODULE);
    handler.isValidRefinement(inst, inst2);
  }
  @Test(expected=UnknownPropertyException.class) 
  public void validRefinementTest2() throws InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, IncompatibleSemanticPropertyInstancesException, UnknownVariableIdentifierException, InvalidRefinementException  {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    handler2.add(new File(PATH + "substringEg.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
    SemanticPropertyInstance inst2 = handler2.parse("substringEg a des. (sd) 'hd'",  LevelId.BON_FORMAL, ScopeId.MODULE);
    handler.isValidRefinement(inst, inst2);
  }
  
  @Test(expected=IncompatibleSemanticPropertyInstancesException.class)
  public void validRefinementTest3() throws InvalidRefinementException, InvalidSemanticPropertySpecificationException, IOException, UnknownPropertyException, InvalidSemanticPropertyUseException, UnknownVariableIdentifierException, InvalidRefinementException, IncompatibleSemanticPropertyInstancesException {
    handler.add(new File(PATH + "../concurrency_full.yaml"));
    SemanticPropertyInstance inst = handler.parse("concurrency SEQUENTIAL",  LevelId.BON_FORMAL, ScopeId.MODULE);
    SemanticPropertyInstance inst2 = handler.parse("concurrency SEQ",  LevelId.JAVA_JML, ScopeId.MODULE);
    handler.isValidRefinement(inst2,inst);

  }
}
