package com.salwa.salwa.homepage.ui.cart.location.response;

import org.jetbrains.annotations.NotNull;

public class KelurahanItem{
	private String nama;
	private long id;

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	public String toString() {
		return nama;
	}
}
