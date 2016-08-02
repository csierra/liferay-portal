package com.liferay.document.library.jaxrs;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sergiogonzalez on 26/07/16.
 */
@XmlRootElement
public class RepositoryContentObject {

	public RepositoryContentObject() {
	}

	public RepositoryContentObject(
		String uuid, String title, String url,
		RepositoryContentType type) {
		_uuid = uuid;
		_title = title;
		_url = url;
		_type = type;
	}

	private String _uuid;
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

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
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

	public static RepositoryContentObject createContentObject(Object object) {
		if (object instanceof FileEntry) {
			FileEntry fileEntry = (FileEntry)object;

			return new RepositoryContentObject(
				fileEntry.getUuid(), fileEntry.getTitle(), "",
				RepositoryContentType.FILE);
		}
		else if (object instanceof Folder) {
			Folder folder = (Folder)object;

			return new RepositoryContentObject(
				folder.getUuid(), folder.getName(), "",
				RepositoryContentType.FOLDER);
		}
		else if (object instanceof FileShortcut) {
			FileShortcut fileShortcut = (FileShortcut)object;

			return new RepositoryContentObject(
				fileShortcut.getUuid(), fileShortcut.getToTitle(), "",
				RepositoryContentType.SHORTCUT);
		}
		else {
			throw new IllegalArgumentException(
				"Object must be an instance of FileEntry, Folder of FileShortcut");
		}
	}
}
