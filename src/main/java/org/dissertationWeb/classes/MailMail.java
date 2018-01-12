package org.dissertationWeb.classes;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Class created for email purposes, it is contains an object from the class mailSender and a basic method to send email, plus setter and getter
 * @author ismael
 *
 */
public class MailMail
{
	private MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(String from, String to, String subject, String msg) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
	}

	/**
	 * Automatic email body generator for a project proposal
	 * @param username
	 * @param description
	 * @param compulsoryReading
	 * @param title
	 * @param topics
	 * @return
	 */
	public String createMessageProjectProposal(String username, String description, 
			String compulsoryReading, String title, String topics) {
		String message = username + " has made a new project proposal.\nThe description offered by the student is: "
				+ description + "\nThe recommended readings are: " + compulsoryReading+ "\nThe title is: "
				+ title + "\nAnd the topics are: " + topics;
		return message;
	}
	
	/**
	 * Automatic body method generator for a new project
	 * @param username
	 * @param description
	 * @param compulsoryReading
	 * @param title
	 * @param topics
	 * @return
	 */
	public String newOrApproveProjectMessage(String username, String title, String text) {
		String message = username + " has " + text + " a project .\n" + "\nThe title is: "
				+ title + "\n please visit the application to check the new project "
				+ "<a href=\'http://localhost:8080/DissertationWeb/\' target=\'_blank\'>Click here to login into the app</a>";;
				return message;
	}
	
	

	public String createGeneralMessage(String username, String description, 
			String compulsoryReading, String title, String topics) {
		String message = "";
		return message;
	}
}