package br.cwust.billscontrol.enums;

public enum SupportedLanguage {
	EN("en", "English"), 
	PT_BR("pt_BR", "Português do Brasil");

	private String code;
	private String description;

	private SupportedLanguage(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
