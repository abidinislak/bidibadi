package com.bidibadi.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

		Path upLoadPath = Paths.get(uploadDir);

		if (!Files.exists(upLoadPath)) {

			Files.createDirectories(upLoadPath);

		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = upLoadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}

		catch (IOException ex) {
			throw new IOException("uupsss. Kopyalanırkenhata oluştu   : " + fileName, ex);

		}

	}

	public static void cleanDir(String dir) {
		Path dirPAth = Paths.get(dir);

		try {
			Files.list(dirPAth).forEach(file -> {

				if (!Files.isDirectory(file)) {

					try {
						Files.delete(file);

					} catch (Exception e) {

						LOGGER.error("silme İşlemiBaşarısız" + file);
						System.err.println("" + file);

					}

				}

			});

		} catch (IOException e) {

			LOGGER.error("BuKlasoruBulamadi" + dirPAth);
			System.err.println("BuKlasoruBulamadi");
		}
	}

	public static void removeDir(String categoryDir) {
		cleanDir(categoryDir);

		try {
			Files.delete(Paths.get(categoryDir));
		} catch (Exception e) {
			LOGGER.error("cOULD NOT REMOVE DİRECTORY" + categoryDir);

		}
	}

}
