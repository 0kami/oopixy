package at.ac.tuwien.infosys.www.pixy.sanitation;

import at.ac.tuwien.infosys.www.pixy.MyOptions;
import at.ac.tuwien.infosys.www.pixy.SqlAnalysis;
import at.ac.tuwien.infosys.www.pixy.Utils;
import at.ac.tuwien.infosys.www.pixy.VulnerabilityInformation;
import at.ac.tuwien.infosys.www.pixy.analysis.dependency.DependencyAnalysis;
import at.ac.tuwien.infosys.www.pixy.analysis.dependency.Sink;
import at.ac.tuwien.infosys.www.pixy.conversion.TacActualParameter;
import at.ac.tuwien.infosys.www.pixy.conversion.TacFunction;
import at.ac.tuwien.infosys.www.pixy.conversion.cfgnodes.AbstractCfgNode;
import at.ac.tuwien.infosys.www.pixy.conversion.cfgnodes.CallBuiltinFunction;
import at.ac.tuwien.infosys.www.pixy.conversion.cfgnodes.CallPreparation;

import java.util.List;

/**
 * SQL Injection detection (with precise sanitation detection).
 *
 * Note: This class will be instantiated via reflection in GenericTaintAnalysis.createAnalysis. It is registered in
 * MyOptions.VulnerabilityAnalysisInformation.
 *
 * @author Nenad Jovanovic <enji@seclab.tuwien.ac.at>
 */
public class SQLSanitationAnalysis extends AbstractSanitationAnalysis {
    public SQLSanitationAnalysis(DependencyAnalysis dependencyAnalysis) {
        this(dependencyAnalysis, true);
    }

    public SQLSanitationAnalysis(DependencyAnalysis dependencyAnalysis, boolean getIsTainted) {
        super("sql", dependencyAnalysis, FSAAutomaton.getUndesiredSQLTest());
        this.getIsTainted = getIsTainted;
        if (MyOptions.fsaHome == null) {
            Utils.bail("SQL Sanitization analysis requires FSA Utilities.\n" +
                "Please set a valid path in the config file.");
        }
    }

//  ********************************************************************************

    public List<Integer> detectVulnerabilities() {
        return detectVulnerabilities(new SqlAnalysis(this.dependencyAnalysis));
    }

    public VulnerabilityInformation detectAlternative() {
        throw new RuntimeException("not yet");
    }

//  ********************************************************************************

    // checks if the given node (inside the given function) is a sensitive sink;
    // adds an appropriate sink object to the given list if it is a sink
    protected void checkForSink(AbstractCfgNode cfgNodeX, TacFunction traversedFunction,
                                List<Sink> sinks) {

        if (cfgNodeX instanceof CallBuiltinFunction) {

            // builtin function sinks

            CallBuiltinFunction cfgNode = (CallBuiltinFunction) cfgNodeX;
            String functionName = cfgNode.getFunctionName();

            checkForSinkHelper(functionName, cfgNode, cfgNode.getParamList(), traversedFunction, sinks);
        } else if (cfgNodeX instanceof CallPreparation) {

            CallPreparation cfgNode = (CallPreparation) cfgNodeX;
            String functionName = cfgNode.getFunctionNamePlace().toString();

            // user-defined custom sinks

            checkForSinkHelper(functionName, cfgNode, cfgNode.getParamList(), traversedFunction, sinks);
        } else {
            // not a sink
        }
    }

//  ********************************************************************************

    private void checkForSinkHelper(String functionName, AbstractCfgNode cfgNode,
                                    List<TacActualParameter> paramList, TacFunction traversedFunction, List<Sink> sinks) {

        if (this.vulnerabilityAnalysisInformation.getSinks().containsKey(functionName)) {
            Sink sink = new Sink(cfgNode, traversedFunction);
            for (Integer param : this.vulnerabilityAnalysisInformation.getSinks().get(functionName)) {
                if (paramList.size() > param) {
                    sink.addSensitivePlace(paramList.get(param).getPlace());
                    // add this sink to the list of sensitive sinks
                    sinks.add(sink);
                }
            }
        } else {
            // not a sink
        }
    }
}