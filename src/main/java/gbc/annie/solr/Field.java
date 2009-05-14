package gbc.annie.solr;

public enum Field {

  AUTHOR("author", FieldType.STR, true),
  BASE_PRICE("basePrice", FieldType.DOUBLE, false),
  DIRECTOR("director", FieldType.STR, true),
  DISLAY_NAME("displayName", FieldType.STR, false),
  GOLD_PRICE("displayName", FieldType.DOUBLE, false),
  MOVIE_CAST("movieCast", FieldType.STR, true),
  ORIGINAL_SUB_TITLE("originalSubTitle", FieldType.STR, false),
  ORIGINAL_TITLE("originalTitle", FieldType.STR, false),
  PERFORMER("performer", FieldType.STR, true),
  SALE_PRICE("salePrice", FieldType.DOUBLE, false),
  PRODUCT_DISLAY_NAMES("roductDisplayNames", FieldType.STR, true),
  SUB_TITLE("subTitle", FieldType.STR, false),
  TITLE("title", FieldType.STR, true);

  private String fieldName;
  private FieldType type;
  private boolean isMultiple;

  Field(String fieldName, FieldType type, boolean isMultiple) {
    this.fieldName = fieldName;
    this.type = type;
    this.isMultiple = isMultiple;
  }

  public String getFieldName() {
    return fieldName;
  }

  public FieldType getType() {
    return type;
  }

  public boolean isMultiple() {
    return isMultiple;
  }

}
