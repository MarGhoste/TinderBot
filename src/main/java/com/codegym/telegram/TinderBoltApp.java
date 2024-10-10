package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Date;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "7721580519:AAGOaaDUs5Ha7G13mMMU9WVqUbs7is4h7aY"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "gpt:n4QuBf69EdaWO7T77o0oJFkblB3Tof5yWFt5yj7DNhdQFq4K"; //TODO: añadir el token de ChatGPT entre comillas

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode;
    private ArrayList<String> list = new ArrayList<>();

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí

    public void iniciarComando(){

        mode = DialogMode.MAIN;
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);

        showMainMenu(
                "start", "menú principal del bot",
                "profile", "generación de perfil de Tinder 😎",
                "opener", "mensaje para iniciar conversación 🥰",
                "message", "correspondencia en su nombre 😈",
                "date", "correspondencia con celebridades 🔥",
                "gpt", "hacer una pregunta a chat GPT 🧠"
        );

    }
    public void gptComando(){
        mode = DialogMode.GPT;

        String text = loadMessage("gpt");
        sendPhotoMessage("gpt");
        sendTextMessage(text);

    }
    public void gptDiaLog(){

        String text = getMessageText();
        String prompt = loadPrompt("gpt");
        var myMessage = sendTextMessage("GPT esta escribiendo...");
        String answer = chatGPT.sendMessage(prompt, text);
        // sendTextMessage(answer);
        updateTextMessage(myMessage,answer);

    }
    public void dateComando(){
        mode = DialogMode.DATE;

        String text = loadMessage("date");
        sendPhotoMessage("date");
        sendTextMessage(text);
        sendTextButtonsMessage(text,
                "date_grande", "Ariana Grande",
                "date_weisz", "Rachel Weisz",
                "date_won","Kim Ji-Won",
                "date_fox","Megan Fox",
                "date_romanoff","Natasha Romanoff");

    }
    public void dateButton(){
        String key = getButtonKey();
        sendPhotoMessage(key);
        sendHtmlMessage(key);

        String prompt = loadPrompt(key);
        chatGPT.setPrompt(prompt);
    }
    public void dateDialog(){
        String text = getMessageText();

        var myMessage = sendTextMessage("el usuario esta escribiendo...");
        String answer = chatGPT.addMessage(text);
        //sendTextMessage(answer);
        updateTextMessage(myMessage,answer);
    }
    public void messageComando(){
        mode = DialogMode.MESSAGE;

        String text = loadMessage("message");
        sendPhotoMessage("message");
        sendTextButtonsMessage(text,
                "message_next", "Escribe nuevo mensaje",
                "message_date", "Invita a alguien a salir en una cita");

        list.clear();

    }
    public void messageButton(){
        String key = getButtonKey();
        //sendPhotoMessage(key);
       // sendHtmlMessage(key);

        String prompt = loadPrompt(key);
        String history = String.join("\n\n",list);
        var myMessage = sendTextMessage("CHAT GPT esta escribiendo...");
        String answer = chatGPT.sendMessage(prompt, history);
        updateTextMessage(myMessage,answer);


    }
    public void messageDialog(){
        String text = getMessageText();



        list.add(text);
        //sendTextMessage(answer);
    }
    public void profileComando(){
        mode = DialogMode.PROFILE;

        String text = loadMessage("profile");
        sendPhotoMessage("profile");
        sendTextMessage(text);

        sendTextMessage("Dime tu nombre: ");

        user = new UserInfo();
        questionCount = 0;

    }
        private UserInfo user = new UserInfo();
        private int questionCount = 0;

    public void profileDiaLog(){

        String text = getMessageText();
        questionCount++;

        if(questionCount==1){
            user.name = text;
            sendTextMessage("Cual es tu edad: ");
        } else if (questionCount==2) {
            user.age = text;
            sendTextMessage("Ahora dime tu hobby favorito: ");
        } else if (questionCount==3) {
            user.hobby = text;
            sendTextMessage("Cual es tu proposito: ");
        } else if (questionCount==4) {
            user.goals = text;

        String prompt = loadPrompt("profile");
        String userInfo = user.toString();
        var myMessage = sendTextMessage("GPT esta escribiendo...");
        String answer = chatGPT.sendMessage(prompt, userInfo);
        // sendTextMessage(answer);
        updateTextMessage(myMessage,answer);

        }
    }

    public void openerComando(){
        mode = DialogMode.OPENER;

        String text = loadMessage("opener");
        sendPhotoMessage("opener");
        sendTextMessage(text);

        sendTextMessage("Cual es nombre: ");
        user = new UserInfo();
        questionCount = 0;

    }


    public void openerDiaLog(){

        String text = getMessageText();
        questionCount++;

        if(questionCount==1){
            user.name = text;
            sendTextMessage("Su edad: ");
        } else if (questionCount==2) {
            user.age = text;
            sendTextMessage("En que trabaja: ");
        } else if (questionCount==3) {
            user.occupation = text;
            sendTextMessage("Del 1 - 10 , Que tan atractiva es: ");
        } else if (questionCount==4) {
            user.handsome = text;

            String prompt = loadPrompt("opener");
            String userInfo = user.toString();
            var myMessage = sendTextMessage("GPT esta escribiendo...");
            String answer = chatGPT.sendMessage(prompt, userInfo);
            // sendTextMessage(answer);
            updateTextMessage(myMessage,answer);

        }
    }

    public void hola(){

        if (mode == DialogMode.GPT){
            gptDiaLog();
        } else if (mode == DialogMode.DATE) {
            dateDialog();
        } else if (mode == DialogMode.MESSAGE) {
            messageDialog();
        } else if (mode == DialogMode.PROFILE) {
            profileDiaLog();
        } else if (mode == DialogMode.OPENER) {
            openerDiaLog();
        } else {

            String text = getMessageText();
            sendTextMessage("*Hola soy Viko, bienvenido a mi bot de Tinder*");
            sendTextMessage("_¿Quien eres?_");
            sendTextMessage("Tu eres:" + text);

            sendPhotoMessage("avatar_main");
            sendTextButtonsMessage("Launch process",
                    "Start", "Empezar", "Stop", "Detener");
        }
    }

    public void holaButton(){
        String key = getButtonKey();
        if (key.equals("Start")){
            sendTextMessage("Empezando");
        }else {
            sendTextMessage("Deteniendo");
        }

    }
    @Override
    public void onInitialize() {
        //TODO: y un poco más aquí :)
        addCommandHandler("start",this::iniciarComando);
        addCommandHandler("gpt",this::gptComando);
        addCommandHandler("date",this::dateComando);
        addCommandHandler("message",this::messageComando);
        addCommandHandler("profile",this::profileComando);
        addCommandHandler("opener",this::openerComando);
        addMessageHandler(this::hola);
        // addButtonHandler("^.*",this::holaButton);
        addButtonHandler("^date_.*",this::dateButton);
        addButtonHandler("^message_.*",this::messageButton);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
