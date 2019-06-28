package com.contactsunny.poc.normalizePhoneNumbersPOC;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class App implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(App.class);

    private static PhoneNumberUtil phoneUtil;

    private String brazilShortCode = "BR";
    private String columnbiaShortCode = "CO";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> brazilPhoneNumbers = Arrays.asList(
                "+5521976923932",
                "5561991474841",
                "055 719 9174 8722",
                "719 9174 8722",
                "55 (719) (9174) (8722)"
        );

        List<String> columbiaPhoneNumbers = Arrays.asList(
                "573146339375",
                "3137145988",
                "+573154417054",
                "316537219731323123",
                "3013755140",
                "+57 31 5441 7054",
                "+57 (31) (8393) (1081)"
        );

        phoneUtil = PhoneNumberUtil.getInstance();

        for (String inputPhoneNumber : brazilPhoneNumbers) {
            validateAndFormatPhoneNumber(inputPhoneNumber, brazilShortCode);
        }

        for (String inputPhoneNumber : columbiaPhoneNumbers) {
            validateAndFormatPhoneNumber(inputPhoneNumber, columnbiaShortCode);
        }

        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("Interchanging country code, all numbers should be invalid now.");


        for (String inputPhoneNumber : brazilPhoneNumbers) {
            validateAndFormatPhoneNumber(inputPhoneNumber, columnbiaShortCode);
        }

        for (String inputPhoneNumber : columbiaPhoneNumbers) {
            validateAndFormatPhoneNumber(inputPhoneNumber, brazilShortCode);
        }

    }

    private void validateAndFormatPhoneNumber(String inputPhoneNumber, String shortCode) {

        logger.info("Processing phone number: " + inputPhoneNumber + " with short code: " + shortCode);

        PhoneNumber phoneNumberProto = null;

        try {
            phoneNumberProto = phoneUtil.parse(inputPhoneNumber, shortCode);

            logger.info("phoneNumberProto: " + new Gson().toJson(phoneNumberProto));

            boolean isValid = phoneUtil.isValidNumber(phoneNumberProto);

            logger.info("Is phone number valid: " + isValid);

            if (phoneNumberProto.hasCountryCode()) {
                logger.info("Country code is present: " + phoneNumberProto.getCountryCode());
            } else {
                logger.info("Country code is not present.");
            }

            if (phoneNumberProto.hasNationalNumber()) {
                logger.info("National number is present: " + phoneNumberProto.getNationalNumber());
            } else {
                logger.info("National number is not present.");
            }

            String formattedPhoneNumber = phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);

            if (formattedPhoneNumber.startsWith("+")) {
                logger.info("Removing leading + from phone number");
                formattedPhoneNumber = formattedPhoneNumber.replace("+", "");
            }

            logger.info("Formatted phone number: " + formattedPhoneNumber);

        } catch (NumberParseException e) {
            logger.error(e.getMessage());
        }

        logger.info("==================================");
    }
}
