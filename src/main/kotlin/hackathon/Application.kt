package hackathon

import org.springframework.scheduling.annotation.Scheduled
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Call
import com.twilio.rest.api.v2010.account.Message
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Say
import com.twilio.type.PhoneNumber
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import java.net.URI
import spark.Spark.*
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@SpringBootApplication
@EnableScheduling
open class MySpringBootApp
fun main(args: Array<String>) {
    port(4567)
    runApplication<MySpringBootApp>(*args)
}

@Component
class MySchedulerService {
    @Scheduled(fixedRate = 120000L)
    fun test() {
        post("/handle-choice") { req, res ->
            val userResponse = req.queryParams("Digits")
            val twimlResponse = handleChoice(userResponse)
            res.type("application/xml")
            twimlResponse.toXml()
        }

        val bookingInfo =
            "https://wwwexpediacom.integration.sb.karmalab.net/trips/egti-PRY-NZV-TBS8/details/OTU2MWNlMmEtYzMxNS01M2I0LWExNmItODNkNjM3YzU0YTg4O2Q5YmQ5ZTdmLTE0MzMtNDRkOC04MDk3LTc3MDQ5Mjk4YmI4Nl8w"

        val whatsappMessage = "\uD83C\uDF1F Hello Traveler! \uD83C\uDF1F\n" +
                "\n" +
                "We hope you're excited about your upcoming trip with us! Your comfort and satisfaction are our top priorities, and we're here to assist you at any time.\n" +
                "\n" +
                "\uD83E\uDD14 Have any questions, concerns, or issues that you'd like to discuss? No worries, we're just a click away! ✨\n" +
                "\n" +
                "➡\uFE0F [Customer Support & Issue Resolution]($bookingInfo)\n" +
                "\n" +
                "If the link doesn't work for you, you can also find the solution you need on our trips page. Just visit your trips, and you'll discover a friendly Virtual Agent/Chat Bot ready to help you out. \uD83E\uDD16\n" +
                "\n" +
                "\uD83C\uDF10 [Trips Page](https://wwwexpediacom.integration.sb.karmalab.net/trips)\n" +
                "\n" +
                "Your feedback is invaluable to us, and we're committed to ensuring you have the best experience possible. Don't hesitate to reach out. We're here to make your trip a memorable one! \uD83C\uDF0D✈\uFE0F\n" +
                "\n" +
                "Safe travels, and we look forward to serving you soon! \uD83D\uDE4C\n" +
                "\n" +
                "Best regards,\n" +
                "\n" +
                "Expedia Group"

        val emailMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p><h3>Dear Traveler,</h3></p>\n" +
                "    <p>We hope this email finds you well and eagerly anticipating your upcoming trip with us. Your comfort, satisfaction, and peace of mind are of utmost importance to us.</p>\n" +
                "    <p>If you have any questions, concerns, or issues that you'd like to discuss regarding your booking, we're here to assist you every step of the way.</p>\n" +
                "    <p><strong><h3>Option 1: Fast & Convenient Resolution</h3></strong></p>\n" +
                "    <p>You can quickly and conveniently resolve any issues or inquiries by clicking the following link:</p>\n" +
                "    <p><a href=\"$bookingInfo\">Customer Support & Issue Resolution</a></p>\n" +
                "    <p><strong><h3>Option 2: Personalized Assistance via Virtual Agent</h3></strong></p>\n" +
                "    <p>In case the link doesn't work for you, we have a Virtual Agent/Chat Bot ready to assist you right from your Trips page. Visit your trips, and you'll find the Chat Bot, always at your service.</p>\n" +
                "    <p><a href=\"https://wwwexpediacom.integration.sb.karmalab.net/trips\">Trips Page</a></p>\n" +
                "    <p>Our aim is to ensure your trip is seamless, enjoyable, and memorable. Your feedback is invaluable, and we are committed to addressing your needs promptly.</p>\n" +
                "    <p>Your satisfaction is our highest priority, and we want to make your journey as smooth as possible. Don't hesitate to reach out if you have any questions or concerns.</p>\n" +
                "    <p>We're looking forward to serving you and ensuring your travel experience is exceptional. Thank you for choosing [Your Company Name] for your journey.</p>\n" +
                "    <p>Safe travels and best regards,</p>\n" +
                "    <p>Ekansh Saxena</p>\n" +
                "    <p>Customer Support Team</p>\n" +
                "    <p>Expedia Group</p>\n" +
                "    <p><a href=\"mailto:esaxena@expediagroup.com\">esaxena@expediagroup.com</a></p>\n" +
                "</body>\n" +
                "</html>\n"

        listOf(
            listOf("esaxena9927@gmail.com", "+919927475711", bookingInfo),
            listOf("aamishajangra@gmail.com", "+919971944254", bookingInfo),
            listOf("namitavarsney@gmail.com", "+919897003207", bookingInfo),
            listOf("nagendra.kyd@gmail.com", "+919164502594", bookingInfo),
            listOf("prashantsharma2031@gmail.com", "+919821431340", bookingInfo),
            listOf("prateek.jaiswal37@gmail.com", "+918529116019", bookingInfo)
        ).map {
            phoneCall(it[1])
            whatsappNotification(it[1], whatsappMessage)
            sendEmail(it[0], emailMessage)
        }
    }

