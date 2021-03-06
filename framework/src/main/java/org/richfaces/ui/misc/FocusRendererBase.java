package org.richfaces.ui.misc;

import org.richfaces.javascript.JSLiteral;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.services.ServiceTracker;
import org.richfaces.ui.misc.focus.FocusRenderStrategy;
import org.richfaces.ui.misc.focus.FocusRendererInterface;
import org.richfaces.ui.misc.focus.FormFocusRenderStrategy;
import org.richfaces.ui.misc.focus.ViewFocusRenderStrategy;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.core.js"),
        @ResourceDependency(library = "org.richfaces", name = "focus.js") })
public class FocusRendererBase extends RendererBase implements FocusRendererInterface {

    public static final String RENDERER_TYPE = "org.richfaces.FocusRenderer";

    private final FormFocusRenderStrategy FORM_RENDERING_STRATEGY = new FormFocusRenderStrategy();
    private final ViewFocusRenderStrategy VIEW_RENDERING_STRATEGY = new ViewFocusRenderStrategy();

    /**
     * Determines whether the currently rendered Focus should be rendered or not based on if request is postback and if Focus
     * belongs to form which has been submitted.
     */
    public boolean shouldApply(FacesContext context, AbstractFocus component) {
        return getStrategy(component).shouldApply(context, component);
    }

    /**
     * Get space-separated list of clientIds as component candidates to be focused on client.
     */
    public String getFocusCandidatesAsString(FacesContext context, AbstractFocus component) {
        return getStrategy(component).getFocusCandidatesAsString(context, component);
    }

    @Override
    public void postAddToView(FacesContext context, AbstractFocus component) {
        getStrategy(component).postAddToView(context, component);
    }

    /**
     * This method ensures that component which should be focused will be present in the page in the time of running script (oncomplete).
     */
    public void renderOncompleteScript(FacesContext context, String script) {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        javaScriptService.addScript(context, new JSLiteral(script));
    }

    private FocusRenderStrategy getStrategy(AbstractFocus component) {
        switch (component.getMode()) {
            case FORM:
                return FORM_RENDERING_STRATEGY;
            case VIEW:
                return VIEW_RENDERING_STRATEGY;
            default:
                throw new UnsupportedOperationException("Retrieving focus candidates in " + component.getMode()
                        + " mode is not supported");
        }
    }
}
