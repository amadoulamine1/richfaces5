/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.util;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.ui.ajax.AjaxFunction;
import org.richfaces.ui.ajax.AjaxOptions;
import org.richfaces.ui.behavior.HandlersChain;

import com.google.common.base.Strings;

/**
 * @author shura
 *         <p/>
 *         Some utilites for render AJAX components.
 */
public final class AjaxRendererUtils {
    public static final String BEGIN_EVENT_NAME = "begin";
    public static final String AJAX_ABORT_ATTR = "ignoreDupResponses";
    public static final String AJAX_AREAS_RENDERED = "org.ajax4jsf.areas.rendered";
    public static final String AJAX_DELAY_ATTR = "requestDelay";
    public static final String AJAX_QUEUE_ATTR = "eventsQueue";
    public static final String AJAX_SINGLE_ATTR = "ajaxSingle";
    public static final String AJAX_SINGLE_PARAMETER_NAME = "ajaxSingle";
    public static final String ONBEGIN_ATTR_NAME = "onbegin";
    /**
     * Attribute for keep JavaScript function name for call after complete request.
     */
    public static final String ONCOMPLETE_CONTENT_ID = "org.ajax4jsf.oncomplete";
    public static final String SIMILARITY_GROUPING_ID_ATTR = "similarityGroupingId";
    /**
     * Attribute for keep clientId of status component
     */
    public static final String STATUS_ATTR_NAME = "status";
    public static final String VALUE_ATTR = "value";
    public static final String QUEUE_ID_ATTRIBUTE = "queueId";
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    /**
     * Static class - protect constructor
     */
    private AjaxRendererUtils() {
    }

    // TODO make this function private
    public static AjaxOptions buildEventOptions(FacesContext facesContext, UIComponent component) {
        AjaxOptions ajaxOptions = new AjaxOptions();
        appendComponentOptions(facesContext, component, ajaxOptions);

        Map<String, Object> parametersMap = RENDERER_UTILS.createParametersMap(facesContext, component);
        ajaxOptions.addParameters(parametersMap);

        return ajaxOptions;
    }

    private static String getHandlerScript(FacesContext facesContext, UIComponent component, String attributeName,
        String eventName) {
        HandlersChain handlersChain = new HandlersChain(facesContext, component);
        String inlineHandler = (String) component.getAttributes().get(attributeName);

        handlersChain.addInlineHandlerAsValue(inlineHandler);
        handlersChain.addBehaviors(eventName);

        return handlersChain.toScript();
    }

    private static void appendComponentOptions(FacesContext facesContext, UIComponent component, AjaxOptions ajaxOptions) {
        String handlerScript = getHandlerScript(facesContext, component, ONBEGIN_ATTR_NAME, BEGIN_EVENT_NAME);
        if (!Strings.isNullOrEmpty(handlerScript)) {
            ajaxOptions.set(BEGIN_EVENT_NAME, handlerScript);
        }

        String queueId = getQueueId(component);
        if (!Strings.isNullOrEmpty(queueId)) {
            ajaxOptions.set(QUEUE_ID_ATTRIBUTE, queueId);
        }

        ajaxOptions.set("incId", "1");

        String status = getAjaxStatus(component);
        if (!Strings.isNullOrEmpty(status)) {
            ajaxOptions.set(STATUS_ATTR_NAME, status);
        }
    }

    /**
     * Create call to Ajax Submit function with first two parameters
     *
     * @param facesContext
     * @param component
     * @return
     */
    public static AjaxFunction buildAjaxFunction(FacesContext facesContext, UIComponent component) {
        return new AjaxFunction(component.getClientId(facesContext), buildEventOptions(facesContext, component));
    }

    /**
     * Get status area Id for given component.
     *
     * @param component
     * @return clientId of status area, or <code>null</code>
     */
    public static String getAjaxStatus(UIComponent component) {
        return (String) component.getAttributes().get(STATUS_ATTR_NAME);
    }

    private static String getQueueId(UIComponent component) {
        return (String) component.getAttributes().get(QUEUE_ID_ATTRIBUTE);
    }
}
