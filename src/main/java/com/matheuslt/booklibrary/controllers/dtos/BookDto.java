package com.matheuslt.booklibrary.controllers.dtos;

import java.util.Date;

public class BookDto {
	
	private String name;
	private String author;
	private String description;
	private Date publicationDate;
	private Integer pages;
	
	public BookDto() {
	}

	public BookDto(String name, String author, String description, Date publicationDate, Integer pages) {
		this.name = name;
		this.author = author;
		this.description = description;
		this.publicationDate = publicationDate;
		this.pages = pages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}
}
