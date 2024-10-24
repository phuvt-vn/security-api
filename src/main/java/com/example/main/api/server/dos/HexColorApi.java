package com.example.main.api.server.dos;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.example.main.api.response.dos.HexColor;
import com.example.main.api.response.dos.HexColorPaginationResponse;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/dos/v1")

@Validated
public class HexColorApi {

	private static final int COLORS_SIZE = 1_000_000;

	private List<HexColor> hexColors;

	public HexColorApi() {
		hexColors = IntStream.rangeClosed(1, COLORS_SIZE).boxed().parallel().map(v -> {
			var hexColor = new HexColor();
			hexColor.setId(v);
			hexColor.setHexColor(randomColorHex());

			return hexColor;
		}).collect(Collectors.toList());
	}

	@GetMapping(value = "/random-colors", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HexColor> randomColors() {
		return hexColors;
	}


	@GetMapping(value = "/random-colors-pagination", produces = MediaType.APPLICATION_JSON_VALUE)
	// use 1-based for easier
	public HexColorPaginationResponse randomColors(@RequestParam(required = true, name = "page") int page,
			@Valid @Min(10) @Max(100) @RequestParam(required = true, name = "size") int size) {
		var startIndex = (page - 1) * size;
		var sublist = hexColors.subList(startIndex, startIndex + size);

		var response = new HexColorPaginationResponse();
		response.setColors(sublist);
		response.setCurrentPage(page);
		response.setSize(size);
		response.setTotalPages((int) Math.ceil(COLORS_SIZE / size));

		return response;
	}

	private String randomColorHex() {
		// create a random number between 0 and ffffff (hex)
		var randomInt = ThreadLocalRandom.current().nextInt(0xffffff + 1);

		// format as hashtag and leading zeros (hex color code)
		return String.format("#%06x", randomInt);
	}

}
