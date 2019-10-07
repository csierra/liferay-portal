package com.liferay.symposium.rest.app;

import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.remote.cors.annotation.CORS;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

import javax.annotation.Resource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component(
	immediate = true, property = {
		"liferay.cors.annotation=true",
		"oauth2.scope.checker.type=annotations",
		"osgi.jaxrs.application.base=/symposium",
		"osgi.jaxrs.name=Registro.Horario",
	},
	service = Application.class
)
public class RestApp extends Application {

	@CORS(allowMethods = "GET")
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresScope("registro.horario.read")
	public String getUsers() {
		List<User> users = UserLocalServiceUtil.getUsers(
			PortalUtil.getDefaultCompanyId(), false,
			WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		JSONArray result = JSONFactoryUtil.createJSONArray();

		users.forEach(user -> {
			JSONObject json = JSONFactoryUtil.createJSONObject();
			json.put("name", user.getFullName());
			json.put("id", user.getUserId());
			if (!user.getFullName().startsWith("Test"))
				result.put(json);
		});

		return result.toString();

	}

	@Override
	public Set<Object> getSingletons() {
		return Collections.singleton(this);
	}


}
