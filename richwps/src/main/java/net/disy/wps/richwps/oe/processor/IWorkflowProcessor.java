package net.disy.wps.richwps.oe.processor;

import java.util.Map;

import net.opengis.wps.x100.ExecuteDocument;

import org.n52.wps.io.data.IData;

import de.hsos.richwps.dsl.api.elements.Workflow;

public interface IWorkflowProcessor {

	Map<String, IData> process(ExecuteDocument document, Workflow worksequence);

	ProcessingContext getProcessingContext();

	ProfilingOutputs examineProcess(ExecuteDocument executeDocument,
			Workflow workflow);

}
