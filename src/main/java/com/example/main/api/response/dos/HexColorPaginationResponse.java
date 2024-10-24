package com.example.main.api.response.dos;

import java.util.List;

public class HexColorPaginationResponse {


	private int currentPage;

	private int size;

	private int totalPages;

	private List<HexColor> colors;

	public List<HexColor> getColors() {
		return colors;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getSize() {
		return size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setColors(List<HexColor> colors) {
		this.colors = colors;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
