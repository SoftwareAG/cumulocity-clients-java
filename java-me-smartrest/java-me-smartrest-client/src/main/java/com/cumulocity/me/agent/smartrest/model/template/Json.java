package com.cumulocity.me.agent.smartrest.model.template;

import java.util.Vector;

public class Json {

	public static class JsonField {
        private final String quotation;
		private final String name;
		private final Object value;
		private final Supplier string;

		public JsonField(String quotation, String name, Object value, Supplier string) {
			super();
            this.quotation = quotation;
			this.name = name;
			this.value = value;
			this.string = string;
		}

		protected StringBuffer addEntry(final StringBuffer builder) {
			return appendValue(appendName(builder));
		}

		private StringBuffer appendName(final StringBuffer builder) {
			if (name != null) {
				builder.append(quotation).append(name).append(quotation).append(":");
			}
			return builder;
		}

		private StringBuffer appendValue(final StringBuffer builder) {
			if (value != null && string != null) {
				return builder.append(string.get());
			}
			return builder.append("null");
		}
	}

	public static class JsonObjectBuilder implements Supplier {

        private final String quotation;
        private final String prefix;
		private final String suffix;
		private final Vector values = new Vector();

		public JsonObjectBuilder(String quotation, String prefix, String suffix) {
			super();
            this.quotation = quotation;
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public JsonObjectBuilder add(final JsonObjectBuilder value) {
			values.addElement(new JsonField(quotation, null, value, new Supplier() {
                public String get() {
                    return value.get();
                }
            }));
			return this;
		}

		public JsonObjectBuilder addString(final String name, final String value) {
			values.addElement(new JsonField(quotation, name, value, new Supplier() {
                public String get() {
                    return quotation + value + quotation;
                }
            }));
			return this;
		}

		public JsonObjectBuilder addValue(final String name, final Object value) {
			values.addElement(new JsonField(quotation, name, value, new Supplier() {
				public String get() {
					return String.valueOf(value);
				}
			}));
			return this;
		}

		public JsonObjectBuilder addInteger(final String name, final int value) {
			values.addElement(new JsonField(quotation, name, new Integer(value), new Supplier() {
                public String get() {
                    return Integer.toString(value);
                }
            }));
			return this;
		}

		public JsonObjectBuilder addBoolean(final String name, final Boolean value) {
			values.addElement(new JsonField(quotation, name, value, new Supplier() {
                public String get() {
                    return value.toString();
                }
            }));
			return this;
		}

		public JsonObjectBuilder addJson(final String name, final JsonObjectBuilder value) {
			values.addElement(new JsonField(quotation, name, value, new Supplier() {
                public String get() {
                    return value.get();
                }
            }));
			return this;
		}

		public String get() {
			final StringBuffer builder = new StringBuffer();
            for (int i = 0; i < values.size(); i++) {
                final JsonField entry = (JsonField) values.elementAt(i);
                entry.addEntry(builder).append(",");
            }
			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
			return builder.insert(0, prefix).append(suffix).toString();
		}
	}

	public static JsonObjectBuilder json() {
		return json("\"\"");
	}

    public static JsonObjectBuilder json(String quotation) {
        return new JsonObjectBuilder(quotation, "{", "}");
    }

	public static JsonObjectBuilder array() {
		return array("\"\"");
	}

    public static JsonObjectBuilder array(String quotation) {
        return new JsonObjectBuilder(quotation, "[", "]");
    }
}
