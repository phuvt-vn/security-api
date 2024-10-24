package com.example.main.api.server.xss;

import java.io.IOException;
import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.owasp.encoder.Encode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/xss/safe/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class XssSimpleSafeApi {

	@GetMapping(value = "/greeting", produces = MediaType.TEXT_PLAIN_VALUE)
	public String greeting(@RequestParam(value = "name", required = true) String name) {
		var nowHour = LocalTime.now().getHour();
		var greetString = (nowHour >= 6 && nowHour < 18) ? ("Good morning " + name) : ("Good night " + name);
		var encode = Encode.forHtml(name);
		var encode2 = Encode.forHtmlContent(name);
		var encode3 = Encode.forHtmlAttribute(name);
		var encode4 = Encode.forJava(name);
		return Encode.forHtml(greetString);
	}

	private Tika tika = new Tika();

	@GetMapping(value = "/file")
	public ResponseEntity<Resource> downloadFile() throws IOException {
		var resource = new ClassPathResource("static/fileWithXss.csv");

		// determine content type
		var determinedContentType = tika.detect(resource.getInputStream());

		if (StringUtils.isBlank(determinedContentType)
				|| StringUtils.equalsIgnoreCase(determinedContentType, MediaType.TEXT_HTML_VALUE)) {
			determinedContentType = MediaType.TEXT_PLAIN_VALUE;
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(determinedContentType)).body(resource);
	}

}
