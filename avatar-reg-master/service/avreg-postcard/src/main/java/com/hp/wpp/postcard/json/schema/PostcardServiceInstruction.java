package com.hp.wpp.postcard.json.schema;

import java.util.ArrayList;
import java.util.List;

public class PostcardServiceInstruction {

	private List<PostcardServiceInstruction.Commands> commands;
	
	private PostcardChallenge challenge;

	public List<PostcardServiceInstruction.Commands> getCommands() {
        if (commands == null) {
            commands = new ArrayList<PostcardServiceInstruction.Commands>();
        }
        return this.commands;
    }

	/**
	 * Gets the value of the challenge property.
	 * 
	 * @return possible object is {@link PostcardChallenge }
	 * 
	 */
	public PostcardChallenge getChallenge() {
		return challenge;
	}

	/**
	 * Sets the value of the challenge property.
	 * 
	 * @param value
	 *            allowed object is {@link PostcardChallenge }
	 * 
	 */
	public void setChallenge(PostcardChallenge value) {
		this.challenge = value;
	}
	
	public static class Commands {

        protected String command;
        protected List<String> options;

        /**
         * Gets the value of the command property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCommand() {
            return command;
        }

        /**
         * Sets the value of the command property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCommand(String value) {
            this.command = value;
        }

        public List<String> getOptions() {
            if (options == null) {
                options = new ArrayList<String>();
            }
            return this.options;
        }

    }

}
