package net.kosa.kapsuleserver.base.config;

import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class UpperCaseNamingStrategy implements PhysicalNamingStrategy {

	public UpperCaseNamingStrategy() {
	}

	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.apply(name, jdbcEnvironment);
	}

	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.apply(name, jdbcEnvironment);
	}

	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.apply(name, jdbcEnvironment);
	}

	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.apply(name, jdbcEnvironment);
	}

	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.apply(name, jdbcEnvironment);
	}

	private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
		if (name == null) {
			return null;
		} else {
			StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));

			for(int i = 1; i < builder.length() - 1; ++i) {
				if (this.isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
					builder.insert(i++, '_');
				}
			}

			return this.getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
		}
	}

	protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
		if (this.isCaseInsensitive(jdbcEnvironment)) {
			name = name.toUpperCase(Locale.ROOT);
		}

		return new Identifier(name, quoted);
	}

	protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
		return true;
	}

	private boolean isUnderscoreRequired(char before, char current, char after) {
		return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
	}
}
