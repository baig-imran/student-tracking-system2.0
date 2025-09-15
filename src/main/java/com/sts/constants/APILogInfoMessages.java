package com.sts.constants;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An enum to centralize and manage all application log messages.
 * This approach promotes consistency, prevents typos, and makes
 * log messages easier to maintain.
 */
public enum APILogInfoMessages {

	// --- Generic Messages ---
	REQUEST_PROCESSED("API: Request processed successfully for resource: %s "),
	OPERATION_SUCCESSFUL("API: Operation '%s' completed successfully"),
	ENTITY_RECEIVED_WITH_CODE("API: Received %s with Code %s "),

	FETCHING_ALL_ENTITIES("API: All %s fetched successfully"),


	// --- CRUD Operations ---
	CREATE_ENTITY_WITH_ID("API: Request received to create %s with %s "),
	ENTITY_CREATED_WITH_ID("API: Successfully created %s with ID %s "),

	CREATE_BULK_ENTITIES_WITH_SIZE("API: Request received to create %s %s "), //Request received to create x departments
	BULK_ENTITIES_CREATED_WITH_SIZE("API: Successfully created %s %s "),

	FETCH_ENTITY_WITH_ID("API: Fetching %s with ID %s "),
	ENTITY_FETCHED_WITH_ID("API: Fetched %s with ID: %s "),

	FETCH_ALL_ENTITIES("API: Request received to fetch all %s "),
	ALL_ENTITIES_FETCHED("API: Successfully fetched all %s %s  "),

	UPDATE_ENTITY_WITH_ID("API: Request received to update %s with %s"),
	ENTITY_UPDATED_WITH_ID("API: Successfully updated %s with ID %s "),

	UPDATE_BULK_ENTITIES_WITH_SIZE("API: Request received to update %s %s "),
	BULK_ENTITIES_UPDATED_WITH_SIZE("API: Successfully updated %s %s "),

	DELETE_ENTITY_WITH_ID("API: Request received to delete %s with ID %s "),
	ENTITY_DELETED_WITH_ID("API: Successfully deleted %s with ID %s "),

	DELETE_BULK_ENTITIES_WITH_SIZE("API: Request received to delete bulk %s "),
	BULK_ENTITIES_DELETED_WITH_SIZE("API: Successfully deleted %s %s "),
	
	SEARCH_ENTITIES("API: Search %s "),
	ENTITIES_SEARCHED("API: Successfully searched %s %s"),
	FETCHING_ENTITIES_BY_SPECIFICATION("SERVICE: Fetching %s with Specification "),
	ENTITIES_FETCHED_BY_SPECIFICATION("SERVICE: Successfully fetched %s %s "),
	;


	private final String message;

	APILogInfoMessages(String message) {
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





