package com.salwa.salwa.homepage.ui.cart.location.response;

import org.jetbrains.annotations.NotNull;

public class KecamatanItem{
	private String nama;
	private int id;

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotNull
	public String toString() {
		return nama;
	}
}
