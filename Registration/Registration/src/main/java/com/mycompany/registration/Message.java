package com.mycompany.registration;






import org.json.JSONObject;

public class Message {
    // Static counter to generate unique message IDs sequentially
    private static int messageCounter = 0;

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private boolean sent;
    private boolean stored;

    /**
     * Constructor for a new Message. Generates a unique ID and hash.
     * @param recipient The recipient's number.
     * @param messageText The text content of the message.
     */
    public Message(String recipient, String messageText) {
        messageCounter++;
        this.messageID = "m" + String.format("%09d", messageCounter); // e.g., m000000001
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(messageCounter, messageText);
        this.sent = false; // Default status
        this.stored = false; // Default status
    }

    /**
     * Validates if the recipient number is in a valid format.
     * A valid recipient starts with '+' and has a length of at most 10 characters.
     * @param recipient The recipient number to validate.
     * @return true if the recipient is valid, false otherwise.
     */
    public static boolean isValidRecipient(String recipient) {
        return recipient != null && recipient.startsWith("+") && recipient.length() <= 10;
    }

    /**
     * Validates if the message text is within the allowed length.
     * A valid message should not be more than 250 characters.
     * @param message The message text to validate.
     * @return true if the message is valid, false otherwise.
     */
    public static boolean isValidMessage(String message) {
        return message != null && message.length() <= 250;
    }

    /**
     * Creates a unique message hash based on the message ID part, counter, and first/last words.
     * @param msgNumber The current message counter value.
     * @param message The full message text.
     * @return The generated message hash string.
     */
    private String createMessageHash(int msgNumber, String message) {
        String idPart = this.messageID.substring(0, 2); // Corrected: should be 'm' + 2nd char or just m
        

        String formattedCounter = String.format("%02d", msgNumber); // e.g., 01, 02, 03, 04

        // Split message into words
        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord; // If only one word, use it for both

        // Combine parts to form the hash
        return (formattedCounter + ":" + msgNumber + firstWord + lastWord).toUpperCase();
    }


    /**
     * Sets the message status to 'sent'.
     */
    public void send() {
        this.sent = true;
        this.stored = false; // Cannot be stored if sent
    }

    /**
     * Sets the message status to 'stored'.
     */
    public void store() {
        this.stored = true;
        this.sent = false; // Cannot be sent if stored for later
    }

    /**
     * Sets the message status to 'disregarded'.
     * A disregarded message is neither sent nor stored.
     */
    public void disregard() {
        this.sent = false;
        this.stored = false;
    }

    /**
     * Converts the Message object to a JSONObject for JSON serialization.
     * @return A JSONObject representation of the message.
     */
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("MessageID", messageID);
        obj.put("Recipient", recipient);
        obj.put("Message", messageText);
        obj.put("Hash", messageHash);
        obj.put("Sent", sent);
        obj.put("Stored", stored);
        return obj;
    }

    /**
     * Provides a detailed string representation of the message.
     * @return A formatted string with message ID, hash, recipient, and text.
     */
    public String getMessageDetails() {
        return "MessageID: " + messageID + "\n" +
                "Message Hash: " + messageHash + "\n" +
                "Recipient: " + recipient + "\n" +
                "Message: " + messageText;
    }

    // --- Getters for accessing message properties ---
    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isStored() {
        return stored;
    }

    /**
     * Checks if the message has been disregarded (neither sent nor stored).
     * @return true if the message is disregarded, false otherwise.
     */
    public boolean isDisregarded() {
        return !sent && !stored;
    }

    // --- Setters for loading from JSON (to reconstruct objects) ---
    // Note: These setters bypass the messageCounter and hash generation for existing messages.
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }

    /**
     * Resets the static message counter. Useful for unit testing to ensure consistent IDs.
     */
    public static void resetMessageCounter() {
        messageCounter = 0;
    }
}
