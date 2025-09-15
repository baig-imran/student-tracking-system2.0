package com.sts.constants;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An enum to centralize and manage all application log messages.
 * This approach promotes consistency, prevents typos, and makes
 * log messages easier to maintain.
 */
public enum ServiceLogDebugMessages {

	
	REQUEST_OBJECT("Service: Request object for %s: %s"),
    RESPONSE_OBJECT("Service: Response object for %s: %s"),
    BULK_REQUEST_OBJECT("Service: Bulk request for %s with size %s: %s");

	private final String message;

	ServiceLogDebugMessages(String message) {
		this.message = message;
	}

	/**
	 * Returns the formatted log message by substituting placeholders with provided arguments.
	 * The method uses String.format() to handle various types of arguments,
	 * including strings and objects.
	 *
	 * @param args The arguments to be inserted into the message string.
	 * @return The formatted log message.
	 */
	public String getMessage(Object... args) {
		// Ensure that the number of arguments matches the number of placeholders
		// to prevent String.format() from throwing an exception.
		long placeholderCount = message.chars().filter(ch -> ch == '%').count();
		if (placeholderCount != 0 && (args == null || args.length == 0)) {
			// Handle cases where a message expects args but none are provided.
			// This logic can be customized (e.g., throwing an exception).
			return message.replaceAll("%s", " ");
		}

		// This is the core logic. String.format() handles all formatting.
		return String.format(message, args);
	}
}





