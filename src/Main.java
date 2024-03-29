// Creator of Project: ToastyyX

import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Email email =  new Email();
        Scanner stdIn = new Scanner(System.in);

        String response;
        String appPassword = ""; // Generate an Application Password

        // *** Please read the comment below regarding the 'appPassword' variable. ***

        // Regarding the 'appPassword' variable, when using the Gmail SMTP server,
        // the sender's email password is required.
        // When inputting the password, in some cases it will fail and throw you an Authentication exception.
        // One way to avoid this problem is by generating an 'app password' provided by Google.
        // According to Google, "an 'app password' is a 16-digit passcode
        // that gives an app or device permission to access your Google account."
        // Here is a link to the Google support page on how to generate an app password:
        // https://support.google.com/mail/answer/185833?hl=en

        System.out.print("From: ");
        email.setSender(stdIn.nextLine());

        System.out.print("\nVerify your email" + "\nIs the following email correct? " + "\nEmail: " + "\"" + email.getSender() + "\"" + " \n(Yes/No): ");
        response = stdIn.nextLine();
        while (isInvalidEmail(response)) {
            System.out.print("\nFrom: ");
            email.setSender(stdIn.nextLine());
            System.out.print("\nVerify your email" + "\nIs the following email correct? " + "\n" + "\"" + email.getSender() + "\"" + " \n(Yes/No): ");
            response = stdIn.nextLine();
        }

        System.out.print("\nTo: ");
        email.setRecipient(stdIn.nextLine());
        System.out.print("\nVerify your email" + "\nIs the following email correct? " + "\nEmail: " + "\"" + email.getRecipient() + "\"" + " \n(Yes/No): ");
        response = stdIn.nextLine();
        while (isInvalidEmail(response)) {
            System.out.print("\nTo: ");
            email.setRecipient(stdIn.nextLine());
            System.out.print("\nVerify your email" + "\nIs the following email correct? " + "\nEmail: " + "\"" + email.getRecipient() + "\"" + " \n(Yes/No): ");
            response = stdIn.nextLine();
        }

        // Create system properties for the mail server
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true"); // Enables authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Enables the use of STARTTLS
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Creates a mail server
        properties.put("mail.smtp.port", "587"); // Sets the port of the mail server to 587 (recommended)

        // Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() { // Authenticates the sender's email account
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email.getSender(), appPassword);
            }
        });

        // For additional information regarding the 'MimeMultiPart' parameter subtypes
        // Reference the following article:
        // https://en.wikipedia.org/wiki/MIME#:~:text=at%20the%20end.-,Multipart%20subtypes,field%20of%20the%20overall%20message.
        try {
            MimeMessage message = new MimeMessage(session);

            String messageContent = "Hey! This email message was sent by me using the <strong>JavaMail API!</strong> " +
                    "There's an attachment file containing an image of a cute cat.";
            MimeBodyPart messageBodyPart = new MimeBodyPart(); // Create a new body part that contains a message.
            messageBodyPart.setContent(messageContent, "text/html"); // Sets the content of the message body part to HTML.

            MimeBodyPart attachmentBodyPart = new MimeBodyPart(); // Creates a new body part that contains an attachment file.
            String filePath = "C:/Example/Example/Example/CuteCatImage.jpg"; // Insert a valid file path
            attachmentBodyPart.attachFile(filePath); // Attaches the file to the body part.

            String secondMessageContent = "<br><br>While you're add it, " +
                    "I highly recommend checking out this song by Cocteau Twins called " +
                    "<a href=\"https://youtu.be/PV2bO40zL0I\">'She Will Destroy You'</a>.";
            MimeBodyPart messageBodyPart2 = new MimeBodyPart(); // Create a new body part that contains a message.
            messageBodyPart2.setContent(secondMessageContent, "text/html"); // Sets the content of the message body part to HTML.

            MimeMultipart multipart = new MimeMultipart("mixed"); // Creates a new multipart object w/ subtype 'mixed.'

            message.setFrom(new InternetAddress(email.getSender()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));
            message.setSentDate(new Date());
            message.setSubject("JavaMail API Test Message");
            multipart.addBodyPart(messageBodyPart); // Adds the message body part to the multipart object.
            multipart.addBodyPart(attachmentBodyPart); // Adds the attachment body part to the multipart object.
            multipart.addBodyPart(messageBodyPart2); // Adds the second message body part to the multipart object.
            message.setContent(multipart);

            Transport.send(message); // Sends the message by the Transport class.
            System.out.println("\nMail Successfully Sent");
        } catch (MessagingException messagingException) {
            System.out.println("\nSend Failed: Exception: " + messagingException);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
        stdIn.close();
    }

    public static boolean checkLength(@NotNull String email) {
        final int MIN_CHARACTER_LENGTH = 6;
        final int MAX_CHARACTER_LENGTH = 254;
        return email.length() < MIN_CHARACTER_LENGTH || email.length() > MAX_CHARACTER_LENGTH;
    }

    public static boolean isInvalidEmail(@NotNull String response) {
        if (response.equalsIgnoreCase("no")) {
            return true;
        } else if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no") || response.contains(" ")) {
            System.out.println("Invalid Input.");
            return true;
        }
        return false;
    }

    // TODO:
    //  Implement a method that allows the user to input a path of a file
    //  (i.e. image, audio) as part of the email attachment.

}