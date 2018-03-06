package org.dissertationWeb.classes;

import org.springframework.web.multipart.MultipartFile;

/**
 * Class I create since I need to have a multipartFile as the parameter passed to the fron end to accept CSV files
 * Since multipartFile can not be created I been forced to create this class with the attribute multipartFile
 * @author ismael
 *
 */
public class FileBucket {

	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}