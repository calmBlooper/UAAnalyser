import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyser {
    private String surnameLettersWord = "[ЛЕВЧУКлевчук]+";
    private String fractionNumber = "[+-]?\\d+(\\.\\d+){1}(e(\\+|\\-)\\d+)?";
    private String wholeNumber = "[+-]?\\d+";
    private String identificator = "[А-Яа-я]{1}.+";
    private String UAPhoneNumber = "\\+380\\d{9}";
    private String date = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private String punctMark ="#..?#";
    private Pattern surnamePattern;
    private Pattern fractionNumberPattern;
    private Pattern wholeNumberPattern;
    private Pattern identificatorPattern;
    private Pattern UAPhoneNumberPattern;
    private Pattern datePattern;
    private Pattern punctMarkPattern;
    private Matcher matcher;
    private EmailValidator validator;

    public Analyser() {
        surnamePattern = Pattern.compile(surnameLettersWord);
        fractionNumberPattern = Pattern.compile(fractionNumber);
        wholeNumberPattern = Pattern.compile(wholeNumber);
        identificatorPattern = Pattern.compile(identificator);
        UAPhoneNumberPattern = Pattern.compile(UAPhoneNumber);
        datePattern = Pattern.compile(date);
        punctMarkPattern = Pattern.compile(punctMark);
        validator = EmailValidator.getInstance();
    }

    public void analyseText(String text){
        String toProcess=text.replaceAll("(\\. )|( \\.)"," #.# ");
        toProcess=toProcess.replaceAll("! "," #!# ");
        toProcess=toProcess.replaceAll(","," #,# ");
        toProcess=toProcess.replaceAll(":"," #:# ");
        toProcess=toProcess.replaceAll(";"," #;# ");
        toProcess=toProcess.replaceAll("\\)"," #(# ");
        toProcess=toProcess.replaceAll("\\)"," #(# ");
        toProcess=toProcess.replaceAll(" - "," #-# ");
        toProcess=toProcess.replaceAll("\\? "," #?# ");
        toProcess=toProcess.replaceAll("\n"," #\\\\n# ");
        toProcess=toProcess.replaceAll("[ ]+"," #_# ");
        String[] words = toProcess.split(" ");
        for (String word:words) analyseWord(word);
    }
    private void analyseWord(String word) {
        matcher = punctMarkPattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word.substring(1,(word.length()-1)) + "\" — розділовий знак");
            return;
        }
        matcher = surnamePattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word + "\" — слово, що складається з літер прізвища Левчук");
            return;
        }
        matcher = UAPhoneNumberPattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word + "\" — український номер телефону");
            return;
        }
        matcher = fractionNumberPattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word + "\" — дробове число");
            return;
        }
        matcher = wholeNumberPattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word + "\" — ціле число");
            return;
        }
        matcher = identificatorPattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word + "\" — ідентифікатор");
            return;
        }
        matcher = datePattern.matcher(word);
        if (matcher.matches()) {
            System.out.println("\""+word + "\" — дата");
            return;
        }
        if (validator.isValid(word)) {
            System.out.println("\""+word + "\" — адреса елетронної поштової скриньки");
            return;
        }
        System.out.println("\""+word + "\" — недопустиме слово");
    }

    public static void main(String[] args) {
        Analyser analyser=new Analyser();
        String textToProcess="Стаття 7. +9574662224 Реклама 13.12.2012 , +389673647597 споживчого кредиту\n" +
                "\n" +
                "1. Якщо в 422.535 рекламі! 9.10.2004 Лев щодо pifagor6541@gmail.com надання кулеч  -42422 процентна ставка.\n" +
                "\n" +
                "2. У @frv.com 0 інформації +380963757474 визначається:\n" +
                "\n" +
                "1) даіаіюіа@ukma.edu.ua максимальна сума, куче -422.0053 на яку може бути виданий кредит;";
        analyser.analyseText(textToProcess);
        System.out.println("Text succesfully processed");
    }
}
