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

	/**
	 * General method used by some method to send automatic emails
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 */
	public void sendMail(String from, String to, String subject, String msg) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
	}

	/**
	 * General method used by some methods that send automatic email
	 * @param user
	 * @param message
	 * @param from
	 * @param to
	 * @param title
	 * @return
	 */
	public boolean sendAutomaticEmail(User user, String message, String from, String to, String title) {
		//String lecturerEmail = user.getEmail();
		try{
			sendMail(from,to,title + user.getUsername(),message);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * Automatic email for schedule
	 * @param message
	 * @param from
	 * @param to
	 * @param title
	 * @return
	 */
	public boolean sendAutomaticEmailSchedule(String message, String from, String to, String title) {
		//String lecturerEmail = user.getEmail();
		try{
			sendMail(from,to,title,message);
		}catch(Exception e) {
			return false;
		}
		return true;
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
				+ description + "\nThe compulsory reading is: " + compulsoryReading+ "\nThe title is: "
				+ title + "\nThe technologies are: " + topics;
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
				+ title + "\n please visit the application to check the new project";
		return message;
	}


	/**
	 * Method that generate the general content of the message
	 * @param username
	 * @param description
	 * @param compulsoryReading
	 * @param title
	 * @param topics
	 * @return
	 */
	public String createGeneralMessage(String username, String description, 
			String compulsoryReading, String title, String topics) {
		String message = "";
		return message;
	}
}