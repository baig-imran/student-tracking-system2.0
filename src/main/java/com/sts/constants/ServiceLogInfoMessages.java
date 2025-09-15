package com.sts.constants;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An enum to centralize and manage all application log messages.
 * This approach promotes consistency, prevents typos, and makes
 * log messages easier to maintain.
 */
public enum ServiceLogInfoMessages {

	// --- Generic Messages ---
//	REQUEST_PROCESSED("SERVICE: Request processed successfully for resource: %s "),
//	OPERATION_SUCCESSFUL("SERVICE: Operation '%s' completed successfully"),
//	ENTITY_RECEIVED_WITH_CODE("SERVICE: Received %s with Code %s "),
//


	// --- CRUD Operations ---
	CREATING_ENTITY_WITH_ID("SERVICE: Request received to create %s with %s "),
	ENTITY_CREATED_WITH_ID("SERVICE: Successfully created %s with ID %s "),

	CREATING_BULK_ENTITIES_WITH_SIZE("SERVICE: Request received to create %s %s "), //Request received to create x departments
	BULK_ENTITIES_CREATED("SERVICE: Successfully created %s %s "),
//
//	FETCH_ENTITY("SERVICE: Request received to fetch %s with ID %s "),
	FETCHING_ENTITY_WITH_ID("SERVICE: Fetching %s with ID %s "),
	ENTITY_FETCHED_WITH_ID("SERVICE: Fetched %s with ID: %s "),
//	ENTITY_FETCHED("SERVICE: Successfully fetched %s with ID %s "),
//
	FETCHING_ALL_ENTITIES("SERVICE: Request received to fetch all %s "),
	ALL_ENTITIES_FETCHED("SERVICE: Successfully fetched all %s (%s records) "),
//
	UPDATING_ENTITY_WITH_ID("SERVICE: Request received to update %s with %s"),
	ENTITY_UPDATED_WITH_ID("SERVICE: Successfully updated %s with ID %s "),
//
//	UPDATE_BULK_ENTITIES("SERVICE: Request received to update %s %s "),
//	BULK_ENTITIES_UPDATED("SERVICE: Successfully updated %s %s "),
//
	DELETING_ENTITY_WITH_ID("SERVICE: Deleting %s with ID %s "),
	ENTITY_DELETED_WITH_ID("SERVICE: Successfully deleted %s with ID %s "), 
	FETCHING_ENTITIES_BY_SPECIFICATION("SERVICE: Fetching %s with Specification "),
	ENTITIES_FETCHED_BY_SPECIFICATION("SERVICE: Successfully fetched %s %s "),

//
//	DELETE_BULK_ENTITIES("SERVICE: Request received to delete bulk %s "),
//	BULK_ENTITIES_DELETED("SERVICE: Successfully deleted %s %s ")
	;


	private final String message;

	ServiceLogInfoMessages(String message) {
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