    fun phoneCall(phone: String) {
        val accountSid = "AC4c74464b614c087e7850ab06ded8ddb1"
        val authToken = "2672f66a57c5f0896b4762a6bb480531"

        Twilio.init(accountSid, authToken)

        val from = PhoneNumber("+917379120054")
        val to = PhoneNumber(phone)

        val call = Call.creator(
            to,
            from,
            URI.create("https://handler.twilio.com/twiml/EH4cc053265abd5fbe1aa9fa46f32c084b")
        ).create()

        println("Phone call sent with SID: ${call.sid}")
    }

    fun handleChoice(choice: String): VoiceResponse {
        val respOne =
            "Thank you for your response! We are sending you one whatsApp/Email message, please check that to raise your query further."
        val respTwo =
            "Have a great trip! We are sending you one whatsApp/Email message, please follow that to give your valuable feedback."
        return when (choice) {
            "1" -> {
                VoiceResponse.Builder().say(Say.Builder(respOne).build()).build()
            }

            "2" -> {
                VoiceResponse.Builder().say(Say.Builder(respTwo).build()).build()
            }

            else -> {
                VoiceResponse.Builder().say(Say.Builder("Invalid choice. Goodbye!").build()).build()
            }
        }
    }

    fun whatsappNotification(phone: String, messageTemplate: String) {
        val accountSid = "AC4c74464b614c087e7850ab06ded8ddb1"
        val authToken = "2672f66a57c5f0896b4762a6bb480531"

        Twilio.init(accountSid, authToken)

        val from = PhoneNumber("whatsapp:+14155238886") // Your Twilio WhatsApp number
        val to = PhoneNumber("whatsapp:$phone") // Recipient's WhatsApp number

        val message = Message.creator(
            to,
            from,
            messageTemplate
        ).create()

        println("Message sent with SID: ${message.sid}")
    }

    fun sendEmail(email: String, messageTemplate: String) {

        val username = "saxenaekansh7@gmail.com" // Your email address
        val password = "uwmiblxobldiycro" // Your email password
        val to = email // Recipient's email address

        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com" // SMTP server for Gmail
        props["mail.smtp.port"] = "587" // Port for Gmail

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val multipart = MimeMultipart()
            val htmlPart = MimeBodyPart()
            htmlPart.setContent(messageTemplate, "text/html")
            multipart.addBodyPart(htmlPart)

            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to))
            message.subject = "We're Here to Help - Your Ongoing Trip with Expedia"
            message.setContent(multipart)
            Transport.send(message)
            println("Email sent successfully.")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

}