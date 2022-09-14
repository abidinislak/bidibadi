package com.bidibadi.admin.user.exportcvs;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.bidibadi.common.entity.User;

public class UserCsvExporter {

	public void export(List<User> listusers, HttpServletResponse response) throws IOException {

		DateFormat dateFormatter = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");

		String timestamp = dateFormatter.format(new Date());

		String fileName = "users_" + timestamp + ".csv";

		String headerKey = "Content-Disposition";
		response.setContentType("text/scv");
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "User ID", "E-mail", "First Name", "Last Name", "Role", "Enabled" };
		String[] fieldMapping = { "id", "email", "firstname", "lastname", "roles", "enabled" };

		csvWriter.writeHeader(csvHeader);

		for (User user : listusers) {

			csvWriter.write(user, fieldMapping);

		}

		csvWriter.close();

	}
}
