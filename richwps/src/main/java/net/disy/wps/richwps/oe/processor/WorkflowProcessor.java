package net.disy.wps.richwps.oe.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.opengis.wps.x100.ExecuteDocument;
import net.opengis.wps.x100.InputType;

import org.apache.commons.lang.Validate;
import org.n52.wps.io.data.IData;
import org.n52.wps.server.ExceptionReport;
import org.n52.wps.server.request.InputHandler;

import de.hsos.richwps.dsl.api.elements.IOperation;
import de.hsos.richwps.dsl.api.elements.Workflow;

public class WorkflowProcessor implements IWorkflowProcessor {

	private List<IOperationHandler> operationHandlers = new ArrayList<IOperationHandler>();
	private ProcessingContext processingContext;

	public WorkflowProcessor() {
		initOperationHandlers();
	}

	private void initOperationHandlers() {
		operationHandlers.add(new AssignmentHandler());
		operationHandlers.add(new BindingHandler());
		operationHandlers.add(new ExecuteHandler());
		operationHandlers.add(new ConditionalHandler());
	}

	@Override
	public Map<String, IData> process(ExecuteDocument executeDocument,
			Workflow workflow) {
		Validate.notNull(executeDocument);
		Validate.notNull(workflow);
		processingContext = createProcessingContext(executeDocument);

		for (IOperation operation : workflow) {
			IOperationHandler handler = getHandlerForOperation(operation);
			handler.handleOperation(operation, processingContext);
		}
		return processingContext.getOutputData();
	}

	@Override
	public ProfilingOutputs examineProcess(ExecuteDocument executeDocument,
			Workflow workflow) {
		Validate.notNull(executeDocument);
		Validate.notNull(workflow);
		processingContext = createProcessingContext(executeDocument);
		TimeMeasurement timeMeasurement = processingContext
				.getTimeMeasurement();
		for (IOperation operation : workflow) {
			timeMeasurement.start(operation);
			IOperationHandler handler = getHandlerForOperation(operation);
			handler.handleOperation(operation, processingContext);
			timeMeasurement.stop();
		}
		return new ProfilingOutputs(timeMeasurement,
				processingContext.getOutputData());
	}

	@Override
	public ProcessingContext getProcessingContext() {
		return processingContext;
	}

	private ProcessingContext createProcessingContext(
			ExecuteDocument executeDocument) {
		ProcessingContext context = new ProcessingContext(executeDocument);
		InputType[] outerProcessInputs = executeDocument.getExecute()
				.getDataInputs().getInputArray();
		Map<String, List<IData>> outerProcessInputData = null;
		try {
			InputHandler outerProcessInputHandler = new InputHandler.Builder(
					outerProcessInputs, executeDocument.getExecute()
							.getIdentifier().getStringValue()).build();
			outerProcessInputData = outerProcessInputHandler
					.getParsedInputData();
			context.getInputData().putAll(outerProcessInputData);
		} catch (ExceptionReport e) {
			throw new RuntimeException("Could not parse input data", e);
		}
		return context;
	}

	private IOperationHandler getHandlerForOperation(IOperation operation) {
		for (IOperationHandler handler : operationHandlers) {
			if (handler.canHandle(operation)) {
				return handler;
			}
		}
		return null;
	}

}
