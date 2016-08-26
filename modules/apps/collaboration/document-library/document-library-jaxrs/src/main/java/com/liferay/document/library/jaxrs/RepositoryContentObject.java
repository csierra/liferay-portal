package com.liferay.document.library.jaxrs;

import com.liferay.document.library.kernel.util.comparator.RepositoryModelCreateDateComparator;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelSizeComparator;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelTitleComparator;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.OrderByComparator;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by sergiogonzalez on 26/07/16.
 */
@XmlRootElement
public class RepositoryContentObject {

	public RepositoryContentObject() {
	}

	public RepositoryContentObject(
		long id, String title, String url, RepositoryContentType type) {

		_id = id;
		_title = title;
		_url = url;
		_type = type;
	}

	private long _id;
	private String _title;
	private String _url;
	private RepositoryContentType _type;

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public RepositoryContentType getType() {
		return _type;
	}

	public void setType(
		RepositoryContentType type) {
		_type = type;
	}

	public long getId() {
		return _id;
	}

	public void setId(long id) {
		_id = id;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public enum RepositoryContentType {
		FILE, FOLDER, SHORTCUT
	}

	public static final Map<String, Function<Boolean, OrderByComparator<Object>>>
		comparators =
			new HashMap<String, Function<Boolean, OrderByComparator<Object>>>() {{
				put("title", RepositoryModelTitleComparator::new);
				put("date", RepositoryModelCreateDateComparator::new);
				put("size", RepositoryModelSizeComparator::new);
			}};
}
