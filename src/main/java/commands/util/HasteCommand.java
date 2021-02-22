package commands.util;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Config;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HasteCommand implements ICommand {

    private static final Logger logger = LoggerFactory.getLogger(HasteCommand.class);
    private static final String HASTEBIN_URL = "https://hasteb.in";

    @Override
    public void execute(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Message textMessage = ctx.getMessage();
        String rawText = null;

        //Send help
        if(args.isEmpty()) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        //Handle MessageId
        if(args.get(0).equals("mid")) {
            handleMessageId(args.get(1), channel);
            return;
        }

        //Handle FileId
        if(args.get(0).equals("fid")) {
            handleFileId(args.get(1), channel);
            return;
        }

        //Handle Content Raw
        rawText = removeSubStr(textMessage.getContentRaw(), "$haste", 1);
        String formattedText = MarkdownSanitizer.sanitize(rawText);

        try {
            String pasteLink = createPaste(formattedText);
            channel.sendMessage(pasteLink).queue();
        } catch (IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getCommand() {
        return "haste";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "haste [Text/(FileId)[fid]/(MessageId)[mid]/MarkDownText]";
    }

    private String removeSubStr(String str, String remove, int additional) {
        int start = str.indexOf(remove);
        int stop = start + remove.length() + additional;

        StringBuffer strb = new StringBuffer(str);
        strb.delete(start, stop);
        return strb.toString();
    }

    public String createPaste(String text) throws IOException {
        String response = null;
        String requestURL = HASTEBIN_URL + "/documents";
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        int dataLen = data.length;
        DataOutputStream os;

        URL url = new URL(requestURL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", Integer.toString(dataLen));
        conn.setUseCaches(false);

        try {
            os = new DataOutputStream(conn.getOutputStream());
            os.write(data);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = br.readLine();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        if(response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);
            response = HASTEBIN_URL + "/" + response;
        }

        return response;
    }

    private void handleMessageId(String messageId, TextChannel channel) {

        channel.retrieveMessageById(messageId).queue((message) -> {
            String rawText = removeSubStr(message.getContentRaw(), "$haste", 1);
            String formattedText = MarkdownSanitizer.sanitize(rawText);

            try {
                String pasteLink = createPaste(formattedText);
                channel.sendMessage(pasteLink).queue();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

        }, (failure) -> {
            if (failure instanceof ErrorResponseException) {
                ErrorResponseException ex = (ErrorResponseException) failure;
                if (ex.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
                    channel.sendMessage("Invalid Message ID!").queue();
                }
            }
            logger.error(failure.getMessage());
        });
    }

    private void handleFileId(String fileId, TextChannel channel) {

        channel.retrieveMessageById(fileId).queue((message) -> {

            String fileText = null;
            Message.Attachment file = null;

            //Checks if file exists
            if(message.getAttachments().isEmpty()) {
                channel.sendMessage("No File Found").queue();
                return;
            }

            file = message.getAttachments().get(0);

            //Limit fize Size to 10MB (10000000 Bytes)
            if(file.getSize() == 10000000) {
                channel.sendMessage("File Size limit is 10MB").queue();
                return;
            }

            //Dont allow Video/Image
            if(file.isImage() || file.isVideo()) {
                channel.sendMessage("File can't be Video Or Image").queue();
                return;
            }

            //Get File Data
            StringWriter sr = new StringWriter();
            try {
                IOUtils.copy(file.retrieveInputStream().get(), sr, StandardCharsets.UTF_8);
                fileText = sr.toString();
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }

            //Create Paste
            try {
                String pasteLink = createPaste(fileText);
                channel.sendMessage(pasteLink).queue();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

        }, (failure) -> {
            if (failure instanceof ErrorResponseException) {
                ErrorResponseException ex = (ErrorResponseException) failure;
                if (ex.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
                    channel.sendMessage("Invalid File ID!").queue();
                }
            }
            logger.error(failure.getMessage());
        });

    }
}
